package com.peterliu.peterrabbit.balance;

import java.nio.channels.Selector;

/**
 * 从列表中获取最小的
 * Created by bavatinolab on 17/1/25.
 */
public class MiniPreferDispatcher implements Dispatcher {

    @Override
    public Selector dispatch(Selector[] candidates) {
        int loc = 0;
        int tempLoc = 0;
        int size = Integer.MAX_VALUE;
        int tempSize = 0;
        for (Selector selector : candidates) {
            if ((tempSize = selector.keys().size()) < size) {
                size = tempSize;
                loc = tempLoc;
            }
            tempLoc++;
        }
        return candidates[loc];
    }
}
