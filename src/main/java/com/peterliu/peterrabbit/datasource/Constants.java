package com.peterliu.peterrabbit.datasource;

import java.io.File;

/**
 * 配置常量
 * Created by bavatinolab on 17/1/24.
 */
public interface Constants {

    /**
     * mock文件存储路径
     */
    String MOCK_FILE_ROOT_PATH = "com.peterliu.peterrabbit.file.path";

    /**
     * mock文件存储后缀
     */
    String MOCK_FILE_SUFFIX = "com.peterliu.peterrabbit.file.suffix";

    /**
     * 默认的文件后缀,如果不给的话,默认设置为这个
     */
    String DEFAULT_FILE_SUFFIX = "com.peterliu.peterrabbit.file.suffix.default";

    /**
     * mock配置文件名称
     */
    String MOCK_PROPERTIES_FILE_NAME = "mock.properties";

    /**
     * 指定配置文件的属性名称,比如-Dsimplemock.datasource=mock.properties
     */
    String MOCK_JVM_CONFIGURATION_KEY = "peterrabbit.datasource";

    /**
     * 指定当前mock服务器监听的端口,默认是8300
     */
    String MOCK_SERVER_PORT = "com.peterliu.peterrabbit.server.port";

    /**
     * 指定当前的并发处理线程数, 默认是1个
     */
    String MOCK_SERVER_CONCURRENT_SIZE = "com.peterliu.peterrabbit.server.curr.size";

    /**
     * 处理器的并发线程数, 默认5个,队列100个
     */
    String MOCK_SERVER_CONCURRENT_HANDLER_CORESIZE = "com.peterliu.peterrabbit.handler.curr.coresize";

    /**
     * 处理器的处理超时时间,默认是15s
     */
    String MOCK_SERVER_CONCURRENT_HANDLER_TIMEOUT = "com.peterliu.peterrabbit.handler.curr.timeout";

    /**
     * 文件分割路径
     */
    String PATH_SEPARATOR = File.separator;

    /**
     * 默认的目录地址模板
     */
    String DEFAULT_INDEX_PAGE = "com.peterliu.peterrabbit.server.page.default.index";

    /**
     * 判断当前目录是否通过文件类型进行过滤
     */
    String FILE_FILTER_SWITCH = "com.peterliu.peterrabbit.server.file.dictionary.filter.switch";

    /**
     * 内部文件的根目录
     */
    String INNER_RESOURCES_PATH = "/~resources_path/";

    /**
     * 客户端缓存的最大时间, 1个小时
     */
    String CLIENT_RESOURCES_CACHE_MAX_AGE_SECONDS = "com.peterliu.peterrabbit.client.cache.maxage";

    /**
     * 判断是否需要进行加签
     */
    String MESSAGE_DIGEST_ETAG_ENABLE = "com.peterliu.peterrabbit.message.digest";
}
