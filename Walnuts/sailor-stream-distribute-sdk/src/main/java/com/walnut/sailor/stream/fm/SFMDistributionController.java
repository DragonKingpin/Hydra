package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sailor.stream.fm.protocol.RequestHead;
import com.walnut.sailor.stream.fm.session.SFMTransaction;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@AddressMapping( "com.pinecone.hydra.uofs.ufm.EFileMultiDistributionIface." )
public class SFMDistributionController implements Pinenut {

    protected Logger              log;

    protected SessionPhaser       sessionPhaser;

    protected SFMSessionValidator sessionValidator;

    protected SFMConfig           config;

    protected SingleStreamFileMultiDistributionService distributionService;

    public SFMDistributionController( SingleStreamFileMultiDistributionService service ) {
        this.log                 = LoggerFactory.getLogger( this.getClass() );
        this.distributionService = service;
        this.config              = service.getConfig();
        this.sessionPhaser       = new SFMSessionPhaser();
        this.sessionValidator    = new SFMSessionValidator( service );
    }

    @AddressMapping( "startDistribution" )
    public void startDistribution( RequestHead head, String fileName ) throws IOException {
        if( this.sessionPhaser.getSFMTransaction(head.getSessionId()) != null ){
            log.info("异常存在的事务");
            this.sessionPhaser.removeSFMTransaction( head.getSessionId() );
        }

        log.info("开始");
        long sessionId = head.getSessionId();

        File newFile = new File(UCDNConstants.defaultStoragePath + fileName);
        newFile.createNewFile();

        SFMTransaction SFMTransaction = new SFMTransaction();
        SFMTransaction.setLastEventArrivedMills( System.currentTimeMillis() );
        SFMTransaction.finishStartTransmit();
        this.sessionPhaser.registerSessionTransaction( sessionId, SFMTransaction);
        this.sessionPhaser.getSFMTransaction( sessionId ).finishStartTransmit();
        this.sessionPhaser.registerFileOutputStream( sessionId, new RandomAccessFile(newFile, "rw"));
    }

    @AddressMapping( "transmitFileContent" )
    public void transmitFileContent( RequestHead head, SFMFileFrame fileContent ) throws IOException {
        long sessionId = head.getSessionId();

        File file = new File(UCDNConstants.defaultStoragePath + fileContent.getFileName());
        if ( this.assertTransmitTransaction( head, fileContent.getFileName() ) ){
            return;
        }

        RandomAccessFile randomAccessFile = this.sessionPhaser.getFileOutputStream(sessionId);

        randomAccessFile.seek( fileContent.getOffset() );
        randomAccessFile.write( fileContent.getBytes() );
        this.sessionPhaser.getSFMTransaction( sessionId ).setLastEventArrivedMills( System.currentTimeMillis() );

        if( file.length() == fileContent.getFileSize() ){
            randomAccessFile.close();
            this.sessionPhaser.removeFileOutputStream( sessionId );
            this.sessionPhaser.removeSFMTransaction( sessionId );
            this.sessionValidator.fileTransmitComplete( head );
        }
    }

    protected boolean assertTransmitTransaction( RequestHead head, String fileName ) {
        long sessionId = head.getSessionId();
        SFMTransaction transaction = this.sessionPhaser.getSFMTransaction( sessionId );

        if( transaction == null ){
            this.log.info("不存在的事务，直接忽略");
            this.sessionPhaser.removeSFMTransaction( sessionId );
            return true;
        }

        long currentTimeMillis = System.currentTimeMillis();
        if( currentTimeMillis - transaction.getLastEventArrivedMills() > UCDNConstants.expireTimeMillis ){
            log.info("事务过期");
            this.rollBack( sessionId, fileName );
            return true;
        }

        if( !transaction.isStartTransmit() ){
            this.log.info("异常的事务流程");
            this.rollBack( sessionId, fileName );
            return true;
        }

        return false;
    }

    protected void rollBack( long sessionId, String fileName ) {
        this.sessionPhaser.removeSFMTransaction( sessionId );
        this.sessionPhaser.removeFileOutputStream( sessionId );
        File file = new File(UCDNConstants.defaultStoragePath + fileName);
        file.delete();
    }

}
