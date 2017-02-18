package com.peterliu.peterrabbit;

import com.peterliu.peterrabbit.channel.ChannelType;
import com.peterliu.peterrabbit.channel.SelectorRegister;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bavatinolab on 17/1/30.
 */
public abstract class SelectorRegisterFactory {

    static final Map<ChannelType, SelectorRegister> channelTypeSelectorRegisters = new ConcurrentHashMap<ChannelType, SelectorRegister>();

    public static SelectorRegister init(ChannelType type, InetAddress address, int port) {
        if (!channelTypeSelectorRegisters.containsKey(type)) {
            channelTypeSelectorRegisters.putIfAbsent(type, new SelectorRegister(type, new InetSocketAddress(address, port)));
        }
        return channelTypeSelectorRegisters.get(type);
    }

    public static SelectorRegister getRegister(ChannelType type) {
        return channelTypeSelectorRegisters.get(type);
    }
}
