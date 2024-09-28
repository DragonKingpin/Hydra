package com.pinecone.hydra.kernel.ibatis.hydranium;

import com.pinecone.framework.system.construction.Structure;
import com.pinecone.hydra.kernel.ibatis.KernelObjectMapper;
import com.pinecone.hydra.kernel.ibatis.KernelObjectMetaMapper;
import com.pinecone.hydra.kernel.source.KernelObjectManipulator;
import com.pinecone.hydra.kernel.source.KernelObjectMasterManipulator;
import com.pinecone.hydra.kernel.source.KernelObjectMetaManipulator;
import com.pinecone.hydra.registry.ibatis.RegistryNodePathMapper;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMasterManipulatorImpl;
import com.pinecone.hydra.registry.ibatis.hydranium.RegistryMasterTreeManipulatorImpl;
import com.pinecone.hydra.system.ko.driver.KOIMappingDriver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class KernelObjectMasterManipulatorImpl implements KernelObjectMasterManipulator {
    @Resource
    @Structure( type = KernelObjectMapper.class )
    KernelObjectManipulator         kernelObjectManipulator;
    @Resource
    @Structure( type = KernelObjectMetaMapper.class )
    KernelObjectMetaManipulator     kernelObjectMetaManipulator;

    public KernelObjectMasterManipulatorImpl() {

    }

    public KernelObjectMasterManipulatorImpl( KOIMappingDriver driver ) {
        driver.autoConstruct( RegistryMasterManipulatorImpl.class, Map.of(), this );
    }

    @Override
    public KernelObjectManipulator getKernelObjectManipulator() {
        return this.kernelObjectManipulator;
    }

    @Override
    public KernelObjectMetaManipulator getKernelObjectMetaManipulator() {
        return this.kernelObjectMetaManipulator;
    }
}
