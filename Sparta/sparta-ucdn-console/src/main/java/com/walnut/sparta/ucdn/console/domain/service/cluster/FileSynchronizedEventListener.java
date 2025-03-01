package com.walnut.sparta.ucdn.console.domain.service.cluster;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;
import javax.websocket.Session;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.version.VersionManage;
import com.walnut.sparta.ucdn.console.infrastructure.vo.SyncFinishedVO;
import com.walnut.sparta.ucdn.console.ufm.event.UFMEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSynchronizedEventListener implements UFMEventListener {
    private Logger logger;
    private VersionManage versionManage;

    private ClusterFileTransactionManager transactionManager;

    private UFMTransactionSynchronizedNotifier transactionSynchronizedNotifier;

    private BucketInstrument  bucketInstrument;

    public FileSynchronizedEventListener(VersionManage versionManage, ClusterFileTransactionManager transactionManager,
                                         UFMTransactionSynchronizedNotifier transactionSynchronizedNotifier, BucketInstrument bucketInstrument) {
        this.logger   = LoggerFactory.getLogger( this.getClass() );
        this.versionManage = versionManage;
        this.transactionManager = transactionManager;
        this.transactionSynchronizedNotifier = transactionSynchronizedNotifier;
        this.bucketInstrument = bucketInstrument;
    }

    @Override
    public void afterEventTriggered( String path, String serviceId, FileNode fileNode ) throws IOException {
        GUID versionFileGuid = this.versionManage.getVersionFileByGuid(fileNode.getGuid());
        ConcurrentMap<GUID, ClusterFileSyncTransaction> map = this.transactionManager.getTransactions(versionFileGuid);
        ClusterFileSyncTransaction clusterFileSyncTransaction = map.get(fileNode.getGuid());
        clusterFileSyncTransaction.decreaseRemainingCount();
        Session session = this.transactionSynchronizedNotifier.getSession();
        SyncFinishedVO finishedVO = new SyncFinishedVO(path, serviceId, 1);
        session.getBasicRemote().sendText(finishedVO.toJSONString());
        if( this.transactionManager.checkTransactionFinished( versionFileGuid ) ){
            logger.info("文件{} 同步事务已完毕", versionFileGuid);
            this.bucketInstrument.createSyncState( versionFileGuid, 1 );
            session.close();
            this.transactionManager.removeTransactions( versionFileGuid );
        }
    }
}
