package com.walnut.redstone.ether.shuttle.kernel;

import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;

public class KernelMappedFileMeta implements Pinenut {
    protected String              mszPath;
    protected String              mszName;
    protected String              mszType;
    protected String              mszContentType;
    protected boolean             mbExists;
    protected boolean             mbDirectory;
    protected boolean             mbReadable = true;
    protected boolean             mbWritable;
    protected Long                mnContentLength;
    protected Map<String, String> mHeaders = new LinkedHashMap<>();

    public String getPath() {
        return this.mszPath;
    }

    public void setPath( String szPath ) {
        this.mszPath = szPath;
    }

    public String getName() {
        return this.mszName;
    }

    public void setName( String szName ) {
        this.mszName = szName;
    }

    public String getType() {
        return this.mszType;
    }

    public void setType( String szType ) {
        this.mszType = szType;
    }

    public String getContentType() {
        return this.mszContentType;
    }

    public void setContentType( String szContentType ) {
        this.mszContentType = szContentType;
    }

    public boolean isExists() {
        return this.mbExists;
    }

    public void setExists( boolean bExists ) {
        this.mbExists = bExists;
    }

    public boolean isDirectory() {
        return this.mbDirectory;
    }

    public void setDirectory( boolean bDirectory ) {
        this.mbDirectory = bDirectory;
    }

    public boolean isReadable() {
        return this.mbReadable;
    }

    public void setReadable( boolean bReadable ) {
        this.mbReadable = bReadable;
    }

    public boolean isWritable() {
        return this.mbWritable;
    }

    public void setWritable( boolean bWritable ) {
        this.mbWritable = bWritable;
    }

    public Long getContentLength() {
        return this.mnContentLength;
    }

    public void setContentLength( Long nContentLength ) {
        this.mnContentLength = nContentLength;
    }

    public Map<String, String> getHeaders() {
        return this.mHeaders;
    }

    public void setHeaders( Map<String, String> headers ) {
        this.mHeaders = headers == null ? new LinkedHashMap<>() : new LinkedHashMap<>( headers );
    }
}
