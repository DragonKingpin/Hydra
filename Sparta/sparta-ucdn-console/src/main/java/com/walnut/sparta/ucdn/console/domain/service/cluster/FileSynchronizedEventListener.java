package com.walnut.sparta.ucdn.console.domain.service.cluster;

import java.util.concurrent.ConcurrentMap;
import javax.websocket.Session;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.walnut.sparta.ucdn.console.infrastructure.vo.SyncFinishedVO;
import com.walnut.sparta.ucdn.console.ufm.event.UFMEventListener;

public class FileSynchronizedEventListener implements UFMEventListener {
    public FileSynchronizedEventListener() {

    }

    @Override
    public void afterEventTriggered( String path, String serviceId, FileNode fileNode ) {
        GUID versionFileGuid = this.versionManage.getVersionFileByGuid(fileNode.getGuid());
        ConcurrentMap<GUID, ClusterFileSyncTransaction> map = this.transactionManager.getTransactions(versionFileGuid);
        ClusterFileSyncTransaction clusterFileSyncTransaction = map.get(fileNode.getGuid());
        clusterFileSyncTransaction.decreaseRemainingCount();
        Session session = this.webSocketService.getSession();
        SyncFinishedVO finishedVO = new SyncFinishedVO(path, serviceId, 1);
        session.getBasicRemote().sendText(finishedVO.toJSONString());
        if( this.transactionManager.checkTransactionFinished( versionFileGuid ) ){
            logger.info("文件{} 同步事务已完毕", versionFileGuid);
            session.close();
            this.transactionManager.removeTransactions( versionFileGuid );
        }
    }
}
