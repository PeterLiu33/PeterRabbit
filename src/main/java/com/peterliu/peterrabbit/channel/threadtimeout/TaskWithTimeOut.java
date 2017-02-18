package com.peterliu.peterrabbit.channel.threadtimeout;

import java.util.concurrent.Future;

/**
 * 用于记录需要超时处理的任务
 * Created by bavatinolab on 17/1/25.
 */
public class TaskWithTimeOut implements Comparable{

    private Future task;

    /**
     * 超时时间
     */
    private long timeOut;


    public TaskWithTimeOut(Future task, long timeOut) {
        this.task = task;
        this.timeOut = timeOut;
    }

    public Future getTask() {
        return task;
    }

    /**
     * 判断当前任务是否已经超时
     *
     * @return
     */
    public boolean isTimeOut() {
        if (System.currentTimeMillis() > this.timeOut) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
