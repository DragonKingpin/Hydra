package com.pinecone.hydra.umb.kafka;

import com.pinecone.framework.system.Nullable;
import com.pinecone.hydra.umb.UMBBytesDecoder;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.UlfMBInformMessage;
import com.pinecone.hydra.umb.UlfPackageMessageHandler;
import com.pinecone.hydra.umb.broadcast.UMCBroadcastConsumer;
import com.pinecone.hydra.umb.rocket.RocketMedium;
import com.pinecone.hydra.umb.rocket.RocketReceiver;
import com.pinecone.hydra.umb.rocket.RocketTransmit;
import com.pinecone.hydra.umc.msg.EMCBytesDecoder;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCHead;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.UMCMethod;
import com.pinecone.hydra.umc.msg.UMCReceiver;
import com.pinecone.hydra.umc.msg.UMCTransmit;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.umc.wolfmc.UlfBytesTransferMessage;
import com.pinecone.hydra.umct.UMCTExpressHandler;

import java.io.IOException;

public class WolfKafkaConsumer extends UlfBroadcastPollConsumer<String, byte[] > implements UMCBroadcastConsumer {
    protected EMCBytesDecoder          mEMCBytesDecoder;

    protected ExtraHeadCoder           mExtraHeadCoder;

    protected Medium                   mMedium;
    protected UMCTransmit              mUMCTransmit;
    protected UMCReceiver              mUMCReceiver;

    public WolfKafkaConsumer( KClient client, String topic, String group, @Nullable ExtraHeadCoder extraHeadCoder ) {
        super( client,topic,group );

        this.mExtraHeadCoder           = extraHeadCoder;
        if ( this.mExtraHeadCoder == null ) {
            this.mExtraHeadCoder = client.getExtraHeadCoder();
        }

        this.mEMCBytesDecoder = new UMBBytesDecoder();

        // Dummy [ MQ is base on unidirectional communication. ]
        this.mMedium          = new RocketMedium( this.getKafkaClient() );
        this.mUMCReceiver     = new RocketReceiver( this.mMedium );
        this.mUMCTransmit     = new RocketTransmit( this.mMedium );

    }

    public WolfKafkaConsumer( KClient client, String topic, String group ) {
        this( client, topic, group, null );
    }

    protected UMCMessage decodeMessage( byte[] raw ) throws IOException {
        UMCHead head = WolfKafkaConsumer.this.mEMCBytesDecoder.decodeIntegrated( raw, WolfKafkaConsumer.this.mExtraHeadCoder );
        if ( head.getMethod() == UMCMethod.TRANSFER ) {
            int bodyLen = (int)head.getBodyLength();
            byte[] bodyBuf = new byte[ bodyLen ];
            int headSize = head.sizeof() + head.getExtraHeadLength();
            System.arraycopy( raw, headSize, bodyBuf, 0, bodyLen );

            return new UlfBytesTransferMessage( head, bodyBuf );
        }
        return new UlfMBInformMessage( head );
    }

    @Override
    public void start( UMCTExpressHandler handler ) throws UMBServiceException {
        super.start(new UlfPackageMessageHandler() {
            @Override
            public void onSuccessfulMsgReceived( byte[] raw, Object[] args ) throws Exception {
                UMCMessage message = WolfKafkaConsumer.this.decodeMessage( raw );
                handler.onSuccessfulMsgReceived( WolfKafkaConsumer.this.mMedium, WolfKafkaConsumer.this.mUMCTransmit, WolfKafkaConsumer.this.mUMCReceiver, message, args );
            }

            @Override
            public void onErrorMsgReceived( byte[] raw, Object[] args ) throws Exception {
                UMCMessage message = WolfKafkaConsumer.this.decodeMessage( raw );
                handler.onErrorMsgReceived( WolfKafkaConsumer.this.mMedium, WolfKafkaConsumer.this.mUMCTransmit, WolfKafkaConsumer.this.mUMCReceiver, message, args );
            }

            @Override
            public void onError( Object data, Throwable cause ) {
                handler.onError( data, cause );
            }
        });
    }

    @Override
    public UlfKafkaClient getKafkaClient() {
        return (UlfKafkaClient) this.kafkaClient;
    }
}
