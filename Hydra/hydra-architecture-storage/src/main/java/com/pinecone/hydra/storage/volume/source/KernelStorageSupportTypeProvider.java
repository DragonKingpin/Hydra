package com.pinecone.hydra.storage.volume.source;

import com.pinecone.hydra.storage.volume.core.BuiltinStorageSupportType;
import com.pinecone.hydra.storage.volume.core.StorageSupportDescriptor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class KernelStorageSupportTypeProvider implements StorageSupportTypeProvider {
    @Override
    public List<StorageSupportDescriptor> listBuiltinSupportTypes() {
        return Arrays.stream( BuiltinStorageSupportType.values() )
                .map( BuiltinStorageSupportType::toDescriptor )
                .collect( Collectors.toList() );
    }

    @Override
    public StorageSupportDescriptor affirmBuiltinSupportType( String code ) {
        return BuiltinStorageSupportType.find( code )
                .map( BuiltinStorageSupportType::toDescriptor )
                .orElseThrow( () -> new IllegalArgumentException( "Builtin storage support type not found: " + code ) );
    }
}
