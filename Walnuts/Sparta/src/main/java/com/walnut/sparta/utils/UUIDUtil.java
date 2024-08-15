package com.walnut.sparta.utils;

import com.pinecone.ulf.util.id.UUID;
import com.pinecone.ulf.util.id.UUIDBuilder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UUIDUtil {
    public static String createUUID() {
        long uid = UUIDBuilder.getBuilder().getUID(); // 获取64位的UID
        UUID uuid = UUIDBuilder.getBuilder().getUUID(uid);
        String UUID=generateUUID(uuid.getSequence(),uuid.getWorkerId(),uuid.getDeltaSeconds());
        String nanoSecondsSeed = getNanoSecondsSeed(); // 获取8位的纳秒种子

        // 将64位的UID与8位的纳秒种子合并成一个72位的字符串
        String combinedUID = combineUIDWithNanoSeed(UUID, nanoSecondsSeed);

        return combinedUID;
    }

    /**
     * 获取当前时间的纳秒部分并截取为8位。
     *
     * @return 8位的纳秒种子
     */
    private static String getNanoSecondsSeed() {
        LocalDateTime now = LocalDateTime.now();
        long nanoseconds = now.toLocalTime().truncatedTo(ChronoUnit.NANOS).getNano();
        return String.format("%08d", nanoseconds % 100000000L); // 截取为8位并格式化为字符串
    }

    /**
     * 合并64位的UID与8位的纳秒种子为一个72位的字符串。
     *
     * @param uid 64位的UID
     * @param nanoSecondsSeed 8位的纳秒种子
     * @return 合并后的72位的字符串
     */
    private static String combineUIDWithNanoSeed(String uid, String nanoSecondsSeed) {
        // 将64位的UID转换为字符串

        // 将UID与纳秒种子合并为一个字符串
        String combinedUID = uid +"-" +nanoSecondsSeed;

        return combinedUID;
    }

    public static String generateUUID(long sequence, long workerId, long deltaSeconds) {
        // 将sequence转换为16进制，并确保长度为4位
        String seqHex = String.format("%04X", sequence);

        // 将workerId转换为16进制，并确保长度为4位
        String workerIdHex = String.format("%04X", workerId);

        // 对deltaSeconds进行取模操作以适应16位
        long truncatedDeltaSeconds = deltaSeconds % 65536;
        // 将truncatedDeltaSeconds转换为16进制，并确保长度为4位
        String deltaSecondsHex = String.format("%04X", truncatedDeltaSeconds);

        // 组合三个部分
        return seqHex + "-" + workerIdHex + "-" + deltaSecondsHex;
    }

}
