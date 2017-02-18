package com.peterliu.peterrabbit.protocol.http.filter;

import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;
import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.FilterAdapter;
import com.peterliu.peterrabbit.protocol.Request;
import com.peterliu.peterrabbit.protocol.http.HttpRequest;
import com.peterliu.peterrabbit.protocol.http.ResponseFactory;

import java.io.File;
import java.util.Calendar;
import java.util.Map;

/**
 * 判断当前是否需要返回304,即让客户端使用缓存
 * 
 * Created by bavatinolab on 17/2/19.
 */
public class CacheCheckFilter extends FilterAdapter {

    @Override
    public boolean doPre(ConfigSource configSource) {
        Context context = Context.getCurrentContext();
        HttpRequest request = context.getRequest();
        long clientMaxAge = configSource.getClientMaxAge();
        if(request.getCacheMaxAge() == 0 || request.getCacheMaxAge() < clientMaxAge){
            //重置缓存时间
            request.setCacheMaxAge(clientMaxAge);
        }
        if(request.isNeedJudgeCache()){
            //判断是否需要直接返回304
            File file = context.getFileObject();
            if(file == null){
                return true;
            }
            Map<String, String> headers = request.getHeaders();
            if(headers != null && headers.size() > 0){
                String lastModified = String.valueOf(file.lastModified());
                if(lastModified.equals(headers.get("If-Modified-Since"))){
                    //客户端没有改变,请使用客户端缓存
                    context.setResponse(ResponseFactory.build304Response());
                    return false;
                }
            }
        }
        return true;
    }
}
