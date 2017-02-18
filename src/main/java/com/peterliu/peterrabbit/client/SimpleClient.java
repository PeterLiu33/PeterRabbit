package com.peterliu.peterrabbit.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 一个用于测试的客户端
 * <p>
 * Created by bavatinolab on 17/2/2.
 */
public class SimpleClient {

    private static final Logger logger = Logger.getLogger(SimpleClient.class.getCanonicalName());

    private void connect(SocketChannel socketChannel) throws IOException {
        socketChannel.socket().setSoTimeout(10000);
        while (!socketChannel.finishConnect()) {
            logger.info("connecting...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("connected!");
    }

    public void run(final InetSocketAddress inetSocketAddress) {
        SocketChannel socketChannel = null;
        Timer timer = null;
        try {
            socketChannel = SocketChannel.open();
            //设置为非阻塞模式
            socketChannel.configureBlocking(false);
            socketChannel.connect(inetSocketAddress);
            connect(socketChannel);
            final SocketChannel finalSocketChannel = socketChannel;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (finalSocketChannel.isConnected()) {
                        try {
                            //发送心跳包
                            logger.info("sending urgentdata...");
                            finalSocketChannel.socket().sendUrgentData(0XFF);
                        } catch (IOException e) {
                            logger.log(Level.WARNING, "fail to send urgentdata", e);
                        }
                    } else {
                        try {
                            //重新链接
                            logger.info("reconnecting...");
                            finalSocketChannel.connect(inetSocketAddress);
                            connect(finalSocketChannel);
                        } catch (IOException e) {
                            logger.log(Level.WARNING, "fail to connect", e);
                        }
                    }
                }
            }, 0, 5000);
            logger.info("please enter txt:");
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                logger.info("please enter txt:");
                String HELLO_REQUEST = stdin.readLine().toString();
                if (HELLO_REQUEST.equalsIgnoreCase("end")) {
                    break;
                }

                logger.info("sending a request to HelloServer");
                ByteBuffer buffer = ByteBuffer.wrap(HELLO_REQUEST.getBytes());
                socketChannel.write(buffer);
            }
//            String saySome = "hello world! peterliu1233445555555555 你好";
//            byte[] bytes = saySome.getBytes();
//            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
//            byteBuffer.clear();
//            byteBuffer.put(bytes, 0, bytes.length);
//            byteBuffer.flip();
//            while(byteBuffer.hasRemaining()){
//                socketChannel.write(byteBuffer);
//            }
//
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            byteBuffer = ByteBuffer.allocate(bytes.length);
//            byteBuffer.clear();
//            byteBuffer.put(bytes, 0, bytes.length);
//            byteBuffer.flip();
//            while(byteBuffer.hasRemaining()){
//                socketChannel.write(byteBuffer);
//            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "fail to open socket", e);
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "fail to close socket", e);
                }
            }
            if(timer != null){
                timer.cancel();
            }
        }

    }

    public static void main(String[] args) throws UnknownHostException {
        new SimpleClient().run(new InetSocketAddress(InetAddress.getLocalHost(), 8300));
    }
}
