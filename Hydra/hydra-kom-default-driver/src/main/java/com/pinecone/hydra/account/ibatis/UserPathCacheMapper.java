package com.pinecone.hydra.account.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.ImperialTreeConstants;
import com.pinecone.hydra.unit.imperium.PathCacheAtomicity;
import com.pinecone.hydra.unit.imperium.entity.CachePathBinding;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.hydra.util.UniformHashing;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@IbatisDataAccessObject
public interface UserPathCacheMapper extends TriePathCacheManipulator {
    @Override
    default void insert( GUID guid, String path ) {
        this.upsertCachePathAtomically( guid, path );
    }

    @Override
    default void insertLongPath( GUID guid, String path, String longPath ) {
        this.upsertCachePathAtomically( guid, this.joinPathParts( path, longPath ) );
    }

    @Override
    void remove( @Param("guid") GUID guid );

    @Override
    default String getPath( GUID guid ) {
        CachePathBinding binding = this.getPath0( guid );
        if ( binding == null ) {
            return null;
        }
        return binding.getResolvedPath();
    }

    @Override
    default GUID getNode( String path ) {
        return this.queryGUIDByPath( path );
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
        this.upsertCachePathAtomically( guid, path );
    }

    @Override
    default void insertLongCachePathAtomically( GUID guid, String path, String longPath ) {
        this.upsertCachePathAtomically( guid, this.joinPathParts( path, longPath ) );
    }

    default void upsertCachePathAtomically( GUID guid, String path ) {
        if ( guid == null || path == null ) {
            return;
        }

        String pathHash = UniformHashing.sha256Hex( path );
        for ( int i = 0; i < ImperialTreeConstants.AtomicPathCacheInsertRetryLimit; ++i ) {
            List<CachePathBinding> bindings = this.listByPathHashForUpdate( pathHash );
            int nextSlot = 0;
            if ( bindings != null ) {
                for ( CachePathBinding binding : bindings ) {
                    Integer hashSlot = binding.getHashSlot();
                    if ( hashSlot != null && hashSlot >= nextSlot ) {
                        nextSlot = hashSlot + 1;
                    }
                    if ( path.equals( binding.getResolvedPath() ) ) {
                        if ( PathCacheAtomicity.sameGuid( binding.getGuid(), guid ) ) {
                            return;
                        }
                        throw PathCacheAtomicity.pathConflict( "account", binding.getGuid(), guid, path );
                    }
                }
            }

            try {
                this.insertHashed( guid, pathHash, nextSlot, path );
                return;
            }
            catch ( RuntimeException exception ) {
                if ( !PathCacheAtomicity.isDuplicateKey( exception ) ) {
                    throw exception;
                }
            }
        }

        throw PathCacheAtomicity.insertExhausted( "account", path );
    }

    default CachePathBinding getPath0( GUID guid ) {
        List<CachePathBinding> bindings = this.listByGuid( guid );
        if ( bindings == null || bindings.isEmpty() ) {
            return null;
        }
        return bindings.get( 0 );
    }

    List<CachePathBinding> listByPathHash( @Param("pathHash") String pathHash );

    List<CachePathBinding> listByPathHashForUpdate( @Param("pathHash") String pathHash );

    List<CachePathBinding> listByGuid( @Param("guid") GUID guid );

    void insertHashed(
            @Param("guid") GUID guid, @Param("pathHash") String pathHash, @Param("hashSlot") int hashSlot,
            @Param("path") String path
    );

    void updateHashed(
            @Param("id") Long id, @Param("guid") GUID guid,
            @Param("pathHash") String pathHash, @Param("hashSlot") Integer hashSlot,
            @Param("path") String path
    );

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
