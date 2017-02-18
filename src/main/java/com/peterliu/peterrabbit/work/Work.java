package com.peterliu.peterrabbit.work;

import com.peterliu.peterrabbit.channel.SelectorRegister;
import com.peterliu.peterrabbit.protocol.ProtocolType;
import com.peterliu.peterrabbit.protocol.Response;

/**
 * Created by bavatinolab on 17/2/3.
 */
public interface Work {

    /**
     * 执行任务
     *
     * @param <T>
     * @return
     */
    <T extends Response> T run();

    /**
     * 获取选择器
     *
     * @return
     */
    SelectorRegister getSelectorRegister();

    /**
     * 设置本处理器对应的类型
     *
     * @return
     */
    ProtocolType getProtocolType();
}
