package com.pinecone.hydra.storage.volume;

import com.pinecone.framework.system.executum.ArchThreadum;
import com.pinecone.framework.system.executum.Executum;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.file.entity.FileNode;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;

import java.io.IOException;
import java.nio.channels.FileChannel;

public class KenThread /*extends ArchThreadum implements Runnable, Executum*/ implements Runnable{
    private Thread thread;
    private LogicVolume volume;
    public KenThread( LogicVolume volume ){
//        super( null, heist.getHeistium() );
//        Thread affinityThread = new Thread( this );
        //this.setThreadAffinity( affinityThread );
        //this.getAffiliateThread().setName( this.nomenclature() );
        //this.setName( affinityThread.getName() );
        this.volume = volume;
    }

    @Override
    public void run() {

    }

    //    public void start(KOMFileSystem fileSystem, FileNode file, FileChannel channel, long offset, long endSize) {
//        this.thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    volume.channelReceive( fileSystem,file,channel,offset,endSize );
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        );
//        this.thread.start();
//    }
    public void stop(){
        this.thread.interrupt();
    }
}
