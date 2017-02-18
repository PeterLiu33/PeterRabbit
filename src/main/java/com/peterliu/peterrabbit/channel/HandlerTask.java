package com.peterliu.peterrabbit.channel;

import com.peterliu.peterrabbit.channel.threadtimeout.TaskTimer;
import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;
import com.peterliu.peterrabbit.utils.ChannelUtils;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * 任务执行器
 * <p>
 * Created by bavatinolab on 17/1/25.
 */
public abstract class HandlerTask {

    private static final Logger logger = Logger.getLogger(HandlerTask.class.getCanonicalName());

    private static final ConfigSource configSource = ConfigSourceImpl.instance();

    private static final Map<String, ExecutorService> threadPoolMap = new ConcurrentHashMap<String, ExecutorService>();

    private static final TaskTimer taskTimer = new TaskTimer();

    private static Timer timer = null;

    static {
        timer = new Timer();
        timer.schedule(taskTimer, 1000, 2000);
    }

    public static void run(final ChannelHandler channelHandler, final SelectionKey selectionKey, final int ops) {
        //检测是否为心跳包
        byte[] loadByte = null;
        if (SelectionKey.OP_READ == ops) {
            loadByte = ChannelUtils.readByte((SocketChannel) selectionKey.channel());
            if (loadByte == null || loadByte.length < TaskData.UrgentDataByteLength) {
                //心跳包,不启动线程
                return;
            }
        }
//        else if (SelectionKey.OP_WRITE == ops) {
//            Object attachment = selectionKey.attachment();
//            if (attachment == null || !(attachment instanceof Response)) {
//                return;
//            }
//        }
        String threadPoolKey = Thread.currentThread().getName();
        if ((threadPoolMap.get(threadPoolKey)) == null) {
            threadPoolMap.put(threadPoolKey, buildExecutor());
        }
        ExecutorService executorService = threadPoolMap.get(threadPoolKey);
        final TaskData finalTaskData = new TaskData(selectionKey, loadByte);
        Future future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                switch (ops) {
                    case SelectionKey.OP_ACCEPT:
                        channelHandler.handleAccept(finalTaskData);
                        break;
                    case SelectionKey.OP_CONNECT:
                        channelHandler.handleConnect(finalTaskData);
                        break;
                    case SelectionKey.OP_READ:
                        channelHandler.handleRead(finalTaskData);
                        break;
                    case SelectionKey.OP_WRITE:
                        logger.info("======================");
                        channelHandler.handleWrite(finalTaskData);
                        break;
                    default:
                        ;
                }

            }
        });
//        taskTimer.addTask(future, configSource.getHandlerTimeOut());
    }

    private static ExecutorService buildExecutor() {
        int nThreads = configSource.getHandlerCoreSize();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(nThreads, nThreads * 100,
                5000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(nThreads * 10));
        //允许核心线程池空闲收回
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }

    /**
     * 关闭当前线程池线程
     */
    public static void cancelThread() {
        Collection<ExecutorService> values = threadPoolMap.values();
        for (ExecutorService executorService : values) {
            //直接全部关闭
            executorService.shutdownNow();
        }
        //定时器清除
        timer.cancel();
    }
}
