package com.peterliu.peterrabbit.protocol.http;

import com.peterliu.peterrabbit.channel.TaskData;
import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;
import com.peterliu.peterrabbit.protocol.*;
import com.peterliu.peterrabbit.work.Work;

/**
 * Created by bavatinolab on 17/2/3.
 */
public abstract class HttpWork implements Work {

    static ConfigSource configSource = ConfigSourceImpl.instance();

    public <T extends Response> T doGet(Context context, ConfigSource configSource) {
        ProtocolHandler handler = context.getHandler();
        TaskData taskData1 = context.getTaskData();
        return handler.getResponse(taskData1, context.getRequest(), "unsupported!");
    }

    public <T extends Response> T doPost(Context context, ConfigSource configSource) {
        ProtocolHandler handler = context.getHandler();
        TaskData taskData1 = context.getTaskData();
        return handler.getResponse(taskData1, context.getRequest(), "unsupported!");
    }

    @Override
    public final <T extends Response> T run() {
        Context context = Context.getCurrentContext();
        HttpRequest request = context.getRequest();
        if (HttpRequest.GET.equals(request.getMethod())) {
            return doGet(context, configSource);
        } else if (HttpRequest.POST.equals(request.getMethod())) {
            return doPost(context, configSource);
        }
        return null;
    }

    @Override
    public ProtocolType getProtocolType() {
        return ProtocolType.http;
    }
}
