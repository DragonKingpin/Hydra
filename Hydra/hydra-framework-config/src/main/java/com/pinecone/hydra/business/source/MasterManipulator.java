package com.pinecone.hydra.business.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface MasterManipulator extends KOIMasterManipulator, Pinenut {

    NodeManipulator getNodeManipulator();

    TreeManipulator getTreeManipulator();

    PathManipulator getPathManipulator();

    ScenarioManipulator getScenarioManipulator();

    ProjectManipulator getProjectManipulator();

    IdeaManipulator getIdeaManipulator();
}
