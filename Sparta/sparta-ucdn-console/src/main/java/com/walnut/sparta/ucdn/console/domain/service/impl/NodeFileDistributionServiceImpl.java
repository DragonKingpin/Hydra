package com.walnut.sparta.ucdn.console.domain.service.impl;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.Folder;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.version.VersionManage;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.ucdn.console.domain.service.cluster.ClusterFileSyncTransaction;
import com.walnut.sparta.ucdn.console.domain.service.cluster.ClusterFileSyncTransactionManager;
import com.walnut.sparta.ucdn.console.domain.service.cluster.ClusterFileTransactionManager;
import com.walnut.sparta.ucdn.console.domain.service.cluster.FileSynchronizedEventListener;
import com.walnut.sparta.ucdn.console.domain.service.cluster.UFMTransactionSynchronizedNotifier;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.domain.service.cluster.MultiClusterFileSyncTransaction;
import com.walnut.sparta.ucdn.console.infrastructure.dto.ClusterFileSyncDTO;
import com.walnut.sparta.ucdn.console.ufm.FileMultiDistributionService;
import com.walnut.sparta.ucdn.console.domain.service.NodeFileDistributionService;
import com.walnut.sparta.ucdn.console.ufm.UOFSFileMultiDistributionService;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;
import com.walnut.sparta.ucdn.console.infrastructure.service.UCDNServiceManager;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NodeFileDistributionServiceImpl implements NodeFileDistributionService {

    @Resource
    private KOMFileSystem                               primaryFileSystem;

    @Resource
    private UniformVolumeManager                        primaryVolume;

    private FileMultiDistributionService                fileMultiDistributionService;

    @Resource
    private UOFSContentDelivery                         uofsContentDelivery;

    @Resource
    private BucketInstrument                            bucketInstrument;

    @Resource
    private UCDNServiceManager                          ucdnServiceManager;

    @Resource
    private VersionManage                               primaryVersion;

    private ClusterFileTransactionManager               clusterFileTransactionManager;

    @Resource
    private UFMTransactionSynchronizedNotifier          ufmTransactionSynchronizedNotifier;


    @PostConstruct
    private void init() throws UMBServiceException {
        this.clusterFileTransactionManager = new ClusterFileSyncTransactionManager();
        this.fileMultiDistributionService = new UOFSFileMultiDistributionService( this.uofsContentDelivery.getSpartaUCDNService() );
        this.fileMultiDistributionService.registerFileTransmitCompleteEventListener( new FileSynchronizedEventListener(
                this.primaryVersion, this.clusterFileTransactionManager,this.ufmTransactionSynchronizedNotifier, this.bucketInstrument )
        );
        this.fileMultiDistributionService.start();
    }

    @Override
    public void upload( String path, File file, String topic ) throws IOException, InterruptedException {
        FSNodeAllotment fsNodeAllotment = this.primaryFileSystem.getFSNodeAllotment();
        FileChannel channel = FileChannel.open(file.toPath(), StandardOpenOption.READ);
        TitanFileChannelChanface titanFileChannelKChannel = new TitanFileChannelChanface( channel );
        FileNode fileNode = fsNodeAllotment.newFileNode();
        fileNode.setDefinitionSize( file.length() );
        fileNode.setName( file.getName() );
        TitanFileReceiveEntity64 receiveEntity = new TitanFileReceiveEntity64( this.primaryFileSystem,path, fileNode,titanFileChannelKChannel,this.primaryVolume );

        this.primaryFileSystem.receive( receiveEntity );

        if( !topic.isBlank() ){
            this.fileMultiDistributionService.fileDistribution( fileNode, topic );
        }

    }

    @Override
    public void testDistribution(String path, String topic) throws IOException, InterruptedException {
        FileNode fileNode = (FileNode)this.primaryFileSystem.queryElement(path);

        this.fileMultiDistributionService.fileDistribution( fileNode, topic );
    }

    @Override
    public void clusterFileSync( ClusterFileSyncDTO dto ) throws IOException, InterruptedException {
        Folder folder = this.primaryFileSystem.getFolder( GUIDs.GUID72(dto.getFileGuid()) );
        List<GUID> guids = this.primaryVersion.fetchVersions(folder.getGuid());
        ServiceLifecycleIface lifecycleIface = this.ucdnServiceManager.getLifecycleIface();
        int serviceNum = lifecycleIface.countRegisteredService();

        ConcurrentHashMap<GUID, ClusterFileSyncTransaction> map = new ConcurrentHashMap<>();
        for ( GUID guid : guids ){
            MultiClusterFileSyncTransaction transaction = new MultiClusterFileSyncTransaction(serviceNum);
            map.put( guid, transaction );
        }

        this.clusterFileTransactionManager.register( folder.getGuid(), map );
        for( GUID guid : guids ){
            FileNode fileNode = this.primaryFileSystem.getFileNode(guid);
            this.fileMultiDistributionService.fileDistribution( fileNode, UCDNConstants.UCDNFileCloudDistributeTransmitTopic);
        }
    }
}
