package com.genius.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Genius
 * @date 2023/05/16 21:56
 **/
public class IntToByteUtil {

    public static byte[] intToByte(int num)
    {
        byte[] result=null;
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        DataOutputStream dos=new DataOutputStream(bos);
        try {
            dos.writeInt(num);
            result=bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int byteArrayToInt(byte[] bytes){
        return (bytes[3] & 0xff)
                |((bytes[2] & 0xff) << 8)
                |((bytes[1] & 0xff) << 16)
                |((bytes[0] & 0xff) << 24);
    }
}
