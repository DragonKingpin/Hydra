package com.walnut.sailor.stream.fm;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.sailor.stream.fm.event.SFMEventSubscriber;
import com.walnut.sailor.stream.fm.protocol.RequestHead;
import com.walnut.sailor.stream.fm.session.SFMTransaction;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@AddressMapping( "com.walnut.sailor.stream.fm.FileMultiDistributionIface." )
public class SFMDistributionController implements Pinenut {

    protected Logger              logger;

    protected SessionPhaser       sessionPhaser;

    protected SFMSessionValidator sessionValidator;

    protected SFMConfig           config;

    protected SingleStreamFileMultiDistributionService distributionService;

    public SFMDistributionController( SingleStreamFileMultiDistributionService service ) {
        this.logger = LoggerFactory.getLogger( this.getClass() );
        this.distributionService = service;
        this.config              = service.getConfig();
        this.sessionPhaser       = new SFMSessionPhaser();
        this.sessionValidator    = new SFMSessionValidator( service );
    }

    protected String queryDirectoryPath( String directionRouteToken ) {
        String directoryPath = this.config.getStorageDirectory();
        if ( StringUtils.isNoneEmpty( directionRouteToken ) ) {
            String sz = this.distributionService.queryDestinedDirectoryByToken( directionRouteToken );
            if ( StringUtils.isNoneEmpty( sz ) ) {
                directoryPath = sz;
            }
        }

        return directoryPath;
    }

    protected Path formatFilePath( long sessionId, String fileName, String directionRouteToken ) {
        String directoryPath = this.queryDirectoryPath( directionRouteToken );
        this.sessionPhaser.registerDestinationDirectory( sessionId, directoryPath );
        return Path.of( directoryPath, fileName );
    }

    protected Path formatFilePath( long sessionId, String fileName ) {
        String directoryPath = this.config.getStorageDirectory();
        String sz = this.sessionPhaser.getDestinationDirectory( sessionId );
        if ( StringUtils.isNoneEmpty( sz ) ) {
            directoryPath = sz;
        }

        return Path.of( directoryPath, fileName );
    }

    @AddressMapping( "startDistribution" )
    public void startDistribution( RequestHead head, String fileName, String directionRouteToken ) throws IOException {
        if( this.sessionPhaser.getSFMTransaction(head.getSessionId()) != null ){
            this.logger.warn( "[Warning] SFMService `startDistribution` session assertion compromised." );
            this.sessionPhaser.removeSession( head.getSessionId() );
        }

        this.logger.info( "SFMService invoked `startDistribution`. <Start>" );
        long sessionId = head.getSessionId();

        Path desPath = this.formatFilePath( sessionId, fileName, directionRouteToken );
        File newFile = new File( desPath.toString() );
        if( newFile.length() != 0 ){
            if ( !newFile.delete() ) {
                throw new IOException( "Purging file has compromised, what => " + fileName );
            }
            this.logger.info( "The destination file (" + fileName + ") exists, and has been successfully eliminated. " );
        }

        SFMTransaction SFMTransaction = new SFMTransaction();
        SFMTransaction.setLastEventArrivedMills( System.currentTimeMillis() );
        SFMTransaction.finishStartTransmit();
        this.sessionPhaser.registerSessionTransaction( sessionId, SFMTransaction);
        this.sessionPhaser.getSFMTransaction( sessionId ).finishStartTransmit();
        this.sessionPhaser.registerFileHandler( sessionId, new RandomAccessFile(newFile, "rw") );

        this.logger.info( "SFMService invoked `startDistribution`. <Done>" );
    }

    @AddressMapping( "transmitFileContent" )
    public void transmitFileContent( RequestHead head, SFMFileFrame fileFrame ) throws IOException {
        if ( this.assertTransmitTransaction( head, fileFrame.getFileName() ) ){
            this.logger.warn( "[Warning] SFMService `transmitFileContent` session assertion compromised." );
            return;
        }

        this.logger.info( "SFMService invoked `transmitFileContent`. <Start>" );

        long sessionId = head.getSessionId();
        String fileName  = fileFrame.getFileName();
        Path desPath     = this.formatFilePath( sessionId, fileName );
        String szDesPath = desPath.toString();
        File file = new File( szDesPath );
        RandomAccessFile randomAccessFile = this.sessionPhaser.getFileHandler(sessionId);

        randomAccessFile.seek( fileFrame.getOffset() );
        randomAccessFile.write( fileFrame.getBytes() );
        this.sessionPhaser.getSFMTransaction( sessionId ).setLastEventArrivedMills( System.currentTimeMillis() );

        String desDirectory = this.sessionPhaser.getDestinationDirectory( sessionId );
        if( file.length() == fileFrame.getFileSize() ){
            randomAccessFile.close();
            this.sessionPhaser.removeSession( sessionId );
            this.sessionValidator.fileTransmitComplete( head );

            this.notifyFileTransmitCompleteEventSubscribers( sessionId, szDesPath, fileName, desDirectory );
        }

        this.logger.info( "SFMService invoked `transmitFileContent`. <Done>" );
    }

    protected void notifyFileTransmitCompleteEventSubscribers( long sessionId, String szDesPath, String fileName, String desDirectory ) {

        Collection<SFMEventSubscriber> subscribers = this.distributionService.fetchFileTransmitCompleteEventSubscribers();
        for ( SFMEventSubscriber subscriber : subscribers ) {
            subscriber.afterEventTriggered( szDesPath, fileName, desDirectory );
        }
    }

    protected boolean assertTransmitTransaction( RequestHead head, String fileName ) {
        long sessionId = head.getSessionId();
        SFMTransaction transaction = this.sessionPhaser.getSFMTransaction( sessionId );

        if( transaction == null ){
            this.logger.warn( "[Warning] SFMService `assertTransmitTransaction` session doesn`t existed. <Pass>" );
            this.sessionPhaser.removeSession( sessionId );
            return true;
        }

        long currentTimeMillis = System.currentTimeMillis();
        if( currentTimeMillis - transaction.getLastEventArrivedMills() > this.config.getSessionExpiredTimeMillis() ){
            this.logger.warn( "[Warning] SFMService `assertTransmitTransaction` session has expired. <Pass>" );
            this.rollBack( sessionId, fileName );
            return true;
        }

        if( !transaction.isStartTransmit() ){
            this.logger.warn( "[Warning] SFMService `assertTransmitTransaction` illegal transaction stage, which should never has started yet. <Pass>" );
            this.rollBack( sessionId, fileName );
            return true;
        }

        return false;
    }

    protected void rollBack( long sessionId, String fileName ) {
        this.logger.warn( "[Warning] SFMService `rollBack`. <Start>" );

        Path desPath = this.formatFilePath( sessionId, fileName );
        File file = new File( desPath.toString() );
        if ( !file.delete() ) {
            throw new IllegalStateException( "Purging file has compromised, what :" + fileName );
        }
        this.sessionPhaser.removeSession( sessionId );

        this.logger.warn( "[Warning] SFMService `rollBack`. <Done>" );
    }

}
