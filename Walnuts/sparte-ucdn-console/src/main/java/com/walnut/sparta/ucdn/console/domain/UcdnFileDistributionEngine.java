package com.walnut.sparta.ucdn.console.domain;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.summer.prototype.Component;
import com.walnut.sparta.ucdn.console.umc.FileDistributionController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

@Component
public class UcdnFileDistributionEngine implements FileDistributionEngine{
    @Resource
    protected FileDistributionController    fileDistributionController;

    @Resource
    private KOMFileSystem                   primaryFileSystem;

    @Resource
    private UniformVolumeManager            primaryVolume;

    protected WolfMCBClient                 client;

    protected BroadcastControlProducer      producer;


    public UcdnFileDistributionEngine( WolfMCBClient client ){
        this.producer = client.createBroadcastControlProducer();
    }


    @Override
    public void FileDistribution(FileNode fileNode, String topic) {

    }

    @Override
    public BroadcastControlConsumer getConsumer( String topic,String group ) {
        BroadcastControlConsumer consumer = this.client.createBroadcastControlConsumer(topic, group);
        consumer.registerController( this.fileDistributionController );
        return consumer;
    }

    @Override
    public BroadcastControlProducer getProducer() {
        return this.producer;
    }
}
