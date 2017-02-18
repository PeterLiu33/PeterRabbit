package com.peterliu.peterrabbit.protocol.filter;

import com.peterliu.peterrabbit.channel.TaskData;
import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.FilterAdapter;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 编解码过滤器
 * Created by bavatinolab on 17/2/1.
 */
public class EnCodeDeCodeFilter extends FilterAdapter {

    private static final Logger logger = Logger.getLogger(EnCodeDeCodeFilter.class.getCanonicalName());

    @Override
    public boolean doPre(ConfigSource configSource) {
        Context context = Context.getCurrentContext();
        TaskData taskData = context.getTaskData();
        byte[] load = taskData.getLoad();
        try {
            context.setLoaderStr(new String(load, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING, "fail to decode load", e);
        }
        return true;
    }
}
