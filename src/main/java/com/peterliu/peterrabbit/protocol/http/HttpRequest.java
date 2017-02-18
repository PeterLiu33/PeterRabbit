package com.peterliu.peterrabbit.protocol.http;

import com.peterliu.peterrabbit.protocol.Request;

import java.util.Map;

/**
 * Created by bavatinolab on 17/2/1.
 */
public class HttpRequest extends Request {

    /**
     * 断点续传使用
     */
    private int startRange = -1;

    private int endRange = -1;

    public int getStartRange() {
        return startRange;
    }

    public int getEndRange() {
        return endRange;
    }

    public static final String POST = "POST";

    public static final String GET = "GET";

    private Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpRequest setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
}
