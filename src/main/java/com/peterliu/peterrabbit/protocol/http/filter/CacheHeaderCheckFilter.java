package com.peterliu.peterrabbit.protocol.http.filter;

import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.Filter;
import com.peterliu.peterrabbit.protocol.FilterAdapter;
import com.peterliu.peterrabbit.protocol.Request;
import com.peterliu.peterrabbit.protocol.http.HttpRequest;
import com.peterliu.peterrabbit.utils.StringUtils;

import java.util.Map;

/**
 * 用于判断请求头相关信息
 * Created by bavatinolab on 17/2/19.
 */
public class CacheHeaderCheckFilter extends FilterAdapter {
    @Override
    public boolean doPre(ConfigSource configSource) {
        Context context = Context.getCurrentContext();
        HttpRequest request = context.getRequest();
        Map<String, String> headers = request.getHeaders();
        if(headers != null && headers.size() > 0) {
            String cacheControl = headers.get("Cache-Control");
            if ("no-cache".equalsIgnoreCase(cacheControl) ||
                    "no-cache".equalsIgnoreCase(headers.get("Pragma")) ||
                    "no-store".equalsIgnoreCase(cacheControl) ||
                    configSource.getClientMaxAge() <= 0){
                //客户端需要强刷最新数据, 或者服务端不允许进行缓存
                request.setNeedJudgeCache(false);
            }else if(StringUtils.isNotBlank(cacheControl) && cacheControl.contains("max-age=")){
                //获取客户端允许缓存的最大时间
                String[] strings = cacheControl.split(",");
                if(strings != null && strings.length > 0){
                    for(String temp : strings){
                        if(temp.startsWith("max-age=")){
                            long maxAge = Long.valueOf(temp.split("=")[1]);
                            request.setNeedJudgeCache(true);
                            request.setCacheMaxAge(maxAge);
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }
}
