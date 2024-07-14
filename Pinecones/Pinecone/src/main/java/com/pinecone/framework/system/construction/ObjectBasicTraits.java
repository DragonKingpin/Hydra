package com.pinecone.framework.system.construction;

public class ObjectBasicTraits implements ObjectTraits {
    private boolean   mIsBean             = false;
    private boolean   mIsDirectlyStruct   = false;
    private String    mName               = "";
    private String    mMappedKey          = "";
    private Object    mTargetAnnotation   = null;
    private Class<?>  mDeclaredType       = null;
    private Class<?>  mAffiliatedType     = null;

    public ObjectBasicTraits() {
    }

    public ObjectBasicTraits( Structure structure ) {
        this.fromStructure( structure );
    }

    public ObjectTraits fromStructure( Structure structure ) {
        this.setBean( true );
        this.setDirectlyStruct( false );
        this.setName( structure.name() );
        this.setTargetAnnotation( structure );
        this.setDeclaredType( structure.type() );
        this.setMappedKey( structure.mappedName() );
        return this;
    }


    @Override
    public boolean isDirectlyStruct() {
        return this.mIsDirectlyStruct;
    }

    @Override
    public void setDirectlyStruct( boolean isDirectlyStruct ) {
        this.mIsDirectlyStruct = isDirectlyStruct;
    }

    @Override
    public boolean isBean() {
        return this.mIsBean;
    }

    @Override
    public void setBean( boolean isBean ) {
        this.mIsBean = isBean;
    }

    @Override
    public String getName() {
        return this.mName;
    }

    @Override
    public void setName( String name ) {
        this.mName = name;
    }

    @Override
    public String getMappedKey() {
        return this.mMappedKey;
    }

    @Override
    public void setMappedKey( String mappedKey ) {
        this.mMappedKey = mappedKey;
    }

    @Override
    public Object getTargetAnnotation() {
        return this.mTargetAnnotation;
    }

    @Override
    public void setTargetAnnotation( Object targetAnnotation ) {
        this.mTargetAnnotation = targetAnnotation;
    }

    @Override
    public Class<?> getDeclaredType() {
        return this.mDeclaredType;
    }

    @Override
    public void setDeclaredType( Class<?> declaredType ) {
        this.mDeclaredType = declaredType;
    }

    @Override
    public Class<?> getAffiliatedType() {
        return this.mAffiliatedType;
    }

    @Override
    public void setAffiliatedType( Class<?> affiliatedType ) {
        this.mAffiliatedType = affiliatedType;
    }

    @Override
    public boolean isStructure() {
        return this.getTargetAnnotation() instanceof Structure;
    }

}
