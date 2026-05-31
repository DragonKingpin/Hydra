package com.walnut.redstone.ether.shuttle.client.engine.minio;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.walnut.redstone.ether.object.ObjectEntry;
import com.walnut.redstone.ether.object.ObjectListResult;
import com.walnut.redstone.ether.object.ObjectMetadata;
import com.walnut.redstone.ether.object.ObjectRange;
import com.walnut.redstone.ether.red.RedHeaders;
import com.walnut.redstone.ether.red.RedSchemes;
import com.walnut.redstone.ether.resource.ResourceType;
import com.walnut.redstone.ether.shuttle.client.RedShuttleClientConfig;
import com.walnut.redstone.ether.shuttle.client.engine.ObjectStorageEngine;
import com.walnut.redstone.ether.shuttle.client.error.RedShuttleEngineException;
import com.walnut.redstone.ether.shuttle.client.object.RedBucketRef;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectGetOptions;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectListOptions;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectPutOptions;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectPutResult;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectRef;
import com.walnut.redstone.ether.shuttle.client.object.RedObjectStream;

import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.messages.Item;

public class MinioObjectStorageEngine implements ObjectStorageEngine {
    protected final RedShuttleClientConfig config;
    protected final Map<String, MinioClient> clients = new ConcurrentHashMap<>();

    public MinioObjectStorageEngine( RedShuttleClientConfig config ) {
        this.config = config;
    }

    @Override
    public RedObjectStream getObject( RedObjectRef ref, RedObjectGetOptions options ) {
        try {
            GetObjectArgs.Builder builder = GetObjectArgs.builder()
                    .bucket( ref.getBucket() )
                    .object( ref.getKey() );
            ObjectRange range = options == null ? null : options.getRange();
            if ( range != null && range.isPartial() ) {
                builder.offset( range.getStart() );
                builder.length( range.getLength() );
            }
            GetObjectResponse response = this.client( ref.getEndpoint() ).getObject( builder.build() );
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setBucket( response.bucket() );
            metadata.setKey( response.object() );
            metadata.setName( this.name( response.object() ) );
            metadata.setContentType( response.headers().get( RedHeaders.ContentType ) );
            metadata.setEtag( response.headers().get( RedHeaders.ETag ) );
            String szLength = response.headers().get( RedHeaders.ContentLength );
            if ( szLength != null && !szLength.isBlank() ) {
                metadata.setSize( Long.parseLong( szLength ) );
            }
            return new RedObjectStream( response, metadata );
        }
        catch ( Exception ex ) {
            throw new RedShuttleEngineException( "Failed to get red object: " + ref.getBucket() + "/" + ref.getKey(), ex );
        }
    }

    @Override
    public ObjectMetadata statObject( RedObjectRef ref ) {
        try {
            StatObjectResponse response = this.client( ref.getEndpoint() ).statObject(
                    StatObjectArgs.builder()
                            .bucket( ref.getBucket() )
                            .object( ref.getKey() )
                            .build()
            );
            return this.toMetadata( response );
        }
        catch ( Exception ex ) {
            throw new RedShuttleEngineException( "Failed to stat red object: " + ref.getBucket() + "/" + ref.getKey(), ex );
        }
    }

    @Override
    public RedObjectPutResult putObject(
            RedObjectRef ref,
            InputStream inputStream,
            long nSize,
            RedObjectPutOptions options
    ) {
        try {
            PutObjectArgs.Builder builder = PutObjectArgs.builder()
                    .bucket( ref.getBucket() )
                    .object( ref.getKey() )
                    .stream( inputStream, nSize, this.config.getPartSize() );
            if ( options != null && options.getContentType() != null && !options.getContentType().isBlank() ) {
                builder.contentType( options.getContentType() );
            }
            if ( options != null && options.getMetadata() != null && !options.getMetadata().isEmpty() ) {
                builder.userMetadata( options.getMetadata() );
            }
            ObjectWriteResponse response = this.client( ref.getEndpoint() ).putObject( builder.build() );
            RedObjectPutResult ret = new RedObjectPutResult();
            ret.setBucket( response.bucket() );
            ret.setKey( response.object() );
            ret.setEtag( response.etag() );
            return ret;
        }
        catch ( Exception ex ) {
            throw new RedShuttleEngineException( "Failed to put red object: " + ref.getBucket() + "/" + ref.getKey(), ex );
        }
    }

