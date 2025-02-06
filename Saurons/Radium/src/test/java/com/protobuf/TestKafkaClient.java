package com.protobuf;

import com.pinecone.Pinecone;
import com.pinecone.framework.system.CascadeSystem;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.umb.UMBClientException;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.UlfMBInformMessage;
import com.pinecone.hydra.umb.UlfPackageMessageHandler;
import com.pinecone.hydra.umb.broadcast.BroadcastConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlNode;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.broadcast.BroadcastProducer;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastConsumer;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastProducer;
import com.pinecone.hydra.umb.kafka.KafkaClient;
import com.pinecone.hydra.umb.kafka.WolfMCKafkaClient;
import com.pinecone.hydra.umb.rocket.RocketClient;
import com.pinecone.hydra.umb.rocket.RocketMQClient;
import com.pinecone.hydra.umb.rocket.UlfRocketClient;
import com.pinecone.hydra.umb.rocket.WolfMCRocketClient;
import com.pinecone.hydra.umb.wolf.WolfMCBClient;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.UMCReceiver;
import com.pinecone.hydra.umc.msg.UMCTransmit;
import com.pinecone.hydra.umct.UMCTExpressHandler;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.sauron.radium.Radium;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;


class Luben extends Radium {
    public Luben( String[] args, CascadeSystem parent ) {
        this( args, null, parent );
    }

    public Luben( String[] args, String szName, CascadeSystem parent ){
        super( args, szName, parent );
    }

    @Override
    public void vitalize () throws Exception {
        //this.testFundamental();
        //this.testWolfMB();
        //this.testWolfMCTB();
        //this.testKafka();
        //this.testWolfKafka();
        this.testWolfMCTBKafka();
    }

    public void testFundamental() throws Exception {
        String nameSrvAddr = "localhost:9876";
        String groupName = "testGroup";
        String topic = "testTopic";
        String tags = "*";
        String keys = "testKeys";
        String body = "This is a test message";


        RocketClient client = new RocketMQClient( nameSrvAddr, groupName );
        BroadcastConsumer consumer = client.createConsumer( topic );
        consumer.start(new UlfPackageMessageHandler() {
            @Override
            public void onSuccessfulMsgReceived( byte[] body, Object[] args ) throws Exception {
                Debug.trace( new String( body ) );
            }
        });


        BroadcastProducer producer = client.createProducer();
        producer.start();
        producer.sendMessage( topic, body.getBytes() );

        Debug.sleep( 100000 );
    }

    public void testKafka() throws UMBClientException, UMBServiceException {
        String server = "localhost:9092";
        String keySerializer = StringSerializer.class.getName();
        String valueSerializer = StringSerializer.class.getName();
        String topic = "testTopic";
        String group = "testGroup";
        String keyDeserializer = StringDeserializer.class.getName();
        String valueDeserializer = StringDeserializer.class.getName();
        String autoOffsetReset = "earliest";

        KafkaClient kafkaClient = new KafkaClient( server );
        byte[] bytes = new byte[100000];
        for( int i=0; i< 100000; i++ ){
            int j = 0;
           j = i % 128;
            bytes[i] = (byte) j;
        }

        BroadcastProducer producer = kafkaClient.createProducer();
        producer.sendMessage( topic, bytes );


        BroadcastConsumer consumer = kafkaClient.createConsumer(topic,group);
        consumer.start(new UlfPackageMessageHandler() {
            @Override
            public void onSuccessfulMsgReceived( byte[] body, Object[] args ) throws Exception {
                Debug.trace( body.length );
                for( byte c : body ){
                    Debug.trace(c);
                }
            }
        });

    }

    public void testWolfKafka() throws UMBServiceException, UMBClientException {
        String server = "localhost:9092";
        String keySerializer = StringSerializer.class.getName();
        String valueSerializer = StringSerializer.class.getName();
        String topic = "testTopic";
        String group = "testGroup";
        String keyDeserializer = StringDeserializer.class.getName();
        String valueDeserializer = StringDeserializer.class.getName();
        String autoOffsetReset = "earliest";

        WolfMCKafkaClient wolfMCKafkaClient = new WolfMCKafkaClient( server );
        UMCBroadcastProducer producer = wolfMCKafkaClient.createUlfProducer();
        producer.sendMessage( topic,"你好".getBytes() );

        UMCBroadcastConsumer consumer = wolfMCKafkaClient.createUlfConsumer(topic, group);
        consumer.start( new UlfPackageMessageHandler() {
            @Override
            public void onSuccessfulMsgReceived( byte[] body, Object[] args ) throws Exception {
                Debug.trace( new String( body ) );
            }
        } );


    }

    public void testWolfMB() throws Exception {
        String nameSrvAddr = "localhost:9876";
        String groupName = "testGroup";
        String topic = "testTopic";
        String tags = "*";
        String keys = "testKeys";


        UlfRocketClient client = new WolfMCRocketClient( nameSrvAddr, groupName );
        UMCBroadcastConsumer consumer = client.createUlfConsumer( topic );
        consumer.start(new UMCTExpressHandler() {
            @Override
            public void onSuccessfulMsgReceived( Medium medium, UMCTransmit transmit, UMCReceiver receiver, UMCMessage msg, Object[] args ) throws Exception {
                if ( msg.evinceTransferMessage() != null ) {
                    Debug.greenfs( msg.getHead(), new String( (byte[]) msg.evinceTransferMessage().getBody() ) );
                }
                else {
                    Debug.redf( msg.getHead() );
                }
            }
        });


        UMCBroadcastProducer producer = client.createUlfProducer();
        producer.start();

        producer.sendMessage( topic, new UlfMBInformMessage( new JSONMaptron( "{ path: '/user/getName ' }" ) ) );
        //producer.sendMessage( topic, new UlfMBInformMessage( new JSONMaptron( "{ msg: 'Jesus, Mr.Garrison! ' }" ), 0xFA ) );
        //producer.sendMessage( topic, new UlfBytesTransferMessage( new JSONMaptron( "{ msg: 'Jesus, Mr.Garrison! ' }" ), "fuck you" ) );


        Debug.sleep( 100000 );
    }
    public void testWolfMCTBKafka() throws IOException {
        String server = "localhost:9092";
        String keySerializer = StringSerializer.class.getName();
        String valueSerializer = StringSerializer.class.getName();
        String topic = "testTopic";
        String group = "testGroup";
        String keyDeserializer = StringDeserializer.class.getName();
        String valueDeserializer = StringDeserializer.class.getName();
        String autoOffsetReset = "earliest";

        WolfMCBClient client = new WolfMCBClient(new WolfMCKafkaClient(server), "", this, WolfMCExpress.class);

        client.compile( Raccoon.class, false );
        BroadcastControlProducer producer = client.createBroadcastControlProducer();


        producer.start();
        producer.issueInform( topic, "com.protobuf.Raccoon.scratch", "fuck you !", 2025 );

        Raccoon raccoon = producer.getIface( Raccoon.class, topic );
        raccoon.scratch("haha, I am XiaoMing", 5202 );

        BroadcastControlConsumer consumer = client.createBroadcastControlConsumer(topic,group);
        RaccoonController controller  = new RaccoonController();
        consumer.registerController( controller );
        consumer.start();



        Debug.sleep( 100000 );
    }
}


public class TestKafkaClient {
    public static void main(String[] args) throws Exception {
        Pinecone.init( (Object...cfg )->{

            Luben luben = (Luben) Pinecone.sys().getTaskManager().add( new Luben( args, Pinecone.sys() ) );
            luben.vitalize();

            return 0;
        }, (Object[]) args );
    }
}
