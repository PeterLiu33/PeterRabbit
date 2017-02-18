package com.peterliu.peterrabbit.protocol.command;

/**
 * Created by bavatinolab on 17/2/4.
 */
public abstract class CommandAdapter implements Command{

    private Resolver resolver;

    public CommandAdapter(){

    }

    public CommandAdapter(Resolver resolver){
        this.resolver = resolver;
    }

    @Override
    public final void setResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public final Resolver getResolver() {
        return resolver;
    }

}
