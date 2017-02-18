package com.peterliu.peterrabbit.datasource;

/**
 * 配置数据来源接口
 * <p>
 * Created by bavatinolab on 17/1/25.
 */
public interface ConfigSource {

    /**
     * 获取监听端口
     *
     * @return
     */
    int getServerPort();

    /**
     * 获取虚拟文件根目录
     *
     * @return
     */
    String getRootPath();

    /**
     * 获取默认的文件后缀
     *
     * @return
     */
    String getDefaultSuffix();

    /**
     * 获取虚拟文件系统合法的文件后缀
     *
     * @return
     */
    String[] getLegalSuffix();

    /**
     * 获取多路复用器个数
     *
     * @return
     */
    int getCurrentSelectorSize();

    /**
     * 获取通道处理器核心线程数
     *
     * @return
     */
    int getHandlerCoreSize();

    /**
     * 获取通道处理器超时时间
     *
     * @return
     */
    long getHandlerTimeOut();

    /**
     * 获取默认的目录页
     *
     * @return
     */
    String getDefaultIndexPage();

    /**
     * 获取默认的目录页所在地址
     *
     * @return
     */
    String getDefaultIndexPath();

    /**
     * 获取目录过滤开关参数配置
     *
     * @return
     */
    boolean isDictionaryFilterOpen();

    /**
     * 获取客户端缓存最大时间
     *
     * @return
     */
    long getClientMaxAge();
}
