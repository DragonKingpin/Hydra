package com.pinecone.hydra.business.ibatis.hydranium;

import java.util.Map;

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

    public MasterManipulatorImpl() {
    }

    public MasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( MasterManipulatorImpl.class, Map.of(), this );
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
