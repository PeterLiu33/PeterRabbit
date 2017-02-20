package com.peterliu.peterrabbit.utils;

/**
 * Created by bavatinolab on 17/2/21.
 */
public interface CacheSource<T> {

    /**
     * 缓存的主键
     *
     * @return
     */
    String getKey();

    /**
     * 返回当前缓存的
     *
     * @return
     */
    T getCurrent();

    /**
     * 获取新的
     *
     * @return
     */
    T refresh();
}
