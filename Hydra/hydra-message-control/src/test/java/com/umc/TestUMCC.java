package com.umc;

import java.nio.ByteBuffer;

import com.pinecone.Pinecone;
import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.hydra.umc.msg.UMCCHead;
import com.pinecone.hydra.umc.msg.UMCCHeadV1;
import com.pinecone.hydra.umc.msg.UMCMethod;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.umc.msg.extra.GenericExtraHeadCoder;

public class TestUMCC {
    public static void testUMCC() throws Exception {
        UMCCHeadV1 head = new UMCCHeadV1();

        ExtraHeadCoder coder = new GenericExtraHeadCoder();


        head.setBodyLength( 136 );
        head.setControlBits( 512 );
        head.applyExtraHeadCoder( coder );
        head.setExtraHead( new JSONMaptron( "{k:123, k1: abcdefg}" ) );

        UMCCHeadV1.EncodePair pair = UMCCHeadV1.encode( head, coder );
        ByteBuffer buffer = pair.byteBuffer;

        //new UMCCHeadV1("", UMCMethod.INFORM );

        Debug.redf( head, pair.bufLength );


        UMCCHead dec = UMCCHeadV1.decode( buffer.array(), head.getSignature(), coder );

        byte[] headBuf = new byte[ head.getExtraHeadLength() ];
        int headSize = head.sizeof();
        System.arraycopy( buffer.array(), headSize, headBuf, 0, head.getExtraHeadLength() );
        Object object = coder.getDecoder().decode( dec, headBuf );

        Debug.bluef( dec, object );
    }

    public static void main( String[] args ) throws Exception {
        //String szJson = FileUtils.readAll("J:/120KWordsPhonetics.json5");
        Pinecone.init( (Object...cfg )->{

            TestUMCC.testUMCC();

            return 0;
        }, (Object[]) args );
    }
}
