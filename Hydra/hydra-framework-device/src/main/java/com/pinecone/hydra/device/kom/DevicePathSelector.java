package com.pinecone.hydra.device.kom;

import com.pinecone.framework.util.name.path.PathResolver;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.system.ko.kom.StandardPathSelector;
import com.pinecone.hydra.unit.imperium.ImperialTree;

public class DevicePathSelector extends StandardPathSelector {
    public DevicePathSelector(PathResolver pathResolver, ImperialTree trieTree, GUIDNameManipulator dirMan, GUIDNameManipulator[] fileMans ) {
        super( pathResolver, trieTree, dirMan, fileMans );
    }
}
