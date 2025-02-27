package com.walnut.sparta.ucdn.console.domain.service.impl;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.storage.bucket.BucketInstrument;
import com.pinecone.hydra.storage.bucket.entity.SiteNode;
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
import com.walnut.sparta.ucdn.console.infrastructure.SyncTransaction;
import com.walnut.sparta.ucdn.console.infrastructure.TransactionManage;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNSyncTransaction;
import com.walnut.sparta.ucdn.console.infrastructure.dto.SyncFileDTO;
import com.walnut.sparta.ucdn.console.umc.ufm.FileMultiDistributionService;
import com.walnut.sparta.ucdn.console.domain.service.UCDNService;
import com.walnut.sparta.ucdn.console.umc.ufm.UOFSFileMultiDistributionService;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;
import com.walnut.sparta.ucdn.console.umc.UMCMasterWarehouse;
import com.walnut.sparta.ucdn.console.umc.ssfm.SingleStreamFileMultiDistributionService;
import com.walnut.sparta.ucdn.console.umc.ssfm.SailorSSFMDistributionService;
import com.walnut.sparta.ucdn.console.umc.wolf.WolfRPCManage;
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
public class UCDNServiceImpl implements UCDNService {
    @Resource
    private KOMFileSystem                               primaryFileSystem;

    @Resource
    private UniformVolumeManager                        primaryVolume;

    private FileMultiDistributionService                fileMultiDistributionService;

    private SingleStreamFileMultiDistributionService    EFileMultiDistributionService;

    @Resource
    UOFSContentDelivery                                 uofsContentDelivery;

    @Resource
    private BucketInstrument                            bucketInstrument;

    @Resource
    private WolfRPCManage                               wolfRPCManage;

    @Resource
    private VersionManage                               primaryVersion;

    @Resource
    private TransactionManage                           transactionManage;

    @PostConstruct
    private void init() throws UMBServiceException {
//        this.primaryVolume          = masterWarehouse.getUniformVolumeManager();
//        this.primaryFileSystem      = masterWarehouse.getKOMFileSystem();

        UMCMasterWarehouse warehouse = new UMCMasterWarehouse( this.primaryFileSystem, this.primaryVolume,this.uofsContentDelivery, this.primaryVersion, this.transactionManage );
        this.fileMultiDistributionService = new UOFSFileMultiDistributionService( warehouse );
        this.EFileMultiDistributionService = new SailorSSFMDistributionService( warehouse );
    }

    @Override
    public void upload(String path, File file, String topic) throws IOException, InterruptedException {
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
    public void test() throws UMBServiceException {
        this.fileMultiDistributionService.test();
    }

    @Override
    public void testDistribution(String path, String topic) throws IOException, InterruptedException {
        FileNode fileNode = (FileNode)this.primaryFileSystem.queryElement(path);

        this.fileMultiDistributionService.fileDistribution( fileNode, topic );
    }

    @Override
    public void testEDdistribution( String path, String topic ) throws IOException {
        File file = new File(path);
        this.EFileMultiDistributionService.fileDistribution( file, topic );
    }

    @Override
    public void syncFile(SyncFileDTO dto) throws IOException, InterruptedException {
        Folder folder = this.primaryFileSystem.getFolder(GUIDs.GUID72(dto.getFileGuid()));
        List<GUID> guids = this.primaryVersion.fetchVersions(folder.getGuid());
        ServiceLifecycleIface lifecycleIFace = this.wolfRPCManage.getLifecycleIFace();
        int serviceNum = lifecycleIFace.liveServiceNum("hhhh");

        ConcurrentHashMap<GUID, SyncTransaction> map = new ConcurrentHashMap<>();
        for( GUID guid : guids ){
            UCDNSyncTransaction transaction = new UCDNSyncTransaction(serviceNum);
            map.put( guid, transaction );
        }

        this.transactionManage.register( folder.getGuid(), map );

        for( GUID guid : guids ){
            FileNode fileNode = this.primaryFileSystem.getFileNode(guid);
            this.fileMultiDistributionService.fileDistribution( fileNode, UCDNConstants.UCDNFileCloudDistributeTopic);
        }
        this.bucketInstrument.createSyncState( folder.getGuid(), 1 );
    }
}
