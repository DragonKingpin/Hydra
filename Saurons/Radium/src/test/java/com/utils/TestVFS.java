package com.utils;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.cache.SoftRefFilesCache;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.http.HttpFileProvider;
import org.apache.commons.vfs2.provider.http.HttpFileSystemConfigBuilder;
import org.apache.commons.vfs2.provider.http4.Http4FileProvider;
import org.apache.commons.vfs2.provider.http4s.Http4sFileProvider;
import org.apache.commons.vfs2.provider.http5.Http5FileProvider;
import org.apache.commons.vfs2.provider.http5s.Http5sFileProvider;
import org.apache.commons.vfs2.provider.webdav.WebdavFileProvider;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;

import java.net.URLEncoder;

public class TestVFS {
    public static void testCRUD ( ) throws Exception {
//        FileSystemManager fsManager = null;
//
//        fsManager = VFS.getManager();
        StandardFileSystemManager fsManager = new StandardFileSystemManager();
        Http5FileProvider http5FileProvider = new Http5FileProvider();
        fsManager.addProvider( "http", http5FileProvider );
        fsManager.addProvider( "https", new Http5sFileProvider());
        fsManager.addProvider( "webdav", new WebdavFileProvider() );
        fsManager.setCacheStrategy(CacheStrategy.ON_CALL);
        fsManager.setFilesCache(new SoftRefFilesCache());

        String localFilePath = "file:///K:/test/1.txt";
        localFilePath = "\\\\b-serverkingpin\\ARBOmnium/EnderChest/1.txt";

        String username = "undefined";
        String password = "";
        String webdavHost = "b-serverkingpin:8077";
        String encodedPassword = URLEncoder.encode(password, "UTF-8");
        localFilePath = "http://" + username + ":" + encodedPassword + "@" + webdavHost + "/EnderChest/test/1.text";
        //localFilePath = "https://www.rednest.cn/index.html";

        //String webdavFilePath = "webdav://username:password@xxx/test";
        FileObject fileObject = fsManager.resolveFile(localFilePath);

        //FileObject webdavFile = fsManager.resolveFile(webdavFilePath);


        if ( !fileObject.exists() ) {
            if ( fileObject.getType() == FileType.IMAGINARY ) {
                fileObject.createFile();
                Debug.trace( fileObject.getName()  );
            }
        }


        Debug.trace( fileObject.getName() + " exists." );

        if ( fileObject.getType() == FileType.FOLDER ) {
            for ( FileObject child : fileObject.getChildren() ) {
                Debug.trace(" - " + child.getName().getBaseName());
            }
        }

        if ( fileObject.getType() == FileType.FILE ) {

            FileContent content = fileObject.getContent();
            byte[] buffer = content.getInputStream().readAllBytes();
            Debug.echo( new String(buffer) );

        }
    }

    public static void main( String[] args ) throws Exception {
        Pinecone.init( (Object...cfg )->{

            TestVFS.testCRUD();

            return 0;
        }, (Object[]) args );
    }
}
