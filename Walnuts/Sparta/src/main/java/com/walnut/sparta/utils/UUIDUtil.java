package com.walnut.sparta.utils;

import com.pinecone.ulf.util.id.UUIDBuilder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class UUIDUtil {
    public static String createUUID() {
        long uid = UUIDBuilder.getBuilder().getUID(); // 获取64位的UID
        String nanoSecondsSeed = getNanoSecondsSeed(); // 获取8位的纳秒种子

        // 将64位的UID与8位的纳秒种子合并成一个72位的字符串
        String combinedUID = combineUIDWithNanoSeed(uid, nanoSecondsSeed);

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
    private static String combineUIDWithNanoSeed(long uid, String nanoSecondsSeed) {
        // 将64位的UID转换为字符串
        String uidStr = Long.toString(uid);

        // 将UID与纳秒种子合并为一个字符串
        String combinedUID = uidStr + nanoSecondsSeed;

        return combinedUID;
    }

}
