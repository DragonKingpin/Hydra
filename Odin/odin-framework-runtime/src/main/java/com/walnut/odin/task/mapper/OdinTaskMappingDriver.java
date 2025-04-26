package com.walnut.odin.task.mapper;

import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;

public interface OdinTaskMappingDriver extends KOIMappingDriver {

    KOIMappingDriver getParentDriver();

}
