package com.peterliu.peterrabbit.datasource;

import com.peterliu.peterrabbit.utils.LocalFileUtils;
import com.peterliu.peterrabbit.utils.StringUtils;

/**
 * Created by bavatinolab on 17/1/25.
 */
public class FileSourceImpl implements FileSource, Constants {

    ConfigSource configSource = ConfigSourceImpl.instance();

    /**
     * 从指定文件加载对应的mock结果
     *
     * @param app
     * @param proxyID
     * @return
     */
    public String loadMockResult(String app, String proxyID) {
        String filePath = configSource.getRootPath();
        if (StringUtils.isNotBlank(app)) {
            filePath = filePath + app + PATH_SEPARATOR;
        }
        if (StringUtils.isNotBlank(proxyID)) {
            filePath = filePath + proxyID + configSource.getLegalSuffix()[0];
        }
        return LocalFileUtils.loadFileToString(filePath);
    }
}