    @Override
    public void deleteObject( RedObjectRef ref ) {
        try {
            this.client( ref.getEndpoint() ).removeObject(
                    RemoveObjectArgs.builder()
                            .bucket( ref.getBucket() )
                            .object( ref.getKey() )
                            .build()
            );
        }
        catch ( Exception ex ) {
            throw new RedShuttleEngineException( "Failed to delete red object: " + ref.getBucket() + "/" + ref.getKey(), ex );
        }
    }

    @Override
    public ObjectListResult listObjects( RedBucketRef bucketRef, RedObjectListOptions options ) {
        try {
            RedObjectListOptions safeOptions = options == null ? RedObjectListOptions.empty() : options;
            Iterable<Result<Item>> results = this.client( bucketRef.getEndpoint() ).listObjects(
                    ListObjectsArgs.builder()
                            .bucket( bucketRef.getBucket() )
                            .prefix( safeOptions.getPrefix() == null ? bucketRef.getPrefix() : safeOptions.getPrefix() )
                            .recursive( safeOptions.isRecursive() )
                            .build()
            );
            ObjectListResult ret = new ObjectListResult();
            ret.setBucket( bucketRef.getBucket() );
            ret.setPrefix( safeOptions.getPrefix() == null ? bucketRef.getPrefix() : safeOptions.getPrefix() );
            List<ObjectEntry> objects = new ArrayList<>();
            for ( Result<Item> result : results ) {
                objects.add( this.toEntry( bucketRef.getBucket(), result.get() ) );
            }
            ret.setObjects( objects );
            return ret;
        }
        catch ( Exception ex ) {
            throw new RedShuttleEngineException( "Failed to list red objects: " + bucketRef.getBucket(), ex );
        }
    }

    protected MinioClient client( String szEndpoint ) {
        return this.clients.computeIfAbsent( szEndpoint, this::createClient );
    }

    protected MinioClient createClient( String szEndpoint ) {
        return MinioClient.builder()
                .endpoint( this.normalizeEndpoint( szEndpoint ) )
                .credentials( this.config.getAccessKey(), this.config.getSecretKey() )
                .build();
    }

    protected String normalizeEndpoint( String szEndpoint ) {
        if ( szEndpoint == null || szEndpoint.isBlank() ) {
            return szEndpoint;
        }
        if ( szEndpoint.startsWith( RedSchemes.Http + "://" ) || szEndpoint.startsWith( RedSchemes.Https + "://" ) ) {
            return szEndpoint;
        }
        if ( this.config.isSecure() ) {
            return RedSchemes.Https + "://" + szEndpoint;
        }
        return RedSchemes.Http + "://" + szEndpoint;
    }

    protected ObjectMetadata toMetadata( StatObjectResponse response ) {
        ObjectMetadata ret = new ObjectMetadata();
        ret.setBucket( response.bucket() );
        ret.setKey( response.object() );
        ret.setName( this.name( response.object() ) );
        ret.setSize( response.size() );
        ret.setEtag( response.etag() );
        ret.setContentType( response.contentType() );
        ret.setType( ResourceType.Object );
        if ( response.lastModified() != null ) {
            ret.setLastModified( LocalDateTime.ofInstant(
                    response.lastModified().toInstant(),
                    ZoneId.systemDefault()
            ) );
        }
        return ret;
    }

    protected ObjectEntry toEntry( String szBucket, Item item ) {
        ObjectEntry ret = new ObjectEntry();
        ret.setBucket( szBucket );
        ret.setKey( item.objectName() );
        ret.setName( this.name( item.objectName() ) );
        ret.setSize( item.size() );
        ret.setEtag( item.etag() );
        ret.setType( item.isDir() ? ResourceType.Directory : ResourceType.Object );
        if ( item.lastModified() != null ) {
            ret.setLastModified( LocalDateTime.ofInstant(
                    item.lastModified().toInstant(),
                    ZoneId.systemDefault()
            ) );
        }
        return ret;
    }

    protected String name( String szKey ) {
        if ( szKey == null || szKey.isBlank() ) {
            return "";
        }
        String szBody = szKey;
        while ( szBody.endsWith( "/" ) ) {
            szBody = szBody.substring( 0, szBody.length() - 1 );
        }
        int nSlash = szBody.lastIndexOf( '/' );
        return nSlash < 0 ? szBody : szBody.substring( nSlash + 1 );
    }
}
