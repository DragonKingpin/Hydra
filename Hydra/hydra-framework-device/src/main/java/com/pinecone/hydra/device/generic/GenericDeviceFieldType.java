package com.pinecone.hydra.device.generic;

import com.pinecone.framework.system.prototype.Pinenut;

public enum GenericDeviceFieldType implements Pinenut {
    TEXT,
    LONG_TEXT,
    INTEGER,
    DECIMAL,
    BOOLEAN,
    DATE,
    DATETIME,
    SELECT,
    MULTI_SELECT,
    JSON
}
