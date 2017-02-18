package com.peterliu.peterrabbit.channel;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 固定线程数,并没有采用线程池
 *
 * Created by bavatinolab on 17/1/25.
 */
public class DefaultChannelHandlerFactory implements ChannelHandlerFactory {

    private static final Map<ChannelType, ThreadGroup> channelHandlerGroup = new ConcurrentHashMap<ChannelType, ThreadGroup>();

    /**
     * 每调用一次,会启用一个新的线程
     *
     * @param selectorRegister
     * @param type
     * @param threadPoolName
     */
    public void handler(SelectorRegister selectorRegister, ChannelType type, String threadPoolName) {
        ThreadGroup threadGroup = null;
        if ((threadGroup = channelHandlerGroup.get(type)) == null) {
            channelHandlerGroup.put(type, (threadGroup = new ThreadGroup(type.name())));
            threadGroup.setDaemon(true);
        }
        Thread thread = null;
        switch (type) {
            case READ_FILE:
                thread = new Thread(threadGroup, new ReadFileChannelHandler(selectorRegister));
                break;
            case WRITE_FILE:
                thread = new Thread(threadGroup, new WriteFileChannelHandler(selectorRegister));
                break;
            case DOWNLOAD_FILE:
                thread = new Thread(threadGroup, new DownloadFileChannelHandler(selectorRegister));
                break;
            default:
                ;
        }
        if(thread != null){
            thread.setName(threadPoolName);
            thread.start();
        }
    }
}
