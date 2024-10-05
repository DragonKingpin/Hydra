package com.pinecone.hydra.registry.marshaling;

import java.util.List;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ElementNode;

public interface RegistryDecoder extends Pinenut {
    default ElementNode decode( Object val, GUID parentGUID ) {
        if ( val instanceof Map ) {
            Map map = (Map) val;
            if( map.isEmpty() ) {
                return null;
            }
            else if( map.size() > 1 ) {
                throw new IllegalArgumentException( "Root element should be 1" );
            }

            Map.Entry kv = (Map.Entry) map.entrySet().iterator().next();
            return this.decode( kv.getKey().toString(), kv.getValue(), parentGUID );
        }
        else if ( val instanceof List ) {
            List list = (List) val;
            if( list.isEmpty() ) {
                return null;
            }
            else if( list.size() > 1 ) {
                throw new IllegalArgumentException( "Root element should be 1" );
            }

            return this.decode( Integer.toString( 0 ), list.get( 0 ), parentGUID );
        }

        return null;
    }

    ElementNode decode( String key, Object val, GUID parentGUID );

    default ElementNode decode( Map.Entry kv, GUID parentGUID ) {
        return this.decode( kv.getKey().toString(), kv.getValue(), parentGUID );
    }

    default ElementNode decode( Object val ) {
        return this.decode( val, null );
    }

    default ElementNode decode( String key, Object val ) {
        return this.decode( key, val, null );
    }
}
