package com.peterliu.peterrabbit.channel.threadtimeout;

import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Future;

/**
 * 任务计时器
 * <p>
 * Created by bavatinolab on 17/1/25.
 */
public class TaskTimer extends TimerTask {

    private ConcurrentSkipListSet<TaskWithTimeOut> taskWithTimeOuts = new ConcurrentSkipListSet<TaskWithTimeOut>();

    @Override
    public void run() {
        Iterator<TaskWithTimeOut> iterator = taskWithTimeOuts.iterator();
        while (iterator.hasNext()) {
            TaskWithTimeOut taskWithTimeOut = iterator.next();
            if (taskWithTimeOut.isTimeOut()) {
                //直接将该线程终端
                //被中断后future.get会抛出java.util.concurrent.CancellationException异常
                taskWithTimeOut.getTask().cancel(true);
            }
            if (taskWithTimeOut.getTask().isCancelled()) {
                //如果中断成功,则从Set中去除
                iterator.remove();
            }
        }
    }

    /**
     * 添加任务
     *
     * @param task
     * @param timeOut
     */
    public void addTask(Future task, long timeOut) {
        taskWithTimeOuts.add(new TaskWithTimeOut(task, System.currentTimeMillis() + timeOut));
    }

}
