package com.peterliu.peterrabbit.protocol;

import com.peterliu.peterrabbit.channel.TaskData;
import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;
import com.peterliu.peterrabbit.protocol.command.*;
import com.peterliu.peterrabbit.protocol.def.DefaultProtocolHandler;
import com.peterliu.peterrabbit.protocol.filter.EnCodeDeCodeFilter;
import com.peterliu.peterrabbit.protocol.filter.RequestResponseFilter;
import com.peterliu.peterrabbit.protocol.http.HttpProtocolHandler;
import com.peterliu.peterrabbit.work.Work;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/1/29.
 */
public abstract class ProtocolFactory {

    private static final Logger logger = Logger.getLogger(ProtocolFactory.class.getCanonicalName());

    static ConfigSource configSource = ConfigSourceImpl.instance();

    static {
        //初始化协议处理器
        new HttpProtocolHandler();
        //默认协议处理器
        new DefaultProtocolHandler();
    }

    /**
     * 自动检测获取协议处理类
     *
     * @param taskData
     * @return
     */
    public static ProtocolHandler getHandler(TaskData taskData) {
        Collection<ProtocolHandler> values = ProtocolHandlerAdapter.protocolTypeProtocolHandlerTreeMap.values();
        for (ProtocolHandler protocolHandler : values) {
            if (!ProtocolType.def.equals(protocolHandler.getType()) && protocolHandler.detectType(taskData)) {
                return protocolHandler;
            }
        }
        //返回默认的协议处理器
        return ProtocolHandlerAdapter.protocolTypeProtocolHandlerTreeMap.get(ProtocolType.def);
    }

    /**
     * 自定义协议处理器
     *
     * @param protocolHandler
     */
    public static void registerHandler(ProtocolHandler protocolHandler) {
        ProtocolHandlerAdapter.protocolTypeProtocolHandlerTreeMap.put(protocolHandler.getType(), protocolHandler);
    }

    /**
     * 初始化上下文
     *
     * @param taskData
     */
    public static void init(TaskData taskData) {
        Context context = Context.getCurrentContext();
        context.setTaskData(taskData);
        ProtocolHandler handler = ProtocolFactory.getHandler(taskData);
        context.setHandler(handler);
        context.addFilter(new EnCodeDeCodeFilter());
        context.addFilter(new RequestResponseFilter());
        List<Filter> filters = handler.getFilters();
        if (filters != null && filters.size() > 0) {
            for (Filter filter : filters) {
                context.addFilter((FilterAdapter) filter);
            }
        }
    }

    public static void run(Work work) {
        Context context = Context.getCurrentContext();
        context.setWork(work);
        DefaultProtocolResolver resolver = new DefaultProtocolResolver();
        Invoker invoker = new Invoker();
        invoker.addCommand(new PreFilterCommand(), resolver)
                .addCommand(new RunCommand(), resolver)
                .addCommand(new PostFilterCommand(), resolver)
                .addCommand(new WriteCommand(), resolver)
                .addCommand(new CleanCommand(), resolver)
                .redo();
    }

    /**
     * 清理
     */
    public static void clear() {
        Context.release();
    }
}
