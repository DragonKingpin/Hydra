package com.pinecone.hydra.registry;

import java.util.List;
import java.util.Stack;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;

public class StandardPathSelector implements PathSelector {
    protected PathResolver                    pathResolver;
    protected DistributedTrieTree             distributedTrieTree;
    protected GUIDNameManipulator             dirManipulator;
    protected GUIDNameManipulator[]           fileManipulators;

    public StandardPathSelector( PathResolver pathResolver, DistributedTrieTree trieTree, GUIDNameManipulator dirMan, GUIDNameManipulator[] fileMans ) {
        this.pathResolver        = pathResolver;
        this.distributedTrieTree = trieTree;
        this.dirManipulator      = dirMan;
        this.fileManipulators    = fileMans;
    }

    @Override
    public GUID searchGUID( String[] parts ) {
        return this.searchGUID( parts, null );
    }

    @Override
    public GUID searchGUID( String[] parts, @Nullable String[] lpResolvedPath ) {
        List<String > resolvedParts = this.pathResolver.resolvePath( parts );
        if( lpResolvedPath != null ) {
            lpResolvedPath[ 0 ] = this.pathResolver.assemblePath( resolvedParts );
        }
        //return dfsSearchGUID(fileMan, dirMan, resolvedParts, 0, null);
        return (GUID) this.dfsSearch( resolvedParts );
    }

    /** Iterative DFS, 迭代 DFS 法 **/
    protected Object dfsSearch( List<String > parts ) {
        Stack<SearchArgs > stack = new Stack<>();
        stack.push( new SearchArgs( null, 0 ) );

        while ( !stack.isEmpty() ) {
            SearchArgs currentArgs = stack.pop();
            int depth       = currentArgs.depth;
            GUID parentGuid = currentArgs.parentGuid;

            // If we've reached the last part, try to match the current part with all file manipulators
            // 如果是第一个部分，判断路径长度，来决定查询器的使用
            if ( depth == parts.size() ) {
                continue;
            }

            String currentPart = parts.get( depth );
            List<GUID > guids;

            if ( depth == 0 ) {
                if ( parts.size() > 1 ) {
                    // Case1: If more than one part, first part can only be a directory.
                    guids = this.searchDirAndLinksFirstCase( currentPart );
                }
                else {
                    // Case2: If there's only one part, it could be either file or directory.
                    // 只有一个部分，可能是文件或文件夹，查询所有操纵器. [且必须是Root]
                    guids = this.fetchAllGuidsRootCase( currentPart );
                }
            }
            else {
                // Case3: For middle and last parts, retrieve children GUIDs using distributedTrieTree
                guids = this.distributedTrieTree.getChildrenGuids( parentGuid );
            }

            if ( guids == null || guids.isEmpty() ) {
                continue;
            }

            for ( GUID guid : guids ) {
                if ( this.isGuidMatchingPartName( guid, currentPart, depth, parts.size() ) ) {
                    if ( depth == parts.size() - 1 ) {
                        return this.beforeDFSTermination( currentPart, guid );
                    }
                    stack.push( new SearchArgs( guid, depth + 1 ) );
                }
            }
        }

        return null;
    }

    /** Recursive DFS, 废弃递归 DFS 法，留着考古**/
    @Deprecated
    protected Object dfsSearch( List<String> parts, int depth, GUID parentGuid ) {
        String currentPart = parts.get(depth);
        List<GUID> guids;

        if ( depth == 0 ) {
            if ( parts.size() > 1 ) {
                // Case1: If more than one part, first part can only be a directory.
                guids = this.searchDirAndLinksFirstCase( currentPart );
            }
            else {
                // Case2: If there's only one part, it could be either file or directory.
                // 只有一个部分，可能是文件或文件夹，查询所有操纵器. [且必须是Root]
                guids = /*this.*/fetchAllGuidsRootCase( currentPart );
            }
        }
        else {
            // Case3: For middle and last parts, retrieve children GUIDs using distributedTrieTree
            guids = this.distributedTrieTree.getChildrenGuids( parentGuid );
        }

        if ( guids == null || guids.isEmpty() ) {
            return null;
        }

        // 索引法遍历所有可能的 GUID，并继续向下递归.
        // Indexing method traverses all possible GUIs and continues to recursively descend.
        for ( GUID guid : guids ) {
            // Using index to find.
            if ( this.isGuidMatchingPartName( guid, currentPart, depth, parts.size() ) ) {
                if ( depth == parts.size() - 1 ) {
                    return this.beforeDFSTermination( currentPart, guid );
                }

                Object result = this.dfsSearch( parts, depth + 1, guid );
                if ( result != null ) {
                    return result;
                }
            }
        }

        return null;
    }

    protected Object beforeDFSTermination( String currentPart, GUID guid ) {
        return guid;
    }

    protected boolean checkPartInAllManipulators( GUID guid, String partName ) {
        for ( GUIDNameManipulator manipulator : this.fileManipulators ) {
            List<GUID > guids = manipulator.getGuidsByNameID( partName, guid );
            if ( guids != null && !guids.isEmpty() ) {
                return true;
            }
        }

        List<GUID > guids = this.searchDirAndLinks( guid, partName );
        return guids != null && !guids.isEmpty();
    }

    protected boolean isGuidMatchingPartName( GUID guid, String partName, int depth, int nParts ) {
        // 在中间部分只匹配文件夹，最后一部分匹配文件和文件夹
        // In the last part, check both files and directories

        if ( depth == nParts - 1 ) {
            return this.checkPartInAllManipulators( guid, partName );
        }
        else {
            // Middle part: Directory only.
            //List<GUID > guids = this.dirManipulator.getGuidsByNameID( partName, guid );
            List<GUID > guids = this.searchDirAndLinks( guid, partName );
            return guids != null && !guids.isEmpty();
        }
    }

    protected List<GUID > searchDirAndLinks ( GUID guid, String partName ) {
        List<GUID > guids = this.dirManipulator.getGuidsByNameID( partName, guid );
        if( guids != null && !guids.isEmpty() ) {
            return guids;
        }

        GUID linkGuid = this.distributedTrieTree.getOriginalGuidByNodeGuid( partName, guid );
        if( linkGuid != null ) {
            return List.of( linkGuid );
        }
        return null;
    }

    protected List<GUID > searchDirAndLinksFirstCase ( String partName ) {
        List<GUID > guids = this.dirManipulator.getGuidsByName( partName );
        if( guids != null && !guids.isEmpty() ) {
            return guids;
        }

        return this.distributedTrieTree.fetchOriginalGuidRoot( partName );
    }

    protected List<GUID > fetchAllGuidsRootCase( String partName ) {
        List<GUID > guids = this.dirManipulator.getGuidsByName( partName );
        guids.removeIf( guid -> !this.distributedTrieTree.isRoot( guid ) );

        // Notice: Critical error, querying root element should checks if it is the root.
        for ( GUIDNameManipulator manipulator : this.fileManipulators ) {
            List<GUID > gs = manipulator.getGuidsByName( partName );
            for( GUID guid : gs ) {
                if( this.distributedTrieTree.isRoot( guid ) ) {
                    guids.add( guid );
                }
            }
        }

        guids.addAll( this.distributedTrieTree.fetchOriginalGuidRoot( partName ) );

        return guids;
    }

    static class SearchArgs {
        GUID parentGuid;
        int  depth;

        SearchArgs( GUID parentGuid, int depth ) {
            this.parentGuid = parentGuid;
            this.depth      = depth;
        }
    }
}
