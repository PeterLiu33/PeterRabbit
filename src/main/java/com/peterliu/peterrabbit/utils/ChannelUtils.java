package com.peterliu.peterrabbit.utils;

import com.peterliu.peterrabbit.channel.TaskData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.logging.Logger;

/**
 * 渠道工具类
 * <p>
 * Created by bavatinolab on 17/1/29.
 */
public abstract class ChannelUtils {

    private static final Logger logger = Logger.getLogger(ChannelUtils.class.getCanonicalName());

    /**
     * 判断是否为心跳包, 没有意义,无法重复读
     *
     * @param channel
     * @param stringBuffer 读取出来的数据
     * @return
     */
    public static boolean isNormalData(ReadableByteChannel channel, StringBuffer stringBuffer) {
        if (channel == null) {
            return false;
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(TaskData.UrgentDataByteLength);
        try {
            int read = channel.read(byteBuffer);
            if (read == TaskData.UrgentDataByteLength) {
                //尝试读取10位,如果大于1,则视为正常包,否则为心跳包
                byteBuffer.flip();
                CharBuffer charBuffer = CharsetUtils.deCode(byteBuffer);
                while (charBuffer.hasRemaining()) {
                    //将所有数据都读取完毕
                    stringBuffer.append(charBuffer.get());
                }
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            logger.info("fail to read urgent data from channel");
        }
        return false;
    }


    public static void write(WritableByteChannel channel, ByteBuffer byteBuffer) throws IOException {
        while (byteBuffer.hasRemaining()){
            channel.write(byteBuffer);
        }
    }

    /**
     * 从通道中读取数据
     *
     * @param channel
     * @return
     */
    public static byte[] readByte(ReadableByteChannel channel) {
        if (channel == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        try {
            int readBytes = channel.read(byteBuffer);
            while (readBytes > 0) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    byteArrayOutputStream.write(byteBuffer.get());
                }
                byteBuffer.clear();
                readBytes = channel.read(byteBuffer);
            }
        } catch (IOException e) {
            logger.info("fail to read data from channel");
        }
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (bytes != null && bytes.length > 0) {
            return bytes;
        }
        return null;
    }

    /**
     * 采用UTF-8解码,从通道中获取数据
     *
     * @param channel
     * @return
     */
    public static String read(ReadableByteChannel channel) {
        try {
            byte[] bytes = readByte(channel);
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.info("fail to parse data into utf-8");
        }
        return null;
    }


}
