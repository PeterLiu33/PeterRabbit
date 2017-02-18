package com.peterliu.peterrabbit.utils;

import com.peterliu.peterrabbit.channel.threadtimeout.TaskTimer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

/**
 * 采用内存映射方式,实现多线程并发文件读写操作
 * <p>
 * Created by bavatinolab on 17/1/28.
 */
public class FileConcurrentUtils {


    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static final TaskTimer taskTimer = new TaskTimer();

    private static Timer timer = null;

    static {
        timer = new Timer();
        timer.schedule(taskTimer, 0, 200);
    }

    /**
     * 获取byteBuffer
     *
     * @param filePath
     * @return
     */
    public static MappedByteBuffer getMappedByteBuffer(final String filePath, final int startPoc, final int length) {
        return AccessController.doPrivileged(new PrivilegedAction<MappedByteBuffer>() {
            @Override
            public MappedByteBuffer run() {
                if (StringUtils.isBlank(filePath)) {
                    return null;
                }
                RandomAccessFile randomAccessFile = null;
                FileChannel fileChannel = null;
                try {
                    randomAccessFile = new RandomAccessFile(filePath, "r");
                    if(randomAccessFile == null){
                        return null;
                    }
                    fileChannel = randomAccessFile.getChannel();
                    //把文件的更改刷新到文件
                    if(startPoc < 0 || length < 0 || startPoc >= fileChannel.size() || length > fileChannel.size()){
                        return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
                    }else{
                        if(length > fileChannel.size() - startPoc){
                            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startPoc, fileChannel.size() - startPoc);
                        }
                        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startPoc, length);
                    }
                } catch (Exception e) {
                    return null;
                } finally {
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                    if (fileChannel != null) {
                        try {
                            fileChannel.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                }

            }
        });
    }

    /**
     * 获取MappedByteBuffer用于写文件
     *
     * @param filePath
     * @param lengthToWrite
     * @param isAppend
     * @return
     */
    public static MappedByteBuffer getMappedByteBufferForWriting(final String filePath, final int lengthToWrite, final boolean isAppend) {
        return AccessController.doPrivileged(new PrivilegedAction<MappedByteBuffer>() {
            @Override
            public MappedByteBuffer run() {
                if (StringUtils.isBlank(filePath) || lengthToWrite <= 0) {
                    return null;
                }
                File file = LocalFileUtils.checkFile(filePath);
                if (file == null) {
                    return null;
                }
                RandomAccessFile randomAccessFile = null;
                FileChannel fileChannel = null;
                try {
                    long offset = 0, length = file.length();
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    fileChannel = randomAccessFile.getChannel();
                    if (isAppend) {
                        //判断是追加
                        fileChannel.position(length);
                        fileChannel.truncate(length + lengthToWrite);
                        offset = length;
                    } else {
                        fileChannel.position(0);
                        //强制指定长度
                        fileChannel.truncate(lengthToWrite);
                    }
                    //把文件的更改刷新到文件
                    fileChannel.force(true);
                    return fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, lengthToWrite);
                } catch (Exception e) {
                    return null;
                } finally {
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                    if (fileChannel != null) {
                        try {
                            fileChannel.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                }

            }
        });
    }


    /**
     * 将StringContent写入内存映射文件,并释放文件资源
     *
     * @param mappedByteBuffer
     * @param bytesToWrite
     */
    public static void write(MappedByteBuffer mappedByteBuffer, byte[] bytesToWrite) {
        try {
            //写内容
            mappedByteBuffer.put(bytesToWrite, 0, bytesToWrite.length);
            //将内容刷新到文件
            mappedByteBuffer.force();
        } finally {
            //释放资源
            clean(mappedByteBuffer);
        }
    }

    /**
     * 写文件内容,用多线程进行并发写
     *
     * @param filePath
     * @param stringContent
     * @param isAppend
     */
    public static Future runWriting(final String filePath, final String stringContent, final boolean isAppend, long timeOut) {
        if (StringUtils.isBlank(filePath) || StringUtils.isBlank(stringContent)) {
            return null;
        }
        byte[] byteToWriting = stringContent.getBytes(Charset.forName("utf-8"));
        MappedByteBuffer mappedByteBuffer = getMappedByteBufferForWriting(filePath, byteToWriting.length, isAppend);
        Future future = executorService.submit(new Writer(mappedByteBuffer, byteToWriting));
        taskTimer.addTask(future, timeOut);
        return future;
    }

    public static void main(String[] args) {
        try {
            int maxsize = (0x3fffffff - 3) / 3;
            char[] chars = new char[maxsize];

            for (int i = 0; i < maxsize; i++) {
                chars[i] = 'b';
            }
            String str = new String(chars);
            long startTime = System.currentTimeMillis();
            List<Future> futures = new ArrayList<Future>();
            for (int j = 0; j < 1; j++) {
                futures.add(runWriting("/Users/bavatinolab/IdeaProjects/SimpleMock/test5.txt", str, true, 10000000));
            }
            for (Future future : futures) {
                try {
                    future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(System.currentTimeMillis() - startTime);
        } finally {
            shutdown();
        }

    }

    /**
     * 用于释放内存映射文件
     *
     * @param mappedByteBuffer
     */
    public static void clean(MappedByteBuffer mappedByteBuffer) {
        LocalFileUtils.clean(mappedByteBuffer);
    }

    public static class Writer implements Runnable {

        private MappedByteBuffer mappedByteBuffer;

        private byte[] bytesToWrite;

        public Writer(MappedByteBuffer mappedByteBuffer, byte[] bytesToWrite) {
            this.mappedByteBuffer = mappedByteBuffer;
            this.bytesToWrite = bytesToWrite;
        }

        @Override
        public void run() {
            write(this.mappedByteBuffer, this.bytesToWrite);
        }
    }

    /**
     * 停止线程池
     */
    public static void shutdown() {
        executorService.shutdownNow();
        timer.cancel();
    }
}
