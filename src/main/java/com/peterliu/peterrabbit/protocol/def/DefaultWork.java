package com.peterliu.peterrabbit.protocol.def;

import com.peterliu.peterrabbit.channel.SelectorRegister;
import com.peterliu.peterrabbit.protocol.ProtocolType;
import com.peterliu.peterrabbit.protocol.Response;
import com.peterliu.peterrabbit.work.Work;

/**
 * Created by bavatinolab on 17/2/6.
 */
public class DefaultWork implements Work {

    private SelectorRegister selectorRegister;

    public DefaultWork(SelectorRegister selectorRegister){
        this.selectorRegister = selectorRegister;
    }

    @Override
    public <T extends Response> T run() {
        return null;
    }

    @Override
    public SelectorRegister getSelectorRegister() {
        return this.selectorRegister;
    }

    @Override
    public ProtocolType getProtocolType() {
        return ProtocolType.def;
    }
}
