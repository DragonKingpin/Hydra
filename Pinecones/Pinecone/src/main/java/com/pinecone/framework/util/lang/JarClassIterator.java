package com.pinecone.framework.util.lang;
import java.io.IOException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class JarClassIterator implements NamespaceIterator {
    protected JarFile                  mJarFile;
    protected Enumeration<JarEntry >   mEntries;
    protected String                   mPackagePath;
    protected JarEntry                 mCurrentEntry;

    public JarClassIterator( String szResourcePath ) throws IOException {
        String[] jarInfo   = szResourcePath.split ( "!" );
        String jarFilePath = jarInfo[0].substring ( jarInfo[0].indexOf ( NamespaceCollector.RESOURCE_NAME_SEPARATOR ) );

        this.mJarFile      = new JarFile( jarFilePath );
        this.mEntries      = this.mJarFile.entries();

        String packagePath = szResourcePath;
        if( jarInfo.length > 1 ) {
            packagePath = jarInfo[1].substring ( 1 );
        }
        this.mPackagePath  = packagePath;

        this.skipEntries();
    }

    @Override
    public boolean hasNext() {
        return this.mCurrentEntry != null;
    }

    @Override
    public String next() {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException();
        }

        String entryName = this.mCurrentEntry.getName();
        String className = entryName.replace( NamespaceCollector.RESOURCE_NAME_SEPARATOR, NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR ).substring(
                0, entryName.lastIndexOf( NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR_C )
        );

        this.skipEntries();

        return className;
    }

    @Override
    public void forEachRemaining( Consumer<? super String> action ) {
        Objects.requireNonNull( action );
        while ( this.hasNext() ) {
            action.accept( this.next() );
        }
    }

    protected void skipEntries() {
        while ( this.mEntries.hasMoreElements() ) {
            JarEntry entry   = this.mEntries.nextElement();
            String entryName = entry.getName();
            if ( entryName.endsWith( ".class" ) ) {
                int index = entryName.lastIndexOf( NamespaceCollector.RESOURCE_NAME_SEPARATOR );
                String myPackagePath;
                if ( index == -1 ) {
                    myPackagePath = entryName;
                }
                else {
                    myPackagePath = entryName.substring( 0, index );
                }

                if ( myPackagePath.equals( this.mPackagePath ) ) {
                    this.mCurrentEntry = entry;
                    return;
                }
            }
        }
        this.mCurrentEntry = null; // No more valid entries
    }
}