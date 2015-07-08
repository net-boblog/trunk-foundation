package com.tumbleweed.platform.trunk.util.log;

import org.apache.commons.logging.Log;

/**
 * Created by Lenovo on 2015/5/14.
 */
public class LogUtil {

    public static long writMessage(Log logger, Long lastTime, String message){
        long now = System.currentTimeMillis();
        if(lastTime == null){
            logger.info(message + " " + now);
        }else{
            logger.info(message + ", start time is " + now + ", run time is " + (now - lastTime));
        }
        return now;
    }

}
