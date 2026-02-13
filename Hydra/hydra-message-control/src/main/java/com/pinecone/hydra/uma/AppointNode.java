package com.pinecone.hydra.uma;

import com.pinecone.hydra.appoints.AppointNodus;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umct.UMCTNode;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;
import com.pinecone.hydra.umct.husky.compiler.MethodDigest;
import com.pinecone.hydra.umct.husky.machinery.MCTContextMachinery;

public interface AppointNode extends UMCTNode, AppointNodus {

    MessageNode getMessageNode();

    default long getMessageNodeId() {
        return getMessageNode().getMessageNodeId();
    }

    MCTContextMachinery getMCTTransformer();

    InterfacialCompiler getInterfacialCompiler();

    ClassDigest queryClassDigest( String name );

    MethodDigest queryMethodDigest( String name );

    void addClassDigest( ClassDigest that );

    void addMethodDigest( MethodDigest that );

    ClassDigest compile( Class<? > clazz, boolean bAsIface );

    void close();

}
