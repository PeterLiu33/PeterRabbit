package com.peterliu.peterrabbit.datasource;

import com.peterliu.peterrabbit.utils.LocalFileUtils;
import com.peterliu.peterrabbit.utils.PropertyOrFileUtils;
import com.peterliu.peterrabbit.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据来源
 * <p>
 * Created by bavatinolab on 17/1/24.
 */
public class ConfigSourceImpl implements Constants, ConfigSource {

    private static final Map<String, String> configurations = new HashMap<String, String>();

    Properties properties = null;

    {

        //初始化配置参数信息
        String configurationFile = PropertyOrFileUtils.getSystemOrJVMProperty(MOCK_JVM_CONFIGURATION_KEY, null);
        if (StringUtils.isNotBlank(configurationFile)) {
            properties = LocalFileUtils.loadFileToProperties(configurationFile);
        }
        if (properties == null) {
            properties = new Properties();
        }
        //配置文件路径,如果没有指定,则使用当前目录下的mock文件夹
        configurations.put(MOCK_FILE_ROOT_PATH, getProperty(MOCK_FILE_ROOT_PATH, "" + PATH_SEPARATOR));
        //指定mock文件后缀
        configurations.put(MOCK_FILE_SUFFIX, getProperty(MOCK_FILE_SUFFIX, "json,java,html,htm,js,png,jpg,txt,css,pptx,vm,xml,jar,pdf,sh,bat,md,c,c++,jpeg,doc,docx,ppt,gif"));
        //指定默认的文件后缀
        configurations.put(DEFAULT_FILE_SUFFIX, getProperty(DEFAULT_FILE_SUFFIX, "json"));
        //指定服务器端口
        configurations.put(MOCK_SERVER_PORT, getProperty(MOCK_SERVER_PORT, "8300"));
        //指定并发线程处理数
        configurations.put(MOCK_SERVER_CONCURRENT_SIZE, getProperty(MOCK_SERVER_CONCURRENT_SIZE, "1"));
        //处理器的并发线程数, 默认5个,队列100个
        configurations.put(MOCK_SERVER_CONCURRENT_HANDLER_CORESIZE, getProperty(MOCK_SERVER_CONCURRENT_HANDLER_CORESIZE, "5"));
        //处理器的处理超时时间,默认是15s
        configurations.put(MOCK_SERVER_CONCURRENT_HANDLER_TIMEOUT, getProperty(MOCK_SERVER_CONCURRENT_HANDLER_TIMEOUT, "15000"));
        //处理器的处理超时时间,默认是15s
        configurations.put(DEFAULT_INDEX_PAGE, getProperty(DEFAULT_INDEX_PAGE, INNER_RESOURCES_PATH + "them" + PATH_SEPARATOR + "default" + PATH_SEPARATOR));
        //目录列表过滤开关,on-打开;off-关闭
        configurations.put(FILE_FILTER_SWITCH, getProperty(FILE_FILTER_SWITCH, "off"));
        //客户端缓存最大时间
        configurations.put(CLIENT_RESOURCES_CACHE_MAX_AGE_SECONDS, getProperty(CLIENT_RESOURCES_CACHE_MAX_AGE_SECONDS, "3600000"));
        //是否需要MD5加签
        configurations.put(MESSAGE_DIGEST_ETAG_ENABLE, getProperty(MESSAGE_DIGEST_ETAG_ENABLE, "false"));
    }

    private String getProperty(String name, String defaultValue) {
        return properties.getProperty(name,
                PropertyOrFileUtils.getSystemOrJVMProperty(name, defaultValue));
    }

    private ConfigSourceImpl() {

    }

    /**
     * 获取服务器监听端口
     *
     * @return
     */
    public int getServerPort() {
        return Integer.valueOf(configurations.get(MOCK_SERVER_PORT));
    }

    @Override
    public String getRootPath() {
        return configurations.get(MOCK_FILE_ROOT_PATH);
    }

    @Override
    public String getDefaultSuffix() {
        return configurations.get(DEFAULT_FILE_SUFFIX);
    }

    @Override
    public String[] getLegalSuffix() {
        return configurations.get(MOCK_FILE_SUFFIX).split(",");
    }

    @Override
    public int getCurrentSelectorSize() {
        return Integer.parseInt(configurations.get(MOCK_SERVER_CONCURRENT_SIZE));
    }

    @Override
    public int getHandlerCoreSize() {
        return Integer.parseInt(configurations.get(MOCK_SERVER_CONCURRENT_HANDLER_CORESIZE));
    }

    @Override
    public long getHandlerTimeOut() {
        return Long.parseLong(configurations.get(MOCK_SERVER_CONCURRENT_HANDLER_TIMEOUT));
    }



    @Override
    public String getDefaultIndexPage() {
        return getDefaultIndexPath() + "index.html";
    }

    @Override
    public String getDefaultIndexPath() {
        return configurations.get(DEFAULT_INDEX_PAGE);
    }

    @Override
    public boolean isDictionaryFilterOpen() {
        return "on".equalsIgnoreCase(configurations.get(FILE_FILTER_SWITCH));
    }

    @Override
    public long getClientMaxAge() {
        return Long.valueOf(configurations.get(CLIENT_RESOURCES_CACHE_MAX_AGE_SECONDS));
    }

    private static final ConfigSource INSTANCE = new ConfigSourceImpl();

    public static ConfigSource instance() {
        return INSTANCE;
    }

    @Override
    public boolean isMessageDigest() {
        return Boolean.parseBoolean(configurations.get(MESSAGE_DIGEST_ETAG_ENABLE));
    }
}
