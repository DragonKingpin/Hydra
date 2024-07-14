package com.pinecone.framework.util.lang;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class DirectoryClassIterator implements NamespaceIterator {
    protected File   mFile          ;
    protected File[] mChildFiles    ;
    protected String mNSNamePathFmt ;

    protected int    mCursor           = 0;
    protected int    mLastRet          = -1;

    public DirectoryClassIterator( String szResourcePath, String szNSName ) {
        this.mFile          = new File( szResourcePath );
        this.mChildFiles    = this.mFile.listFiles();
        this.mNSNamePathFmt = szNSName.replace( NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR, File.separator );

        if( this.mChildFiles == null ) {
            this.mChildFiles = new File[0];
        }

        this.skipEntities();
    }

    @Override
    public boolean hasNext() {
        return this.mCursor < this.mChildFiles.length;
    }

    protected String replacePathName( String sz ) {
        String szPackageSegment = sz.substring( sz.indexOf( this.mNSNamePathFmt ) );
        return szPackageSegment.replace( File.separator, NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR ).replace( ".class", "" );
    }


    public String next() {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException();
        }

        this.mLastRet = this.mCursor;
        String childFilePath = this.mChildFiles[ this.mCursor++ ].getPath();

        this.skipEntities();

        return this.replacePathName( childFilePath );
    }

    @Override
    public void forEachRemaining( Consumer<? super String > action ) {
        Objects.requireNonNull(action);
        final int size = this.mChildFiles.length;
        int i = this.mCursor;
        if ( i < size ) {
            for ( ; i < size ; i++ )  {
                if( this.sift( this.mChildFiles[i] ) ) {
                    continue;
                }
                action.accept( this.mChildFiles[i].getPath() );
            }

            this.mCursor  = i;
            this.mLastRet = i - 1;
        }
    }

    protected boolean sift( File file ) {
        return file.isDirectory() && ! file.getPath().endsWith ( ".class" );
    }

    protected void skipEntities() {
        while ( this.mCursor < this.mChildFiles.length && this.sift( this.mChildFiles[ this.mCursor ] ) ) {
            ++this.mCursor;
        }
    }

}
