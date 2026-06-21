package com.pinecone.hydra.task.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.entity.CachePathBinding;
import com.pinecone.hydra.unit.imperium.entity.CachePath;
import com.pinecone.hydra.unit.imperium.entity.GenericCachePath;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface TaskPathCacheMapper extends TriePathCacheManipulator {
    int MaxAtomicInsertRetries = 8;

    @Override
    void insert( @Param("guid") GUID guid, @Param("path") String path );

    @Override
    void insertLongPath( @Param("guid") GUID guid, @Param("path") String path, @Param("longPath") String longPath );

    @Override
    void remove( @Param("guid") GUID guid );


    @Override
    default String getPath( GUID guid ) {
        CachePath cachePath = this.getPath0( guid );
        if ( cachePath == null ) {
            return null;
        }
        return cachePath.getResolvedPath();
    }

    @Override
    default GUID getNode( String path ) {
        return this.queryGUIDByPath( path );
    }

    @Override
    default GUID queryGUIDByPath( String path ) {
        GUID guid = this.queryGUIDByPathAtomically( path );
        if ( guid != null ) {
            return guid;
        }

        List<GUID> legacyGuids = this.listLegacyGuidsByPath( path );
        if ( legacyGuids == null || legacyGuids.isEmpty() ) {
            return null;
        }
        return legacyGuids.get( 0 );
    }

    @Override
    default GUID queryGUIDByPathAtomically( String path ) {
        if ( path == null ) {
            return null;
        }

        String pathHash = TaskPathCacheMapper.sha256Hex( path );
        List<CachePathBinding> bindings = this.listByPathHash( pathHash );
        if ( bindings == null || bindings.isEmpty() ) {
            return null;
        }

        for ( CachePathBinding binding : bindings ) {
            if ( path.equals( binding.getResolvedPath() ) ) {
                return binding.getGuid();
            }
        }
        return null;
    }

    @Override
    default void insertCachePathAtomically( GUID guid, String path ) {
        this.upsertCachePathAtomically( guid, path, null );
    }

    @Override
    default void insertLongCachePathAtomically( GUID guid, String path, String longPath ) {
        this.upsertCachePathAtomically( guid, path, longPath );
    }

    default void upsertCachePathAtomically( GUID guid, String path, String longPath ) {
        if ( guid == null || path == null ) {
            return;
        }

        String fullPath = longPath == null ? path : path + longPath;
        String pathHash = TaskPathCacheMapper.sha256Hex( fullPath );
        RuntimeException lastException = null;
        for ( int i = 0; i < MaxAtomicInsertRetries; ++i ) {
            List<CachePathBinding> bindings = this.listByPathHashForUpdate( pathHash );
            int nextSlot = 0;
            if ( bindings != null ) {
                for ( CachePathBinding binding : bindings ) {
                    Integer hashSlot = binding.getHashSlot();
                    if ( hashSlot != null && hashSlot >= nextSlot ) {
                        nextSlot = hashSlot + 1;
                    }
                    if ( fullPath.equals( binding.getResolvedPath() ) ) {
                        this.updateHashed( binding.getId(), guid, pathHash, binding.getHashSlot(), path, longPath );
                        return;
                    }
                }
            }

            List<CachePathBinding> legacyBindings = this.listByPath( path );
            if ( legacyBindings != null ) {
                for ( CachePathBinding binding : legacyBindings ) {
                    if ( fullPath.equals( binding.getResolvedPath() ) ) {
                        try {
                            this.updateHashed( binding.getId(), guid, pathHash, nextSlot, path, longPath );
                            return;
                        } catch ( RuntimeException exception ) {
                            lastException = exception;
                        }
                    }
                }
            }

            try {
                this.insertHashed( guid, pathHash, nextSlot, path, longPath );
                return;
            } catch ( RuntimeException exception ) {
                lastException = exception;
            }
        }

        if ( lastException != null ) {
            throw lastException;
        }
    }

    default GenericCachePath getPath0( GUID guid ) {
        List<CachePathBinding> bindings = this.listByGuid( guid );
        if ( bindings == null || bindings.isEmpty() ) {
            return null;
        }
        return bindings.get( 0 );
    }

    List<CachePathBinding> listByPathHash( @Param("pathHash") String pathHash );

    List<CachePathBinding> listByPathHashForUpdate( @Param("pathHash") String pathHash );

    List<CachePathBinding> listByGuid( @Param("guid") GUID guid );

    List<CachePathBinding> listByPath( @Param("path") String path );

    List<GUID> listLegacyGuidsByPath( @Param("path") String path );

    void insertHashed(
            @Param("guid") GUID guid, @Param("pathHash") String pathHash, @Param("hashSlot") int hashSlot,
            @Param("path") String path, @Param("longPath") String longPath
    );

    void updateHashed(
            @Param("id") Long id, @Param("guid") GUID guid,
            @Param("pathHash") String pathHash, @Param("hashSlot") Integer hashSlot,
            @Param("path") String path, @Param("longPath") String longPath
    );

    static String sha256Hex( String text ) {
        try {
            MessageDigest digest = MessageDigest.getInstance( "SHA-256" );
            byte[] bytes = digest.digest( text.getBytes( StandardCharsets.UTF_8 ) );
            StringBuilder builder = new StringBuilder( bytes.length * 2 );
            for ( byte value : bytes ) {
                builder.append( String.format( "%02x", value & 0xff ) );
            }
            return builder.toString();
        } catch ( NoSuchAlgorithmException exception ) {
            throw new IllegalStateException( "SHA-256 algorithm is unavailable.", exception );
        }
    }
}
