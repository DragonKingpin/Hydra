package com.walnut.sparta.ucdn.console.domain.service.impl;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.transmit.receiver.TitanFileReceiveEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.UMBServiceException;
import com.walnut.sparta.ucdn.console.domain.engine.FileDistributionEngine;
import com.walnut.sparta.ucdn.console.domain.service.UCDNService;
import org.springframework.stereotype.Service;

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

    @Resource
    private FileDistributionEngine fileDistributionEngine;

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
            this.fileDistributionEngine.fileDistribution( fileNode, topic );
        }

    }

    @Override
    public void test() throws UMBServiceException {
        this.fileDistributionEngine.test();
    }

    @Override
    public void testDistribution(String path, String topic) throws IOException, InterruptedException {
        FileNode fileNode = (FileNode)this.primaryFileSystem.queryElement(path);

        this.fileDistributionEngine.fileDistribution( fileNode, topic );
    }
}
