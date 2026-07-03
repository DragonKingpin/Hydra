package com.acorn.redqueen.service.purge;

import java.util.Collection;
import java.util.List;

import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.ElementNode;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.constant.ServiceInstanceStatus;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public class RedServicePurgeService implements PurgeService {
    protected ServiceInstrument mServiceInstrument;
    protected ServiceManager mServiceManager;

    public RedServicePurgeService( ServiceInstrument serviceInstrument, ServiceManager serviceManager ) {
        this.mServiceInstrument = serviceInstrument;
        this.mServiceManager = serviceManager;
    }

    @Override
    public PurgeSafetyReport check( String serviceIdentifier ) {
        ServiceElement serviceElement = this.resolveServiceElement( serviceIdentifier );
        PurgeSafetyReport report = this.newPurgeReport( serviceElement );
        this.fillPurgeBlockers( serviceElement, report );
        report.setPurgeable( report.getBlockers().isEmpty() );
        return report;
    }

    @Override
    public PurgeSafetyReport purge( String serviceIdentifier ) {
        ServiceElement serviceElement = this.resolveServiceElement( serviceIdentifier );
        PurgeSafetyReport report = this.check( serviceElement.getGuid().toString() );
        if ( !report.isPurgeable() ) {
            return report;
        }

        this.requireServiceInstrument().getServiceMasterManipulator().purgeServiceNode( serviceElement.getGuid() );
        report.setPurged( true );
        return report;
    }

    @Override
    public void shutdownInstance( Identification instanceId, String reason ) throws PurgeException {
        try {
            this.requireServiceManager().shutdownServiceInstance( instanceId, reason );
        }
        catch ( ServiceControlRPCException exception ) {
            throw new PurgeException( exception );
        }
    }

    protected ServiceElement resolveServiceElement( String identifier ) {
        if ( StringUtils.isBlank( identifier ) ) {
            throw new IllegalArgumentException( "service identifier should not be blank." );
        }

        String value = identifier.trim();
        if ( this.isRootPath( value ) ) {
            throw new IllegalArgumentException( "service identifier should not be root path." );
        }

        ElementNode node = null;
        if ( value.contains( "/" ) || value.contains( "\\" ) ) {
            node = this.requireServiceInstrument().queryElement( value );
        }
        else {
            TreeNode treeNode = this.requireServiceInstrument().get(
                    this.requireServiceInstrument().getGuidAllocator().parse( value )
            );
            if ( treeNode instanceof ElementNode ) {
                node = (ElementNode) treeNode;
            }
        }

        ServiceElement serviceElement = node == null ? null : node.evinceServiceElement();
        if ( serviceElement == null ) {
            throw new IllegalArgumentException( "Target node should be ServiceElement: " + identifier );
        }
        return serviceElement;
    }

    protected PurgeSafetyReport newPurgeReport( ServiceElement serviceElement ) {
        PurgeSafetyReport report = new PurgeSafetyReport();
        report.setPurgeable( true );
        report.setPurged( false );
        report.setServiceGuid( serviceElement.getGuid() == null ? null : serviceElement.getGuid().toString() );
        report.setServicePath( this.requireServiceInstrument().getPath( serviceElement.getGuid() ) );
        report.setServiceName( serviceElement.getName() );
        return report;
    }

    protected void fillPurgeBlockers( ServiceElement serviceElement, PurgeSafetyReport report ) {
        if ( serviceElement == null || serviceElement.getGuid() == null ) {
            report.addBlocker( "Service", null, "Missing", "Service does not exist." );
            return;
        }

        Collection<GUID> children = this.requireServiceInstrument().fetchChildrenGuids( serviceElement.getGuid() );
        if ( children != null && !children.isEmpty() ) {
            report.addBlocker(
                    "ServiceChildren",
                    serviceElement.getGuid().toString(),
                    String.valueOf( children.size() ),
                    "Service still has child tree nodes."
            );
        }

        List<ServiceInstanceEntry> instances =
                this.requireServiceInstrument().fetchServiceInstancesByServiceGuid( serviceElement.getGuid() );
        report.setInstanceCount( instances == null ? 0 : instances.size() );
        if ( instances == null ) {
            return;
        }

        for ( ServiceInstanceEntry instance : instances ) {
            if ( instance != null && this.isPurgeBlockingStatus( instance.getStatus() ) ) {
                report.addBlocker(
                        "ServiceInstance",
                        instance.getGuid() == null ? null : instance.getGuid().toString(),
                        instance.getStatus(),
                        "Service instance is not offline. Stop it first and wait until status becomes Offline."
                );
            }
        }
    }

    protected boolean isPurgeBlockingStatus( String status ) {
        ServiceInstanceStatus type = ServiceInstanceStatus.getByName( status );
        return type != ServiceInstanceStatus.Offline
                && type != ServiceInstanceStatus.Expired
                && type != ServiceInstanceStatus.Error;
    }

    protected boolean isRootPath( String path ) {
        String normalizedPath = path == null ? "" : path.trim().replace( '\\', '/' );
        return "/".equals( normalizedPath ) || normalizedPath.replace( "/", "" ).isEmpty();
    }

    protected ServiceInstrument requireServiceInstrument() {
        if ( this.mServiceInstrument == null ) {
            throw new IllegalStateException( "RedQueen service instrument is not available." );
        }
        return this.mServiceInstrument;
    }

    protected ServiceManager requireServiceManager() {
        if ( this.mServiceManager == null ) {
            throw new IllegalStateException( "RedQueen service manager is not available." );
        }
        return this.mServiceManager;
    }
}
