package com.peterliu.peterrabbit.protocol.command;

import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/2/4.
 */
public class PreFilterCommand extends CommandAdapter {

    private static final Logger logger = Logger.getLogger(PreFilterCommand.class.getCanonicalName());

    @Override
    public void execute() throws Exception {
        logger.info(">>>>>>>running pre command......");
        getResolver().action(Resolver.ActionType.pre);
    }

    @Override
    public void unExecute() {

    }

}
