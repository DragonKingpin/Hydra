package com.walnut.sailor.stream.fm;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sailor.stream.fm.protocol.RequestHead;
import com.walnut.sailor.stream.fm.session.UFMCTransaction;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNConstants;
import com.walnut.sparta.ucdn.console.umc.MasterWarehouse;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Slf4j
@Controller
@AddressMapping( "com.pinecone.hydra.uofs.ufm.EFileMultiDistributionIface." )
public class UCDNEFMDController {

    protected SessionPhaser sessionPhaser;

    protected UEFMSessionValidator      sessionValidator;

    public UCDNEFMDController( MasterWarehouse masterWarehouse ) throws UMBServiceException {
        this.sessionPhaser = masterWarehouse.getExternalSessionPhaser();
        this.sessionValidator = new UEFMSessionValidator( masterWarehouse );
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
        this.sessionPhaser.registerFileOutputStream( sessionId, new RandomAccessFile(newFile, "rw"));
    }

    @AddressMapping("transmitFileContent")
    public void transmitFileContent(RequestHead head, EFileContent fileContent) throws IOException {
        long sessionId = head.getSessionId();

        File file = new File(UCDNConstants.defaultStoragePath + fileContent.getFileName());
        if ( this.assertTransmitTransaction( head, fileContent.getFileName() ) ){
            return;
        }

        RandomAccessFile randomAccessFile = this.sessionPhaser.getFileOutputStream(sessionId);

        randomAccessFile.seek( fileContent.getOffset() );
        randomAccessFile.write( fileContent.getBytes() );
        this.sessionPhaser.getUFMCTransaction( sessionId ).setLastEventArrivedMills( System.currentTimeMillis() );

        if( file.length() == fileContent.getFileSize() ){
            randomAccessFile.close();
            this.sessionPhaser.removeFileOutputStream( sessionId );
            this.sessionPhaser.removeUFMCTransaction( sessionId );
            this.sessionValidator.fileTransmitComplete( head );
        }
    }

    protected boolean assertTransmitTransaction( RequestHead head, String fileName ){
        long sessionId = head.getSessionId();
        UFMCTransaction ufmcTransaction = this.sessionPhaser.getUFMCTransaction(sessionId);

        if( ufmcTransaction == null ){
            log.info("不存在的事务，直接忽略");
            this.sessionPhaser.removeUFMCTransaction( sessionId );
            return true;
        }

        long currentTimeMillis = System.currentTimeMillis();
        if( currentTimeMillis - ufmcTransaction.getLastEventArrivedMills() > UCDNConstants.expireTimeMillis){
            log.info("事务过期");
            this.rollBack( sessionId, fileName );
            return true;
        }

        if( !ufmcTransaction.isStartTransmit() ){
            log.info("异常的事务流程");
            this.rollBack( sessionId, fileName );
            return true;
        }

        return false;
    }

    private void rollBack( long sessionId, String fileName ) {
        this.sessionPhaser.removeUFMCTransaction( sessionId );
        this.sessionPhaser.removeFileOutputStream( sessionId );
        File file = new File(UCDNConstants.defaultStoragePath + fileName);
        file.delete();
    }

}
