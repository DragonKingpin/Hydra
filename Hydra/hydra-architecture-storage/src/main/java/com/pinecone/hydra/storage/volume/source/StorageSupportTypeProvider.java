package com.pinecone.hydra.storage.volume.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.volume.core.BuiltinStorageSupportType;
import com.pinecone.hydra.storage.volume.core.StorageSupportDescriptor;

import java.util.List;

public interface StorageSupportTypeProvider extends Pinenut {
    List<StorageSupportDescriptor> listBuiltinSupportTypes();

    StorageSupportDescriptor affirmBuiltinSupportType( String code );
}
