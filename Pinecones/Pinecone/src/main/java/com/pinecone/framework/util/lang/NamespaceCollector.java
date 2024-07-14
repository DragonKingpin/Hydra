package com.pinecone.framework.util.lang;

import com.pinecone.framework.system.prototype.Pinenut;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public interface NamespaceCollector extends Pinenut {
    String KEY_FILE_PROTOCOL             = "file";
    String KEY_JAR_PROTOCOL              = "jar";
    String RESOURCE_NAME_SEPARATOR       = "/";
    String JAVA_PKG_CLASS_SEPARATOR      = ".";
    char   JAVA_PKG_CLASS_SEPARATOR_C    = '.';

    default List<String> fetch ( String szNSName ) {
        return this.fetch( szNSName, true );
    }

    default List<String> fetch ( String szNSName, boolean bCollectChildPackage ) {
        List<String > list = new ArrayList<>();
        this.fetch( szNSName, list,bCollectChildPackage );
        return list;
    }

    default void fetch ( String szNSName, List<String > collections ) {
        this.fetch( szNSName, collections, true );
    }

    void fetch ( String szNSName, List<String > collections, boolean bCollectChildPackage ) ;

    String fetchFirst ( String szNSName ) ;


    default List<String> fetch ( URL url, String szNSName ) {
        return this.fetch( url, szNSName, true );
    }

    default List<String> fetch ( URL url, String szNSName, boolean bCollectChildPackage ) {
        List<String > list = new ArrayList<>();
        this.fetch( url, szNSName, list,bCollectChildPackage );
        return list;
    }

    default void fetch ( URL url, String szNSName, List<String > collections ) {
        this.fetch( url, szNSName, collections, true );
    }

    void fetch ( URL url, String szNSName, List<String > collections, boolean bCollectChildPackage ) ;

    String fetchFirst ( URL url, String szNSName ) ;

    ClassLoader getClassLoader();
}
