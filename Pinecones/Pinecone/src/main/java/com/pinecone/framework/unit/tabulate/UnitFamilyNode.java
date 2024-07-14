package com.pinecone.framework.unit.tabulate;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.json.JSONEncoder;

import java.util.Map;

public interface UnitFamilyNode<K, V > extends Pinenut {
    UnitFamilyNode<K, V > parent();

    K getSelfKey();

    Map.Entry<K, V > getEntry();

    default String namespacify( String szSeparator, boolean bNameForValue ) {
        return FamilyEntryNameEncoder.DefaultEncoder.encode( this, szSeparator, bNameForValue );
    }

    default String namespacify( String szSeparator ) {
        return this.namespacify( szSeparator, false );
    }

    default String namespacify( boolean bNameForValue ) {
        return this.namespacify( "::", bNameForValue );
    }

    default String namespacify() {
        return this.namespacify( false );
    }

    @Override
    default String toJSONString() {
        return JSONEncoder.stringifyMapFormat( new KeyValue[]{
                new KeyValue<>( "class", this.className() ),
                new KeyValue<>( "key", this.getSelfKey() ),
                new KeyValue<>( "entry", this.getEntry() )
        } );
    }
}
