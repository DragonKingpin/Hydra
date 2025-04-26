package com.walnut.odin.task.dto;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.slime.entity.ArchEnumIndexableEntity;


public class GenericCategoryTag extends ArchEnumIndexableEntity implements CategoryTag {
    protected GUID   mTaskGuid;

    protected String mszCategoryType;

    protected String mszCategoryName;

    public GenericCategoryTag() {
        super();
    }

    @Override
    public void setEnumId( long id ) {
        this.mnEnumId = id;
    }

    @Override
    public void setTaskGuid( GUID taskGuid ) {
        this.mTaskGuid = taskGuid;
    }

    @Override
    public GUID getTaskGuid() {
        return this.mTaskGuid;
    }

    @Override
    public void setCategoryName( String categoryName ) {
        this.mszCategoryName = categoryName;
    }

    @Override
    public String getCategoryName() {
        return this.mszCategoryName;
    }

    @Override
    public void setCategoryType( String categoryType ) {
        this.mszCategoryType = categoryType;
    }

    @Override
    public String getCategoryType() {
        return this.mszCategoryType;
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
