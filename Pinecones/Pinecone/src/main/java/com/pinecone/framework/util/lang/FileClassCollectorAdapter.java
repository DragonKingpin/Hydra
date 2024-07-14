package com.pinecone.framework.util.lang;

import java.io.File;
import java.util.List;

public class FileClassCollectorAdapter implements PathNamespaceCollectum {
    @Override
    public boolean matched      ( String szProtocol ) {
        return szProtocol.toLowerCase().equals( NamespaceCollector.KEY_FILE_PROTOCOL );
    }

    @Override
    public void collect         ( String szResourcePath, String szNSName, List<String > classNames, boolean bCollectChildren ) {
        this.collect0( szResourcePath, szNSName, classNames, bCollectChildren );
    }

    @Override
    public String collectFirst  ( String szResourcePath, String szNSName ) {
        return this.collect0( szResourcePath, szNSName, null, false );
    }


    protected String collect0        ( String szResourcePath, String szNSName, List<String > classNames, boolean bCollectChildren ) {
        File file         = new File( szResourcePath );
        File[] childFiles = file.listFiles ();
        if( childFiles != null ) {
            String szPackageNamePathFmt = szNSName.replace( NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR, File.separator );
            for ( File childFile : childFiles ) {
                if ( childFile.isDirectory () ) {
                    if ( bCollectChildren && classNames != null ) {
                        this.collect ( childFile.getPath (), szNSName, classNames, bCollectChildren );
                    }
                }
                else {
                    String childFilePath = childFile.getPath ();
                    if ( childFilePath.endsWith ( ".class" ) ) {
                        String szPackageSegment = childFilePath.substring( childFilePath.indexOf( szPackageNamePathFmt ) );
                        String szChildPackage   = szPackageSegment.replace( File.separator, NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR ).replace( ".class", "" );

                        if( classNames == null ) {
                            return szChildPackage;
                        }
                        else {
                            classNames.add ( szChildPackage );
                        }
                    }
                }
            }
        }

        return null;
    }
}
