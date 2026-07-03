package com.pinecone.hydra.storage.file.transfer.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public interface UofsTransferMasterManipulator extends KOIMasterManipulator {
    UofsTransferTaskManipulator getTaskManipulator();

    UofsTransferItemManipulator getItemManipulator();
}
