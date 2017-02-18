package com.peterliu.peterrabbit.protocol;

import com.peterliu.peterrabbit.datasource.ConfigSource;

/**
 * 包装Request
 *
 * Created by bavatinolab on 17/2/2.
 */
public class RequestResponseFilter extends FilterAdapter {

    @Override
    public boolean doPre(ConfigSource configSource) {
        Context context = Context.getCurrentContext();
        ProtocolHandler handler = context.getHandler();
        Request request = handler.getRequest(context.getTaskData());
        context.setRequest(request);
        return true;
    }
}
