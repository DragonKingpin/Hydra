package com.pinecone.hydra.orchestration;

public class BranchNoticeException extends BranchControlException {
    protected Object  noticeMsg;
    protected boolean noticeAll;

    public BranchNoticeException() {
        super();
    }

    public BranchNoticeException( String message ) {
        super( message );
    }

    public BranchNoticeException( Object noticeMsg, boolean noticeAll, String message ) {
        super( message );
        this.noticeMsg = noticeMsg;
        this.noticeAll = noticeAll;
    }

    public BranchNoticeException( Object noticeMsg ) {
        super();
        this.noticeMsg = noticeMsg;
    }

    public Object getNoticeMsg() {
        return this.noticeMsg;
    }

    public boolean isNoticeAll() {
        return this.noticeAll;
    }
}
