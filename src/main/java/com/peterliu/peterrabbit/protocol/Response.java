package com.peterliu.peterrabbit.protocol;

import com.peterliu.peterrabbit.utils.StringUtils;

import java.nio.ByteBuffer;

/**
 * 响应信息
 * Created by bavatinolab on 17/1/29.
 */
public class Response {

    private Request request;

    /**
     * 针对于文本文件
     */
    private String content;

    /**
     * 对于二进制文件
     */
    private ByteBuffer contentBuffer;

    public Request getRequest() {
        return request;
    }

    public Response setRequest(Request request) {
        this.request = request;
        return this;
    }

    public String getContent() {
        if(getContentBuffer() == null) {
            return StringUtils.isBlank(content) ? "no response!" : content;
        }else{
            return content;
        }
    }

    public Response setContent(String content) {
        this.content = content;
        return this;
    }

    public ByteBuffer getContentBuffer() {
        return contentBuffer;
    }

    public Response setContentBuffer(ByteBuffer contentBuffer) {
        this.contentBuffer = contentBuffer;
        return this;
    }

    public String getResponseStr() {
        return getContent();
    }
}
