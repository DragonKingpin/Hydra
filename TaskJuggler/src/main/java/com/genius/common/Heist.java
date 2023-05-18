package com.genius.common;

import com.genius.core.HeistCenter;
import com.genius.util.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Genius
 * @date 2023/05/08 23:41
 **/
public class Heist implements Runnable{

    private Logger logger;

    private String heistName;

    private HeistCenter heistCenter;

    private int spoilSum;

    private int failureRetryTimes;

    public Heist(HeistCenter heistCenter){

        this.heistCenter = heistCenter;
        this.heistName = "Heist:"+this.hashCode();
        this.spoilSum = this.heistCenter.getSpoilNum() + this.heistCenter.getSpoilBase();
        this.failureRetryTimes = this.heistCenter.getHeistConfig().getFailureRetryTimes();

        logger = LoggerFactory.getLogger(SystemUtils.getLoggerFormatName(heistName));
    }

    //根据任务数量获取线程数

    private int getSpoil(){
        int index = this.heistCenter.getNowSpoil().getAndIncrement();
        if(index<=spoilSum){
            logger.info("{} get the spoil[{}]",heistName,index);
        }
        return index;
    }

    private boolean handlerSpoil(int index){
        logger.info("{} handler the spoil[{}]",heistName,index);
        return new Random().nextInt(100)+1>80;
    }

    private boolean failureRetry(int index,int retryTimes){
        logger.info("{} retry the spoil[{}] retryTimes:{}",heistName,index,retryTimes);
        return this.handlerSpoil(index);
    }

    private boolean completeRob(int index){
        logger.info("{}  complete the spoil[{}]",heistName,index);
        this.heistCenter.getSpoilsLock().countDown();
        return true;
    }

    private void failureHandler(int index){
        logger.info("{} report failure info: spoil[{}] rob fail",heistName,index);
    }

    @Override
    public void run() {
        int index = this.getSpoil();
        while(index<=spoilSum){
            int nowFailureRetryTimes = 0;
            if(!handlerSpoil(index)){
                while((nowFailureRetryTimes++)<this.failureRetryTimes&&!failureRetry(index,nowFailureRetryTimes));
                if(nowFailureRetryTimes>this.failureRetryTimes){
                    failureHandler(index);
                }
            }
            completeRob(index);
            index = this.getSpoil();
        }
    }
}
