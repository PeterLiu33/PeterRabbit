package com.peterliu.peterrabbit.protocol.command;

/**
 * Created by bavatinolab on 17/2/4.
 */
public interface Resolver {


    void action(ActionType actionType) throws Exception;

    /**
     * 命令类型
     */
    public enum ActionType {
        pre,
        run,
        post,
        write,
        clean;
    }


}
