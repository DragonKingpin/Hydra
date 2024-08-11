package com.pinecone.framework.system.prototype;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  Pinecone Ursus For Java Objectom
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Objectom is an uniformity map-operator, supported wrapped unified operation class.
 *  It has implemented following types, and will let them conformed the unified interface.
 *  Array, List, Map, Fielded-Class, Bean-Class
 *  *****************************************************************************************
 *  Notice:
 *  1. All objects are un-appendable, and should consider as the `class`, only supported get/set.
 *  2. Excepted the `set`, other methods should consider as the const, `const Type* method() const;`
 *  3. Some scenarios likes the `bean`, which the `gets` could not paired with the `sets` therein.
 *  4. Some scenarios e.g. the `class`, the value needed to retrieve from inner fields or methods.
 *  4.1 In these condition, it may provokes exceptions, so no explicit `values()` method given.
 *  4.2 The implicated keys in the `class`, will be all retrieved, and may not be expected.
 *  *****************************************************************************************
 *  Dragon King, the undefined
 */
public interface Objectom extends PineUnit {
    int size();

    boolean isEmpty();

    Object get( Object key );

    void set( Object key, Object val );

    boolean containsKey( Object k ) ;

    // Readonly
    // const Object* keys() const;
    Object[] keys();

    Map<String, Object > toMap( Class<? > mapType );

    default Map<String, Object > toMap() {
        return this.toMap( LinkedHashMap.class );
    }

    @SuppressWarnings("unchecked")
    static Objectom wrap( Object that ) {
        if( that instanceof Objectom ) {
            return (Objectom) that;
        }
        else if( that instanceof Map ) {
            return new ObjectiveMap<>( (Map) that );
        }
        else if( that instanceof List) {
            return new ObjectiveList<>( (List) that );
        }
        else if( that.getClass().isArray() ){
            return new ObjectiveArray( (Object[]) that );
        }

        return new ObjectiveBean(that);
    }
}
