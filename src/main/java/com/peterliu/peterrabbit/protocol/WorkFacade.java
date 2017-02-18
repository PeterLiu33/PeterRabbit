package com.peterliu.peterrabbit.protocol;

import com.peterliu.peterrabbit.channel.SelectorRegister;
import com.peterliu.peterrabbit.work.Work;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bavatinolab on 17/2/6.
 */
public class WorkFacade implements Work {

    private Map<ProtocolType, Work> protocolTypeWorkMap = new HashMap<ProtocolType, Work>();

    public WorkFacade(Work... works){
        for(Work work : works){
            protocolTypeWorkMap.put(work.getProtocolType(), work);
        }
    }

    @Override
    public <T extends Response> T run() {
        Context context = Context.getCurrentContext();
        ProtocolType protocolType = context.getProtocolType();
        Work work1 = protocolTypeWorkMap.get(protocolType);
        if(work1 != null){
            return work1.run();
        }
        return (T) new Response().setRequest(context.getRequest()).setContent("unsupported protocol!");
    }

    @Override
    public SelectorRegister getSelectorRegister() {
        return null;
    }

    @Override
    public ProtocolType getProtocolType() {
        return null;
    }
}
