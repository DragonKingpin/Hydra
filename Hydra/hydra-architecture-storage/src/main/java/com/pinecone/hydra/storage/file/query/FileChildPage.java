package com.pinecone.hydra.storage.file.query;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.FileTreeNode;

public class FileChildPage implements Pinenut {
    protected List<FileTreeNode> records = new ArrayList<>();
    protected long total;
    protected int offset;
    protected int limit;

    public static FileChildPage of( List<FileTreeNode> records, long total, int offset, int limit ) {
        FileChildPage page = new FileChildPage();
        page.setRecords( records );
        page.setTotal( total );
        page.setOffset( offset );
        page.setLimit( limit );
        return page;
    }

    public List<FileTreeNode> getRecords() {
        return this.records;
    }

    public void setRecords( List<FileTreeNode> records ) {
        this.records = records == null ? new ArrayList<>() : new ArrayList<>( records );
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal( long total ) {
        this.total = Math.max( 0L, total );
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset( int offset ) {
        this.offset = Math.max( 0, offset );
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit( int limit ) {
        this.limit = Math.max( 0, limit );
    }

    public boolean getHasMore() {
        return this.offset + this.records.size() < this.total;
    }
}
