package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.umc.msg.UMCHeadV1;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.pinecone.hydra.umc.msg.ArchUMCProtocol;
import com.pinecone.hydra.umc.msg.UMCHead;

import java.util.List;


public class GenericUMCByteMessageDecoder extends ByteToMessageDecoder {
    private ByteBuf        cumulation;
    private ExtraHeadCoder extraHeadCoder;
    private long           byteSum;
    private long           bodyBytes;
    private int            readAt;
    private int            readBytes; //Each

    public GenericUMCByteMessageDecoder( ExtraHeadCoder extraHeadCoder ) {
        this.extraHeadCoder = extraHeadCoder;
        this.byteSum   = -1;
        this.bodyBytes = 0;
        this.readAt    = 0;
        this.readBytes = 0;
    }

    @Override
    protected void decode( ChannelHandlerContext ctx, ByteBuf in, List<Object> out ) throws Exception {
        while ( in.readableBytes() > 0 ) {
            if ( this.byteSum == -1 ) {
                int nBufSize = ArchUMCProtocol.basicHeadLength( UMCHeadV1.ProtocolSignature );
                // Waiting for more data to arrive, and that will be enough to decode the header.
                if ( in.readableBytes() < nBufSize ) {
                    return;
                }
                this.readBytes = 0;
                byte[] buf = new byte[ nBufSize ];
                in.readBytes(buf);

                UMCHead head = ArchUMCProtocol.onlyReadMsgBasicHead( buf, UMCHeadV1.ProtocolSignature, this.extraHeadCoder );
                this.bodyBytes = head.getBodyLength();
                this.byteSum   = nBufSize + head.getExtraHeadLength() + this.bodyBytes;
                this.readAt    += nBufSize;
                this.readBytes += nBufSize;

                if ( this.byteSum < 0 ) {
                    throw new IllegalArgumentException( "Invalid byteSum calculation: " + this.byteSum );
                }
            }

            int startAt = this.readAt - this.readBytes;
            in.readerIndex( startAt );
            this.readAt -= this.readBytes;
            if ( in.readableBytes() >= this.byteSum ) {
                //Debug.redfs( in.readableBytes(), in.toString() );
                this.readBytes = (int)this.byteSum;
                ByteBuf completeMessage = in.readRetainedSlice((int) this.readBytes);
                this.readAt += this.readBytes;

                try {
                    ctx.fireChannelRead(completeMessage);
                }
                finally {
                    completeMessage.release();
                }

                this.byteSum   = -1;
                this.bodyBytes = 0;
                this.readBytes = 0;
            }

            //Debug.bluef( in.readableBytes() );
        }

        if ( this.byteSum == -1 ) {
            this.readAt = 0;
        }

        // Waiting for more data to arrive.
//        else {
//            return;
//        }
    }

    private void resetState() {
        this.byteSum   = -1;
        this.bodyBytes = 0;
        this.readAt    = 0;
        this.readBytes = 0;
    }

    @Override
    public void channelInactive( ChannelHandlerContext ctx ) throws Exception {
        super.channelInactive(ctx);
        if ( this.cumulation != null ) {
            this.cumulation.clear();
            this.cumulation.release();
            this.cumulation = null;
        }
        this.resetState();
    }
}