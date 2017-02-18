package com.peterliu.peterrabbit.utils;


import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用来加载属性信息
 * <p/>
 * Created by liujun on 17/1/23.
 */
public abstract class PropertyOrFileUtils {

    private static final Logger logger = Logger.getLogger(PropertyOrFileUtils.class.getName());

    /**
     * 从系统属性或JVM属性中获取对应的配置信息
     *
     * @param key 属性名
     * @param def 默认取值
     * @return
     */
    public static String getSystemOrJVMProperty(final String key, final String def) {
        try {
            return AccessController.doPrivileged(new PrivilegedAction<String>() {
                @Override
                public String run() {
                    return System.getProperty(key, def);
                }
            });
        } catch (Exception e) {
            return def;
        }
    }

    /**
     * 从指定文件中,获取配置属性, 获取第一个获得的属性文件
     *
     * @param fileName
     * @return
     */
    public static Properties loadPropertyFile(String fileName) {
        Enumeration enumeration = getResources(getClassLoader(), fileName);
        if (enumeration != null) {
            URL url = null;
            InputStream inputStream = null;
            Properties properties = null;
            try {
                while (enumeration.hasMoreElements()) {
                    url = (URL) enumeration.nextElement();
                    inputStream = getInputStream(url);
                    if (inputStream != null) {
                        properties = new Properties();
                        try {
                            properties.load(inputStream);
                            return properties;
                        } catch (IOException e) {
                            ;
                        }
                    }

                }
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        ;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 从当前线程中获取ClassLoader
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        try {
            return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return Thread.currentThread().getContextClassLoader();
                }
            });
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 从classpath根目录寻找指定路径的filename
     *
     * @param classLoader
     * @param fileName
     * @return
     */
    public static Enumeration getResources(final ClassLoader classLoader, final String fileName) {
        try {
            return AccessController.doPrivileged(new PrivilegedAction<Enumeration>() {
                @Override
                public Enumeration run() {
                    try {
                        if (classLoader != null) {
                            return classLoader.getResources(fileName);
                        } else {
                            return ClassLoader.getSystemResources(fileName);
                        }
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "fail to load properties file, " + fileName);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 该方法只会从class根目录下去找,不会找子目录的内容
     *
     * @param classLoader
     * @param fileName
     * @return
     */
    public static URL getResource(final ClassLoader classLoader, final String fileName) {
        try {
            return AccessController.doPrivileged(new PrivilegedAction<URL>() {
                @Override
                public URL run() {
                    if (classLoader != null) {
                        return classLoader.getResource(fileName);
                    } else {
                        return ClassLoader.getSystemResource(fileName);
                    }
                }
            });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 采用指定的classLoader, 加载指定的file,只会从classPath根目录文件
     *
     * @param classLoader
     * @param fileName
     * @return
     */
    public static String getFileAsString(final ClassLoader classLoader, final String fileName) {
        URL url = getResource(classLoader, fileName);
        return getFileAsString(url);
    }

    /**
     * 采用指定的classLoader, 加载指定的目录列表,只会从classPath根目录文件
     *
     * @param classLoader
     * @param filePath
     * @return
     */
    public static String[] getFileDictionary(final ClassLoader classLoader, String filePath, String suffix) {
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        URL url = getResource(classLoader, filePath);
        if (url != null) {
            JarFile jarFile = null;
            List<String> fileList = new ArrayList<String>();
            try {
                URLConnection urlConnection = url.openConnection();
                if (urlConnection != null) {
                    if (urlConnection instanceof JarURLConnection) {
                        //是jar包,未解压
                        JarURLConnection jarURLConnection = (JarURLConnection) urlConnection;
                        jarFile = jarURLConnection.getJarFile();
                        Enumeration<JarEntry> entrys = jarFile.entries();
                        while (entrys.hasMoreElements()) {
                            JarEntry imagEntry = entrys.nextElement();
                            String name = imagEntry.getName();
                            if (name.startsWith(filePath)) {
                                name = name.substring(filePath.length());
                                if (name.matches("[^\\/]+\\" + suffix)) {
                                    fileList.add(name);
                                }
                            }
                        }
                    } else {
                        //是文件,已经解压
                        urlConnection.setUseCaches(false);
                        String result = toString(urlConnection.getInputStream());
                        String[] files = result.split("\n");
                        if (files != null && files.length > 0) {
                            for (String name : files) {
                                if (name.matches("[^\\/]+\\" + suffix)) {
                                    fileList.add(name);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                ;
            } finally {
                if (jarFile != null) {
                    try {
                        jarFile.close();
                    } catch (IOException e) {
                        ;
                    }
                }

            }
            return fileList.toArray(new String[]{});
        }
        return null;
    }

    /**
     * 将内部文件读取转换为ByteBuffer
     *
     * @param classLoader
     * @param fileName
     * @return
     */
    public static ByteBuffer getFileAsByteBuffer(final ClassLoader classLoader, final String fileName) {
        URL url = getResource(classLoader, fileName);
        InputStream inputStream = getInputStream(url);
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        if (inputStream != null) {
            try {
                bufferedInputStream = new BufferedInputStream(inputStream);
                int length = 1024;
                byteArrayOutputStream = new ByteArrayOutputStream(length);
                byte[] bytes = new byte[length];
                int len = -1;
                while ((len = bufferedInputStream.read(bytes, 0, length)) != -1) {
                    byteArrayOutputStream.write(bytes, 0, len);
                }
                return ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                logger.log(Level.WARNING, "fail to read bytes from inner file", e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        ;
                    }
                }
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        ;
                    }
                }
                if (byteArrayOutputStream != null) {
                    try {
                        byteArrayOutputStream.close();
                    } catch (IOException e) {
                        ;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 采用指定的classLoader, 加载指定的file,只会从classPath根目录文件
     *
     * @param url
     * @return
     */
    public static String getFileAsString(URL url) {
        InputStream inputStream = getInputStream(url);
        return getFileAsString(inputStream);
    }

    /**
     * 采用指定的classLoader, 加载指定的file,只会从classPath根目录文件
     *
     * @param inputStream
     * @return
     */
    public static String getFileAsString(InputStream inputStream) {
        if (inputStream != null) {
            try {
                return toString(inputStream);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        ;
                    }
                }
            }
        }
        return null;
    }

    public static String toString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                ;
            }
        }

        return sb.toString();
    }

    /**
     * 注意InputStream用完要关闭, 该方法无法读取文件列表
     *
     * @param url
     * @return
     */
    public static InputStream getInputStream(final URL url) {
        if (url != null) {
            return AccessController.doPrivileged(new PrivilegedAction<InputStream>() {
                @Override
                public InputStream run() {
                    try {
                        URLConnection urlConnection = url.openConnection();
                        urlConnection.setUseCaches(false);
                        return urlConnection.getInputStream();
                    } catch (IOException e) {
                        ;
                    }
                    return null;
                }
            });

        }
        return null;
    }
}
