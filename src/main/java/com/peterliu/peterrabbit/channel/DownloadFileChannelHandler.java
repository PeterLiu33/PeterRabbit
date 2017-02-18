package com.peterliu.peterrabbit.channel;

import java.util.concurrent.CancellationException;

/**
 * 用于处理下载文件
 *
 * Created by bavatinolab on 17/1/25.
 */
public class DownloadFileChannelHandler extends ChannelHandlerAdapter {


    public DownloadFileChannelHandler(SelectorRegister selectorRegister) {
        super(selectorRegister);
    }

    @Override
    public void doHandle(TaskData taskData) throws CancellationException {

    }

    @Override
    public ChannelType getType() {
        return ChannelType.DOWNLOAD_FILE;
    }



}
