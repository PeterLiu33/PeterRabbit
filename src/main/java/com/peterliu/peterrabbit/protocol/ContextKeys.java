package com.peterliu.peterrabbit.protocol;

/**
 * Created by bavatinolab on 17/2/1.
 */
public interface ContextKeys {

    /**
     * 协议处理器
     */
    String PROTOCOL_HANDLER = "protocolHandler";

    /**
     * 过滤器
     */
    String FILTER = "filter";

    /**
     * 请求对象
     */
    String REQUEST = "request";

    /**
     * 响应对象
     */
    String RESPONSE = "response";

    /**
     * UTF-8的负载数据
     */
    String LOADER_STRING = "loaderStr";

    /**
     * 任务数据
     */
    String TASK_DATA = "taskData";

    /**
     * 缓存数据
     */
    String CASH_DATA = "cashData";

    /**
     * 协议类型
     */
    String PROTOCOL_TYPE = "protocolType";

    /**
     * 任务信息
     */
    String WORK = "work";

    /**
     * 获取文件后缀信息
     */
    String FILE_SUFFIX = "fileSuffix";

    /**
     * 文件对象
     */
    String FILE_OBJECT = "fileObject";
}
