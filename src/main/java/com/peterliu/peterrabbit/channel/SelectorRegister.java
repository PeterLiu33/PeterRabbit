package com.peterliu.peterrabbit.channel;

import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 多路复用器注册器
 * <p>
 * Created by bavatinolab on 17/1/25.
 */
public class SelectorRegister {

    private static final Logger logger = Logger.getLogger(SelectorRegister.class.getCanonicalName());

    ConfigSource configSource = ConfigSourceImpl.instance();

    Selector selector;

    /**
     * 用来记录当前启动了几个selector处理线程
     */
    AtomicInteger startCount = new AtomicInteger(0);

    public int getCount() {
        return startCount.get();
    }

    /**
     * 返回原值,并加1
     *
     * @return
     */
    public int count() {
        return startCount.getAndAdd(1);
    }

    ChannelType type;

    ServerSocketChannel serverSocketChannel;

    InetSocketAddress inetSocketAddress;

    public Selector getSelector() {
        return selector;
    }

    public ChannelType getType() {
        return type;
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public InetSocketAddress getInetSocketAddress() {
        return inetSocketAddress;
    }

    /**
     * 启动服务器
     *
     * @return
     */
    protected boolean startServer() {
        try {
            //创建服务器channel
            this.serverSocketChannel = ServerSocketChannel.open();
            //设置为非阻塞模式
            this.serverSocketChannel.configureBlocking(false);
            //服务器Channel绑定监听端口
            this.serverSocketChannel.socket().setReuseAddress(true);
            //绑定地址
            this.serverSocketChannel.socket().bind(this.inetSocketAddress);

            this.selector = Selector.open();

            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT, this.type);

            logger.info("Server started up at " + inetSocketAddress.getAddress() + ":" + inetSocketAddress.getPort());

            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, "fail to start server", e);
        }
        close();
        return false;
    }

    /**
     * 注册一个服务器
     *
     * @param type
     * @param inetSocketAddress
     * @throws IOException
     */
    public SelectorRegister(ChannelType type, InetSocketAddress inetSocketAddress) {
        this.type = type;
        this.inetSocketAddress = inetSocketAddress;
    }

    public void start() {
        //启动selector处理器线程
        HandlerFactoryStrategy.getFactory().handler(this, type, "main" + type.name());
    }

    /**
     * @param channel 不允许为空
     * @return
     * @throws ClosedChannelException
     */
    public SelectionKey registerRead(SocketChannel channel) throws ClosedChannelException {
        if (channel != null && type != null) {
            return channel.register(this.selector, SelectionKey.OP_READ, this.type);
        }
        return null;
    }

    /**
     * 关闭所有的Selector
     */
    public void close() {
        //释放资源
        if (this.serverSocketChannel != null) {
            try {
                this.serverSocketChannel.close();
            } catch (IOException e) {
                logger.log(Level.INFO, "fail to close socket channel", e);
            }
        }

        if (this.selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                logger.log(Level.INFO, "fail to close selector", e);
            }
        }
    }
}
