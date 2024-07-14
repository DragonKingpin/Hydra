package com.pinecone.framework.util.json;

import com.pinecone.framework.unit.Dictionary;

public interface Dictson extends Dictionary<Object >, JSONDictium {

    default Object insert( Object key, Object value ) {
        if( this.isList() ) {
            int index = JSONUtils.asInt32Key( key );
            if( index >= 0 ) {
                if( index == this.getList().size() ){
                    return this.getList().put( value );
                }
            }

            this.convertToMap();
        }

        return this.getMap().put( JSONUtils.asStringKey(key), value );
    }

    JSONObject  affirmMap() ;

    JSONArray   affirmList() ;

    JSONObject  resetAsMap() ;

    JSONArray   resetAsList() ;

    JSONObject  getMap()  throws ClassCastException ;

    JSONArray   getList() throws ClassCastException ;

}
