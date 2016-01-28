package com.tumbleweed.platform.trunk.scheduler.core;

import com.tumbleweed.platform.trunk.scheduler.base.WorkQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mylover on 1/28/16.
 */
public class CommonWorkQueue extends WorkQueue {
    public static final String MESSAGE_ID_REST_ALARMING = "TRUNK_ALARMING";
    public static final Logger logger = LogManager.getLogger(CommonWorkQueue.class);

    public CommonWorkQueue() {
    }

    public final void putMessage(String ID, Object msg) {
        this.putMessage(null, ID, msg);
    }

    protected final void putMessage(String url, String ID, Object msg) {
        this.put(new CommonWorkQueue.CommonJob(url, ID, msg));
    }

    protected void execute(Object obj) throws Exception {
        try {
            Thread.sleep(40L);
        } catch (InterruptedException var3) {
            ;
        }

        CommonWorkQueue.CommonJob job = (CommonWorkQueue.CommonJob)obj;
        if(job == null) {
            logger.warn("CommonJob is null, please check it...");
        } else {
            if(job.getMsgId() == "TRUNK_ALARMING") {
                logger.error("url {} body {}", job.getUrl(), job.getBody());
            }

        }
    }

    public void onDestroy() {
        this.close();
    }

    public class CommonJob {
        String url;
        String msgId;
        Object body;

        CommonJob(String url, String msgId, Object body) {
            this.url = url;
            this.msgId = msgId;
            this.body = body;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMsgId() {
            return this.msgId;
        }

        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }

        public Object getBody() {
            return this.body;
        }

        public void setBody(Object body) {
            this.body = body;
        }
    }
}
