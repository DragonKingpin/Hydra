package com.walnut.odin.project;

import java.time.LocalDateTime;
import java.util.Map;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.framework.util.json.homotype.BeanMapDecoder;

public class GenericTaskProject implements TaskProject {

    protected long          mnEnumId;

    protected GUID          mGuid;

    protected String        mszName;

    protected String        mszTitle;

    protected GUID          mBizTreeGuid;

    protected boolean       mbEnable;

    protected String        mszDescription;

    protected String        mszExtraInformation;

    protected LocalDateTime createTime;

    protected LocalDateTime updateTime;

    public GenericTaskProject() {
        super();
        this.mbEnable = true;
    }

    public GenericTaskProject( Map<String, Object> joEntity ) {
        this();
        BeanMapDecoder.BasicDecoder.decode( this, joEntity );
    }

    @Override
    public long getEnumId() {
        return this.mnEnumId;
    }

    @Override
    public void setEnumId( long nEnumId ) {
        this.mnEnumId = nEnumId;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public void setName( String szName ) {
        this.mszName = szName;
    }

    @Override
    public String getTitle() {
        return this.mszTitle;
    }

    @Override
    public void setTitle( String szTitle ) {
        this.mszTitle = szTitle;
    }

    @Override
    public GUID getBizTreeGuid() {
        return this.mBizTreeGuid;
    }

    @Override
    public void setBizTreeGuid( GUID bizTreeGuid ) {
        this.mBizTreeGuid = bizTreeGuid;
    }

    @Override
    public boolean isEnable() {
        return this.mbEnable;
    }

    @Override
    public void setEnable( boolean bEnable ) {
        this.mbEnable = bEnable;
    }

    @Override
    public String getDescription() {
        return this.mszDescription;
    }

    @Override
    public void setDescription( String szDescription ) {
        this.mszDescription = szDescription;
    }

    @Override
    public String getExtraInformation() {
        return this.mszExtraInformation;
    }

    @Override
    public void setExtraInformation( String szExtraInformation ) {
        this.mszExtraInformation = szExtraInformation;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return this.createTime;
    }

    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.updateTime = updateTime;
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
