package com.peterliu.peterrabbit.protocol;

import com.peterliu.peterrabbit.channel.TaskData;
import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;
import com.peterliu.peterrabbit.utils.FileUtils;
import com.peterliu.peterrabbit.utils.StringUtils;
import com.peterliu.peterrabbit.work.Work;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求与响应上下文
 * Created by bavatinolab on 17/2/1.
 */
public class Context extends ConcurrentHashMap<String, Object> implements ContextKeys {

    static ConfigSource configSource = ConfigSourceImpl.instance();

    private static final ThreadLocal<Context> threadLocalContext = new ThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            Context context = new Context();
            context.put(CASH_DATA, new HashMap<String, Object>());
            return context;
        }
    };

    /**
     * 清除缓存
     */
    public static void release() {
        threadLocalContext.remove();
    }

    private Context() {

    }


    /**
     * 获取当前上下文
     *
     * @return
     */
    public static Context getCurrentContext() {
        return threadLocalContext.get();
    }

    /**
     * 设置协议处理器
     *
     * @param protocolHandler
     */
    public void setHandler(ProtocolHandler protocolHandler) {
        if (protocolHandler == null) {
            return;
        }
        threadLocalContext.get().put(PROTOCOL_TYPE, protocolHandler.getType());
        threadLocalContext.get().put(PROTOCOL_HANDLER, protocolHandler);
    }

    /**
     * 获取处理器
     *
     * @return
     */
    public ProtocolHandler getHandler() {
        return (ProtocolHandler) threadLocalContext.get().get(PROTOCOL_HANDLER);
    }

    /**
     * 添加新的过滤器
     *
     * @param filter
     */
    public void addFilter(FilterAdapter filter) {
        FilterAdapter filterAdapter = (FilterAdapter) threadLocalContext.get().get(FILTER);
        if (filterAdapter != null) {
            while (filterAdapter.next() != null) {
                filterAdapter = (FilterAdapter) filterAdapter.next();
            }
            filterAdapter.setNextFilter(filter);
            filter.setPrevFilter(filterAdapter);
        } else {
            threadLocalContext.get().put(FILTER, filter);
        }
    }

    /**
     * 获取过滤器
     *
     * @return
     */
    public Filter getFilter() {
        return (FilterAdapter) threadLocalContext.get().get(FILTER);
    }

    /**
     * 设置经过UTF-8解码后的字符串
     *
     * @param loaderStr
     */
    public void setLoaderStr(String loaderStr) {
        threadLocalContext.get().put(LOADER_STRING, loaderStr);
    }

    /**
     * 获取经过UTF-8解码后的字符串
     *
     * @return
     */
    public String getLoaderStr() {
        return (String) threadLocalContext.get().get(LOADER_STRING);
    }

    /**
     * 设置任务信息数据
     *
     * @param taskData
     */
    public void setTaskData(TaskData taskData) {
        threadLocalContext.get().put(TASK_DATA, taskData);
    }

    /**
     * 获取任务信息数据
     *
     * @return
     */
    public TaskData getTaskData() {
        return (TaskData) threadLocalContext.get().get(TASK_DATA);
    }

    /**
     * 增加缓存数据
     *
     * @param key
     * @param object
     */
    public void addCashData(String key, Object object) {
        ((HashMap<String, Object>) threadLocalContext.get().get(CASH_DATA)).put(CASH_DATA, object);
    }

    /**
     * 获取缓存数据
     *
     * @param key
     * @return
     */
    public Object getCashData(String key) {
        return ((HashMap<String, Object>) threadLocalContext.get().get(CASH_DATA)).get(key);
    }

    /**
     * 设置请求对象
     *
     * @param request
     * @param <T>
     */
    public <T extends Request> void setRequest(T request) {
        threadLocalContext.get().put(REQUEST, request);
    }

    /**
     * 获取请求对象
     *
     * @param <T>
     * @return
     */
    public <T extends Request> T getRequest() {
        return (T) threadLocalContext.get().get(REQUEST);
    }

    /**
     * 设置响应
     *
     * @param response
     * @param <T>
     */
    public <T extends Response> void setResponse(T response) {
        if (response == null)
            return;
        threadLocalContext.get().put(RESPONSE, response);
    }

    /**
     * 获取响应
     *
     * @param <T>
     * @return
     */
    public <T extends Response> T getResponse() {
        return (T) threadLocalContext.get().get(RESPONSE);
    }

    /**
     * 获取协议类型
     *
     * @return
     */
    public ProtocolType getProtocolType() {
        return (ProtocolType) threadLocalContext.get().get(PROTOCOL_TYPE);
    }

    /**
     * 设置任务信息
     *
     * @param work
     */
    public void setWork(Work work) {
        threadLocalContext.get().put(WORK, work);
    }

    /**
     * 获取任务信息
     *
     * @return
     */
    public Work getWork() {
        return (Work) threadLocalContext.get().get(WORK);
    }

    /**
     * 也就是文件后缀
     *
     * @param fileType
     */
    public void setFileType(String fileType) {
        //检查是否为非法文件后缀
        if (StringUtils.isBlank(fileType)) {
            return;
        }
        fileType = fileType.toLowerCase();
        String[] legalSuffix = configSource.getLegalSuffix();
        boolean isLegal = FileUtils.isIllegalFile(fileType, legalSuffix);
        if (isLegal) {
            threadLocalContext.get().put(FILE_SUFFIX, fileType);
        }
    }

    /**
     * 获取文件后缀, 如果是非法的文件后缀,则返回为null
     *
     * @return
     */
    public String getFileType() {
        return (String) threadLocalContext.get().get(FILE_SUFFIX);
    }

    /**
     * 设置文件对象
     *
     * @param file
     */
    public void setFileObject(File file) {
        threadLocalContext.get().put(FILE_OBJECT, file);
    }

    /**
     * 获取文件对象
     *
     * @return
     */
    public File getFileObject() {
        return (File) threadLocalContext.get().get(FILE_OBJECT);
    }
}
