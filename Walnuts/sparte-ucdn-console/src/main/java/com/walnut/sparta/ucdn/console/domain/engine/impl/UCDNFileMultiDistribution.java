package com.walnut.sparta.ucdn.console.domain.engine.impl;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.Frame;
import com.pinecone.hydra.storage.file.entity.LocalFrame;
import com.pinecone.hydra.storage.file.transmit.exporter.TitanFileExportEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.ulf.util.guid.GUIDs;
import com.walnut.sparta.ucdn.console.domain.CentralControlUnit;
import com.walnut.sparta.ucdn.console.domain.engine.FileDistributionEngine;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.infrastructure.entity.UFMDClusterFrame;
import com.walnut.sparta.ucdn.console.infrastructure.entity.UFMDClusterDO;
import com.walnut.sparta.ucdn.console.umc.FileDistribution;
import com.walnut.sparta.ucdn.console.umc.FileDistributionController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.TreeMap;

@Component
public class UCDNFileMultiDistribution implements FileDistributionEngine {

    @Resource
    private KOMFileSystem                   primaryFileSystem;

    @Resource
    private CentralControlUnit centralControlUnit;

    @Resource
    private UniformVolumeManager            primaryVolume;

    protected UlfBroadcastControlNode       client;

    protected BroadcastControlProducer      producer;

    protected BroadcastControlConsumer      consumer;


    public UCDNFileMultiDistribution(@Qualifier("kafkaFileServiceClient") UlfBroadcastControlNode client, FileDistributionController fileDistributionController ) throws UMBServiceException {
        this.producer = client.createBroadcastControlProducer();
        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNFileCloudDistributeTopic, UCDNConstants.UCDNFileServiceGroup);
        consumer.registerController( fileDistributionController );
        this.consumer.start();
        this.producer.start();
        this.client = client;
    }


    @Override
    public void fileDistribution(FileNode fileNode, String topic) throws IOException, InterruptedException {
        this.centralControlUnit.register( fileNode.getGuid(), new Object() );
        FSNodeAllotment fsNodeAllotment = this.primaryFileSystem.getFSNodeAllotment();
        FileDistribution fileDistribution = this.producer.getIface(FileDistribution.class, topic);
        String path = this.primaryFileSystem.getPath(fileNode.getGuid());
        fileDistribution.setFileMeta(path,fileNode.getPhysicalSize());
        //this.producer.issueInform( topic, "com.walnut.sparta.ucdn.console.umc.FileDistribution.setFileMeta",path,fileNode.getPhysicalSize() );

        TreeMap<Long, Frame> frames = fileNode.getFrames();
        int distributionFrameNum = 0;

        for( long i = 0; i < frames.size(); i++ ){
            LocalFrame frame = ( LocalFrame ) frames.get( i );
            UFMDClusterDO UFMDClusterDO = new UFMDClusterDO( path, i, frame.getSize(),frame.getCrc32(),frame.getSourceName() );
            fileDistribution.setFrameMeta(UFMDClusterDO);
            //this.producer.issueInform( topic, "com.walnut.sparta.ucdn.console.umc.FileDistribution.setFrameMeta",frameVO );
            File tempFile = File.createTempFile( frame.getSegGuid().toString(), ".temp" );
            FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
            TitanFileChannelChanface kChannel = new TitanFileChannelChanface( channel );
            FileNode newFileNode = fsNodeAllotment.newFileNode();
            newFileNode.setPath( frame.getSourceName() );
            newFileNode.setDefinitionSize( frame.getSize() );
            TitanFileExportEntity64 exportEntity = new TitanFileExportEntity64( this.primaryFileSystem, this.primaryVolume, newFileNode, kChannel );
            exportEntity.export( frame );

            FileInputStream fileInputStream = new FileInputStream(tempFile);
            byte[] buffer = new byte[ 512 * 1024 ];

            while( fileInputStream.read( buffer )!=-1 ){
                fileDistribution.saveFrameContent( new UFMDClusterFrame( buffer, path, i ) );
                //this.producer.issueInform( topic, "com.walnut.sparta.ucdn.console.umc.FileDistribution.saveFrameContent",new UFMDClusterFrame(buffer,path,i));
            }

            tempFile.delete();

            //fileDistribution.frameEnd( path, i );
            distributionFrameNum++;

            if( distributionFrameNum == 10 ){
                synchronized( this.centralControlUnit.getLock( fileNode.getGuid() ) ){
                    this.centralControlUnit.getLock( fileNode.getGuid() ).wait();
                }
                distributionFrameNum = 0;
            }
        }


    }

    @Override
    public void test() throws UMBServiceException {
        FileDistribution fileDistribution = this.producer.getIface(FileDistribution.class,"testTopic");
        FileNode fileNode = this.primaryFileSystem.getFileNode(GUIDs.GUID72("1214792-000373-0003-00"));
        String path = this.primaryFileSystem.getPath(fileNode.getGuid());
        //fileDistribution.setFileMeta( path );
        BroadcastControlConsumer consumer = this.getConsumer("testTopic", "testGroup");
        consumer.start();
    }

    @Override
    public BroadcastControlConsumer getConsumer( String topic,String group ) {
        return this.consumer;
    }

    @Override
    public BroadcastControlProducer getProducer() {
        return this.producer;
    }
}
