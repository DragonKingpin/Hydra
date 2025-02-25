package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.infrastructure.SyncTransaction;
import com.walnut.sparta.ucdn.console.infrastructure.TransactionManage;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentMap;

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

    private VersionManage           versionManage;

    private TransactionManage       transactionManage;


    public UFMSessionValidatorController( MasterWarehouse masterWarehouse ){
        this.logger             = LoggerFactory.getLogger( this.getClass() );
        this.primaryFileSystem  = masterWarehouse.getKOMFileSystem();
        this.sessionPhaser      = masterWarehouse.getSessionPhaser();
        this.primaryVolume      = masterWarehouse.getUniformVolumeManager();
        this.versionManage      = masterWarehouse.getVersionManage();
        this.transactionManage  = masterWarehouse.getTransactionManage();
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
        this.logger.info( "SlaveNode {}, file receive complete.", path );
    }

    @AddressMapping("fileTransmitComplete")
    public void fileTransmitComplete( String path ){
        FileNode fileNode = (FileNode)this.primaryFileSystem.queryElement(path);
        GUID versionFileGuid = this.versionManage.getVersionFileByGuid(fileNode.getGuid());

        ConcurrentMap<GUID, SyncTransaction> map = this.transactionManage.getTransactions(versionFileGuid);
        SyncTransaction syncTransaction = map.get(fileNode.getGuid());
        syncTransaction.decreaseRemainingNum();
        if( transactionManage.checkTransactionOver( versionFileGuid ) ){
            log.info("文件{} 同步事务已完毕", versionFileGuid);
        }

    }
}
