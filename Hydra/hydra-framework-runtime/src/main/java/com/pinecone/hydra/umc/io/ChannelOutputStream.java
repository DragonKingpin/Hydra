package com.pinecone.hydra.umc.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class ChannelOutputStream extends OutputStream {
    protected ChannelHandlerContext mChannelHandlerContext;

    protected ByteBuf               mByteBuf;

    protected byte[]                mTemp = new byte[1];

    public ChannelOutputStream( ChannelHandlerContext context ) {
        this.mChannelHandlerContext = context;
    }

    public ChannelHandlerContext getChannelHandlerContext(){
        return this.mChannelHandlerContext;
    }

    @Override
    public void write( int b ) throws IOException {
        try{
            this.mTemp[0] = (byte)b;
            this.mChannelHandlerContext.write( Unpooled.wrappedBuffer(this.mTemp) );
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    @Override
    public void write( byte[] b ) throws IOException {
        try{
            this.mChannelHandlerContext.write( Unpooled.wrappedBuffer(b) );
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    @Override
    public void write( byte[] b, int off, int len ) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        try{
            this.mChannelHandlerContext.write( Unpooled.wrappedBuffer( b, off, len ) );
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    @Override
    public void flush() throws IOException {
        try{
            this.mChannelHandlerContext.flush();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    public void close() throws IOException {
        try{
            this.mChannelHandlerContext.close();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }
}
