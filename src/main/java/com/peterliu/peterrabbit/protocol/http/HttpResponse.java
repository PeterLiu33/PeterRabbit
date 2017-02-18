package com.peterliu.peterrabbit.protocol.http;

import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.Response;
import com.peterliu.peterrabbit.utils.StringUtils;

import java.io.File;
import java.util.*;

/**
 * Created by bavatinolab on 17/2/3.
 */
public class HttpResponse extends Response {

    public static final String CRLF = "\r\n";

    private ResponseCode statusCode;

    private String contentType;

    private Map<String, String> headers = new HashMap<String, String>();

    public String getContentType() {
        return contentType;
    }

    public HttpResponse setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public ResponseCode getStatusCode() {
        return statusCode;
    }

    public HttpResponse setStatusCode(ResponseCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        if(headers == null){
            return;
        }
        this.headers = headers;
    }

    public void addHeader(String key, String value){
        this.headers.put(key, value);
    }

    public String buildHeaders(){
        Iterator<Map.Entry<String, String>> iterator = this.headers.entrySet().iterator();
        StringBuilder stringBuilder = new StringBuilder("");
        while (iterator.hasNext()){
            Map.Entry<String, String> next = iterator.next();
            stringBuilder.append(next.getKey()).append(":").append(next.getValue()).append(CRLF);
        }
        return stringBuilder.toString();
    }

//    ThreadLocal<>

    {
        //增加默认的响应头
        addHeader("Server", "PeterRabbit/1.0");
        addHeader("X-Powered-By", "Java");
        addHeader("Allow", "get,post");
        Calendar calender = Calendar.getInstance();
        calender.add(Calendar.DAY_OF_YEAR, 1);
        addHeader("Expires", calender.getTime().toString());//缓存一天
        addHeader("Expires", calender.getTime().toString());//缓存一天
        addHeader("Connection", "close");//连接关闭
//        addHeader();
    }
    @Override
    public String getResponseStr() {
        Context context = Context.getCurrentContext();
        StringBuilder stringBuilder = new StringBuilder("HTTP/");
        stringBuilder.append(getRequest().getVersion()).append(" ")
                .append(getStatusCode().getCode()).append(" ")
                .append(getStatusCode().name()).append(CRLF);
        //增加请求头
        addHeader("Content-Type", ContentType.getType(context.getFileType()));
        addHeader("Date", new Date().toString());
        if(StringUtils.isNotBlank(getContent())){
            addHeader("Content-Length", String.valueOf(getContent().getBytes().length));
        }else if(getContentBuffer() != null){
            addHeader("Content-Length", String.valueOf(getContentBuffer().limit()));
        }
        File file = context.getFileObject();
        if(file != null){
            addHeader("Last-Modified", new Date(file.lastModified()).toString());
        }
        stringBuilder.append(buildHeaders()).append(CRLF);
        if(getContent() != null){
            stringBuilder.append(getContent());
        }
        return stringBuilder.toString();
    }

    public static enum ResponseCode {
        OK("200", "success"),
        NOT_MODIFIED("304", "NotModified"),
        NOT_FOUND("404", "NotFound");

        private String code;

        private String msg;


        ResponseCode(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
