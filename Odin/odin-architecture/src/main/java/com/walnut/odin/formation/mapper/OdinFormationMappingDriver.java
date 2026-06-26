package com.walnut.odin.formation.mapper;

import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.walnut.odin.formation.source.MasterManipulator;
import com.walnut.odin.mapper.transaction.OdinTransactionalMappingDriver;

public interface OdinFormationMappingDriver extends KOIMappingDriver, OdinTransactionalMappingDriver {
    MasterManipulator getFormationMasterManipulator();
}
