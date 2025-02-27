package com.pinecone.hydra.umct.stereotype;

import com.pinecone.framework.util.StringUtils;

import java.lang.reflect.Method;

public final class IfaceUtils {

    public static String getIfaceNameFieldVal( Iface annotation ) {
        String name = annotation.name();
        if ( StringUtils.isEmpty( name ) ) {
            name = annotation.value();
        }

        return name;
    }

    public static String queryIfaceLogicClassName ( Iface cIface ) {
        String szLogicClassName = null;
        if ( cIface != null ) {
            String objectAddress = cIface.objectAddress();
            if ( StringUtils.isNoneEmpty( objectAddress ) ) {
                szLogicClassName = objectAddress;
            }
            else {
                objectAddress = IfaceUtils.getIfaceNameFieldVal( cIface );
            }

            if ( StringUtils.isNoneEmpty( objectAddress ) ) {
                szLogicClassName = objectAddress;
            }
        }

        return szLogicClassName;
    }

    public static String queryIfaceLogicClassName ( Class<?> clazz ) {
        Iface cIface = clazz.getAnnotation( Iface.class );
        return IfaceUtils.queryIfaceLogicClassName( cIface );
    }

    public static String queryIfaceClassNameAddress ( Class<?> clazz ) {
        String szLogicCN = IfaceUtils.queryIfaceLogicClassName( clazz );

        if ( szLogicCN != null ) {
            return szLogicCN;
        }
        return clazz.getName();
    }

    public static String getIfaceMethodName( Method method ){
        String ifaceName = method.getName();

        Iface annotation = method.getAnnotation(Iface.class);
        if ( annotation != null ) {
            String name = IfaceUtils.getIfaceNameFieldVal( annotation );
            if ( StringUtils.isNoneEmpty( name ) ) {
                ifaceName = name;
            }
        }

        return ifaceName;
    }
}
