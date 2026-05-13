package com.acorn.skynet.device.conduct;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.kom.DeviceInstrument;
import com.pinecone.hydra.device.kom.entity.ElementNode;
import com.pinecone.hydra.device.registry.DeviceControlException;
import com.pinecone.hydra.device.registry.server.DeviceManager;
import com.pinecone.hydra.system.component.LogStatuses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkyCollectiveDeviceRegiment implements CollectiveDeviceRegiment {

    protected final Logger                  mLogger;

    protected final DeviceInstrument        mDeviceInstrument;

    protected final DeviceManager           mDeviceManager;

    public SkyCollectiveDeviceRegiment( DeviceInstrument deviceInstrument, DeviceManager deviceManager ) {
        if ( deviceInstrument == null ) {
            throw new IllegalArgumentException( "DeviceInstrument is required." );
        }
        if ( deviceManager == null ) {
            throw new IllegalArgumentException( "DeviceManager is required." );
        }

        this.mDeviceInstrument              = deviceInstrument;
        this.mDeviceManager                 = deviceManager;
        this.mLogger                        = LoggerFactory.getLogger( "SkynetCollectiveDeviceRegiment" );
        this.prepare_skynet_collective_device_regiment_subsystem();
    }

    protected void prepare_skynet_collective_device_regiment_subsystem() {
        this.infoLifecycle( "Preparing Skynet collective device regiment.", LogStatuses.StatusStart );
        this.traceWelcomeInfo();
        this.infoLifecycle( "Preparing Skynet collective device regiment.", LogStatuses.StatusDone );
    }

    protected void traceWelcomeInfo() {
        this.mLogger.info( "---------------------------------------------------------------" );
        this.mLogger.info( "Skynet Collective Device Regiment" );
        this.mLogger.info( "Centralized device identity and control management system." );
        this.mLogger.info( "---------------------------------------------------------------" );
    }

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    @Override
    public DeviceInstrument deviceInstrument() {
        return this.mDeviceInstrument;
    }

    @Override
    public DeviceManager deviceManager() {
        return this.mDeviceManager;
    }

    @Override
    public ElementNode queryDeviceByPath( String szPath ) {
        return this.mDeviceManager.queryDeviceByPath( szPath );
    }

    @Override
    public ElementNode getDeviceByGuid( GUID guid ) {
        return this.mDeviceManager.queryDeviceByGuid( guid );
    }

    @Override
    public ElementNode affirmDevice( String szPath, ElementNode elementNode ) {
        if ( this.isBlank( szPath ) ) {
            throw new IllegalArgumentException( "Device path is required." );
        }
        if ( elementNode == null ) {
            throw new IllegalArgumentException( "Device element is required." );
        }

        ElementNode affirmedNode = this.affirmDeviceByElement( szPath, elementNode );
        this.inheritDeviceMeta( affirmedNode, elementNode );
        this.mDeviceManager.updateDevice( affirmedNode );
        return affirmedNode;
    }

    protected ElementNode affirmDeviceByElement( String szPath, ElementNode elementNode ) {
        if ( elementNode.evinceNamespace() != null ) {
            return this.mDeviceInstrument.affirmNamespace( szPath );
        }
        if ( elementNode.evinceClusterElement() != null ) {
            return this.mDeviceInstrument.affirmCluster( szPath );
        }
        if ( elementNode.evinceQuickElement() != null ) {
            return this.mDeviceInstrument.affirmQuick( szPath );
        }
        if ( elementNode.evincePhysicalHostElement() != null ) {
            return this.mDeviceInstrument.affirmPhysicalHost( szPath );
        }
        if ( elementNode.evinceVirtualMachineElement() != null ) {
            return this.mDeviceInstrument.affirmVirtualMachine( szPath );
        }
        if ( elementNode.evinceContainerElement() != null ) {
            return this.mDeviceInstrument.affirmContainerElement( szPath );
        }

        throw new IllegalArgumentException( "Unsupported device element: " + elementNode.getClass().getName() );
    }

    @Override
    public void updateDeviceMeta( ElementNode elementNode ) {
        this.mDeviceManager.updateDevice( elementNode );
    }

    @Override
    public void purgeDevice( GUID guid ) {
        this.mDeviceManager.removeDevice( guid );
    }

    @Override
    public void startDeviceManager() throws DeviceControlException {
        this.mDeviceManager.startDeviceManager();
        this.infoLifecycle( "Skynet Collective Device Regiment Vitalization", LogStatuses.StatusDone );
    }

    protected boolean isBlank( String szValue ) {
        return szValue == null || szValue.trim().isEmpty();
    }

    protected void inheritDeviceMeta( ElementNode affirmedNode, ElementNode elementNode ) {
        affirmedNode.setAlias( elementNode.getAlias() );
        affirmedNode.setCode( elementNode.getCode() );
        affirmedNode.setDescription( elementNode.getDescription() );
        affirmedNode.setExtraInformation( elementNode.getExtraInformation() );
        affirmedNode.setResourceType( elementNode.getResourceType() );
        affirmedNode.setDeviceType( elementNode.getDeviceType() );
        affirmedNode.setCategory( elementNode.getCategory() );
        affirmedNode.setClassCode( elementNode.getClassCode() );
        affirmedNode.setDeploymentProfile( elementNode.getDeploymentProfile() );
        affirmedNode.setTopologyRole( elementNode.getTopologyRole() );
        affirmedNode.setVendor( elementNode.getVendor() );
        affirmedNode.setModel( elementNode.getModel() );
        affirmedNode.setSerialNumber( elementNode.getSerialNumber() );
        affirmedNode.setIpAddress( elementNode.getIpAddress() );
        affirmedNode.setRegion( elementNode.getRegion() );
        affirmedNode.setZone( elementNode.getZone() );
        affirmedNode.setLocation( elementNode.getLocation() );
        affirmedNode.setManagementProtocol( elementNode.getManagementProtocol() );
        affirmedNode.setManagementHost( elementNode.getManagementHost() );
        affirmedNode.setManagementPort( elementNode.getManagementPort() );
        affirmedNode.setCredentialRef( elementNode.getCredentialRef() );
        affirmedNode.setStatus( elementNode.getStatus() );
        affirmedNode.setLifecycleStatus( elementNode.getLifecycleStatus() );
        affirmedNode.setEnabled( elementNode.isEnabled() );
        affirmedNode.setTags( elementNode.getTags() );
        affirmedNode.setResourceSummary( elementNode.getResourceSummary() );
    }
}
