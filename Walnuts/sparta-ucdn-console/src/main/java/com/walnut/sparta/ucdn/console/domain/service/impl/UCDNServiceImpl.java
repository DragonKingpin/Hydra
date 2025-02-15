package com.walnut.sparta.ucdn.console.domain.service.impl;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.UMBServiceException;
import com.walnut.sparta.ucdn.console.umc.ufm.FileMultiDistributionService;
import com.walnut.sparta.ucdn.console.domain.service.UCDNService;
import com.walnut.sparta.ucdn.console.umc.ufm.UOFSFileMultiDistributionService;
import com.walnut.sparta.ucdn.console.infrastructure.UOFSContentDelivery;
import com.walnut.sparta.ucdn.console.umc.UMCMasterWarehouse;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;

@Service
public class UCDNServiceImpl implements UCDNService {
    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;
    private FileMultiDistributionService fileMultiDistributionService;

    @Resource
    UOFSContentDelivery         uofsContentDelivery;

    @PostConstruct
    private void init() throws UMBServiceException {
//        this.primaryVolume          = masterWarehouse.getUniformVolumeManager();
//        this.primaryFileSystem      = masterWarehouse.getKOMFileSystem();

        UMCMasterWarehouse warehouse = new UMCMasterWarehouse( this.primaryFileSystem, this.primaryVolume,this.uofsContentDelivery );
        this.fileMultiDistributionService = new UOFSFileMultiDistributionService( warehouse );
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
}
