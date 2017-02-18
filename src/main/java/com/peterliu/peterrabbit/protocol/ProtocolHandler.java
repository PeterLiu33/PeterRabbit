package com.peterliu.peterrabbit.protocol;

import com.peterliu.peterrabbit.channel.TaskData;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by bavatinolab on 17/1/29.
 */
public interface ProtocolHandler {

    /**
     * 用于检测当前请求的是什么协议
     *
     * @param taskData
     * @return true 是当前类型;false 不是当前类型
     */
    boolean detectType(TaskData taskData);

    /**
     * 用来表示当前是什么类型协议
     *
     * @return
     */
    ProtocolType getType();

    /**
     * 获取请求内容
     *
     * @param taskData
     * @return
     */
    <T extends Request> T getRequest(TaskData taskData);

    /**
     * 构造返回值
     *
     * @param taskData
     * @param request
     * @param content
     * @return
     */
    <K extends Response, T extends Request> K getResponse(TaskData taskData, T request, String content);

    /**
     * 构造返回值
     *
     * @param taskData
     * @param request
     * @param content
     * @return
     */
    <K extends Response, T extends Request> K getResponse(TaskData taskData, T request, ByteBuffer content);

    /**
     * 协议自身的过滤器
     *
     * @return
     */
    List<Filter> getFilters();
}
