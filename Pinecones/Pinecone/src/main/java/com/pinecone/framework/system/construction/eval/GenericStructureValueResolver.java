package com.pinecone.framework.system.construction.eval;

import com.pinecone.framework.unit.Units;
import com.pinecone.framework.util.StringUtils;

public class GenericStructureValueResolver implements StructureValueResolver {
    @Override
    public Object resolve( Object mapLiked, String szFieldKey, String szRawKey ) {
        StructureValueExpression expression = new StructureValueExpression( szRawKey );
        String szPath = expression.getPath();
        String szResolvedFieldKey = this.resolveFieldKey( szFieldKey, szRawKey, szPath );

        Object val = this.getFromMapStructure( mapLiked, szResolvedFieldKey );
        if ( val == null ) {
            val = this.getFromMapStructure( mapLiked, szPath );
        }
        if ( val == null && this.isRecursivePath( szPath ) ) {
            val = this.getValueFromMapRecursively( mapLiked, szPath );
        }
        if ( val == null && expression.isDefaultValuePresented() ) {
            return this.defaultValue( expression.getDefaultValue() );
        }
        return val;
    }

    protected String resolveFieldKey( String szFieldKey, String szRawKey, String szPath ) {
        if ( szRawKey != null && szRawKey.equals( szFieldKey ) ) {
            return szPath;
        }
        if ( StringUtils.isEmpty( szFieldKey ) ) {
            return szPath;
        }
        return szFieldKey;
    }

    protected Object getFromMapStructure( Object mapLiked, String szKey ) {
        if ( StringUtils.isEmpty( szKey ) ) {
            return null;
        }
        return Units.getFromMapStructure( mapLiked, szKey, true, true );
    }

    protected Object getValueFromMapRecursively( Object mapLiked, String szKey ) {
        return Units.getValueFromMapStructureRecursively( mapLiked, szKey );
    }

    protected boolean isRecursivePath( String szKey ) {
        return szKey != null && ( szKey.contains( "." ) || szKey.contains( "/" ) );
    }

    protected Object defaultValue( String szDefaultValue ) {
        if ( "null".equalsIgnoreCase( szDefaultValue ) ) {
            return null;
        }
        return szDefaultValue;
    }
}
