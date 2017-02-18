package com.peterliu.peterrabbit.protocol.command;

import com.peterliu.peterrabbit.channel.TaskData;
import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;
import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.Filter;
import com.peterliu.peterrabbit.protocol.Response;
import com.peterliu.peterrabbit.utils.ChannelUtils;
import com.peterliu.peterrabbit.utils.LocalFileUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by bavatinolab on 17/2/4.
 */
public class DefaultProtocolResolver implements Resolver {

    private boolean preCheckResult = true;

    static ConfigSource configSource = ConfigSourceImpl.instance();

    @Override
    public void action(ActionType actionType) throws Exception {
        Context context = Context.getCurrentContext();
        switch (actionType) {
            case pre:
                runPre(context);
                break;
            case run:
                runWork(context);
                break;
            case post:
                runPost(context);
                break;
            case write:
                runWrite(context);
                break;
            case clean:
            default:
                runClean(context);
                ;
        }
    }

    //执行前置过滤器
    private void runPre(Context context) {
        Filter filter = context.getFilter();
        if (filter != null) {
            Filter temp = filter;
            do {
                if (temp.check(configSource)) {
                    preCheckResult = temp.doPre(configSource);
                }
                filter = temp;
            } while (preCheckResult && (temp = temp.next()) != null);
        }
    }

    //执行任务
    private void runWork(Context context) {
        if (!preCheckResult) {
            return;
        }
        Response reponse = context.getWork().run();
        context.setResponse(reponse);
    }

    //执行后置过滤器
    private void runPost(Context context) {
        if (!preCheckResult) {
            return;
        }
        Filter filter = context.getFilter();
        while(filter != null && filter.next() != null){
            filter = filter.next();
        }
        if (filter != null) {
            do {
                if (filter.check(configSource)) {
                    filter.doPost(configSource);
                }
            } while ((filter = filter.prev()) != null);
        }
    }

    //触发写事件
    private void runWrite(Context context) throws IOException {
        TaskData taskData = context.getTaskData();
        SocketChannel channel = (SocketChannel) taskData.getSelectionKey().channel();
        Response response = context.getResponse();
        if(response == null){
            channel.write(ByteBuffer.wrap("fail to pass check".getBytes()));
        }else{
            if(response.getContentBuffer() == null) {
                ChannelUtils.write(channel, ByteBuffer.wrap(response.getResponseStr().getBytes()));
            }else{
                ChannelUtils.write(channel, ByteBuffer.wrap(response.getResponseStr().getBytes()));
                ChannelUtils.write(channel, response.getContentBuffer());
                LocalFileUtils.clean(response.getContentBuffer());
            }
        }
        channel.shutdownInput();
        channel.close();
    }

    /**
     * 释放资源
     *
     * @param context
     */
    private void runClean(Context context) {
        Context.release();
    }
}
