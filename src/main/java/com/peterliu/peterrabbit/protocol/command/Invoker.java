package com.peterliu.peterrabbit.protocol.command;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/2/4.
 */
public class Invoker {

    private static final Logger logger = Logger.getLogger(Invoker.class.getCanonicalName());

    List<Command> commands = new ArrayList<Command>();

    public Invoker addCommand(Command command, Resolver resolver){
        command.setResolver(resolver);
        commands.add(command);
        return this;
    }

    public void redo(){
        for(Command command : commands){
            try {
                command.execute();
            }catch (Exception e){
                logger.log(Level.WARNING, "fail to run execute command :" + command.getClass().getCanonicalName(), e);
            }
        }
    }

    public void undo(){
        for(Command command : commands){
            try {
                command.unExecute();
            }catch (Exception e){
                logger.log(Level.WARNING, "fail to run unexecute command :" + command.getClass().getCanonicalName(), e);
            }
        }
    }
}
