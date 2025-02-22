package com.pinecone.hydra.umct.husky.compiler;

import java.util.List;

import com.pinecone.framework.lang.field.DataStructureEntity;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.ReflectionUtils;

public interface MethodDigest extends Pinenut {

    ClassDigest getClassDigest();

    String getName();

    String getFullName();

    String getRawName();

    DataStructureEntity getArgumentTemplate();

    Class<?> getReturnType();

    String getGenericReturnTypeLabel();

    default String[] getGenericReturnTypeNames() {
        return ReflectionUtils.extractGenericClassNames( this.getGenericReturnTypeLabel() );
    }

    void applyGenericReturnTypeLabel( String genericTypeLabel );

    default boolean hasDeclaredGenericReturnType() {
        return this.getGenericReturnTypeLabel() != null && this.getGenericReturnTypeLabel().contains( "<" ) && this.getGenericReturnTypeLabel().contains( ">" );
    }

    List<IfaceParamsDigest> getParamsDigests();

    void apply( List<IfaceParamsDigest> ifaceParamsDigests);

    List<String> getArgumentsKey();
}
