package com.pinecone.framework.system.construction;

public class GenericStructureDefinition implements StructureDefinition {
    private String     mLookup         = "";
    private Class<?>   mType           = Object.class;
    private ReuseCycle mCycle          = ReuseCycle.Singleton;
    private boolean    mShareable      = true;
    private String     mDescription    = "";
    private Class<?>   mProvider       =  void.class;
    private String     mProviderMethod = "";

    private Structure.AuthenticationType mAuthenticationType = Structure.AuthenticationType.CONTAINER;

    public GenericStructureDefinition( Class<?> type ) {
        this.mType = type;
    }


    public GenericStructureDefinition( Structure structure ) {
        this( structure.type() );
        this.mCycle              = structure.cycle();
        this.mLookup             = structure.lookup();
        this.mProvider           = structure.provider();
        this.mShareable          = structure.shareable();
        this.mDescription        = structure.description();
        this.mProviderMethod     = structure.providerMethod();
        this.mAuthenticationType = structure.authenticationType();
    }


    @Override
    public String getLookup() {
        return this.mLookup;
    }

    @Override
    public void setLookup( String lookup ) {
        this.mLookup = lookup; //TODO
    }

    @Override
    public Class<? > getType() {
        return this.mType;
    }

    @Override
    public void setType( Class<? > type ) {
        this.mType = type;
    }

    @Override
    public ReuseCycle getCycle() {
        return this.mCycle;
    }

    @Override
    public void setCycle( ReuseCycle cycle ) {
        this.mCycle = cycle;
    }

    @Override
    public Structure.AuthenticationType getAuthenticationType() {
        return this.mAuthenticationType;
    }

    @Override
    public void setAuthenticationType( Structure.AuthenticationType authenticationType ) {
        this.mAuthenticationType = authenticationType;
    }

    @Override
    public boolean isShareable() {
        return this.mShareable;
    }

    @Override
    public void setShareable( boolean shareable ) {
        this.mShareable = shareable;
    }

    @Override
    public String getDescription() {
        return this.mDescription;
    }

    @Override
    public void setDescription(String description) {
        this.mDescription = description;
    }

    @Override
    public Class<?> getProvide() {
        return this.mProvider;
    }

    @Override
    public String getProvideMethod() {
        return this.mProviderMethod;
    }
}