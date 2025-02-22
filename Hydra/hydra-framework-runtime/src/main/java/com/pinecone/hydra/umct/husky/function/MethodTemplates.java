package com.pinecone.hydra.umct.husky.function;

import com.pinecone.framework.lang.field.DataStructureEntity;
import com.pinecone.framework.lang.field.FieldEntity;
import com.pinecone.framework.lang.field.GenericStructure;
import com.pinecone.ulf.util.protobuf.WolfProtobufConstants;

public final class MethodTemplates {
    public static DataStructureEntity from( DataStructureEntity tpl, String szInterceptedPath, Class<? >[] parameters, String[] parametersGenericLabels ) {
        if( tpl == null || parameters.length != tpl.size() ) {
            tpl = new GenericStructure( szInterceptedPath, parameters.length );
        }

        int i = 0;
        if ( parametersGenericLabels == null || parametersGenericLabels.length == 0 ) {
            for( Class<? > parameter : parameters ) {
                MethodTemplates.setDataField( i, parameter, tpl );
                ++i;
            }
        }
        else {
            for( Class<? > parameter : parameters ) {
                MethodTemplates.setDataField( i, parameter, tpl, parametersGenericLabels[ i ] );
                ++i;
            }
        }

        return tpl;
    }

    public static DataStructureEntity from( DataStructureEntity tpl, String szInterceptedPath, Class<? >[] parameters ) {
        if( tpl == null || parameters.length != tpl.size() ) {
            tpl = new GenericStructure( szInterceptedPath, parameters.length );
        }

        int i = 0;
        for( Class<? > parameter : parameters ) {
            MethodTemplates.setDataField( i, parameter, tpl );
            ++i;
        }

        return tpl;
    }

    public static DataStructureEntity conform( DataStructureEntity tpl, String szInterceptedPath ) {
        if( tpl == null ) {
            return null;
        }
        DataStructureEntity neo = new GenericStructure( szInterceptedPath, tpl.size() );
        FieldEntity[] segs = tpl.getSegments();

        int j = 0;
        for ( int i = neo.getDataOffset(); i < segs.length; ++i ) {
            FieldEntity seg = segs[ i ];
            Class<? > parameter = seg.getType();

            MethodTemplates.setDataField( j, parameter, tpl );
            ++j;
        }

        return tpl;
    }

    public static DataStructureEntity conform( DataStructureEntity tpl, String szInterceptedPath, String genericLabel ) {
        if( tpl == null ) {
            return null;
        }
        DataStructureEntity neo = new GenericStructure( szInterceptedPath, tpl.size() );
        FieldEntity[] segs = tpl.getSegments();

        int j = 0;
        for ( int i = neo.getDataOffset(); i < segs.length; ++i ) {
            FieldEntity seg = segs[ i ];
            Class<? > parameter = seg.getType();

            MethodTemplates.setDataField( j, parameter, tpl, genericLabel );
            ++j;
        }

        return tpl;
    }

    protected static void setDataField( int i, Class<? > parameter, DataStructureEntity tpl, String genericLabel ) {
        String szNormalName = parameter.getName();
        if( szNormalName.startsWith( "[" ) ) {
            szNormalName = szNormalName.replace( "[", "" );
            szNormalName += WolfProtobufConstants.ArrayTransformedName;
        }

        szNormalName = szNormalName.replaceAll( "[^a-zA-Z0-9_]", "_" ) + "_" + i;
        tpl.setDataField( i, szNormalName, parameter, genericLabel );
    }

    protected static void setDataField( int i, Class<? > parameter, DataStructureEntity tpl ) {
        String szNormalName = parameter.getName();
        if( szNormalName.startsWith( "[" ) ) {
            szNormalName = szNormalName.replace( "[", "" );
            szNormalName += WolfProtobufConstants.ArrayTransformedName;
        }
        tpl.setDataField( i,
                szNormalName.replaceAll( "[^a-zA-Z0-9_]", "_" ) + "_" + i,
                parameter
        );
    }
}
