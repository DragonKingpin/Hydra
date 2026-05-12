package com.pinecone.hydra.device.registry.ulf;

import com.pinecone.framework.util.config.PatriarchalConfig;
import com.pinecone.hydra.device.registry.appoint.DeviceAppointServer;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.uma.DuplexAppointServer;

public class HuskyDeviceAppointServer implements DeviceAppointServer {

    protected DuplexAppointServer mAppointServer;

    protected DeviceManager mDeviceManager;

    public HuskyDeviceAppointServer( DuplexAppointServer duplexAppointServer ) {
        this.mAppointServer = duplexAppointServer;
    }

    public HuskyDeviceAppointServer( DuplexAppointServer duplexAppointServer, DeviceManager deviceManager ) {
        this( duplexAppointServer );
        this.mDeviceManager = deviceManager;
    }

    public DeviceManager deviceManager() {
        return this.mDeviceManager;
    }

    @Override
    public DeviceAppointServer hookDeviceManager( DeviceManager deviceManager ) {
        if ( this.mDeviceManager != null ) {
            throw new IllegalStateException( "Manager has already hooked." );
        }

        this.mDeviceManager = deviceManager;
        this.mAppointServer.registerController( new DeviceLifecycleController( this.mDeviceManager ) );
        this.mAppointServer.registerController( new DeviceMetaController( this.mDeviceManager ) );
        this.mAppointServer.registerController( new DeviceTopologyController( this.mDeviceManager ) );
        this.mDeviceManager.getLogger().info( "AppointServer[{}] has been hooked to device manager.", this.mAppointServer.getName() );
        return this;
    }

    public String getName() {
        return this.mAppointServer.getName();
    }

    public PatriarchalConfig getConfig() {
        return this.mAppointServer.getConfig();
    }

    @Override
    public void close() {
        this.mAppointServer.close();
    }

    @Override
    public void execute() throws Exception {
        this.mAppointServer.execute();
    }

    @Override
    public Long getMessageNodeId() {
        return this.mAppointServer.getMessageNodeId();
    }

    @Override
    public boolean isStarted() {
        return !this.mAppointServer.getMessageNode().isTerminated();
    }
}
