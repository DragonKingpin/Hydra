package com.walnut.sparta.ucdn.console.umc.ufmc;

import com.pinecone.hydra.storage.file.KOMFileSystem;
import com.pinecone.hydra.storage.volume.UniformVolumeManager;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sparta.ucdn.console.infrastructure.EFileContent;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;
import com.walnut.sparta.ucdn.console.umc.ufm.protocol.RequestHead;
import com.walnut.sparta.ucdn.console.umc.ufmc.session.UFMCTransaction;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Controller
@AddressMapping( "com.pinecone.hydra.uofs.ufm.EFileMultiDistributionIface." )
public class UCDNEFMDController {

    protected ExternalSessionPhaser     sessionPhaser;

    public UCDNEFMDController(MasterWarehouse masterWarehouse){
        this.sessionPhaser = masterWarehouse.getExternalSessionPhaser();
    }

    @AddressMapping("startDistribution")
    public void startDistribution(RequestHead head, String fileName) throws IOException {
        if( this.sessionPhaser.getUFMCTransaction(head.getSessionId()) != null ){
            log.info("异常存在的事务");
            this.sessionPhaser.removeUFMCTransaction( head.getSessionId() );
        }

        log.info("开始");
        long sessionId = head.getSessionId();

        File newFile = new File(UCDNConstants.defaultStoragePath + fileName);
        newFile.createNewFile();

        UFMCTransaction ufmcTransaction = new UFMCTransaction();
        ufmcTransaction.setLastEventArrivedMills( System.currentTimeMillis() );
        ufmcTransaction.finishStartTransmit();
        this.sessionPhaser.registerSessionTransaction( sessionId, ufmcTransaction );
        this.sessionPhaser.getUFMCTransaction( sessionId ).finishStartTransmit();
        this.sessionPhaser.registerFileOutputStream( sessionId, new FileOutputStream( newFile ));
    }

    @AddressMapping("transmitFileContent")
    void transmitFileContent(RequestHead head, EFileContent fileContent) throws IOException {
        long sessionId = head.getSessionId();

        if ( this.assertTransmitTransaction( head ) ){
            return;
        }

        log.info("保存文件内容");
        FileOutputStream fileOutputStream = this.sessionPhaser.getFileOutputStream(sessionId);

        fileOutputStream.write( fileContent.getBytes() );
        this.sessionPhaser.getUFMCTransaction( sessionId ).setLastEventArrivedMills( System.currentTimeMillis() );


    }

    protected boolean assertTransmitTransaction( RequestHead head ){
        long sessionId = head.getSessionId();
        UFMCTransaction ufmcTransaction = this.sessionPhaser.getUFMCTransaction(sessionId);

        if( ufmcTransaction == null ){
            log.info("不存在的事务，直接忽略");
            return true;
        }

        long currentTimeMillis = System.currentTimeMillis();
        if( currentTimeMillis - ufmcTransaction.getLastEventArrivedMills() > UCDNConstants.expireTimeMillis){
            log.info("事务过期");
            this.sessionPhaser.removeUFMCTransaction( sessionId );
            this.sessionPhaser.removeFileOutputStream( sessionId );
            return true;
        }

        if( !ufmcTransaction.isStartTransmit() ){
            log.info("异常的事务流程");
            this.sessionPhaser.removeUFMCTransaction( sessionId );
            this.sessionPhaser.removeFileOutputStream( sessionId );
            return true;
        }

        return false;
    }


}
