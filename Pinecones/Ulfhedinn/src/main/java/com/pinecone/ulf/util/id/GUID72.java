package com.pinecone.ulf.util.id;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;

import java.io.Serializable;
import java.math.BigInteger;

public class GUID72 implements GUID  {
    private long sequence;
    private  long workerId;
    private long deltaSeconds;
    private long nanoSeed;
    public GUID72() {

    }
    public GUID72(long deltaSeconds,long workerId,long sequence, long nanoSeed) {
        this.sequence = sequence;
        this.workerId = workerId;
        this.deltaSeconds = deltaSeconds;
        this.nanoSeed = nanoSeed;
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

    public long getNanoSeed() {
        return nanoSeed;
    }

    public void setNanoSeed(long nanoSeed) {
        this.nanoSeed = nanoSeed;
    }
    @Override
    public String toString() {
        String deltaSecondsHex = String.format("%07x", deltaSeconds);
        String workerIdHex = String.format("%06x", workerId);
        String sequenceHex = String.format("%04x", sequence);
        String nanoSeedHex = String.format("%02x",nanoSeed );
         return deltaSecondsHex+"-"+workerIdHex+"-"+sequenceHex+"-"+nanoSeedHex;
    }

    @Override
    public  GUID parse(String ID) {
        String[] parts = ID.split("-");

        // 将十六进制字符串转换为十进制整数
        long deltaSecondsDec = new BigInteger(parts[0], 16).longValue();
        long workerIdDec = new BigInteger(parts[1], 16).longValue();
        long sequenceDec = new BigInteger(parts[2], 16).longValue();
        long nanoSeedDec = new BigInteger(parts[3], 16).longValue();

        // 创建一个新的GUID64实例
        return new GUID72(deltaSecondsDec, workerIdDec, sequenceDec, nanoSeedDec);
    }
}
