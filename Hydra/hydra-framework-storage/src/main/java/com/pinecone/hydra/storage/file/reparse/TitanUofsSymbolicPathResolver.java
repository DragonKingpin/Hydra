package com.pinecone.hydra.storage.file.reparse;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.storage.file.entity.Symbolic;
import com.pinecone.hydra.storage.file.source.SymbolicManipulator;
import com.pinecone.hydra.unit.imperium.ImperialTree;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class TitanUofsSymbolicPathResolver implements UofsSymbolicPathResolver {
    protected PathResolver              mPathResolver;
    protected ImperialTree              mImperialTree;
    protected SymbolicManipulator       mSymbolicManipulator;
    protected UofsSymbolicResolveConfig mConfig;
    protected String                    mszPathSeparator;

    public TitanUofsSymbolicPathResolver(
            PathResolver pathResolver,
            ImperialTree imperialTree,
            SymbolicManipulator symbolicManipulator,
            String pathSeparator,
            UofsSymbolicResolveConfig config
    ) {
        this.mPathResolver        = pathResolver;
        this.mImperialTree        = imperialTree;
        this.mSymbolicManipulator = symbolicManipulator;
        this.mszPathSeparator     = pathSeparator;
        this.mConfig              = config;
    }

    @Override
    public UofsSymbolicResolveResult resolve( String path, Function<String, GUID> directPathResolver ) {
        String originalPath = path;
        String currentPath = this.normalize(path);
        Set<GUID> visitedSymbolicGuids = new HashSet<>();
        int maxDepth = Math.max(0, this.mConfig.getMaxDepth());

        for ( int depth = 0; depth < maxDepth; ++depth ) {
            SymbolicHit hit = this.findFirstSymbolicHit(currentPath, directPathResolver);
            if ( hit == null ) {
                GUID guid = directPathResolver.apply(currentPath);
                return new UofsSymbolicResolveResult(!currentPath.equals(originalPath), originalPath, currentPath, guid);
            }

            if ( !visitedSymbolicGuids.add(hit.symbolicGuid) ) {
                throw new ReparseLoopException(hit.symbolicGuid, currentPath);
            }

            Symbolic symbolic = this.mSymbolicManipulator.getSymbolicByGuid(hit.symbolicGuid);
            String reparsePoint = symbolic == null ? null : symbolic.getReparsedPoint();
            if ( reparsePoint == null || reparsePoint.isEmpty() ) {
                throw new ReparseException("UOFS symbolic reparse point is empty, symbolicGuid=" + hit.symbolicGuid);
            }

            currentPath = this.joinPath(reparsePoint, hit.remainingParts);
            GUID guid = directPathResolver.apply(currentPath);
            if ( guid != null ) {
                return new UofsSymbolicResolveResult(true, originalPath, currentPath, guid);
            }
        }

        throw new ReparseDepthExceededException(maxDepth, currentPath);
    }

    protected SymbolicHit findFirstSymbolicHit( String path, Function<String, GUID> directPathResolver ) {
        List<String> parts = this.mPathResolver.resolvePathParts(path);
        GUID parentGuid = null;

        for ( int i = 0; i < parts.size(); ++i ) {
            String part = parts.get(i);
            GUID symbolicGuid = parentGuid == null
                    ? this.findRootSymbolic(part)
                    : this.findChildSymbolic(parentGuid, part);
            if ( symbolicGuid != null ) {
                return new SymbolicHit(symbolicGuid, parts.subList(i + 1, parts.size()));
            }

            String prefixPath = this.mPathResolver.assemblePath(parts.subList(0, i + 1));
            parentGuid = directPathResolver.apply(prefixPath);
            if ( parentGuid == null ) {
                return null;
            }
        }

        return null;
    }

    protected GUID findRootSymbolic( String name ) {
        List<GUID> guids = this.mSymbolicManipulator.getGuidsByName(name);
        if ( guids == null ) {
            return null;
        }
        for ( GUID guid : guids ) {
            if ( this.mImperialTree.isRoot(guid) ) {
                return guid;
            }
        }
        return null;
    }

    protected GUID findChildSymbolic( GUID parentGuid, String name ) {
        List<GUID> childGuids = this.mImperialTree.fetchChildrenGuids(parentGuid);
        if ( childGuids == null ) {
            return null;
        }
        for ( GUID childGuid : childGuids ) {
            if ( this.mSymbolicManipulator.isSymbolicMatchedByNameGuid(name, childGuid) ) {
                return childGuid;
            }
        }
        return null;
    }

    protected String normalize( String path ) {
        return this.mPathResolver.assemblePath(this.mPathResolver.resolvePathParts(path));
    }

    protected String joinPath( String reparsePoint, List<String> remainingParts ) {
        String resolvedReparsePoint = this.normalize(reparsePoint);
        if ( remainingParts == null || remainingParts.isEmpty() ) {
            return resolvedReparsePoint;
        }
        return this.normalize(resolvedReparsePoint + this.mszPathSeparator + this.mPathResolver.assemblePath(remainingParts));
    }

    protected static class SymbolicHit {
        GUID symbolicGuid;
        List<String> remainingParts;

        SymbolicHit( GUID symbolicGuid, List<String> remainingParts ) {
            this.symbolicGuid = symbolicGuid;
            this.remainingParts = remainingParts;
        }
    }
}
