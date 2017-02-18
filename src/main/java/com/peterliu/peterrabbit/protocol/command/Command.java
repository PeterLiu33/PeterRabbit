package com.peterliu.peterrabbit.protocol.command;

/**
 * Created by bavatinolab on 17/2/4.
 */
public interface Command {

    void execute() throws Exception;

    void unExecute();

    void setResolver(Resolver resolver);

    Resolver getResolver();
}
