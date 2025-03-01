package com.walnut.sparta.ucdn.console.infrastructure;

public class UCDNConstants {
    public static String KafkaServer = "localhost:9092";

    public static  String RocketServer = "localhost:9876";

    public static  String UCDNFileServiceTransmitGroup = "UCDNFileServiceTransmitGroup";

    public static  String UCDNFileCloudDistributeTransmitTopic = "ucdn-file-cloud-distribute-topic";

    public static String UCDNEFileCloudDistributeTransmitTopic = "ucdn-external-file-cloud-distribute-topic";

    public static  String TempFilePath = "D:/文件系统/temp/";

    public static  String FrameTempFilePath = "D:/文件系统/frameTemp/";

    public static  long expireTimeMillis = 7200000;

    public static  String defaultStoragePath = "D:/文件系统/大文件/";

    public static String serviceLevel = "master";

    public static String serviceId = "1769872-0002d2-0003-cc";

    public static long clientId = 1;
}
