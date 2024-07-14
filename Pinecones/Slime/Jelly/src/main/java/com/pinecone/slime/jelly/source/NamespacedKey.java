package com.pinecone.slime.jelly.source;

import com.pinecone.slime.source.indexable.IndexableTargetScopeMeta;

public final class NamespacedKey {
    public static String getFullKey( IndexableTargetScopeMeta meta, String szNameSeparator, String szNamespace, Object key ) {
        String ns = null;
        if ( szNamespace != null ) {
            ns = szNamespace;
        }
        else if ( meta.getIndexKey() != null ) { // Index as namespace
            ns = meta.getIndexKey();
        }

        if( ns != null && !ns.isEmpty() ) {
            return ns + szNameSeparator + key.toString();
        }
        return key.toString();
    }
}
