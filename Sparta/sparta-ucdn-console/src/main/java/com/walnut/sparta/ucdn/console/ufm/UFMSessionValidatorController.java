package com.walnut.sparta.ucdn.console.ufm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.ufm.event.UFMEventSubscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.ufm.SessionValidator." )
public class UFMSessionValidatorController implements Pinenut {
    private Logger                        logger;

    private SessionPhaser                 sessionPhaser;

    private KOMFileSystem                 primaryFileSystem;

    private FileMultiDistributionService  distributionService;

    public UFMSessionValidatorController( UOFSFileMultiDistributionService distributionService ){
        this.logger               = LoggerFactory.getLogger( this.getClass() );
        this.distributionService  = distributionService;
        this.primaryFileSystem    = distributionService.primaryFileSystem;
        this.sessionPhaser        = distributionService.sessionPhaser;
    }

    @AddressMapping( "stageClusterGroupComplete" )
    public void stageClusterGroupComplete( String path ){
        this.logger.info( "UFMService invoked stageClusterGroupComplete." );

        ElementNode elementNode = this.primaryFileSystem.queryElement(path);
        this.sessionPhaser.incrementConsumerCount( elementNode.getGuid() );

        if( this.sessionPhaser.getConsumerCount( elementNode.getGuid() ) == 1 ){
            final Object lock = this.sessionPhaser.getFileLock( elementNode.getGuid() );
            synchronized ( lock ){
                lock.notify();
            }
            this.sessionPhaser.resetConsumerCount( elementNode.getGuid() );
        }
    }

    @AddressMapping( "stageFileTransmitComplete" )
    public void stageFileTransmitComplete( String path ){
        this.logger.info( "SlaveNode {}, file receive complete.", path );
    }

    @AddressMapping( "fileTransmitComplete" )
    public void fileTransmitComplete( String path, String serviceId ) throws IOException {
        FileNode fileNode = (FileNode)this.primaryFileSystem.queryElement(path);
        //GUID versionFileGuid = this.versionManage.getVersionFileByGuid(fileNode.getGuid());

        Collection<UFMEventSubscriber> subscribers = this.distributionService.fetchFileTransmitCompleteEventSubscribers();
        for ( UFMEventSubscriber subscriber : subscribers ) {
            subscriber.afterEventTriggered( path, serviceId, fileNode );
        }
    }
}
