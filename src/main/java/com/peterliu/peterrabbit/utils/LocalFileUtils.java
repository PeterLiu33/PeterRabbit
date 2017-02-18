package com.peterliu.peterrabbit.utils;


import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.Constants;
import com.peterliu.peterrabbit.protocol.Context;
import com.peterliu.peterrabbit.protocol.http.HttpRequest;
import sun.nio.ch.DirectBuffer;

import java.io.*;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 读取本地文件工具类
 * <p>
 * Created by bavatinolab on 17/1/24.
 */
public abstract class LocalFileUtils {

    private static final Logger logger = Logger.getLogger(LocalFileUtils.class.getCanonicalName());

    public static void main(String[] args) {
//        String s = LocalFileUtils.loadFileToString("/Users/bavatinolab/IdeaProjects/SimpleMock/test1.txt");
//        Properties properties = loadFileToProperties("/Users/bavatinolab/IdeaProjects/SimpleMock/src/test/resources/tesv");
////        System.out.println(properties.getProperty("test"));
////        String s = LocalFileUtils.loadFileToString("test.vm");
////        System.out.println(s);
////        s = LocalFileUtils.loadFileToString("mock/test.vm");
//        System.out.println(s);
////        System.out.println(System.getProperty("user.dir"));
////        File directory = new File("./com/peterliu/peterrabbit/LocalFileUtils.class");
////        System.out.println(directory.getAbsolutePath());
////        try {
////            System.out.println(directory.getCanonicalPath());
////        } catch (IOException e) {
////            ;
////        }
////        System.out.println(directory.getPath());
//        LocalFileUtils.writeStringToFile("123", "/Users/bavatinolab/IdeaProjects/SimpleMock/test4.txt", true);
        int maxsize = (0x3fffffff - 3) / 3;
        char[] chars = new char[maxsize];

        for (int i = 0; i < maxsize; i++) {
            chars[i] = 'b';
        }
        String str = new String(chars);
        long startTime = System.currentTimeMillis();
//        for(int j=0 ; j< 20; j++) {
//            LocalFileUtils.appendStringToLargeFile(str, "/Users/bavatinolab/IdeaProjects/SimpleMock/test3.txt", true);
//        }
        System.out.println(System.currentTimeMillis() - startTime);
        startTime = System.currentTimeMillis();
        for (int j = 0; j < 1; j++) {
            LocalFileUtils.writeStringToFile(str, "/Users/bavatinolab/IdeaProjects/SimpleMock/test4.txt", true);
        }
        System.out.println(System.currentTimeMillis() - startTime);
//        LocalFileUtils.writeStringToFile("爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc爽肤水的方式吧abc", "/Users/bavatinolab/IdeaProjects/SimpleMock/test.txt", true);

    }

