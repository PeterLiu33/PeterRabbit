package com.peterliu.peterrabbit.channel;

/**
 * Created by bavatinolab on 17/1/25.
 */
public abstract class HandlerFactoryStrategy {

    public static ChannelHandlerFactory getFactory(){
        return new DefaultChannelHandlerFactory();
    }
}
