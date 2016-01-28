package com.tumbleweed.platform.trunk.scheduler.core.impl;

import com.tumbleweed.platform.trunk.scheduler.core.CommonWorkQueue;

/**
 * Created by mylover on 1/28/16.
 */
public class CommonWorkQueueImpl extends CommonWorkQueue {

    public static final String MESSAGE_ID_TEST = "MESSAGE_ID_TEST";

    @Override
    protected void execute(Object obj) throws Exception {
        super.execute(obj);

        CommonJob job = (CommonJob) obj;
        if (job == null) {
            logger.warn("CommonJob is null, please check it...");
            return;
        }

        if (job.getMsgId() == MESSAGE_ID_TEST) {
            String body = (String) job.getBody();
            if (body != null && body.length() > 0) {

            }
        }
    }
}
