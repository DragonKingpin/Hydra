package com.pinecone.hydra.deploy.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.deploy.PhysicalHost;
import com.pinecone.hydra.deploy.VirtualMachine;

public class GenericVirtualMachine implements VirtualMachine {

    protected GUID affiliateHostGuid;

    protected String name;

    protected String ipAddress;

    protected String status;

    protected GUID guid;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public GUID getAffiliateHost() {
        return this.affiliateHostGuid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public void setAffiliateHostGuid(GUID guid) {
        this.affiliateHostGuid = guid;
    }

//    @Override
//    public void setGuid(GUID guid) {
//        this.guid = guid;
//    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

//    @Override
//    public void setAffiliateHostGuid( GUID guid ) {
//        this.affiliateHostGuid = guid;
//    }
}
