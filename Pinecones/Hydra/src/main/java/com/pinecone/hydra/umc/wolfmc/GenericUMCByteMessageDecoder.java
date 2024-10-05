package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import com.pinecone.hydra.umc.msg.ArchUMCProtocol;
import com.pinecone.hydra.umc.msg.UMCHead;

import java.util.List;

public class GenericUMCByteMessageDecoder extends ByteToMessageDecoder {
    private ByteBuf           cumulation;
    private ExtraHeadCoder    extraHeadCoder;

    public GenericUMCByteMessageDecoder( ExtraHeadCoder extraHeadCoder ) {
        this.extraHeadCoder = extraHeadCoder;
    }

    @Override
    protected void decode( ChannelHandlerContext ctx, ByteBuf in, List<Object > out ) throws Exception {
        this.cumulation = in;
        int nBufSize = ArchUMCProtocol.basicHeadLength( UMCHead.ProtocolSignature );
        if ( in.readableBytes() < nBufSize ) {
            return;
        }

        byte[] buf = new byte[ nBufSize ];
        in.readBytes( buf );

        UMCHead head = ArchUMCProtocol.onlyReadMsgBasicHead( buf, UMCHead.ProtocolSignature, this.extraHeadCoder );
        long nByteSum = nBufSize + head.getExtraHeadLength() + head.getBodyLength();
        in.resetReaderIndex();

        if( in.readableBytes() >= nByteSum ) {
            ctx.fireChannelRead( this.cumulation.retain() );
        }
    }

    @Override
    public void channelInactive( ChannelHandlerContext ctx ) throws Exception {
        super.channelInactive(ctx);
        if ( this.cumulation != null ) {
            this.cumulation.release();
            this.cumulation = null;
        }
    }
}
