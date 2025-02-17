package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator." )
//@Service
public class UFMSessionValidatorController {
    private Logger                  logger;

    //@Resource
    private SessionPhaser           sessionPhaser;

    //@Resource
    private KOMFileSystem           primaryFileSystem;

    //@Resource
    private UniformVolumeManager    primaryVolume;

    public UFMSessionValidatorController( MasterWarehouse masterWarehouse ){
        this.logger             = LoggerFactory.getLogger( this.getClass() );
        this.primaryFileSystem  = masterWarehouse.getKOMFileSystem();
        this.sessionPhaser      = masterWarehouse.getSessionPhaser();
        this.primaryVolume      = masterWarehouse.getUniformVolumeManager();
    }

    @AddressMapping("stageClusterGroupComplete")
    public void stageClusterGroupComplete( String path ){
        log.info("回调");

        ElementNode elementNode = this.primaryFileSystem.queryElement(path);
        this.sessionPhaser.incrementConsumerCount( elementNode.getGuid() );

        if( this.sessionPhaser.getConsumerCount( elementNode.getGuid() ) == 1 ){
            Object lock = this.sessionPhaser.getFileLock(elementNode.getGuid());
            synchronized ( lock ){
                lock.notify();
            }
            this.sessionPhaser.resetConsumerCount( elementNode.getGuid() );
        }
    }

    @AddressMapping("stageFileTransmitComplete")
    public void stageFileTransmitComplete( String path ){
        this.logger.info( "MasterNode<Kingpin>, file distribution complete, file `{}`.", path );
    }
}
