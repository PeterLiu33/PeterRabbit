package com.peterliu.peterrabbit.datasource;

/**
 * 读取文件的接口
 *
 * Created by bavatinolab on 17/1/25.
 */
public interface FileSource {

    /**
     * 用于读取文件
     *
     * @param app
     * @param proxyID
     * @return
     */
    String loadMockResult(String app, String proxyID);
}
