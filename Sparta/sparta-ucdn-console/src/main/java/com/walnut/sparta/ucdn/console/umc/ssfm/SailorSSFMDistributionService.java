package com.walnut.sparta.ucdn.console.umc.ssfm;


import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.broadcast.BroadcastControlConsumer;
import com.pinecone.hydra.umb.broadcast.BroadcastControlProducer;
import com.pinecone.hydra.umb.wolf.UlfBroadcastControlNode;
import com.walnut.sparta.ucdn.console.infrastructure.EFileContent;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class SailorSSFMDistributionService implements SingleStreamFileMultiDistributionService {

    protected UlfBroadcastControlNode   client;

    protected BroadcastControlProducer  producer;

    protected BroadcastControlConsumer  consumer;

    public SailorSSFMDistributionService(MasterWarehouse masterWarehouse ) throws UMBServiceException {
//        this.client   = masterWarehouse.getKafkaEFileClient();
//        this.producer = client.createBroadcastControlProducer();
//        this.consumer = client.createBroadcastControlConsumer(UCDNConstants.UCDNEFileCloudDistributeTopic, UCDNConstants.UCDNFileServiceGroup);
//        this.consumer.registerController( new UCDNEFMDController( masterWarehouse ) );
//        this.consumer.start();
//        this.producer.start();
    }

    @Override
    public void fileDistribution(File file, String topic) throws IOException {
        EFileMultiDistributionIface distributionIface = this.producer.getIface(EFileMultiDistributionIface.class, topic);

        RequestHead head = RequestHead.newRequest().setSessionId(System.currentTimeMillis());
        distributionIface.startDistribution( head, file.getName() );
        //this.producer.issueInform( topic, "com.walnut.sparta.ucdn.console.umc.ufmc.EFileMultiDistributionIface.startDistribution", head, file.getName() );

        FileInputStream fileInputStream = new FileInputStream(file);
        int bufferSize = 900 * 1024;
        byte[] buffer = new byte[ bufferSize ];
        int bytesRead;
        long currentPosition = 0;

        while( ( bytesRead = fileInputStream.read( buffer ) )!=-1 ) {
            if ( bytesRead < bufferSize ) {
                byte[] validData = Arrays.copyOfRange(buffer, 0, bytesRead);
                buffer = validData;
            }
            distributionIface.transmitFileContent( head, new EFileContent( buffer, file.length(), file.getName(), currentPosition, bytesRead ) );
            currentPosition = currentPosition + bytesRead;
        }
    }
}
