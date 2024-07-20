package com.pinecone.framework.util.lang;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public final class JarUtils {
    public static Enumeration<JarEntry > fetchEnumeration( JarInputStream jarInputStream ) throws IOException {
        LinkedList<JarEntry > buf = new LinkedList<>();

        JarEntry jarEntry;
        while ( ( jarEntry = jarInputStream.getNextJarEntry() ) != null ) {
            buf.add( jarEntry );
            jarInputStream.closeEntry();
        }

        return new Enumeration<JarEntry>() {
            private Iterator<JarEntry > iterator = buf.iterator();
            @Override
            public boolean hasMoreElements() {
                return this.iterator.hasNext();
            }

            @Override
            public JarEntry nextElement() {
                return this.iterator.next();
            }
        };
    }

    public static String normalizeJarClassName( String entryName, String classesScopePath ) {
        if( classesScopePath != null && entryName.startsWith( classesScopePath ) ) {
            entryName = entryName.replace( classesScopePath, "" );
        }
        return entryName.replace( NamespaceCollector.RESOURCE_NAME_SEPARATOR, NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR ).substring(
                0, entryName.lastIndexOf( NamespaceCollector.JAVA_PKG_CLASS_SEPARATOR_C )
        );
    }
}
