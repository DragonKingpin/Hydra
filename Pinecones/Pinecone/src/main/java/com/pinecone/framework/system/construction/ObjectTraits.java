package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.StringUtils;

public interface ObjectTraits extends Pinenut {
    boolean isBean();

    void setBean( boolean isBean );

    boolean isDirectlyStruct();

    void setDirectlyStruct( boolean isDirectlyStruct );

    String getName();

    void setName( String name );

    String getMappedKey();

    void setMappedKey( String mappedKey );

    Object getTargetAnnotation();

    void setTargetAnnotation( Object targetAnnotation );

    Class<? > getDeclaredType();

    void setDeclaredType( Class<?> declaredType );

    Class<? > getAffiliatedType();

    void setAffiliatedType( Class<?> affiliatedType );

    default boolean isStructure() {
        return this.getTargetAnnotation() instanceof Structure;
    }

    default boolean isAnonymous() {
        return StringUtils.isEmpty( this.getMappedKey() );
    }
}
