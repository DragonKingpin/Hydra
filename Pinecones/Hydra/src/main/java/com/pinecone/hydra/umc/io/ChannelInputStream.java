package com.pinecone.hydra.umc.io;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;

public class ChannelInputStream extends InputStream {
    protected ByteBuf mByteBuf;

    public ChannelInputStream( ByteBuf byteBuf ) {
        this.mByteBuf = byteBuf;
    }

    public ByteBuf getByteBuf(){
        return this.mByteBuf;
    }

    @Override
    public int read() throws IOException {
        try{
            return this.mByteBuf.readByte();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    @Override
    public int read( byte[] b ) throws IOException {
        try{
            int n = this.mByteBuf.readableBytes();
            this.mByteBuf.readBytes( b, 0, b.length );
            return n - this.mByteBuf.readableBytes();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    @Override
    public int read( byte[] b, int off, int len ) throws IOException {
        try{
            int n = this.mByteBuf.readableBytes();
            this.mByteBuf.readBytes( b, off, len );
            return n - this.mByteBuf.readableBytes();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    @Override
    public byte[] readAllBytes() throws IOException {
        try{
            int readerIndex  = this.mByteBuf.readerIndex();
            int len          = this.mByteBuf.readableBytes();
            byte[] array;
            int offset;
            if ( this.mByteBuf.hasArray() ) {
                array  = this.mByteBuf.array();
                int arrayOffset = this.mByteBuf.arrayOffset();
                offset = arrayOffset + readerIndex;
            }
            else {
                array = new byte[ len ];
                offset = 0;
                this.mByteBuf.getBytes( readerIndex, array, 0, len );
            }

            int nFinalLen = len - offset;
            byte[] neo = new byte[ nFinalLen ];
            System.arraycopy( array, offset, neo, 0, nFinalLen );
            return neo;
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    @Override
    public int available() throws IOException {
        try{
            return this.mByteBuf.readableBytes();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

    @Override
    public void close() throws IOException {
        try{
            this.mByteBuf.clear();
        }
        catch ( Exception e ) {
            throw new IOException( e );
        }
    }

}
