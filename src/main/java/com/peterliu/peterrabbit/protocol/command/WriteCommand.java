package com.peterliu.peterrabbit.protocol.command;

import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/2/4.
 */
public class WriteCommand extends CommandAdapter {

    private static final Logger logger = Logger.getLogger(WriteCommand.class.getCanonicalName());

    @Override
    public void execute() throws Exception {
        logger.info(">>>>>>>running write command......");
        getResolver().action(Resolver.ActionType.write);
    }

    @Override
    public void unExecute() {

    }
}
