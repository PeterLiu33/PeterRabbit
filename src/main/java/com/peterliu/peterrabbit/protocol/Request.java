package com.peterliu.peterrabbit.protocol;

import java.util.Map;

/**
 * 请求信息
 * Created by bavatinolab on 17/1/29.
 */
public class Request {

    private String url;

    private String version;

    private Map<String, String> params;

    private String method;

    private String content;

    public String getUrl() {
        return url;
    }

    public Request setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Request setVersion(String version) {
        this.version = version;
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Request setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Request setContent(String content) {
        this.content = content;
        return this;
    }
}
