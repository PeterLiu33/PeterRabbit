package com.peterliu.peterrabbit.protocol;

import com.peterliu.peterrabbit.datasource.ConfigSource;

/**
 * 适配器
 * Created by bavatinolab on 17/2/1.
 */
public abstract class FilterAdapter implements Filter {

    private Filter prevFilter = null;

    private Filter nextFilter = null;

    @Override
    public boolean check(ConfigSource configSource) {
        return true;
    }

    public final FilterAdapter setPrevFilter(Filter prevFilter) {
        this.prevFilter = prevFilter;
        return this;
    }

    public final FilterAdapter setNextFilter(Filter nextFilter) {
        this.nextFilter = nextFilter;
        return this;
    }

    @Override
    public boolean doPre(ConfigSource configSource) {
        return true;
    }

    @Override
    public void doPost(ConfigSource configSource) {

    }

    @Override
    public final Filter next() {
        return this.nextFilter;
    }

    @Override
    public final Filter prev() {
        return this.prevFilter;
    }
}
