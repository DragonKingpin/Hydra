package com.acorn.redqueen.service.deletion;

import java.util.Collection;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.kom.entity.ElementNode;
import com.pinecone.hydra.service.kom.entity.ServiceTreeNode;
import com.acorn.redqueen.service.deletion.ServiceDirectoryDeleteSafetyReport.ReasonCode;

public class RedServiceDirectoryDeleteService implements ServiceDirectoryDeleteService {

    protected ServiceInstrument mServiceInstrument;

    public RedServiceDirectoryDeleteService( ServiceInstrument serviceInstrument ) {
        this.mServiceInstrument = serviceInstrument;
    }

    @Override
    public ServiceDirectoryDeleteSafetyReport checkDirectoryDelete( GUID guid ) {
        return this.checkDirectoryPurge( guid );
    }

    @Override
    public ServiceDirectoryDeleteSafetyReport checkDirectoryDelete( String path ) {
        return this.checkDirectoryPurge( path );
    }

    @Override
    public ServiceDirectoryDeleteSafetyReport checkDirectoryPurge( GUID guid ) {
        if ( guid == null ) {
            throw new IllegalArgumentException( "Service directory purge check missing guid." );
        }

        ServiceTreeNode node = (ServiceTreeNode) this.requireServiceInstrument().get( guid );
        if ( node == null ) {
            throw new IllegalArgumentException( "Service directory not found: " + guid );
        }

        ElementNode element = node.evinceElementNode();
        ServiceDirectoryDeleteSafetyReport report = new ServiceDirectoryDeleteSafetyReport();
        report.setGuid( guid );
        report.setName( node.getName() );
        report.setType( node.getMetaType() );
        report.setPath( this.requireServiceInstrument().getPath( guid ) );

        if ( this.isRootPath( report.getPath() ) ) {
            report.setReasonCode( ReasonCode.RootProtected );
            report.setMessage( "Service root directory cannot be deleted." );
            return report;
        }

        if ( element == null || ( element.evinceNamespace() == null && element.evinceApplicationElement() == null ) ) {
            report.setChildCount( 0 );
            report.setDeletable( false );
            report.setReasonCode( ReasonCode.UnsupportedType );
            report.setMessage( "Service directory purge target is not a namespace or application." );
            return report;
        }

        Collection<GUID> children = this.requireServiceInstrument().fetchChildrenGuids( guid );
        int childCount = 0;
        if ( children != null ) {
            childCount = children.size();
        }
        report.setChildCount( childCount );
        report.setDeletable( report.getChildCount() == 0 );
        if ( report.isDeletable() ) {
            report.setReasonCode( ReasonCode.Empty );
            report.setMessage( "Service directory purge safety check passed." );
        }
        else {
            report.setReasonCode( ReasonCode.NotEmpty );
            report.setMessage( "Service directory is not empty." );
        }

        if ( children != null ) {
            for ( GUID childGuid : children ) {
                report.getChildrenPreview().add( this.toDirectoryDeleteChild( childGuid ) );
            }
        }
        return report;
    }

    @Override
    public ServiceDirectoryDeleteSafetyReport checkDirectoryPurge( String path ) {
        if ( path == null || path.trim().isEmpty() ) {
            throw new IllegalArgumentException( "Service directory purge check missing path." );
        }
        if ( this.isRootPath( path ) ) {
            ServiceDirectoryDeleteSafetyReport report = new ServiceDirectoryDeleteSafetyReport();
            report.setPath( "/" );
            report.setReasonCode( ReasonCode.RootProtected );
            report.setMessage( "Service root directory cannot be deleted." );
            return report;
        }
        GUID guid = this.requireServiceInstrument().queryGUIDByPath( path );
        if ( guid == null ) {
            throw new IllegalArgumentException( "Service directory not found: " + path );
        }
        return this.checkDirectoryPurge( guid );
    }

    @Override
    public ServiceDirectoryDeleteSafetyReport purgeDirectory( GUID guid ) {
        ServiceDirectoryDeleteSafetyReport report = this.checkDirectoryPurge( guid );
        if ( !report.isDeletable() ) {
            throw new IllegalArgumentException( report.getMessage() );
        }

        this.requireServiceInstrument().getServiceMasterManipulator().purgeServiceDirectory( guid );
        return report;
    }

    @Override
    public ServiceDirectoryDeleteSafetyReport purgeDirectory( String path ) {
        ServiceDirectoryDeleteSafetyReport report = this.checkDirectoryPurge( path );
        if ( !report.isDeletable() ) {
            throw new IllegalArgumentException( report.getMessage() );
        }
        return this.purgeDirectory( report.getGuid() );
    }

    protected boolean isRootPath( String path ) {
        return path != null && path.trim().replace( '\\', '/' ).matches( "/+" );
    }

    protected ServiceDirectoryDeleteChild toDirectoryDeleteChild( GUID childGuid ) {
        ServiceDirectoryDeleteChild preview = new ServiceDirectoryDeleteChild();
        if ( childGuid == null ) {
            return preview;
        }

        ServiceTreeNode child = (ServiceTreeNode) this.requireServiceInstrument().get( childGuid );
        preview.setGuid( childGuid );
        preview.setPath( this.requireServiceInstrument().getPath( childGuid ) );
        if ( child != null ) {
            preview.setName( child.getName() );
            preview.setType( child.getMetaType() );
        }
        return preview;
    }

    protected ServiceInstrument requireServiceInstrument() {
        if ( this.mServiceInstrument == null ) {
            throw new IllegalStateException( "RedQueen service instrument is null." );
        }
        return this.mServiceInstrument;
    }

}
