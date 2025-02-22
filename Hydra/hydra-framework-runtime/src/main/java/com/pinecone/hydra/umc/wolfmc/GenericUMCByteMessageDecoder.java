package com.pinecone.hydra.umc.wolfmc;

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
    private int            readBytes; // Each package

    public GenericUMCByteMessageDecoder( ExtraHeadCoder extraHeadCoder ) {
        this.extraHeadCoder = extraHeadCoder;
        this.byteSum   = -1;
        this.bodyBytes = 0;
        this.readAt    = 0;
        this.readBytes = 0;
    }

    private static int countOccurrences( byte[] bfs, byte[] target ) {
        int count = 0;
        for ( int i = 0; i <= bfs.length - target.length; ++i ) {
            boolean match = true;
            for ( int j = 0; j < target.length; ++j ) {
                if (bfs[i + j] != target[j]) {
                    match = false;
                    break;
                }
            }
            if (match) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void decode( ChannelHandlerContext ctx, ByteBuf in, List<Object> out ) throws Exception {
//        ByteBuf bufs = in.copy();
//        byte[] bfs = new byte[ bufs.readableBytes() ];
//        bufs.readBytes( bfs );
//        int occurrences = countOccurrences(bfs, "UMC/1.1".getBytes());
//        int kf = countOccurrences(bfs, "afd".getBytes());
//        if ( kf > 0 ) {
//            IC += occurrences;
//            Debug.redfs(IC);
//        }


        while ( in.readableBytes() > 0 ) {
            boolean bContinueRead = false;
            if ( this.byteSum == -1 ) {

                // For debug reference.
//                if ( in.readableBytes() > 100 ) {
//                    Debug.traceSyn( in );
//                }

                int nBufSize = ArchUMCProtocol.basicHeadLength( UMCHeadV1.ProtocolSignature );
                // Waiting for more data to arrive, and that will be enough to decode the header.
                if ( in.readableBytes() < nBufSize ) {
                    return;
                }
                this.readBytes = 0;
                byte[] buf = new byte[ nBufSize ];
                in.readBytes(buf);

                // For debug reference.
//                if ( buf[ 0 ] != 85 ) {
//                    Debug.traceSyn( buf );
//                }

                UMCHead head = ArchUMCProtocol.onlyReadMsgBasicHead( buf, UMCHeadV1.ProtocolSignature, this.extraHeadCoder );
                this.bodyBytes = head.getBodyLength();
                this.byteSum   = nBufSize + head.getExtraHeadLength() + this.bodyBytes;
                this.readAt    += nBufSize;
                this.readBytes += nBufSize;

                if ( this.byteSum < 0 ) {
                    throw new IllegalArgumentException( "Invalid byteSum calculation: " + this.byteSum );
                }
                bContinueRead = true;
            }

            if ( bContinueRead ) {
                int startAt = this.readAt - this.readBytes;
                in.readerIndex( startAt );
                this.readAt -= this.readBytes;
                this.readBytes = 0;
            }
            if ( in.readableBytes() >= this.byteSum ) {
                this.readBytes = (int)this.byteSum;
                ByteBuf completeMessage = in.readRetainedSlice((int) this.readBytes);
                this.readAt += this.readBytes;


                // For debug reference.
//                byte[] bs = new byte[ (int) this.byteSum ];
//                ByteBuf byteBuf = completeMessage.copy();
//                byteBuf.readBytes(bs);
//                byteBuf.release();
//                if ( bs[ 0 ] != 85 ) {
//                    Debug.traceSyn( bs, bContinueRead );
//                }
//                head = ArchUMCProtocol.onlyReadMsgBasicHead( bs, UMCHeadV1.ProtocolSignature, this.extraHeadCoder );
//                Debug.warnSyn( bs, head );


                try {
                    //Debug.bluefs( invokes.getAndIncrement() );
                    ctx.fireChannelRead(completeMessage);
                }
                finally {
                    completeMessage.release();
                }

                this.byteSum   = -1;
                this.bodyBytes = 0;
                this.readBytes = 0;
            }
            else {
                return;
            }
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