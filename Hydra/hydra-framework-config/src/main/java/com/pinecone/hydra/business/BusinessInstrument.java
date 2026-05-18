package com.pinecone.hydra.business;

import java.util.Collection;
import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.ElementNode;
import com.pinecone.hydra.business.entity.IdeaElement;
import com.pinecone.hydra.business.entity.ProjectElement;
import com.pinecone.hydra.business.entity.ScenarioElement;
import com.pinecone.hydra.system.ko.kom.KOMInstrument;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface BusinessInstrument extends KOMInstrument {

    BusinessConfig KERNEL_BUSINESS_CONFIG = new KernelBusinessConfig();

    ScenarioElement affirmScenarioElement( String szPath );

    ProjectElement affirmProjectElement( String szPath );

    IdeaElement affirmIdeaElement( String szPath );

    ScenarioElement createScenarioElement( ScenarioElement scenarioElement, GUID parentGuid );

    ProjectElement createProjectElement( ProjectElement projectElement, GUID parentGuid );

    IdeaElement createIdeaElement( IdeaElement ideaElement, GUID parentGuid );

    default ScenarioElement affirmScenario( String szPath ) {
        return this.affirmScenarioElement( szPath );
    }

    default ProjectElement affirmProject( String szPath ) {
        return this.affirmProjectElement( szPath );
    }

    default IdeaElement affirmIdea( String szPath ) {
        return this.affirmIdeaElement( szPath );
    }

    void addChild( GUID parentGuid, ElementNode child );

    boolean containsChild( GUID parentGuid, String szChildName );

    void update( TreeNode treeNode );

    @Override
    ElementNode get( GUID guid );

    ElementNode queryElement( String szPath );

    Collection<ElementNode > fetchChildren( GUID parentGuid );

    GUID fetchParentGuid( GUID guid );

    @Override
    List<GUID > fetchChildrenGuids( GUID parentGuid );

    String getPath( GUID guid );

    default GUID queryGuidByPath( String szPath ) {
        return this.queryGUIDByPath( szPath );
    }
}
