package com.peterliu.peterrabbit.protocol.http.filter;

import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;
import com.peterliu.peterrabbit.datasource.Constants;
import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.FilterAdapter;
import com.peterliu.peterrabbit.protocol.http.HttpRequest;
import com.peterliu.peterrabbit.protocol.http.ResponseFactory;
import com.peterliu.peterrabbit.utils.LocalFileUtils;
import com.peterliu.peterrabbit.utils.PropertyOrFileUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 判断判断地址是否合法、资源是否存在
 * Created by bavatinolab on 17/2/19.
 */
public class ResourcesExistFilter extends FilterAdapter {

    private static final Logger logger = Logger.getLogger(ResourcesExistFilter.class.getCanonicalName());

    @Override
    public boolean doPre(ConfigSource configSource) {
        Context context = Context.getCurrentContext();
        HttpRequest request = context.getRequest();
        String url = request.getUrl();
        if (url.contains("/../")) {
            //设置返回值
            context.setResponse(ResponseFactory.buildUnsupportURLResponse());
            return false;
        }
        //对请求地址进行预处理
        String rootPath = configSource.getRootPath();
        String filePath = rootPath + url;
        try {
            filePath = URLDecoder.decode(filePath, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING, "fail to decode url filepath", e);
            //设置返回值
            context.setResponse(ResponseFactory.buildUnsupportURLResponse());
            return false;
        }
        filePath = filePath.replaceAll("/+", "/").replaceAll("/", Constants.PATH_SEPARATOR);
        request.setFilePath(filePath);
        //判断是否为内部文件
        if (!url.startsWith(Constants.INNER_RESOURCES_PATH)) {
            //外部数据
            File dir = LocalFileUtils.checkDictionary(url, configSource);
            if(dir == null && context.getFileObject() == null){
                //文件不存在
                context.setResponse(ResponseFactory.build404Response());
                return false;
            }
            request.setDictionary(dir);
        }
        return true;
    }
}
