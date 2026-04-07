package com.pinecone.hydra.umb.wolf;

import com.pinecone.hydra.umb.broadcast.BroadcastControlAgent;
import com.pinecone.hydra.umb.broadcast.BroadcastControlNode;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;
import com.pinecone.hydra.umct.husky.compiler.MethodDigest;
import com.pinecone.hydra.umct.husky.machinery.MCTContextMachinery;

public abstract class ArchBroadcastControlAgent implements BroadcastControlAgent {
    protected MCTContextMachinery mMCTContextMachinery;

    protected BroadcastControlNode mBroadcastControlNode;

    public ArchBroadcastControlAgent( BroadcastControlNode controlNode ) {
        this.mBroadcastControlNode = controlNode;
        this.mMCTContextMachinery = controlNode.getMCTTransformer();
    }


    @Override
    public InterfacialCompiler getInterfacialCompiler() {
        return this.mBroadcastControlNode.getInterfacialCompiler();
    }

    @Override
    public MCTContextMachinery getMCTTransformer() {
        return this.mMCTContextMachinery;
    }

    @Override
    public BroadcastControlNode broadcastControlNode() {
        return this.mBroadcastControlNode;
    }

    @Override
    public ClassDigest queryClassDigest( String name ) {
        return this.mBroadcastControlNode.queryClassDigest( name );
    }

    @Override
    public MethodDigest queryMethodDigest( String name ) {
        return this.mBroadcastControlNode.queryMethodDigest( name );
    }

    @Override
    public void addClassDigest( ClassDigest that ) {
        this.mBroadcastControlNode.addClassDigest( that );
    }

    @Override
    public void addMethodDigest( MethodDigest that ) {
        this.mBroadcastControlNode.addMethodDigest( that );
    }

    @Override
    public ClassDigest compile( Class<? > clazz, boolean bAsIface ) {
        return this.mBroadcastControlNode.compile( clazz, bAsIface );
    }

}
