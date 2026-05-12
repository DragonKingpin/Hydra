package com.pinecone.hydra.device.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.hydra.device.PhysicalHost;
import com.pinecone.hydra.device.kom.entity.ArchElementNode;

public class GenericPhysicalHost extends ArchElementNode implements PhysicalHost {

    protected GUID guid;

    protected String status;

    protected String hardwareSpecs;


    protected String name;

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus( String status ) {
           this.status = status;
    }

    @Override
    public String getHardwareSpecs() {
        return this.hardwareSpecs;
    }

    @Override
    public void setHardwareSpecs( String hardwareSpecs ) {
            this.hardwareSpecs = hardwareSpecs;
    }

    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

//    @Override
//    public GUID getGuid() {
//        return this.guid;
//    }
//
//    @Override
//    public void setGuid(GUID guid) {
//        this.guid = guid;
//    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName( String name ) {
            this.name = name;
    }



    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
