package com.peterliu.peterrabbit.protocol.http;

import com.peterliu.peterrabbit.protocol.Request;

import java.io.File;
import java.util.Map;

/**
 * Created by bavatinolab on 17/2/1.
 */
public class HttpRequest extends Request {

    /**
     * 用于指示是否需要判断缓存
     */
    private boolean isNeedJudgeCache = true;

    //客户端最大缓存事件,单位为s
    private long cacheMaxAge = 0;

    //访问外部文件夹
    private File dictionary = null;

    //文件地址
    private String filePath = null;

    private String messageDigest = null;

    /**
     * 断点续传使用
     */
    private int startRange = -1;

    private int endRange = -1;

    public int getStartRange() {
        return startRange;
    }

    public int getEndRange() {
        return endRange;
    }

    public static final String POST = "POST";

    public static final String GET = "GET";

    private Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpRequest setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public boolean isNeedJudgeCache() {
        return isNeedJudgeCache;
    }

    public void setNeedJudgeCache(boolean isNeedJudgeCache) {
        isNeedJudgeCache = isNeedJudgeCache;
    }

    public long getCacheMaxAge() {
        return cacheMaxAge;
    }

    public void setCacheMaxAge(long cacheMaxAge) {
        this.cacheMaxAge = cacheMaxAge;
    }

    public File getDictionary() {
        return dictionary;
    }

    public void setDictionary(File dictionary) {
        this.dictionary = dictionary;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMessageDigest() {
        return messageDigest;
    }

    public void setMessageDigest(String messageDigest) {
        this.messageDigest = messageDigest;
    }
}
