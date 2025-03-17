package com.walnut.sparta.ucdn.console.ufm;

import com.walnut.sparta.ucdn.console.ufm.protocol.FileMeta64;

public class UFMDClusterDO extends FileMeta64 {
    protected String filePath;

    protected long segId;


    public UFMDClusterDO() {
        super();
    }

    public UFMDClusterDO( String sourceName, long size, long validateVal, String filePath, long segId ) {
        super( sourceName, size, validateVal );
        this.filePath   = filePath;
        this.segId      = segId;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath( String filePath ) {
        this.filePath = filePath;
    }

    public long getSegId() {
        return segId;
    }

    public void setSegId(long segId) {
        this.segId = segId;
    }

}
