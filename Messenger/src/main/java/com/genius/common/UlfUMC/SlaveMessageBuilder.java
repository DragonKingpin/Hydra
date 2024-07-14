package com.genius.common.UlfUMC;

import com.genius.config.SystemConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Genius
 * @date 2023/05/14 20:32
 **/
public class SlaveMessageBuilder extends CommonMessageBuilder{



    public SlaveMessageBuilder(){
        super();
    }

    @Override
    public MessageBuilder data(Map<String,Object> data) {
        HashMap<String,Object> newData = new HashMap<>(data);
        newData.put("serviceId",SystemConfig.ServiceId);
        getMessage().getUlfUMCBody().setData(newData);
        return this;
    }
}
