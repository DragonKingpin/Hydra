package com.pinecone.framework.util.lang;

import java.io.File;

public class DirectoryPackageIterator extends DirectoryClassIterator {
    public DirectoryPackageIterator( String szResourcePath, String szNSName ) {
        super( szResourcePath, szNSName );
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
