package com.peterliu.peterrabbit.balance;

import java.nio.channels.Selector;

/**
 * Created by bavatinolab on 17/1/25.
 */
public interface Dispatcher {

    public Selector dispatch(Selector[] candidates);
}
