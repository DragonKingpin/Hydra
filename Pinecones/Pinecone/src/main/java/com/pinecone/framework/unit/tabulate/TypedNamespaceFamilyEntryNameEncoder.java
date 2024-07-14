package com.pinecone.framework.unit.tabulate;

import java.util.List;
import java.util.Set;

public class TypedNamespaceFamilyEntryNameEncoder extends GenericNamespaceFamilyEntryNameEncoder {
    protected String mszFmtTypeLabel;

    public TypedNamespaceFamilyEntryNameEncoder( String szSeparator, boolean bNameForValue, String szFmtTypeLabel ) {
        super( szSeparator, bNameForValue );
        this.mszFmtTypeLabel = szFmtTypeLabel;
    }

    public TypedNamespaceFamilyEntryNameEncoder( String szSeparator, boolean bNameForValue ) {
        this( szSeparator, bNameForValue, "$" );
    }

    public TypedNamespaceFamilyEntryNameEncoder( boolean bNameForValue ) {
        this( "::", bNameForValue, "$" );
    }

    public TypedNamespaceFamilyEntryNameEncoder( String szFmtTypeLabel ) {
        this( "::", false, szFmtTypeLabel );
    }

    public TypedNamespaceFamilyEntryNameEncoder() {
        this( "::", false );
    }

    @Override
    protected String transferName( String szBad ) {
        return szBad; // TODO
    }

    protected String queryType( Object val ) {
        if( val instanceof List ) {
            return "list";
        }
        else if( val instanceof Set) {
            return "set";
        }

        return "";
    }

    @Override
    protected String wrapGetCurrentNodeName( UnitFamilyNode node ) {
        Object k = node.getSelfKey();
        if( k == null ) {
            k = "";
        }

        String szType = "";
        if( node.parent() != null ) {
            szType = this.queryType( node.parent().getEntry().getValue() );
            if( !szType.isEmpty() ) {
                szType = this.mszFmtTypeLabel + szType;
            }
        }
        return this.transferName( k.toString() + szType );
    }

    @Override
    protected String wrapGetCurrentEntryKey( UnitFamilyNode node ) {
        Object k = node.getEntry().getKey();
        if( k == null ) {
            k = "";
        }
        String szType = this.queryType( node.getEntry().getValue() );
        if( !szType.isEmpty() ) {
            szType = this.mszFmtTypeLabel + szType;
        }
        return this.transferName( k.toString() + szType );
    }
}
