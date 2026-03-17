package com.pinecone.framework.util.lang.iterator;

import java.io.File;

import com.pinecone.framework.util.lang.NamespaceCollector;

public class DirectoryPackageIterator extends DirectoryFileIterator {
    public DirectoryPackageIterator( String szResourcePath, String szNSName, String szSuffix ) {
        super( szResourcePath, szNSName, szSuffix );
    }

    @Override
    protected String replacePathName( String sz ) {
        String szPackageSegment = sz.substring( sz.indexOf( this.mNSNamePathFmt ) );
        return szPackageSegment.replace( File.separator, NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR );
    }

    @Override
    protected boolean sift( File file ) {
        return !file.isDirectory() ;
    }
}
