package com.peterliu.peterrabbit.channel;


/**
 * Created by bavatinolab on 17/1/25.
 */
public interface ChannelHandlerFactory {

    /**
     *
     * @param selectorRegister 选择器
     * @param type 通道类型
     * @param threadPoolName 线程池名字
     */
    void handler(SelectorRegister selectorRegister, ChannelType type, String threadPoolName);
}