    /**
     * 从系统文件中,获取properties
     *
     * @param filePath
     * @return
     */
    public static Properties loadFileToProperties(final String filePath) {
        InputStream inputStream = null;
        try {
            Properties properties = new Properties();
            properties.load(loadFileToInputStream(filePath));
            return properties;
        } catch (IOException e) {
            ;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
        return null;
    }

    /**
     * 从系统文件中获取文件,并转换为InputStream
     *
     * @param filePath
     * @return
     */
    public static InputStream loadFileToInputStream(final String filePath) {
        try {
            return new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            ;
        }
        return null;
    }

    /**
     * 将内容写入文件中,如果文件已经存在,则附加在后面,否则新建文件
     *
     * @param stringContent
     * @param filePath
     * @param isAppend      判断是否追加还是覆盖
     */
    public static boolean writeStringToFile(final String stringContent, final String filePath, final boolean isAppend) {
        return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            @Override
            public Boolean run() {
                if (StringUtils.isBlank(stringContent)) {
                    return false;
                }
                File file = checkFile(filePath);
                if (file == null) {
                    //文件读取失败
                    return false;
                }
                RandomAccessFile randomAccessFile = null;
                FileChannel fileChannel = null;
                ByteBuffer byteBuffer = null;
                try {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    fileChannel = randomAccessFile.getChannel();
                    byte[] bytes = stringContent.getBytes(Charset.forName("utf-8"));
                    byteBuffer = ByteBuffer.wrap(bytes);
                    int byteLength = bytes.length, length = 0;
                    if (isAppend) {
                        //判断是追加
                        fileChannel.position(file.length());
                    } else {
                        fileChannel.position(0);
                        //强制指定长度
                        fileChannel.truncate(byteLength);
                    }
                    while (byteBuffer.hasRemaining()) {
                        fileChannel.write(byteBuffer);
                    }
                    //强制刷新到文件系统
                    fileChannel.force(true);
                    return true;
                } catch (FileNotFoundException e) {
                    //文件读取失败
                    return false;
                } catch (IOException e) {
                    //写文件失败
                    return false;
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
                    if (byteBuffer != null) {
                        byteBuffer.clear();
                    }
                }
            }
        });
    }

    /**
     * 判断当前文件是否存在,如果不存在则创建,否则直接返回File描述文件对象
     * <pre>
     *     注意:如果该文件是一个目录,则直接返回null
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static File checkFile(final String filePath) {

        if (StringUtils.isNotBlank(filePath)) {
            final File file = new File(filePath);
            if (file.isDirectory()) {
                //如果是目录,则直接返回null
                return null;
            }
            if (file.exists()) {
                //如果文件已经存在,则直接返回文件描述对象
                return file;
            } else {
                //如果文件不存在,则直接创建文件
                return AccessController.doPrivileged(new PrivilegedAction<File>() {
                    @Override
                    public File run() {
                        try {
                            boolean success = file.createNewFile();
                            if (success) {
                                return file;
                            }
                            return null;
                        } catch (IOException e) {

                        }
                        return null;
                    }
                });
            }
        }

        return null;
    }

    /**
     * 采用NIO读取文件
     *
     * @param filePath
     * @return
     */
    public static String loadFileToString(final String filePath) {
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            @Override
            public String run() {
                FileChannel channel = null;
                RandomAccessFile randomAccessFile = null;
                ByteBuffer byteBuffer = null;//字节码源码
                CharBuffer charBuffer = null;//字符码解码
                //解码器
                CharsetDecoder charsetDecoder = Charset.forName("utf-8").newDecoder();
                try {
                    randomAccessFile = new RandomAccessFile(filePath, "r");
                    if (randomAccessFile == null) {
                        return null;
                    }
                    channel = randomAccessFile.getChannel();
                    byteBuffer = ByteBuffer.allocate(1024);
                    charBuffer = CharBuffer.allocate(1024);
                    int readByte = channel.read(byteBuffer);
                    StringBuffer stringBuffer = new StringBuffer("");
                    while (readByte != -1) {
                        //状态翻转,从写模式切换为读模式
                        byteBuffer.flip();
                        //解码并输入到charBuffer
                        charsetDecoder.decode(byteBuffer, charBuffer, false);
                        charBuffer.flip();
                        while (charBuffer.hasRemaining()) {
                            //将所有数据都读取完毕
                            stringBuffer.append(charBuffer.get());
                        }
                        //清空全部缓冲区
                        byteBuffer.clear();
                        charBuffer.clear();
                        readByte = channel.read(byteBuffer);
                    }
                    return stringBuffer.toString();
                } catch (FileNotFoundException e) {
                    return null;
                } catch (IOException e) {
                    return null;
                } finally {
                    if (randomAccessFile != null) {
                        try {
                            randomAccessFile.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                    if (byteBuffer != null) {
                        byteBuffer.clear();
                    }
                    if (charBuffer != null) {
                        charBuffer.clear();
                    }
                }
            }
        });
    }

    /**
     * 释放MappedBytedBuffer
     *
     * @param buffer
     * @throws Exception
     */
    public static void clean(final Object buffer) {
        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                try {
                    if(buffer instanceof DirectBuffer){
                        ((DirectBuffer)buffer).cleaner().clean();
                    }
//                    if(!(buffer instanceof MappedByteBuffer)){
//                        return null;
//                    }
//                    Method getCleanerMethod = buffer.getClass().getMethod("cleaner", new Class[0]);
//                    getCleanerMethod.setAccessible(true);
//                    sun.misc.Cleaner cleaner = (sun.misc.Cleaner) getCleanerMethod.invoke(buffer, new Object[0]);
//                    cleaner.clean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

    }

    /**
     * 采用内存映射文件方式写入大文件,写入方式为追加
     *
     * @param stringContent
     * @param filePath
     * @param isAppend      true-追加方式,false-非追加方式
     * @return
     */
    public static boolean appendStringToLargeFile(final String stringContent, final String filePath, final boolean isAppend) {
        return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            @Override
            public Boolean run() {
                if (StringUtils.isBlank(stringContent)) {
                    return false;
                }
                File file = checkFile(filePath);
                if (file == null) {
                    //获取文件失败
                    return false;
                }

                RandomAccessFile randomAccessFile = null;
                FileChannel fileChannel = null;
                MappedByteBuffer mappedByteBuffer = null;
                try {
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    fileChannel = randomAccessFile.getChannel();
                    byte[] bytes = stringContent.getBytes(Charset.forName("utf-8"));
                    long offset = 0, byteLength = bytes.length, length = file.length();
                    if (isAppend) {
                        //判断是追加
                        offset = length;
                        fileChannel.position(length);
                        fileChannel.truncate(length + byteLength);
                    } else {
                        fileChannel.position(0);
                        //强制指定长度
                        fileChannel.truncate(byteLength);
                    }
                    //把文件的更改刷新到文件
                    fileChannel.force(true);
                    mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, byteLength);
                    //内存映射文件和fileChannel以及randomAccessFile无关
                    fileChannel.close();
                    randomAccessFile.close();

                    //写内容
                    mappedByteBuffer.put(bytes, 0, bytes.length);
                    //把内容刷到文件中去
                    mappedByteBuffer.force();
                    return true;
                } catch (FileNotFoundException e) {
                    return false;
                } catch (IOException e) {
                    return false;
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
                    if (mappedByteBuffer != null) {
                        //释放内存映射文件
                        clean(mappedByteBuffer);
                        mappedByteBuffer = null;
                    }
                }
            }
        });
    }

    public static String checkAndModifyPath(String url, ConfigSource configSource) {
        Context context = Context.getCurrentContext();
        String filePath = ((HttpRequest)context.getRequest()).getFilePath();
        //检查后缀名是有后缀名
        int index = filePath.lastIndexOf("/");
        String suffix = null;
        if (index > -1) {
            suffix = filePath.substring(index + 1);
        } else {
            suffix = filePath;
        }
        if (StringUtils.isNotBlank(suffix)) {
            index = suffix.lastIndexOf(".");
            if (index > -1) {
                suffix = suffix.substring(index + 1);
            } else {
                //设置默认的文件后缀
                context.setFileType(configSource.getDefaultSuffix());
                return filePath.trim() + "." + configSource.getDefaultSuffix();
            }
            if (StringUtils.isNotBlank(suffix)) {
                context.setFileType(suffix);
                return filePath.trim();
            } else {
                //设置默认的文件后缀
                context.setFileType(configSource.getDefaultSuffix());
                return filePath.trim() + configSource.getDefaultSuffix();
            }
        } else {
            return null;
        }
    }

    /**
     * 判断是否为目录, 如果是目录, 则返回目录File,否则为null
     *
     * @param url
     * @param configSource
     * @return
     */
    public static File checkDictionary(String url, ConfigSource configSource) {
        Context context = Context.getCurrentContext();
        String filePath = ((HttpRequest)context.getRequest()).getFilePath();
        final File file = new File(filePath);
        if (file.isDirectory()) {
            //如果是目录,则直接返回null
            return file;
        }else if(file.exists()){
            //设置文件对象
            context.setFileObject(file);
        }
        return null;
    }
}
