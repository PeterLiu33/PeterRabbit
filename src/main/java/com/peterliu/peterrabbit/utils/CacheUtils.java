package com.peterliu.peterrabbit.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 用于缓存
 * Created by bavatinolab on 17/2/21.
 */
public class CacheUtils {

    private static final Map<String, CacheObject> cache = new HashMap<String, CacheObject>();

    private static class CacheObject {

        long timeOut;

        CacheSource cacheSource;

        public CacheObject(long timeOut, CacheSource cacheSource) {
            this.timeOut = timeOut;
            this.cacheSource = cacheSource;
        }

        public long getTimeOut() {
            return timeOut;
        }

        public CacheSource getCacheSource() {
            return cacheSource;
        }
    }

    /**
     * 缓存工具
     *
     * @param timeUnit    最少分钟
     * @param time
     * @param cacheSource
     * @param <T>
     * @return
     */
    public static <T> void save(TimeUnit timeUnit, long time, CacheSource<T> cacheSource) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, (int) timeUnit.toMinutes(time));
        cache.put(cacheSource.getKey(), new CacheObject(calendar.getTimeInMillis(), cacheSource));
    }

    /**
     * 获取当前缓存值
     *
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T get(String key) {
        CacheObject cacheObject = cache.get(key);
        if (cacheObject != null) {
            if (System.currentTimeMillis() > cacheObject.getTimeOut()) {
                T t = (T) cacheObject.getCacheSource().refresh();
                if(t == null){
                    cache.remove(key);
                }
                return t;
            } else {
                return (T) cacheObject.getCacheSource().getCurrent();
            }
        }
        return null;
    }

}
