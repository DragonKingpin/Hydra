package com.genius.common.UlfUMC;

import com.alibaba.fastjson.JSONObject;
import com.genius.util.IntToByteUtil;
//import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Genius
 * @date 2023/05/16 16:41
 **/

@Data
public class UlfUMCMessage implements Serializable {

    private UlfUMCProtocol ulfUMCProtocol;

    private UlfUMCBody ulfUMCBody;

    public UlfUMCMessage(UlfUMCMessageType methodType,String function,Map<String,Object> data){
        ulfUMCBody = new UlfUMCBody(methodType,function,data);
        ulfUMCProtocol = new UlfUMCProtocol();
        ulfUMCProtocol.setLength(JSONObject.toJSONString(ulfUMCBody).length());
    }

    public UlfUMCMessage(){
        ulfUMCBody = new UlfUMCBody();
        ulfUMCProtocol = new UlfUMCProtocol();
        ulfUMCProtocol.setLength(0);
    }

    public static byte[] encode(UlfUMCMessage message){
        UlfUMCProtocol ulfUMCProtocol = message.getUlfUMCProtocol();
        UlfUMCBody ulfUMCBody = message.getUlfUMCBody();
        ByteBuffer buffer = ByteBuffer.allocate(message.getLength()+UlfUMCProtocol.header.length()+1028);

        buffer.put(UlfUMCProtocol.header.getBytes());
        String body = JSONObject.toJSONString(ulfUMCBody);
        buffer.put(IntToByteUtil.intToByte(message.getLength()));
        buffer.put(body.getBytes(StandardCharsets.UTF_8));

        return buffer.array();
    }

    public static UlfUMCMessage decode(byte[] in) throws UlfUMCMessageException {
        try {
            int index = 0;
            String UMCProtocolHeader = new String(Arrays.copyOfRange(in,index,UlfUMCProtocol.header.length()));
            if(!UMCProtocolHeader.equals(UlfUMCProtocol.header)){
                throw new UlfUMCMessageException("UlfUMCMessage decode Error");
            }
            index+=UMCProtocolHeader.length();
            int length = IntToByteUtil.byteArrayToInt(Arrays.copyOfRange(in,index,index+4));
            index+=4;
            String body = new String(Arrays.copyOfRange(in,index,index+length));

            Map<String,Object> map = JSONObject.parseObject(body, Map.class);
            UlfUMCMessageType methodType = UlfUMCMessageType.valueOf((String) map.get("method"));
            String function = (String) map.get("function");
            Map<String,Object> data = new HashMap<>();
            if (map.containsKey("data")) {
                data = JSONObject.parseObject(map.get("data").toString(), Map.class);
            }
            return new UlfUMCMessage(methodType,function,data);
        }catch (Exception e){
            throw new UlfUMCMessageException("UlfUMCMessage decode Error");
        }
    }

    public int getLength(){
        int length = JSONObject.toJSONString(ulfUMCBody).length();
        ulfUMCProtocol.setLength(length);
        return length;
    }

    public Map getData(){
        return this.ulfUMCBody.getData();
    }

    public UlfUMCMessageType getMethod(){
        return this.ulfUMCBody.getMethod();
    }

    public String getFunction(){
        return this.ulfUMCBody.getFunction();
    }

}
