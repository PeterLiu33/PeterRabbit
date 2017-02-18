package com.peterliu.peterrabbit.protocol.http;

import com.peterliu.peterrabbit.channel.TaskData;
import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.ProtocolHandler;
import com.peterliu.peterrabbit.protocol.Request;
import com.peterliu.peterrabbit.protocol.Response;

/**
 * Created by bavatinolab on 17/2/19.
 */
public abstract class ResponseFactory {

    public static <T extends Response> T buildUnsupportFileResponse(){
        Context context = Context.getCurrentContext();
        ProtocolHandler handler = context.getHandler();
        return handler.getResponse(context.getTaskData(), context.getRequest(), "unsupported file type!");
    }

    public static <T extends Response> T build404Response(){
        Context context = Context.getCurrentContext();
        ProtocolHandler handler = context.getHandler();
        return handler.getResponse(context.getTaskData(), context.getRequest(), (String) null);
    }

    public static <T extends Response> T build304Response(){
        Context context = Context.getCurrentContext();
        ProtocolHandler handler = context.getHandler();
        HttpResponse response = handler.getResponse(context.getTaskData(), context.getRequest(), (String) null);
        response.setStatusCode(HttpResponse.ResponseCode.NOT_MODIFIED);
        return (T)response;
    }

    public static <T extends Response> T buildUnsupportURLResponse(){
        Context context = Context.getCurrentContext();
        ProtocolHandler handler = context.getHandler();
        return handler.getResponse(context.getTaskData(), context.getRequest(), "unsupported url type!");
    }
}
