package com.peterliu.peterrabbit.protocol.command;

import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/2/4.
 */
public class RunCommand extends CommandAdapter {

    private static final Logger logger = Logger.getLogger(RunCommand.class.getCanonicalName());

    @Override
    public void execute() throws Exception {
        logger.info(">>>>>>>running runwork command......");
        getResolver().action(Resolver.ActionType.run);
    }

    @Override
    public void unExecute() {

    }
}
