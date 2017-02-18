package com.peterliu.peterrabbit.channel;

import java.util.concurrent.CancellationException;

/**
 * 处理写文件
 *
 * Created by bavatinolab on 17/1/25.
 */
public class WriteFileChannelHandler extends ChannelHandlerAdapter {


    public WriteFileChannelHandler(SelectorRegister selectorRegister) {
        super(selectorRegister);
    }

    @Override
    public void doHandle(TaskData taskData) throws CancellationException {

    }

    @Override
    public ChannelType getType() {
        return ChannelType.WRITE_FILE;
    }



}
