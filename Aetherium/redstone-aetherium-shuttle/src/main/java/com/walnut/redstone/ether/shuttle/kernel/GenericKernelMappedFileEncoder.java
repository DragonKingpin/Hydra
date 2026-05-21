package com.walnut.redstone.ether.shuttle.kernel;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSON;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleRequest;
import com.walnut.redstone.ether.shuttle.exchange.ShuttleResponse;

public class GenericKernelMappedFileEncoder implements KernelMappedFileEncoder {
    public static final String ContentTypeJson = "application/json; charset=utf-8";
    public static final String ContentTypeText = "text/plain; charset=utf-8";
    public static final String ContentTypeOctets = "application/octet-stream";

    @Override
    public ShuttleResponse encode( KernelMappedFile file, ShuttleRequest request ) {
        if ( file == null || file.getMeta() == null || !file.getMeta().isExists() ) {
            return this.empty( 404 );
        }
        if ( file.getValue() == null ) {
            return this.empty( 404 );
        }

        byte[] bytes = this.bytes( file.getValue() );
        ShuttleResponse response = new ShuttleResponse();
        response.setStatusCode( 200 );
        response.setHeaders( this.headers( file, bytes ) );
        response.setBodyStream( new ByteArrayInputStream( bytes ) );
        return response;
    }

    protected Map<String, String> headers( KernelMappedFile file, byte[] bytes ) {
        Map<String, String> ret = new LinkedHashMap<>();
        if ( file.getMeta().getHeaders() != null ) {
            ret.putAll( file.getMeta().getHeaders() );
        }
        ret.put( "Content-Type", this.contentType( file ) );
        ret.put( "Content-Length", String.valueOf( bytes.length ) );
        ret.put( "X-Red-Kernel-Readonly", String.valueOf( !file.getMeta().isWritable() ) );
        return ret;
    }

    protected String contentType( KernelMappedFile file ) {
        if ( file.getMeta().getContentType() != null && !file.getMeta().getContentType().isEmpty() ) {
            return file.getMeta().getContentType();
        }
        Object value = file.getValue();
        if ( value instanceof byte[] ) {
            return ContentTypeOctets;
        }
        if ( value instanceof String ) {
            return ContentTypeText;
        }
        return ContentTypeJson;
    }

    protected byte[] bytes( Object value ) {
        if ( value instanceof byte[] ) {
            return (byte[]) value;
        }
        if ( value instanceof String ) {
            return ((String) value).getBytes( StandardCharsets.UTF_8 );
        }
        String szJson = value instanceof Pinenut ? ((Pinenut) value).toJSONString() : JSON.stringify( value );
        if ( szJson == null ) {
            szJson = JSON.stringify( String.valueOf( value ) );
        }
        return szJson.getBytes( StandardCharsets.UTF_8 );
    }

    protected ShuttleResponse empty( int nStatusCode ) {
        ShuttleResponse response = new ShuttleResponse();
        response.setStatusCode( nStatusCode );
        response.setHeaders( new LinkedHashMap<>( Map.of( "Content-Length", "0" ) ) );
        response.setBodyStream( new ByteArrayInputStream( new byte[0] ) );
        return response;
    }
}
