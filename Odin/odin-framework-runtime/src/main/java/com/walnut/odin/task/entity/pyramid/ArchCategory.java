package com.walnut.odin.task.entity.pyramid;

import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.slime.entity.ArchEnumIndexableEntity;

public abstract class ArchCategory extends ArchEnumIndexableEntity implements Category {
    protected String mszName;
    protected String mszAlias;
    protected String mszDescription;

    public ArchCategory() {
        super();
    }

    @Override
    public void setEnumId( long id ) {
        this.mnEnumId = id;
    }

    @Override
    public void setName( String name ) {
        this.mszName = name;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public void setAlias( String alias ) {
        this.mszAlias = alias;
    }

    @Override
    public String getAlias() {
        return this.mszAlias;
    }

    @Override
    public void setDescription( String description ) {
        this.mszDescription = description;
    }

    @Override
    public String getDescription() {
        return this.mszDescription;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}