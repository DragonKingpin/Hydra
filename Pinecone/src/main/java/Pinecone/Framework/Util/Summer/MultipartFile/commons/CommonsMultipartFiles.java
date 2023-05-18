package Pinecone.Framework.Util.Summer.MultipartFile.commons;

import Pinecone.Framework.Unit.LinkedMultiValueMap;
import Pinecone.Framework.Util.Summer.ArchConnection;
import Pinecone.Framework.Util.Summer.ArchHostSystem;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartException;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartFile;
import Pinecone.Framework.Util.Summer.MultipartFile.MultipartHttpServletRequest;
import Pinecone.Framework.Util.Summer.ArchControlDispatcher;
import Pinecone.Framework.Util.Summer.io.PathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

public class CommonsMultipartFiles {
    protected ArchConnection                 mConnection;
    protected ArchHostSystem                 mSystem;
    protected CommonsMultipartResolver       mMultipartResolver;
    protected MultipartHttpServletRequest    mCurrentMultipartHttpServletRequest = null;
    protected Map<String, MultipartFile>     mFilesMap = null;

    public CommonsMultipartFiles( ArchConnection connection ) {
        this.mConnection = connection;
        this.mSystem = this.mConnection.getHostSystem();
        this.init();
    }




    public ArchHostSystem getHostSystem(){
        return this.mSystem;
    }

    public ArchControlDispatcher getSystemDispathcher(){
        return this.mConnection.getDispatcher();
    }

    public CommonsMultipartResolver getMultipartResolver(){
        return this.mMultipartResolver;
    }




    private void init(){
        this.mMultipartResolver = new CommonsMultipartResolver( this.mSystem.getSystemServlet().getServletContext() );

        this.mMultipartResolver.setSingleUploadSize( this.mSystem.getSingleFileSizeMax() );
        this.mMultipartResolver.setMaxUploadSize( this.mSystem.getSumFileSizeMax() );
        this.mMultipartResolver.setDefaultEncoding( this.mSystem.getUploadEncode() );

        String szUploadTempDir = this.mSystem.getUploadTempDir();
        if( szUploadTempDir != null && !szUploadTempDir.isEmpty() ){
            try {
                this.mMultipartResolver.setUploadTempDir( new PathResource(szUploadTempDir) );
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }

    }

    private HttpServletRequest getHttpServletRequest(){
        return this.mConnection.$_REQUEST();
    }

    public boolean isMultipart(){
        return this.mMultipartResolver.isMultipart( this.getHttpServletRequest() );
    }

    public void interceptMultipartFiles() throws MultipartException {
        if ( this.isMultipart() ){
            this.mCurrentMultipartHttpServletRequest = this.mMultipartResolver.resolveMultipart( this.getHttpServletRequest() );
            this.mFilesMap = this.mCurrentMultipartHttpServletRequest.getFileMap();
        }
        else {
            this.refresh();
        }
    }

    public MultipartHttpServletRequest getCurrentMultipartRequest(){
        return this.mCurrentMultipartHttpServletRequest;
    }

    public Map<String, MultipartFile> getCurrentFilesMap(){
        return this.mFilesMap;
    }

    public void refresh() {
        if( this.mFilesMap != null ){
            if( !this.mFilesMap.isEmpty() ){
                this.mFilesMap.clear();
            }
        }
        else {
            this.mFilesMap = new LinkedMultiValueMap() ;
        }
    }

}
