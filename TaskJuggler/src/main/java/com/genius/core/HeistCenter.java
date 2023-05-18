package com.genius.core;

import com.genius.common.Heist;
import com.genius.common.Message;
import com.genius.common.MessageType;
import com.genius.config.HeistConfig;
import com.genius.config.SystemConfig;
import com.genius.mq.Harbor;
import com.genius.util.SystemUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Genius
 * @date 2023/05/08 23:08
 **/

@Data
@Component
//负责劫匪的任务调度分配
public class HeistCenter {

    @Autowired
    private Harbor harbor;  //港口，负责和master结点进行通信
    @Resource
    private HeistConfig heistConfig;
    private ExecutorService heistPool;

    private AtomicInteger nowSpoil;

    private int spoilNum;       //赃物数量

    private int spoilBase;      //赃物基数

    private CountDownLatch spoilsLock;

    Logger logger = LoggerFactory.getLogger(SystemUtils.getLoggerFormatName("HeistCenter"));

    public HeistCenter(){

    }

    public HeistCenter(HeistConfig heistConfig){
        this.heistConfig = heistConfig;
    }

    private void initHeistPool(){
        heistPool = Executors.newFixedThreadPool(heistConfig.getHeistNum());
    }

    private boolean init(){
        if(heistPool==null){
            initHeistPool();
        }
        getSpoil();
        if(this.spoilNum<=0){
            return false;
        }
        nowSpoil = new AtomicInteger(spoilBase);
        spoilsLock = new CountDownLatch(spoilNum+1);
        return true;
    }
    public void getSpoil(){
        //从港口获取任务数量
        try {
            Message msg;

            while((msg = harbor.getSpoil(heistConfig.getRobTaskName()))==null);

            if(msg.getMethod().equals(MessageType.SHUTDOWN)){
                spoilNum = -1;
            }else{
                int upLimit = Integer.parseInt(msg.getData().get("upLimit").toString());
                int lowLimit = Integer.parseInt(msg.getData().get("lowLimit").toString());
                spoilNum = upLimit-lowLimit;
                spoilBase = lowLimit;
            }
        } catch (Exception e) {
            spoilNum = -1;
        }
    }

    public void start() throws InterruptedException {
        while (init()) {
            logger.info("{} Robbing {}[{}~{}]", SystemConfig.ServiceId,heistConfig.getRobTaskName(),spoilBase,spoilBase+spoilNum);
            for (int i = 0; i < heistConfig.getHeistNum(); i++) {
                heistPool.submit(new Heist(this));
            }
            this.spoilsLock.await();
        }
    }


}
