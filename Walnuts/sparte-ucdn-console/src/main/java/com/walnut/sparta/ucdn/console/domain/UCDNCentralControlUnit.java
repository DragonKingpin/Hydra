package com.walnut.sparta.ucdn.console.domain;

import com.pinecone.framework.util.id.GUID;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UCDNCentralControlUnit implements CentralControlUnit{
    private Map<GUID,Object> distributionLockMap;

    public UCDNCentralControlUnit(){
        this.distributionLockMap = new ConcurrentHashMap<>();
    }


    @Override
    public void register(GUID guid, Object object) {
        this.distributionLockMap.put( guid, object );
    }

    @Override
    public Object getLock(GUID guid) {
        return this.distributionLockMap.get( guid );
    }
}
