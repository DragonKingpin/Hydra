package com.pinecone.hydra.file.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.BucketPathCacheManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTreeConstants;
import com.pinecone.hydra.unit.imperium.entity.BucketCachePathBinding;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.hydra.util.UniformHashing;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FilePathCacheMapper extends TriePathCacheManipulator, BucketPathCacheManipulator {
    @Override
    default void insert( GUID guid, String path ) {
        this.insertCachePathAtomically( guid, path );
    }

    @Override
    default void insertLongPath( GUID guid, String path, String longPath ) {
        this.insertCachePathAtomically( guid, this.joinPathParts( path, longPath ) );
    }

    @Override
    void remove( GUID guid );

    @Override
    default String getPath( GUID guid ) {
        BucketCachePathBinding binding = this.getPathBinding( guid );
        if ( binding == null ) {
            return null;
        }
        return binding.getResolvedPath();
    }

    default BucketCachePathBinding getPathBinding( GUID guid ) {
        List<BucketCachePathBinding> bindings = this.listByGuid( guid );
        if ( bindings == null || bindings.isEmpty() ) {
            return null;
        }
        return bindings.get( 0 );
    }

    default String getLongPath( GUID guid ) {
        return null;
    }

    default String getPath0( GUID guid ) {
        return this.getPath( guid );
    }

    @Override
    default GUID getNode( String path ) {
        return this.queryGUIDByPath( path );
    }

    @Override
    default GUID queryGUIDByBucketPath( GUID bucketGuid, String path ) {
        return this.queryGUIDByBucketPathAtomically( bucketGuid, path );
    }

    default GUID queryGUIDByBucketPathAtomically( GUID bucketGuid, String path ) {
        if ( bucketGuid == null || path == null ) {
            return null;
        }

        String pathHash = UniformHashing.sha256Hex( path );
        List<BucketCachePathBinding> bindings = this.listByBucketAndPathHash( bucketGuid, pathHash );
        if ( bindings == null || bindings.isEmpty() ) {
            return null;
        }

        for ( BucketCachePathBinding binding : bindings ) {
            if ( path.equals( binding.getResolvedPath() ) ) {
                return binding.getGuid();
            }
        }
        return null;
    }

    @Override
    default GUID queryGUIDByPath( String path ) {
        return this.queryGUIDByPathAtomically( path );
    }

    @Override
    default GUID queryGUIDByPathAtomically( String path ) {
        if ( path == null ) {
            return null;
        }

        String pathHash = UniformHashing.sha256Hex( path );
        List<BucketCachePathBinding> bindings = this.listByPathHash( pathHash );
        if ( bindings == null || bindings.isEmpty() ) {
            return null;
        }

        for ( BucketCachePathBinding binding : bindings ) {
            if ( path.equals( binding.getResolvedPath() ) ) {
                return binding.getGuid();
            }
        }
        return null;
    }

    @Override
    default void insertCachePathAtomically( GUID guid, String path ) {
        if ( guid == null || path == null ) {
            return;
        }

        GUID bucketGuid = this.getBucketGuidByGuid( guid );
        if ( bucketGuid == null ) {
            return;
        }

        this.upsertCachePathAtomically( bucketGuid, guid, path );
    }

    @Override
    default void insertLongCachePathAtomically( GUID guid, String path, String longPath ) {
        this.insertCachePathAtomically( guid, this.joinPathParts( path, longPath ) );
    }

    default void upsertCachePathAtomically( GUID bucketGuid, GUID guid, String path ) {
        String pathHash = UniformHashing.sha256Hex( path );
        RuntimeException lastException = null;
        for ( int i = 0; i < ImperialTreeConstants.AtomicPathCacheInsertRetryLimit; ++i ) {
            List<BucketCachePathBinding> bindings = this.listByBucketAndPathHashForUpdate( bucketGuid, pathHash );
            int nextSlot = 0;
            if ( bindings != null ) {
                for ( BucketCachePathBinding binding : bindings ) {
                    Integer hashSlot = binding.getHashSlot();
                    if ( hashSlot != null && hashSlot >= nextSlot ) {
                        nextSlot = hashSlot + 1;
                    }
                    if ( path.equals( binding.getResolvedPath() ) ) {
                        this.updateHashed( binding.getId(), bucketGuid, guid, pathHash, binding.getHashSlot(), path );
                        return;
                    }
                }
            }

            try {
                this.insertHashed( bucketGuid, guid, pathHash, nextSlot, path );
                return;
            } catch ( RuntimeException exception ) {
                lastException = exception;
            }
        }

        if ( lastException != null ) {
            throw lastException;
        }
    }

    GUID getBucketGuidByGuid( @Param("guid") GUID guid );

    List<BucketCachePathBinding> listByPathHash( @Param("pathHash") String pathHash );

    List<BucketCachePathBinding> listByBucketAndPathHash(
            @Param("bucketGuid") GUID bucketGuid, @Param("pathHash") String pathHash
    );

    List<BucketCachePathBinding> listByBucketAndPathHashForUpdate(
            @Param("bucketGuid") GUID bucketGuid, @Param("pathHash") String pathHash
    );

    List<BucketCachePathBinding> listByGuid( @Param("guid") GUID guid );

    void insertHashed(
            @Param("bucketGuid") GUID bucketGuid, @Param("guid") GUID guid,
            @Param("pathHash") String pathHash, @Param("hashSlot") int hashSlot,
            @Param("path") String path
    );

    void updateHashed(
            @Param("id") Long id, @Param("bucketGuid") GUID bucketGuid, @Param("guid") GUID guid,
            @Param("pathHash") String pathHash, @Param("hashSlot") Integer hashSlot,
            @Param("path") String path
    );

    @Override
    long countPathCacheByBucketGuid( @Param("bucketGuid") GUID bucketGuid );

    @Override
    void deletePathCacheByBucketGuid( @Param("bucketGuid") GUID bucketGuid );

    default String joinPathParts( String path, String longPath ) {
        if ( path == null ) {
            return longPath;
        }
        if ( longPath == null ) {
            return path;
        }
        return path + longPath;
    }
}
