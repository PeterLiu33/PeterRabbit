package com.peterliu.peterrabbit.protocol;

import com.peterliu.peterrabbit.datasource.ConfigSource;

/**
 * 过滤器,所有信息都从上下文中获取。该链表是个双向链表
 *
 * @see Context
 * Created by bavatinolab on 17/2/1.
 */
public interface Filter {

    /**
     * 前置过滤器
     * @return true - 继续执行后续过滤器, false - 直接退出
     */
    public boolean doPre(ConfigSource configSource);

    /**
     * 后置过滤器
     */
    public void doPost(ConfigSource configSource);

    /**
     * 判断是否需要执行
     *
     * @param configSource
     * @return
     */
    public boolean check(ConfigSource configSource);

    /**
     * 下一个
     *
     * @return
     */
    public Filter next();

    /**
     * 前一个
     *
     * @return
     */
    public Filter prev();
}
