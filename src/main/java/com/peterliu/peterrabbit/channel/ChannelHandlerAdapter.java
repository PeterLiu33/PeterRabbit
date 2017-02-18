package com.peterliu.peterrabbit.channel;

import com.peterliu.peterrabbit.protocol.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/1/25.
 */
public abstract class ChannelHandlerAdapter implements ChannelHandler {

    private static final Logger logger = Logger.getLogger(ChannelHandlerAdapter.class.getCanonicalName());

    SelectorRegister selectorRegister;

    public ChannelHandlerAdapter(SelectorRegister selectorRegister) {
        this.selectorRegister = selectorRegister;
    }

    public SelectorRegister getSelectorRegister() {
        return selectorRegister;
    }

    @Override
    public void handleConnect(TaskData taskData) throws CancellationException {
        logger.info("handleConnect");
    }

    @Override
    public Selector getSelector() {
        return this.selectorRegister.getSelector();
    }

    @Override
    public void handleAccept(TaskData taskData) throws CancellationException {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) taskData.getSelectionKey().channel();
            //创建多路复用器
            SocketChannel socketChannel = ssc.accept();
            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                //将当前创建的channel注册到Selector,并监听OP_READ事件
                logger.info("handleAccept:Received request from " + ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostString());
                getSelectorRegister().registerRead(socketChannel);
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "fail to handle accecept event", e);
        }
    }

    @Override
    public void handleWrite(TaskData taskData) throws CancellationException {
        SocketChannel socketChannel = (SocketChannel) taskData.getSelectionKey().channel();
        try {

            Response response = (Response) taskData.getSelectionKey().attachment();
            if (response == null || !(response instanceof Response)) {
                return;
            }
            socketChannel.shutdownInput();
            socketChannel.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "fail to close channel", e);
        }
    }

    @Override
    public void run() {
        //初始化selector
        boolean isStart = this.getSelectorRegister().startServer();
        if (!isStart) {
            logger.log(Level.WARNING,
                    "fail to start server on " +
                            this.getSelectorRegister().getInetSocketAddress().getAddress().getCanonicalHostName() +
                            ":" +
                            this.getSelectorRegister().getInetSocketAddress().getPort()
            );
            return;
        }
        Selector selector = getSelector();
        if (selector != null && selector.isOpen()) {
            try {
                while (true) {
                    try {
                        //必须采用非阻塞方式
                        if (selector.select() == 0) {
                            continue;
                        }
                        //说明有事件发生
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        SelectionKey selectionKey = null;
                        while (iterator.hasNext()) {
                            selectionKey = iterator.next();
                            //处理完毕后需要从selected-key set中删除
                            iterator.remove();
                            //判断事件类型
                            if (selectionKey.isAcceptable()) {
                                //处理接收就绪事件
                                this.handleAccept(new TaskData(selectionKey, null));
                            } else if (selectionKey.isConnectable()) {
                                //处理连接就绪事件
                                HandlerTask.run(this, selectionKey, SelectionKey.OP_CONNECT);
                            } else if (selectionKey.isReadable()) {
                                //处理读就绪事件
                                HandlerTask.run(this, selectionKey, SelectionKey.OP_READ);
                            } else if (selectionKey.isWritable()) {
                                //处理写就绪事件
                                HandlerTask.run(this, selectionKey, SelectionKey.OP_WRITE);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                HandlerTask.cancelThread();
                if (selectorRegister != null) {
                    selectorRegister.close();
                }
            }
        }
    }

    @Override
    public void handleRead(TaskData taskData) throws CancellationException {
        doHandle(taskData);
    }

    public abstract void doHandle(final TaskData taskData) throws CancellationException;
}
