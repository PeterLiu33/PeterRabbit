package com.peterliu.peterrabbit;

import com.peterliu.peterrabbit.channel.ChannelType;
import com.peterliu.peterrabbit.channel.SelectorRegister;
import com.peterliu.peterrabbit.datasource.ConfigSource;
import com.peterliu.peterrabbit.datasource.ConfigSourceImpl;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by bavatinolab on 17/1/24.
 */
public class Peter {

    public static void main(String[] args) throws UnknownHostException {
        ConfigSource configSource = ConfigSourceImpl.instance();
        SelectorRegisterFactory.init(ChannelType.READ_FILE, InetAddress.getLocalHost(), configSource.getServerPort());
        SelectorRegister selectorRegister = SelectorRegisterFactory.getRegister(ChannelType.READ_FILE);
        selectorRegister.start();
    }
}
