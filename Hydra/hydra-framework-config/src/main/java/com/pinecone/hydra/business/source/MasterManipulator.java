package com.pinecone.hydra.business.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.ElementNode;
import com.pinecone.hydra.business.source.tag.NodeTagManipulator;
import java.util.List;

public interface MasterManipulator extends KOIMasterManipulator, Pinenut {

    NodeTagManipulator getNodeTagManipulator();

    void replaceNodeTags( GUID guid, List<String> tags );

    void removeElement( GUID guid );

    void updateElement( ElementNode node );

    NodeManipulator getNodeManipulator();

    TreeManipulator getTreeManipulator();

    PathManipulator getPathManipulator();

    ScenarioManipulator getScenarioManipulator();

    ProjectManipulator getProjectManipulator();

    IdeaManipulator getIdeaManipulator();
}
