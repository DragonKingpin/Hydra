package com.pinecone.hydra.business.ibatis.hydranium;

import java.util.Map;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.ElementNode;
import com.pinecone.hydra.business.entity.GenericElementNode;
import com.pinecone.hydra.business.entity.tag.NodeTag;
import com.pinecone.hydra.business.ibatis.tag.NodeTagMapper;
import com.pinecone.hydra.business.source.tag.NodeTagManipulator;
import com.pinecone.hydra.business.support.tag.BusinessTags;
import com.pinecone.slime.jelly.source.ibatis.IbatisClient;
import com.pinecone.slime.jelly.source.ibatis.transaction.IbatisTransaction;

import javax.annotation.Resource;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.business.ibatis.IdeaMapper;
import com.pinecone.hydra.business.ibatis.NodeMapper;
import com.pinecone.hydra.business.ibatis.PathMapper;
import com.pinecone.hydra.business.ibatis.ProjectMapper;
import com.pinecone.hydra.business.ibatis.ScenarioMapper;
import com.pinecone.hydra.business.ibatis.TreeMapper;
import com.pinecone.hydra.business.source.IdeaManipulator;
import com.pinecone.hydra.business.source.MasterManipulator;
import com.pinecone.hydra.business.source.NodeManipulator;
import com.pinecone.hydra.business.source.PathManipulator;
import com.pinecone.hydra.business.source.ProjectManipulator;
import com.pinecone.hydra.business.source.ScenarioManipulator;
import com.pinecone.hydra.business.source.TreeManipulator;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.pinecone.hydra.system.ko.driver.KOISkeletonMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TriePathCacheManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;
import com.pinecone.hydra.unit.imperium.source.TreeMasterManipulator;
import org.springframework.stereotype.Component;

@Component
public class MasterManipulatorImpl implements MasterManipulator, TreeMasterManipulator {

    @Resource
    @Structure( type = NodeMapper.class )
    protected NodeManipulator mNodeManipulator;

    @Resource
    @Structure( type = TreeMapper.class )
    protected TreeManipulator mTreeManipulator;

    @Resource
    @Structure( type = PathMapper.class )
    protected PathManipulator mPathManipulator;

    @Resource
    @Structure( type = ScenarioMapper.class )
    protected ScenarioManipulator mScenarioManipulator;

    @Resource
    @Structure( type = ProjectMapper.class )
    protected ProjectManipulator mProjectManipulator;

    @Resource
    @Structure( type = IdeaMapper.class )
    protected IdeaManipulator mIdeaManipulator;

    protected BusinessTreeSkeleton mBusinessTreeSkeleton;

    @Resource
    @Structure( type = NodeTagMapper.class )
    protected NodeTagManipulator mNodeTagManipulator;

    protected IbatisClient mBusinessClient;

    public MasterManipulatorImpl() {
    }

    public MasterManipulatorImpl( KOIMappingDriver driver ) {
        this.mBusinessClient = ( (MappingDriver) driver ).businessClient();
        driver.autoConstruct( MasterManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public NodeTagManipulator getNodeTagManipulator() { return this.mNodeTagManipulator; }

    protected IbatisTransaction transaction() { return this.mBusinessClient.transaction(); }

    @Override
    public void replaceNodeTags( GUID guid, List<String> tags ) {
        List<String> normalized = BusinessTags.normalizeAll( tags );
        this.transaction().required( scope -> {
            NodeMapper nodes = scope.mapper( NodeMapper.class );
            if ( guid == null || nodes.getForUpdate( guid ) == null ) {
                throw new IllegalArgumentException( "Business node does not exist." );
            }
            NodeTagMapper mapper = scope.mapper( NodeTagMapper.class );
            Set<String> existing = mapper.fetchByNodeGuids( List.of( guid ) ).stream()
                    .map( NodeTag::getTag ).collect( Collectors.toSet() );
            Set<String> requested = new LinkedHashSet<>( normalized );
            if ( existing.equals( requested ) ) return null;
            for ( String tag : existing ) {
                if ( !requested.contains( tag ) ) mapper.removeTag( guid, tag );
            }
            for ( String tag : requested ) {
                if ( !existing.contains( tag ) ) mapper.insert( guid, tag );
            }
            nodes.touch( guid );
            return null;
        } );
    }

    @Override
    public void removeElement( GUID guid ) {
        this.transaction().required( scope -> {
            NodeMapper nodes = scope.mapper( NodeMapper.class );
            GenericElementNode node = nodes.getForUpdate( guid );
            if ( node == null ) return null;
            TreeMapper tree = scope.mapper( TreeMapper.class );
            if ( !tree.fetchChildrenGuids( guid ).isEmpty() ) {
                throw new IllegalArgumentException( "Cannot remove business node with children." );
            }
            scope.mapper( NodeTagMapper.class ).removeByNodeGuid( guid );
            Map<String, Runnable> removers = Map.of(
                    "Scenario", () -> scope.mapper( ScenarioMapper.class ).remove( guid ),
                    "Project", () -> scope.mapper( ProjectMapper.class ).remove( guid ),
                    "Idea", () -> scope.mapper( IdeaMapper.class ).remove( guid ) );
            Runnable removeTyped = removers.get( node.getType() );
            if ( removeTyped != null ) removeTyped.run();
            scope.mapper( PathMapper.class ).remove( guid );
            tree.remove( guid );
            nodes.remove( guid );
            return null;
        } );
    }

    @Override
    public void updateElement( ElementNode node ) {
        this.transaction().required( scope -> {
            NodeMapper nodes = scope.mapper( NodeMapper.class );
            if ( nodes.getForUpdate( node.getGuid() ) == null ) {
                throw new IllegalArgumentException( "Business node does not exist." );
            }
            nodes.update( node );
            if ( node.evinceScenarioElement() != null ) scope.mapper( ScenarioMapper.class ).update( node.evinceScenarioElement() );
            if ( node.evinceProjectElement() != null ) scope.mapper( ProjectMapper.class ).update( node.evinceProjectElement() );
            if ( node.evinceIdeaElement() != null ) scope.mapper( IdeaMapper.class ).update( node.evinceIdeaElement() );
            return null;
        } );
    }

    @Override
    public KOISkeletonMasterManipulator getSkeletonMasterManipulator() {
        return this;
    }

    @Override
    public NodeManipulator getNodeManipulator() {
        return this.mNodeManipulator;
    }

    @Override
    public TreeManipulator getTreeManipulator() {
        return this.mTreeManipulator;
    }

    @Override
    public PathManipulator getPathManipulator() {
        return this.mPathManipulator;
    }

    @Override
    public ScenarioManipulator getScenarioManipulator() {
        return this.mScenarioManipulator;
    }

    @Override
    public ProjectManipulator getProjectManipulator() {
        return this.mProjectManipulator;
    }

    @Override
    public IdeaManipulator getIdeaManipulator() {
        return this.mIdeaManipulator;
    }

    @Override
    public TireOwnerManipulator getTireOwnerManipulator() {
        return this.getBusinessTreeSkeleton();
    }

    @Override
    public TrieTreeManipulator getTrieTreeManipulator() {
        return this.getBusinessTreeSkeleton();
    }

    @Override
    public TriePathCacheManipulator getTriePathCacheManipulator() {
        return this.mPathManipulator;
    }

    protected BusinessTreeSkeleton getBusinessTreeSkeleton() {
        if ( this.mBusinessTreeSkeleton == null ) {
            this.mBusinessTreeSkeleton = new BusinessTreeSkeleton( this.mTreeManipulator );
        }

        return this.mBusinessTreeSkeleton;
    }
}
