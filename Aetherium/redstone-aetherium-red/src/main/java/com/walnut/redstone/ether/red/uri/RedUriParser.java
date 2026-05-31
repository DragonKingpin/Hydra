package com.walnut.redstone.ether.red.uri;

import java.net.URI;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.redstone.ether.red.RedSchemes;
import com.walnut.redstone.ether.red.error.RedErrorCode;
import com.walnut.redstone.ether.red.error.RedProtocolException;

public class RedUriParser implements Pinenut {
    public RedUri parse( String value ) {
        URI uri = URI.create( value );
        if ( uri.getScheme() == null || !RedSchemes.Red.equalsIgnoreCase( uri.getScheme() ) ) {
            throw new RedProtocolException( RedErrorCode.InvalidUri, "Only " + RedSchemes.Red + ":// URI is supported." );
        }
        RedUri ret = new RedUri();
        ret.setRaw( value );
        ret.setBucket( uri.getAuthority() );
        ret.setPath( uri.getPath() == null ? "/" : uri.getPath() );
        if ( uri.getAuthority() == null || uri.getAuthority().trim().isEmpty() ) {
            ret.setNamespace( RedReservedPath.isReserved( ret.getPath() ) ? RedNamespace.Reserved : RedNamespace.Kernel );
        }
        else {
            ret.setNamespace( RedNamespace.Object );
        }
        return ret;
    }
}
