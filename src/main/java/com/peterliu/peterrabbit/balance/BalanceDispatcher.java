package com.peterliu.peterrabbit.balance;

import java.nio.channels.Selector;

/**
 * 依据当前的负载,进行智能分配
 * <p>
 * Created by bavatinolab on 17/1/25.
 */
public class BalanceDispatcher implements Dispatcher {

    Dispatcher dispatcher;

    private BalanceDispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public static Selector run(Selector[] candidates, DispatcherStrategy strategy) {
        if (candidates == null || candidates.length == 0) {
            return null;
        }
        switch (strategy) {
            case MINI_PREFER:
                return new BalanceDispatcher(new MiniPreferDispatcher()).dispatch(candidates);
            default:
                //默认分配给第一个
                return candidates[0];
        }
    }

    @Override
    public Selector dispatch(Selector[] candidates) {
        return this.dispatcher.dispatch(candidates);
    }
}
