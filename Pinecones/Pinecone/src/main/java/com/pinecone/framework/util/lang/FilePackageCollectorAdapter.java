package com.pinecone.framework.util.lang;

import java.io.File;
import java.util.List;

public class FilePackageCollectorAdapter implements PathNamespaceCollectum {
    @Override
    public boolean matched( String szProtocol ) {
        return szProtocol.toLowerCase().equals( NamespaceCollector.KEY_FILE_PROTOCOL );
    }

    @Override
    public void collect         ( String szResourcePath, String szNSName, List<String > packageNames, boolean bCollectChildren ) {
        this.collect0( szResourcePath, szNSName, packageNames, bCollectChildren );
    }

    @Override
    public String collectFirst  ( String szResourcePath, String szNSName ) {
        return this.collect0( szResourcePath, szNSName, null, false );
    }

    protected String collect0   ( String szResourcePath, String szNSName, List<String > packageNames, boolean bCollectChildren ) {
        File file         = new File ( szResourcePath );
        File[] childFiles = file.listFiles ();
        if( childFiles != null ) {
            String szPackageNamePathFmt = szNSName.replace( NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR, File.separator );
            for ( File childFile : childFiles ) {
                if ( childFile.isDirectory () ) {
                    String szPackageSegment = childFile.getPath ().substring( childFile.getPath ().indexOf( szPackageNamePathFmt ) );
                    String szChildPackage   = szPackageSegment.replace( File.separator, NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR );
                    if( packageNames == null ) {
                        return szChildPackage;
                    }
                    else {
                        packageNames.add ( szChildPackage );
                    }


                    if ( bCollectChildren ) {
                        this.collect ( childFile.getPath (), szPackageSegment, packageNames, bCollectChildren );
                    }
                }
            }
        }

        return null;
    }
}
