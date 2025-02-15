package com.walnut.sparta.ucdn.service.umct;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.storage.TitanStorageExportIORequest;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.FSNodeAllotment;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.file.entity.Frame;
import com.pinecone.hydra.storage.file.entity.LocalCluster;
import com.pinecone.hydra.storage.file.transmit.exporter.TitanFileExportEntity64;
import com.pinecone.hydra.storage.io.TitanFileChannelChanface;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.kafka.UlfKafkaClient;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umct.WolfMCExpress;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.util.TreeMap;

@Service
public class FileSyncDistributionImpl implements FileSyncDistribution {
    @Resource
    private KOMFileSystem primaryFileSystem;

    @Resource
    private UniformVolumeManager primaryVolume;

    @Resource
    UlfKafkaClient ulfKafkaClient;

    @Override
    public void fileDistribution(FileNode fileNode, String topic, String server,long startSegId, long endSegId)   {
//        FSNodeAllotment fsNodeAllotment = this.primaryFileSystem.getFSNodeAllotment();
//        WolfMCBClient client = new WolfMCBClient(new WolfMCKafkaClient(server), "", this, WolfMCExpress.class);
//        client.compile(  );
//        BroadcastControlProducer producer = client.createBroadcastControlProducer();
//        producer.start();
//        TreeMap<Long, Frame> frames = fileNode.getClusters();
//        for( long i = startSegId; i < endSegId; i++ ){
//            LocalCluster frame = (LocalCluster)frames.get(i);
//            File tempFile = File.createTempFile("temp", frame.getSegGuid().toString());
//            FileChannel channel = FileChannel.open(tempFile.toPath(), StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
//            TitanFileChannelChanface kChannel = new TitanFileChannelChanface( channel );
//            FileNode newFileNode = fsNodeAllotment.newFileNode();
//
//            newFileNode.setPath( frame.getSourceName() );
//            newFileNode.setDefinitionSize( frame.getSize() );
//            TitanFileExportEntity64 exportEntity = new TitanFileExportEntity64( this.primaryFileSystem, this.primaryVolume, newFileNode, kChannel );
//
//            exportEntity.export( frame );
//        }
    }
}
