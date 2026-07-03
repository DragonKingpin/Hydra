package com.walnut.odin.processor.anonymous;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.walnut.odin.processor.anonymous.AnonymousTaskProcessorRegistration;

public class GenericAnonymousTaskProcessorRegistration implements AnonymousTaskProcessorRegistration {

    protected long                mnClientId;
    protected String              mszNodeName;
    protected String              mszAlias;
    protected String              mszBizPath;
    protected String              mszRuntime;
    protected Collection<String>  mExecCaps;
    protected Map<String, String> mMetadata;
    protected long                mnRegisterTime;
    protected long                mnLastUpdateTime;

    public GenericAnonymousTaskProcessorRegistration(
            String szNodeName,
            long nClientId,
            String szAlias,
            String szBizPath,
            String szRuntime,
            Collection<String> execCaps,
            Map<String, String> metadata
    ) {
        long now = System.currentTimeMillis();
        this.mnClientId       = nClientId;
        this.mszNodeName      = szNodeName;
        this.mszAlias         = szAlias;
        this.mszBizPath       = szBizPath;
        this.mszRuntime       = szRuntime;
        this.mExecCaps        = execCaps == null ? new ArrayList<>() : new ArrayList<>( execCaps );
        this.mMetadata        = metadata == null ? new LinkedHashMap<>() : new LinkedHashMap<>( metadata );
        this.mnRegisterTime   = now;
        this.mnLastUpdateTime = now;
    }

    @Override
    public long getClientId() {
        return this.mnClientId;
    }

    @Override
    public String getNodeName() {
        return this.mszNodeName;
    }

    @Override
    public String getAlias() {
        return this.mszAlias;
    }

    @Override
    public String getBizPath() {
        return this.mszBizPath;
    }

    @Override
    public String getRuntime() {
        return this.mszRuntime;
    }

    @Override
    public Collection<String> getExecCaps() {
        return Collections.unmodifiableCollection( this.mExecCaps );
    }

    @Override
    public Map<String, String> getMetadata() {
        return Collections.unmodifiableMap( this.mMetadata );
    }

    @Override
    public long getRegisterTime() {
        return this.mnRegisterTime;
    }

    @Override
    public long getLastUpdateTime() {
        return this.mnLastUpdateTime;
    }
}
