package com.sparta;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.pinecone.tritium.Tritium;
import com.walnut.sailor.stream.fm.SFMConfig;
import com.walnut.sailor.stream.fm.SFMSessionValidatorController;
import com.walnut.sailor.stream.fm.SailorFMConfig;
import com.walnut.sailor.stream.fm.SailorFMDistributionService;
import com.walnut.sailor.stream.fm.SingleStreamFileMultiDistributionService;
import com.walnut.sailor.stream.fm.event.SFMEventSubscriber;

class Lois extends Tritium {
    public Lois( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Lois( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        this.testSFM();
    }

    private void testSFM() throws Exception {
        UlfBroadcastControlNode controlNode  = new WolfMCBClient(new WolfMCKafkaClient("b-serverkingpin:9092"), "", this, WolfMCExpress.class);
        SFMConfig config = new SailorFMConfig( new JSONMaptron("{\n" +
                "      \"fileFrameSize\": 972800,\n" +
                "      \"sessionExpiredTimeMillis\": 7200000,\n" +
                "      \"fileCloudDistributeTransmitTopic\": \"ucdn-file-cloud-distribute-transmit-topic\",\n" +
                "      \"fileServiceTransmitGroup\": \"UCDNFileServiceTransmitGroup\",\n" +
                "      \"storageDirectory\": \"E:/fs/\",\n" +
                "    }") );
        SingleStreamFileMultiDistributionService service = new SailorFMDistributionService( controlNode, config );
        service.registerDirectionRoute( "major", "E:/fs/" );

        service.registerFileTransmitCompleteEventSubscriber(new SFMEventSubscriber() {
            @Override
            public void afterEventTriggered( String path, String fileName, String directoryPath ) {
                Debug.greenfs( "MiaoMiao~", path, fileName, directoryPath );
            }
        });

        service.start();

        BroadcastControlConsumer consumer = controlNode.createBroadcastControlConsumer( config.getFileCloudDistributeTransmitTopic(), config.getFileServiceTransmitGroup() );
        consumer.registerController( new SFMSessionValidatorController() );

        service.distributeFile( "Ton Koopman - Toccata and Fugue in D minor, BWV 565 b.flac", "E:/", "major" );
    }
}


public class TestSFM {
    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{
            Lois lois = (Lois) Pinecone.sys().getTaskManager().add( new Lois( args, Pinecone.sys() ) );
            lois.vitalize();
            return 0;
        }, (Object[]) args );
    }
}