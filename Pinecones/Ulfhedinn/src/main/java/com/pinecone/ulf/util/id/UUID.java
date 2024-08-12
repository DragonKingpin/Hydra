package com.pinecone.ulf.util.id;

public class UUID {
    private long sequence;
    private  long workerId;
    private long deltaSeconds;

    public UUID() {
    }

    public UUID(long sequence, long workerId, long deltaSeconds) {
        this.sequence = sequence;
        this.workerId = workerId;
        this.deltaSeconds = deltaSeconds;
    }

    /**
     * 获取
     * @return sequence
     */
    public long getSequence() {
        return sequence;
    }

    /**
     * 设置
     * @param sequence
     */
    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    /**
     * 获取
     * @return workerId
     */
    public long getWorkerId() {
        return workerId;
    }

    /**
     * 设置
     * @param workerId
     */
    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    /**
     * 获取
     * @return deltaSeconds
     */
    public long getDeltaSeconds() {
        return deltaSeconds;
    }

    /**
     * 设置
     * @param deltaSeconds
     */
    public void setDeltaSeconds(long deltaSeconds) {
        this.deltaSeconds = deltaSeconds;
    }

    public String toString() {
        return "UUID{sequence = " + sequence + ", workerId = " + workerId + ", deltaSeconds = " + deltaSeconds + "}";
    }
}
