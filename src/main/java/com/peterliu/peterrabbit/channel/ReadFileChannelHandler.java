package com.peterliu.peterrabbit.channel;

import com.peterliu.peterrabbit.protocol.*;
import com.peterliu.peterrabbit.protocol.def.DefaultWork;
import com.peterliu.peterrabbit.protocol.http.DefaultHttpWork;

import java.nio.channels.*;
import java.util.concurrent.CancellationException;
import java.util.logging.Logger;

/**
 * 用于处理读文件
 * <p>
 * Created by bavatinolab on 17/1/25.
 */
public class ReadFileChannelHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(ReadFileChannelHandler.class.getCanonicalName());

    public ReadFileChannelHandler(SelectorRegister selectorRegister) {
        super(selectorRegister);
    }


    @Override
    public ChannelType getType() {
        return ChannelType.READ_FILE;
    }


    @Override
    public void doHandle(final TaskData taskData) throws CancellationException {
        SocketChannel socketChannel = (SocketChannel) taskData.getSelectionKey().channel();
        logger.info(String.format("handleRead: receive connection request from " + socketChannel.socket().getRemoteSocketAddress()));
        final SelectorRegister selectorRegister = getSelectorRegister();
        ProtocolFactory.init(taskData);
        ProtocolFactory.run(
                new WorkFacade(
                        new DefaultHttpWork(selectorRegister),
                        new DefaultWork(selectorRegister)
                ));
    }


}
