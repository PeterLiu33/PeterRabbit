package com.peterliu.peterrabbit.channel;

import java.nio.channels.Selector;
import java.util.concurrent.CancellationException;

/**
 * 通道处理器
 * <p>
 * Created by bavatinolab on 17/1/25.
 */
public interface ChannelHandler extends Runnable {

    /**
     * 获取该Handler可以处理的渠道类型
     *
     * @return
     */
    ChannelType getType();

    /**
     * 获取注册器
     *
     * @return
     */
    SelectorRegister getSelectorRegister();

    /**
     * 获取其对应的选择器
     *
     * @return
     */
    Selector getSelector();

    /**
     * 处理接收准备事件
     *
     * @param taskData
     * @throws CancellationException
     */
    void handleAccept(TaskData taskData) throws CancellationException;

    /**
     * 处理连接准备事件
     *
     * @param taskData
     * @throws CancellationException
     */
    void handleConnect(TaskData taskData) throws CancellationException;

    /**
     * 处理读取准备事件
     *
     * @param taskData
     * @throws CancellationException
     */
    void handleRead(TaskData taskData) throws CancellationException;

    /**
     * 处理写准备事件
     *
     * @param taskData
     * @throws CancellationException
     */
    void handleWrite(TaskData taskData) throws CancellationException;
}
