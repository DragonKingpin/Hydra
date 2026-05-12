package com.pinecone.hydra.device.control.server;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.deploy.kom.DeployInstrument;
import com.pinecone.hydra.deploy.kom.entity.ElementNode;
import com.pinecone.hydra.device.control.DeviceControlRPCException;
import com.pinecone.hydra.device.control.DeviceValidationException;
import com.pinecone.hydra.device.control.appoint.DeviceAppointServer;
import com.pinecone.hydra.device.control.constant.DeviceNodeType;
import com.pinecone.hydra.device.control.dto.DeviceRegistrationDTO;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class UniformDeviceControlManager implements DeviceControlManager {

    protected final DeployInstrument mDeployInstrument;

    protected final ConcurrentMap<Long, DeviceAppointServer> mServerPoolMap;

    protected final DeviceLifecycleService mDeviceLifecycleService;

    protected final DeviceMetaService mDeviceMetaService;

    protected final DeviceTopologyService mDeviceTopologyService;

    protected final Logger mLogger;

    public UniformDeviceControlManager( DeployInstrument deployInstrument ) {
        if ( deployInstrument == null ) {
            throw new DeviceValidationException( "DeployInstrument is required." );
        }

        this.mDeployInstrument = deployInstrument;
        this.mServerPoolMap = new ConcurrentHashMap<>();
        this.mDeviceLifecycleService = new DeviceLifecycleService( this );
        this.mDeviceMetaService = new DeviceMetaService( this );
        this.mDeviceTopologyService = new DeviceTopologyService( this );
        this.mLogger = LoggerFactory.getLogger( this.getClass() );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public DeployInstrument getDeployInstrument() {
        return this.mDeployInstrument;
    }

    @Override
    public Collection<DeviceAppointServer> getServers() {
        return this.mServerPoolMap.values();
    }

    @Override
    public DeviceControlManager addAppointServer( DeviceAppointServer appointServer ) {
        this.mServerPoolMap.put( appointServer.getMessageNodeId(), appointServer );
        return this;
    }

    @Override
    public DeviceControlManager hookAppointServer( DeviceAppointServer appointServer ) {
        this.addAppointServer( appointServer );
        appointServer.hookDeviceControlManager( this );
        return this;
    }

    @Override
    public DeviceControlManager vitalizeAppointServer( DeviceAppointServer appointServer ) throws DeviceControlRPCException {
        try {
            this.hookAppointServer( appointServer );
            appointServer.execute();
            return this;
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public DeviceAppointServer getAppointServerById( Long appointNodeId ) {
        return this.mServerPoolMap.get( appointNodeId );
    }

    @Override
    public DeviceAppointServer evictAppointServerById( Long appointNodeId ) {
        DeviceAppointServer legacy = this.mServerPoolMap.remove( appointNodeId );
        if ( legacy != null ) {
            legacy.close();
        }
        return legacy;
    }

    @Override
    public int serverSize() {
        return this.mServerPoolMap.size();
    }

    @Override
    public void startDeviceControl() throws DeviceControlRPCException {
        try {
            for ( Map.Entry<Long, DeviceAppointServer> entry : this.mServerPoolMap.entrySet() ) {
                if ( !entry.getValue().isStarted() ) {
                    entry.getValue().execute();
                }
            }
            this.infoLifecycle( "Device Control RPC Subsystem Vitalization", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new DeviceControlRPCException( e );
        }
    }

    @Override
    public synchronized GUID registerDevice( DeviceRegistrationDTO registrationDTO ) {
        this.validateRegistration( registrationDTO );
        ElementNode node = this.affirmNode( registrationDTO.getPath(), registrationDTO.getNodeType() );
        if ( registrationDTO.getMeta() != null ) {
            registrationDTO.getMeta().applyTo( node, this.mDeployInstrument.getGuidAllocator() );
            this.mDeployInstrument.update( node );
        }
        return node.getGuid();
    }

    @Override
    public ElementNode queryDeviceByPath( String path ) {
        return this.mDeployInstrument.queryElement( path );
    }

    @Override
    public ElementNode queryDeviceByGuid( GUID guid ) {
        if ( guid == null ) {
            return null;
        }
        TreeNode node = this.mDeployInstrument.get( guid );
        return node instanceof ElementNode ? (ElementNode) node : null;
    }

    @Override
    public void updateDevice( ElementNode elementNode ) {
        if ( elementNode == null ) {
            throw new DeviceValidationException( "Device element is required." );
        }
        this.mDeployInstrument.update( elementNode );
    }

    @Override
    public void removeDevice( GUID guid ) {
        if ( guid != null ) {
            this.mDeployInstrument.remove( guid );
        }
    }

    @Override
    public DeviceLifecycleService deviceLifecycleService() {
        return this.mDeviceLifecycleService;
    }

    @Override
    public DeviceMetaService deviceMetaService() {
        return this.mDeviceMetaService;
    }

    @Override
    public DeviceTopologyService deviceTopologyService() {
        return this.mDeviceTopologyService;
    }

    protected void validateRegistration( DeviceRegistrationDTO registrationDTO ) {
        if ( registrationDTO == null ) {
            throw new DeviceValidationException( "Device registration is required." );
        }
        if ( isBlank( registrationDTO.getPath() ) ) {
            throw new DeviceValidationException( "Device path is required." );
        }
        if ( registrationDTO.getNodeType() == null ) {
            throw new DeviceValidationException( "Device node type is required." );
        }
    }

    protected ElementNode affirmNode( String path, DeviceNodeType nodeType ) {
        switch ( nodeType ) {
            case NAMESPACE:
                return this.mDeployInstrument.affirmNamespace( path );
            case CLUSTER:
                return this.mDeployInstrument.affirmCluster( path );
            case PHYSICAL_HOST:
                return this.mDeployInstrument.affirmPhysicalHost( path );
            case VIRTUAL_MACHINE:
                return this.mDeployInstrument.affirmVirtualMachine( path );
            case CONTAINER:
                return this.mDeployInstrument.affirmContainerElement( path );
            case QUICK:
                return this.mDeployInstrument.affirmQuick( path );
            default:
                throw new DeviceValidationException( "Unsupported device node type: " + nodeType );
        }
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
