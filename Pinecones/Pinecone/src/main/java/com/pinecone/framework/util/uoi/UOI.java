package com.pinecone.framework.util.uoi;

import java.net.URI;

import com.pinecone.framework.system.NoSuchProviderException;
import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.prototype.Pinenut;

public class UOI implements Pinenut {
    protected URI                         mResourceIdentifier;
    protected UniformObjectLoader         mUniformObjectLoader;
    protected UniformObjectLoaderFactory  mUniformObjectLoaderFactory;


    public UOI( URI uri, UniformObjectLoaderFactory factory ) {
        this.resolve( uri, factory );
    }

    public UOI( String szURI, UniformObjectLoaderFactory factory ){
        this.resolve( szURI, factory );
    }

    public UOI( URI uri ) {
        this( uri, UniformObjectLoaderFactory.DefaultObjectLoaderFactory );
    }

    public UOI( String szURI ) {
        this( szURI, UniformObjectLoaderFactory.DefaultObjectLoaderFactory );
    }

    public UOI() {
    }


    public void resolve( URI uri, UniformObjectLoaderFactory factory ) throws ProxyProvokeHandleException {
        this.mResourceIdentifier          = uri;
        this.mUniformObjectLoaderFactory  = factory;
        try{
            this.mUniformObjectLoader         = factory.newLoader( this.getScheme() );
        }
        catch ( NoSuchProviderException e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }

    public void resolve( String szURI, UniformObjectLoaderFactory factory ){
        this.resolve( URI.create( szURI ), factory );
    }

    public void resolve( URI uri ) {
        this.resolve( uri, UniformObjectLoaderFactory.DefaultObjectLoaderFactory );
    }

    public void resolve( String str ) {
        this.resolve( str, UniformObjectLoaderFactory.DefaultObjectLoaderFactory );
    }


    public String getObjectName() {
        String szPath = this.mResourceIdentifier.getPath();
        if( szPath.startsWith( "/" ) ) {
            return szPath.substring( 1 );
        }
        return szPath;
    }

    public Class<? > toClass() {
        return this.mUniformObjectLoader.toClass( this );
    }

    public Object newInstance( Class<? >[] paramTypes, Object... args ) {
        return this.mUniformObjectLoader.newInstance( this, paramTypes, args );
    }

    public Object newInstance( Object... args ) {
        return this.mUniformObjectLoader.newInstance( this, args );
    }



    public String getScheme() {
        return this.mResourceIdentifier.getScheme();
    }

    public String getSchemeSpecificPart() {
        return this.mResourceIdentifier.getSchemeSpecificPart();
    }

    public String getRawSchemeSpecificPart() {
        return this.mResourceIdentifier.getRawSchemeSpecificPart();
    }

    public String getUserInfo() {
        return this.mResourceIdentifier.getUserInfo();
    }

    public String getRawUserInfo() {
        return this.mResourceIdentifier.getRawUserInfo();
    }

    public String getHost() {
        return this.mResourceIdentifier.getHost();
    }

    public int getPort() {
        return this.mResourceIdentifier.getPort();
    }

    public String getPath() {
        return this.mResourceIdentifier.getPath();
    }

    public String getRawPath() {
        return this.mResourceIdentifier.getRawPath();
    }

    public String getQuery() {
        return this.mResourceIdentifier.getQuery();
    }

    public String getRawQuery() {
        return this.mResourceIdentifier.getRawQuery();
    }

    public String getFragment() {
        return this.mResourceIdentifier.getFragment();
    }

    public String getRawFragment() {
        return this.mResourceIdentifier.getRawFragment();
    }

    public boolean isAbsolute() {
        return this.mResourceIdentifier.isAbsolute();
    }

    public boolean isOpaque() {
        return this.mResourceIdentifier.isOpaque();
    }

    public URI normalize() {
        return this.mResourceIdentifier.normalize();
    }

    public URI relativize(URI uri) {
        return this.mResourceIdentifier.relativize(uri);
    }

    @Override
    public boolean equals(Object obj) {
        return this.mResourceIdentifier.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.mResourceIdentifier.hashCode();
    }

    @Override
    public String toString() {
        return this.mResourceIdentifier.toString();
    }

    @Override
    public String toJSONString() {
        return "\"" + this.toString() + "\"";
    }

    public String toASCIIString() {
        return this.mResourceIdentifier.toASCIIString();
    }


    public static UOI create( String uri ) {
        return new UOI( uri );
    }

    public static UOI create( String uri, UniformObjectLoaderFactory factory  ) {
        return new UOI( uri, factory );
    }
}
