package com.walnut.odin.dispatch.entity;

import java.util.Map;

import com.walnut.odin.dispatch.TaskQueueMeta;

public abstract class ArchTaskQueueMeta implements TaskQueueMeta {

    protected String mszName;

    protected int mnMaxCapacity;

    protected int mnMinCapacity;

    protected int mnUsedCapacity;

    protected int mnRuntimeInstanceCapacity;

    protected ArchTaskQueueMeta() {

    }

    public ArchTaskQueueMeta( Map<String, Object> jo ) {
        if ( jo == null ) {
            return;
        }

        Object name = jo.get( "name" );
        if ( name instanceof String ) {
            this.mszName = (String) name;
        }

        Object maxCapacity = jo.get( "maxCapacity" );
        if ( maxCapacity instanceof Number ) {
            this.mnMaxCapacity = ( (Number) maxCapacity ).intValue();
        }

        Object minCapacity = jo.get( "minCapacity" );
        if ( minCapacity instanceof Number ) {
            this.mnMinCapacity = ( (Number) minCapacity ).intValue();
        }

        Object runtimeCapacity = jo.get( "runtimeInstanceCapacity" );
        if ( runtimeCapacity instanceof Number ) {
            this.mnRuntimeInstanceCapacity = ( (Number) runtimeCapacity ).intValue();
        }
    }


    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public int getMaxCapacity() {
        return this.mnMaxCapacity;
    }

    @Override
    public int getMinCapacity() {
        return this.mnMinCapacity;
    }

    @Override
    public int getUsedCapacity() {
        return this.mnUsedCapacity;
    }

    @Override
    public int getRuntimeInstanceCapacity() {
        return this.mnRuntimeInstanceCapacity;
    }

    protected void setName( String szName ) {
        this.mszName = szName;
    }

    protected void setMaxCapacity( int nMaxCapacity ) {
        this.mnMaxCapacity = nMaxCapacity;
    }

    protected void setMinCapacity( int nMinCapacity ) {
        this.mnMinCapacity = nMinCapacity;
    }

    protected void setUsedCapacity( int nUsedCapacity ) {
        this.mnUsedCapacity = nUsedCapacity;
    }

    protected void setRuntimeInstanceCapacity( int nRuntimeInstanceCapacity ) {
        this.mnRuntimeInstanceCapacity = nRuntimeInstanceCapacity;
    }

}