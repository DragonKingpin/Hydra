package com.walnut.odin.task.mapper;

import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import com.walnut.odin.mapper.transaction.OdinTransactionalMappingDriver;

public interface OdinTaskMappingDriver extends KOIMappingDriver, OdinTransactionalMappingDriver {

    KOIMappingDriver getParentDriver();

}
