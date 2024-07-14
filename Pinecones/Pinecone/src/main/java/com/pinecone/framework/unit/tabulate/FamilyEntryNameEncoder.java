package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.name.Namespace;

public interface FamilyEntryNameEncoder extends Pinenut {
    FamilyEntryNameEncoder DefaultEncoder = new GenericNamespaceFamilyEntryNameEncoder();

    String getSeparator();

    boolean isNameForValue();

    default String encode( UnitFamilyNode node ) {
        return this.encode( node, this.getSeparator(), this.isNameForValue() );
    }

    default String encode( UnitFamilyNode node, boolean bNameForValue ) {
        return this.encode( node, this.getSeparator(), bNameForValue );
    }

    String encode( UnitFamilyNode node, String szSeparator, boolean bNameForValue ) ;


    default Namespace encodeNS( UnitFamilyNode node ) {
        return this.encodeNS( node, this.getSeparator(), this.isNameForValue() );
    }

    default Namespace encodeNS( UnitFamilyNode node, boolean bNameForValue ) {
        return this.encodeNS( node, this.getSeparator(), bNameForValue );
    }

    Namespace encodeNS( UnitFamilyNode node, String szSeparator, boolean bNameForValue ) ;
}
