package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.util.name.Namespace;
import com.pinecone.framework.util.name.UniNamespace;

public class GenericNamespaceFamilyEntryNameEncoder implements FamilyEntryNameEncoder {
    protected String    mszSeparator;
    protected boolean   mbNameForValue;

    public GenericNamespaceFamilyEntryNameEncoder( String szSeparator, boolean bNameForValue ) {
        this.mszSeparator   = szSeparator;
        this.mbNameForValue = bNameForValue;
    }

    public GenericNamespaceFamilyEntryNameEncoder() {
        this( "::", false );
    }

    @Override
    public String getSeparator() {
        return this.mszSeparator;
    }

    @Override
    public boolean isNameForValue() {
        return this.mbNameForValue;
    }

    @Override
    public String encode( UnitFamilyNode node ) {
        return this.encode( node, this.mszSeparator, this.mbNameForValue );
    }

    @Override
    public String encode( UnitFamilyNode node, String szSeparator, boolean bNameForValue ) {
        if( node.getSelfKey() != null ) {
            StringBuilder sb = new StringBuilder( this.wrapGetCurrentNodeName( node ) );
            UnitFamilyNode p = node.parent();
            while ( p != null ) {
                Object k = this.wrapGetCurrentNodeName( p );

                sb.insert(0, k + szSeparator );
                p = p.parent();
            }

            String sz = sb.toString();
            if( bNameForValue ) {
                sz = sz + szSeparator + this.wrapGetCurrentEntryKey( node );
            }
            return sz;
        }

        if( bNameForValue ) {
            return szSeparator + this.wrapGetCurrentEntryKey( node );
        }
        return null;
    }

    @Override
    public Namespace encodeNS( UnitFamilyNode node ) {
        return this.encodeNS( node, this.mszSeparator, this.mbNameForValue );
    }

    @Override
    public Namespace encodeNS( UnitFamilyNode node, String szSeparator, boolean bNameForValue ) {
        if( node.getSelfKey() != null ) {
            Namespace ns = new UniNamespace( this.wrapGetCurrentNodeName( node ), szSeparator );
            UnitFamilyNode p = node.parent();
            while ( p != null ) {
                Object k = this.wrapGetCurrentNodeName( p );

                Namespace root_p = ns;
                while ( root_p.parent() != null ) {
                    root_p = root_p.parent();
                }
                root_p.setParent( new UniNamespace( k.toString(), szSeparator ) );
                p = p.parent();
            }

            if( bNameForValue ) {
                ns = new UniNamespace( this.wrapGetCurrentEntryKey( node ), ns, szSeparator );
            }
            return ns;
        }

        if( bNameForValue ) {
            return new UniNamespace( this.wrapGetCurrentEntryKey( node ), new UniNamespace( "", szSeparator ), szSeparator );
        }
        return null;
    }

    protected String transferName( String szBad ) {
        return szBad;
    }

    protected String wrapGetCurrentNodeName( UnitFamilyNode node ) {
        Object k = node.getSelfKey();
        if( k == null ) {
            k = "";
        }
        return this.transferName( k.toString() );
    }

    protected String wrapGetCurrentEntryKey( UnitFamilyNode node ) {
        Object k = node.getEntry().getKey();
        if( k == null ) {
            k = "";
        }
        return this.transferName( k.toString() );
    }
}
