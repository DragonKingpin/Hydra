package com.pinecone.ulf.util.id;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.ulf.util.id.utils.DateUtils;

import java.math.BigInteger;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GUID64 implements GUID {
    private long sequence;
    private  long workerId;
    private long deltaSeconds;

    public GUID64() {

    }

    public GUID64(long sequence, long workerId, long deltaSeconds) {
        this.sequence = sequence;
        this.workerId = workerId;
        this.deltaSeconds = deltaSeconds;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDeltaSeconds() {
        return deltaSeconds;
    }

    public void setDeltaSeconds(long deltaSeconds) {
        this.deltaSeconds = deltaSeconds;
    }

    @Override
    public Identification parse(String GUID64) {
        // 分离UUID的各个部分
        String[] parts = GUID64.split("-");

        // 将十六进制字符串转换为十进制整数
        long deltaSecondsDec = new BigInteger(parts[0], 16).longValue();
        long workerIdDec = new BigInteger(parts[1], 16).longValue();
        long sequenceDec = new BigInteger(parts[2], 16).longValue();

        // 创建一个新的GUID64实例
        return new GUID64(sequenceDec, workerIdDec, deltaSecondsDec);
    }
    @Override
    public String toString(){
        String deltaSecondsHex = String.format("%07x", deltaSeconds);
        String workerIdHex = String.format("%06x", workerId);
        String sequenceHex = String.format("%04x", sequence);
        return deltaSecondsHex+"-"+workerIdHex+"-"+sequenceHex;
    }
}
