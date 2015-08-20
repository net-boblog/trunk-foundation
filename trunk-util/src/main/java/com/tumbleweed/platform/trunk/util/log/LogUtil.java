package com.tumbleweed.platform.trunk.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wjp on 2015/5/14.
 */
public class LogUtil {

    private static Logger log = LoggerFactory.getLogger(LogUtil.class);

    public static long writMessage(Logger logger, Long lastTime, String message){
        long now = System.currentTimeMillis();
        if(lastTime == null){
            logger.info(message + " " + now);
        }else{
            logger.info(message + ", start time is " + now + ", run time is " + (now - lastTime));
        }
        return now;
    }

}
