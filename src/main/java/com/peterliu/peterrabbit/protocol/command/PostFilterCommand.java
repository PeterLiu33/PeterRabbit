package com.peterliu.peterrabbit.protocol.command;

import java.util.logging.Logger;

/**
 * Created by bavatinolab on 17/2/4.
 */
public class PostFilterCommand extends CommandAdapter {

    private static final Logger logger = Logger.getLogger(PostFilterCommand.class.getCanonicalName());

    @Override
    public void execute() throws Exception {
        logger.info(">>>>>>>running post command......");
        getResolver().action(Resolver.ActionType.post);
    }

    @Override
    public void unExecute() {

    }
}
