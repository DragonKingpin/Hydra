package com.pinecone.framework.system.prototype;

import com.pinecone.framework.system.stereotype.JavaBeans;
import com.pinecone.framework.unit.KeyValue;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.json.JSONEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class ObjectiveBean implements Objectom {
    protected Object    mObj;
    protected Entry[]   mGetMethods;
    protected Entry[]   mSetMethods;

    public ObjectiveBean( Object bean ) {
        this.mObj = bean;
        this.cacheMethods();
    }

    protected void cacheMethods() {
        Class klass = this.mObj.getClass();
        boolean includeSuperClass = klass.getClassLoader() != null;
        Method[] methods = includeSuperClass ? klass.getMethods() : klass.getDeclaredMethods();

        ArrayList<Entry> getDummy = new ArrayList<>();
        ArrayList<Entry> setDummy = new ArrayList<>();

        for( int i = 0; i < methods.length; ++i ) {
            try {
                Method method = methods[i];
                if ( Modifier.isPublic( method.getModifiers() ) ) {
                    String key = JavaBeans.getGetterMethodKeyName( method );
                    if( StringUtils.isEmpty( key ) ) {
                        key = JavaBeans.getSetterMethodKeyName( method );
                        if( !StringUtils.isEmpty( key ) ) { // Found setter
                            if ( Character.isUpperCase( key.charAt(0) ) && method.getParameterTypes().length == 1 ) {
                                key = JavaBeans.methodKeyNameLowerCaseNormalize( key );

                                setDummy.add( new Entry( key, method ) );
                            }
                        }
                    }
                    else { // Found getter
                        if ( Character.isUpperCase( key.charAt(0) ) && method.getParameterTypes().length == 0 ) {
                            key = JavaBeans.methodKeyNameLowerCaseNormalize( key );

                            getDummy.add( new Entry( key, method ) );
                        }
                    }
                }
            }
            catch ( Exception e ) {
                e.printStackTrace();
                // Do nothing.
            }
        }

        this.mGetMethods = getDummy.toArray( new Entry[]{} );
        this.mSetMethods = setDummy.toArray( new Entry[]{} );
        Arrays.sort( this.mGetMethods );
        Arrays.sort( this.mSetMethods );
    }

    @Override
    public int size() {
        return this.mGetMethods.length;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    public Object get( Object key ) {
        String szKey = key.toString();
        try {
            int index = ObjectiveBean.binarySearch( this.mGetMethods, szKey );
            if( index < 0 ) {
                return null;
            }
            Method method = this.mGetMethods[ index ].method;
            method.setAccessible( true );
            return method.invoke( this.mObj );
        }
        catch ( IllegalAccessException | InvocationTargetException e ) {
            return null;
        }
    }

    protected static int binarySearch( Entry[] those, String key ) {
        int low = 0;
        int high = those.length - 1;
        while ( low <= high ) {
            int mid = (low + high) >>> 1;
            int cmp = those[ mid ].name.compareTo( key );
            if ( cmp < 0 ) {
                low = mid + 1;
            }
            else if ( cmp > 0 ) {
                high = mid - 1;
            }
            else {
                return mid;
            }
        }
        return -(low + 1);
    }

    public void set( Object key, Object val ) {
        String szKey = key.toString();
        try {
            int index = ObjectiveBean.binarySearch( this.mSetMethods, szKey );
            if( index < 0 ) {
                throw new IllegalArgumentException( "Specific setter-method not found: set" + JavaBeans.methodKeyNameUpperCaseNormalize( szKey ) );
            }
            Method method = this.mSetMethods[ index ].method;
            method.setAccessible( true );
            method.invoke( this.mObj, val );
        }
        catch ( IllegalAccessException | InvocationTargetException e ) {
            throw new RuntimeException( e );
        }
    }

    @Override
    public boolean hasOwnProperty( Object k ) {
        if ( this.mObj instanceof PineUnit ) {
            return ( (PineUnit) this.mObj ).hasOwnProperty(k);
        }
        return this.containsKey(k);
    }

    @Override
    public boolean containsKey( Object k ) {
        return ObjectiveBean.binarySearch( this.mGetMethods, k.toString() ) >= 0;
    }

    @Override
    public String toJSONString() {
        ArrayList<KeyValue<String, Object > > dummy = new ArrayList<>();
        for( Entry kv : this.mGetMethods ) {
            Object val;
            try {
                kv.method.setAccessible( true );
                val = kv.method.invoke( this.mObj );
            }
            catch ( IllegalAccessException | InvocationTargetException e ) {
                break;
            }

            dummy.add( new KeyValue<>( kv.name, val ) );
        }

        return JSONEncoder.stringifyMapFormat( dummy );
    }

    @Override
    public TypeIndex prototype() {
        return Prototype.typeid( this.mObj );
    }

    @Override
    public String prototypeName() {
        return Prototype.prototypeName( this.mObj );
    }

    static class Entry implements Comparable<Entry > {
        String name;
        Method method;

        Entry( String name, Method method ) {
            this.name   = name;
            this.method = method;
        }

        @Override
        public int compareTo( Entry o ) {
            return this.name.compareTo( o.name );
        }
    }

    @Override
    public String[] keys() {
        String[] list = new String[ this.mGetMethods.length ];
        for ( int i = 0; i < this.mGetMethods.length; ++i ) {
            list[ i ] = this.mGetMethods[ i ].name;
        }
        return list;
    }
}

