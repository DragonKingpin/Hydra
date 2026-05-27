package com.walnut.odin.dispatch.entity;

import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.Server;
import com.walnut.odin.dispatch.ExecutionArchitects;
import com.walnut.odin.dispatch.TaskQueueMeta;

public class GenericTaskProcessorEntity implements TaskProcessorEntity {

    protected GUID           mGuid;
    protected String         mszName;
    protected Server         mDeployClusterServer;
    protected String         mszClusterPath;
    protected String         mszClusterName;
    protected long           mnControlClientId;
    protected String         mszExecCaps;
    protected boolean        mbLocal;
    protected boolean        mbExclusive;
    protected int            mnPriority;
    protected TaskQueueMeta  mTaskQueueMeta;
    protected boolean        mbEnable;

    public GenericTaskProcessorEntity() {
        this.mTaskQueueMeta = new GenericTaskQueueEntity();
    }

    @SuppressWarnings( "unchecked" )
    public GenericTaskProcessorEntity( Map<String, Object> jo ) {
        this();

        if ( jo == null ) {
            return;
        }

        Object name = jo.get( "name" );
        if ( name instanceof String ) {
            this.mszName = (String) name;
        }

        Object clusterPath = jo.get( "clusterPath" );
        if ( clusterPath instanceof String ) {
            this.mszClusterPath = (String) clusterPath;
        }

        Object clusterName = jo.get( "clusterName" );
        if ( clusterName instanceof String ) {
            this.mszClusterName = (String) clusterName;
        }

        Object controlClientId = jo.get( "controlClientId" );
        if ( controlClientId instanceof Number ) {
            this.mnControlClientId = ( (Number) controlClientId ).intValue();
        }

        Object execCaps = jo.get( "execCaps" );
        if ( execCaps instanceof String ) {
            this.mszExecCaps = (String) execCaps;
        }

        Object local = jo.get( "local" );
        if ( local instanceof Boolean ) {
            this.mbLocal = (Boolean) local;
        }

        Object priority = jo.get( "priority" );
        if ( priority instanceof Number ) {
            this.mnPriority = ( (Number) priority ).intValue();
        }

        Object queueMeta = jo.get( "queueMeta" );
        if ( queueMeta instanceof Map ) {
            this.mTaskQueueMeta = new GenericTaskQueueEntity( (Map<String, Object>)queueMeta );
        }
    }



    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public Server getDeployClusterServer() {
        return this.mDeployClusterServer;
    }

    @Override
    public String getClusterPath() {
        return this.mszClusterPath;
    }

    @Override
    public String getClusterName() {
        return this.mszClusterName;
    }

    @Override
    public long getControlClientId() {
        return this.mnControlClientId;
    }

    @Override
    public String getExecCaps() {
        return ExecutionArchitects.normalizeExecCaps( this.mszExecCaps );
    }

    @Override
    public boolean isLocal() {
        return this.mbLocal;
    }

    @Override
    public boolean isExclusive() {
        return this.mbExclusive;
    }

    @Override
    public int getPriority() {
        return this.mnPriority;
    }

    @Override
    public TaskQueueMeta getTaskQueueMeta() {
        return this.mTaskQueueMeta;
    }

    @Override
    public boolean isEnable() {
        return this.mbEnable;
    }

    public void setEnable( boolean enable ) {
        this.mbEnable = enable;
    }

    public void setExclusive( boolean exclusive ) {
        this.mbExclusive = exclusive;
    }

    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    public void setName(String name ) {
        this.mszName = name;
    }

    public String getQueueName() {
        return this.mTaskQueueMeta != null ? this.asTaskQueueMeta().getName() : null;
    }

    public Integer getQueueMaxCapacity() {
        return this.mTaskQueueMeta != null ? this.asTaskQueueMeta().getMaxCapacity() : null;
    }

    public Integer getQueueMinCapacity() {
        return this.mTaskQueueMeta != null ? this.asTaskQueueMeta().getMinCapacity() : null;
    }

    public Integer getQueueRuntimeInstanceCapacity() {
        return this.mTaskQueueMeta != null ? this.asTaskQueueMeta().getRuntimeInstanceCapacity() : null;
    }

    public void setDeployClusterServer( Server server ) {
        this.mDeployClusterServer = server;
    }

    public void setClusterPath( String clusterPath ) {
        this.mszClusterPath = clusterPath;
    }

    public void setClusterName( String clusterName ) {
        this.mszClusterName = clusterName;
    }

    @Override
    public void setControlClientId( long controlClientId ) {
        this.mnControlClientId = controlClientId;
    }

    @Override
    public void setExecCaps( String execCaps ) {
        this.mszExecCaps = execCaps;
    }

    public void setLocal( boolean bLocal ) {
        this.mbLocal = bLocal;
    }

    public void setPriority( int priority ) {
        this.mnPriority = priority;
    }

    public void setTaskQueueMeta( TaskQueueMeta queueMeta ) {
        this.mTaskQueueMeta = queueMeta;
    }

    protected ArchTaskQueueMeta asTaskQueueMeta() {
        return (ArchTaskQueueMeta) this.mTaskQueueMeta;
    }

    public void setQueueName( String queueName ) {
        this.asTaskQueueMeta().setName( queueName );
    }

    public void setQueueMaxCapacity( int nMaxCapacity ) {
        this.asTaskQueueMeta().setMaxCapacity( nMaxCapacity );
    }

    public void setQueueMinCapacity( int nMinCapacity ) {
        this.asTaskQueueMeta().setMinCapacity( nMinCapacity );
    }

    public void setQueueUsedCapacity( int nUsedCapacity ) {
        this.asTaskQueueMeta().setUsedCapacity( nUsedCapacity );
    }

    public void setQueueRuntimeInstanceCapacity( int nRuntimeInstanceCapacity ) {
        this.asTaskQueueMeta().setRuntimeInstanceCapacity( nRuntimeInstanceCapacity );
    }

}
