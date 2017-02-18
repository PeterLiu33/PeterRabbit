package com.peterliu.peterrabbit.protocol.command;

import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/2/4.
 */
public class CleanCommand extends CommandAdapter {

    private static final Logger logger = Logger.getLogger(CleanCommand.class.getCanonicalName());

    @Override
    public void execute() throws Exception {
        logger.info(">>>>>>>running clean command......");
        getResolver().action(Resolver.ActionType.clean);
    }

    @Override
    public void unExecute() {

    }
}
