package com.sauron.radium.heistron;

import com.sauron.radium.util.HttpBrowserConf;
import com.pinecone.framework.util.io.FileUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

public abstract class HTTPCrew extends ArchCrew {
    protected String heistURL;

    public HTTPCrew ( HTTPHeist heist, int id ){
        super( heist, id );
        this.heistURL = this.parentHeist().getConfig().optString( "HeistURL" );
    }

    public void validateSpoil( Page page ) throws LootAbortException, IllegalStateException {
        if( page.getBytes().length < this.joFailureConf.optInt( "FailedFileSize" ) ) {
            throw new IllegalStateException("CompromisedFilesSize");
        }
    }

    @Override
    public HTTPHeist parentHeist() {
        return (HTTPHeist) this.heist;
    }

    protected void afterPageQueried( Page cache ) {
    }

    // [Query, Get] Inlet method.
    public Page queryHTTPPage( Request request, boolean bPooled ) {
        Page cache = this.parentHeist().queryHTTPPage( request, bPooled );
        try{
            HttpBrowserConf browserConf = this.parentHeist().getBrowserConf();
            if( browserConf.enableRandomDelay ){
                Thread.sleep( ( new Random() ).nextInt( browserConf.randomDelayMax - browserConf.randomDelayMin + 1 ) + browserConf.randomDelayMin );
            }
        }
        catch ( InterruptedException e ) {
            this.parentHeist().handleKillException( e );
        }

        this.afterPageQueried( cache );
        this.tracer().info( "[{}] [PageFetched:<Status:{}, Size:{}>]", this.lifecycleTracerSignature(), cache.getStatusCode(), cache.getBytes().length );
        return cache;
    }

    public Page queryHTTPPage( Request request ) {
        return this.queryHTTPPage( request, true );
    }

    public Page getHTTPPage( String szHref, boolean bPooled ) {
        Request request = new Request( szHref );
        request.putExtra("requestType", "CrewDefault");
        request.setMethod( "GET" );

        return this.queryHTTPPage( request, bPooled );
    }

    public Page getHTTPPage( String szHref ) {
        return this.getHTTPPage( szHref, true );
    }

    public String getHTTPFile( String szHref, boolean bPooled ) {
        return this.getHTTPPage( szHref, bPooled ).getRawText();
    }

    public String getHTTPFile( String szHref ) {
        return this.getHTTPFile( szHref, true );
    }

    // No validate
    public Page queryHTTPPage( Request request, String szFilePath ) {
        try {
            Page cachePage;
            byte[] cache ;
            FileSystemManager fsm = this.getDafaultFileSystemManager();
            FileObject fileObject = fsm.resolveFile( szFilePath );
            if ( fileObject.exists() ) {
                try ( InputStream inputStream = fileObject.getContent().getInputStream() ) {
                    cache = inputStream.readAllBytes();
                    cachePage = this.parentHeist().extendPage( cache, request );
                }
            }
            else {
                cachePage = this.queryHTTPPage( request );
                fileObject.createFile();
                try ( OutputStream outputStream = fileObject.getContent().getOutputStream() ) {
                    outputStream.write( cachePage.getBytes() );
                }
            }

            return cachePage;
        }
        catch ( IOException e ){
            this.parentHeist().handleAliveException( e );
        }
        return null;
    }

    public Page getHTTPPage( String szHref, String szFilePath ) {
        return this.queryHTTPPage( new Request( szHref ), szFilePath );
    }

    public Page queryHTTPPageSafe( Request request ) {
        Page page = null;
        int nRetry = 0;
        IllegalStateException lpLastError = null;
        for ( int i = 0; i < this.fileRetrieveTime; ++i ) {
            try {
                page = this.queryHTTPPage( request );
                this.validateSpoil( page );
                break;
            }
            catch ( IllegalStateException e ) {
                ++nRetry;
                lpLastError = e;
            }
            catch ( LootAbortException e ) {
                return page;
            }
        }

        if ( nRetry >= this.fileRetrieveTime - 1 && lpLastError != null ) {
            throw new IllegalStateException("IrredeemableLoot");
        }
        return page;
    }

    protected Page afterPageFetched( Page page, Request request ){
        return page;
    }

    Page tryRecoverFromLocalFile( String szStoragePath, Request request ) throws LootRecoveredException, LootAbortException {
        try {
            byte[] cache ;
            FileSystemManager fsm = this.getDafaultFileSystemManager();
            FileObject fileObject = fsm.resolveFile( szStoragePath );
            try ( InputStream inputStream = fileObject.getContent().getInputStream() ) {
                cache = inputStream.readAllBytes();
            }

            Page page = this.parentHeist().extendPage( cache, request );

            this.validateSpoil( page );
            throw new LootRecoveredException();
        }
        catch ( LootRecoveredException | LootAbortException e ) {
            throw e;
        }
        catch ( IOException e1 ) {
            return null;
        }
    }

    void storeHrefCache( String szStoragePath, Request request ) throws LootRecoveredException, LootAbortException, IOException {
        Page cache;

        try {
            cache = this.tryRecoverFromLocalFile( szStoragePath, request );
        }
        catch ( LootRecoveredException | LootAbortException e ) {
            throw e;
        }
        catch ( IllegalStateException e ) {
            this.logger.info( "[Mission::Lifecycle] [Heistum<{}>] <RecoverFromLocalFileFailed.>", this.className() );
        }

        cache = this.queryHTTPPageSafe( request );
        cache = this.afterPageFetched( cache, request );

        FileSystemManager fsm = this.getDafaultFileSystemManager();
        FileObject fileObject = fsm.resolveFile( szStoragePath );
        if ( !fileObject.exists() ) {
            fileObject.createFile();
        }
        try ( OutputStream outputStream = fileObject.getContent().getOutputStream() ) {
            outputStream.write( cache.getBytes() );
        }
    }
}
