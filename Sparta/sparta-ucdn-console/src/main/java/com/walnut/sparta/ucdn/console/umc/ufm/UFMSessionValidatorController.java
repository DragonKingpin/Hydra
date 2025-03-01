package com.walnut.sparta.ucdn.console.umc.ufm;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.domain.service.WebSocketService;
import com.walnut.sparta.ucdn.console.infrastructure.SyncTransaction;
import com.walnut.sparta.ucdn.console.infrastructure.TransactionManage;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNService;
import com.walnut.sparta.ucdn.console.infrastructure.vo.SyncFinishedVO;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

@Controller
@AddressMapping( "com.walnut.sparta.ucdn.console.umc.ufm.SessionValidator." )
//@Service
public class UFMSessionValidatorController {
    private Logger                  logger;

    private SessionPhaser           sessionPhaser;

    private KOMFileSystem           primaryFileSystem;

    private UniformVolumeManager    primaryVolume;

    private VersionManage           versionManage;

    private TransactionManage       transactionManage;

    private WebSocketService        webSocketService;


    public UFMSessionValidatorController(MasterWarehouse masterWarehouse, UCDNService ucdnService){
        this.logger             = LoggerFactory.getLogger( this.getClass() );
        this.primaryFileSystem  = ucdnService.getKOMFileSystem();
        this.sessionPhaser      = masterWarehouse.getSessionPhaser();
        this.primaryVolume      = ucdnService.getUniformVolumeManager();
        this.versionManage      = ucdnService.getTitanVersionManage();
        this.transactionManage  = masterWarehouse.getTransactionManage();
        this.webSocketService   = masterWarehouse.getWebSocketService();
    }

    @AddressMapping("stageClusterGroupComplete")
    public void stageClusterGroupComplete( String path ){
        logger.info("回调");

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
    public void fileTransmitComplete( String path, String serviceId ) throws IOException {
        FileNode fileNode = (FileNode)this.primaryFileSystem.queryElement(path);
        GUID versionFileGuid = this.versionManage.getVersionFileByGuid(fileNode.getGuid());

        ConcurrentMap<GUID, SyncTransaction> map = this.transactionManage.getTransactions(versionFileGuid);
        SyncTransaction syncTransaction = map.get(fileNode.getGuid());
        syncTransaction.decreaseRemainingNum();
        Session session = this.webSocketService.getSession();
        SyncFinishedVO finishedVO = new SyncFinishedVO(path, serviceId, 1);
        session.getBasicRemote().sendText(finishedVO.toJSONString());
        if( transactionManage.checkTransactionOver( versionFileGuid ) ){
            logger.info("文件{} 同步事务已完毕", versionFileGuid);
            session.close();
            this.transactionManage.removeTransactions( versionFileGuid );
        }

    }
}
