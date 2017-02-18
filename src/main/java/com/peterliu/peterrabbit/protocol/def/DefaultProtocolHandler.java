package com.peterliu.peterrabbit.protocol.def;

import com.peterliu.peterrabbit.channel.TaskData;
import com.peterliu.peterrabbit.protocol.*;
import com.peterliu.peterrabbit.protocol.http.filter.CacheHeaderCheckFilter;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * Created by bavatinolab on 17/2/2.
 */
public class DefaultProtocolHandler extends ProtocolHandlerAdapter {
    @Override
    public boolean detectType(TaskData taskData) {
        return true;
    }

    @Override
    public ProtocolType getType() {
        return ProtocolType.def;
    }

    @Override
    public <T extends Request> T getRequest(TaskData taskData) {
        return null;
    }

    @Override
    public <K extends Response, T extends Request> K getResponse(TaskData taskData, T request, String content) {
        return (K) new Response().setRequest(request).setContent("unsupport protocol!");
    }

    @Override
    public <K extends Response, T extends Request> K getResponse(TaskData taskData, T request, ByteBuffer content) {
        return (K) new Response().setRequest(request).setContent("unsupport protocol!");
    }
}
