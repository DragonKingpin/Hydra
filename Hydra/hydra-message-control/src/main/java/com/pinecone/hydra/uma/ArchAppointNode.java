package com.pinecone.hydra.uma;

import com.pinecone.framework.system.GenericMasterTaskManager;
import com.pinecone.framework.system.executum.ArchProcessum;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.servgram.ArchServgramium;
import com.pinecone.hydra.servgram.Servgram;
import com.pinecone.hydra.servgram.Servgramium;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umct.ServiceException;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;
import com.pinecone.hydra.umct.husky.compiler.MethodDigest;
import com.pinecone.hydra.umct.husky.machinery.MCTContextMachinery;


public abstract class ArchAppointNode extends ArchServgramium implements AppointNode {
    protected MCTContextMachinery mMCTContextMachinery;

    protected ArchAppointNode( Servgramium sharded ) {
        super( sharded, true );
        this.mAffiliateThread       = sharded.getAffiliateThread();
    }

    protected ArchAppointNode( Servgramium sharded, MCTContextMachinery machinery ) {
        this( sharded );
        this.mMCTContextMachinery = machinery;
    }

    public abstract MessageNode getMessageNode();

    @Override
    public InterfacialCompiler getInterfacialCompiler() {
        return this.mMCTContextMachinery.getInterfacialCompiler();
    }

    @Override
    public MCTContextMachinery getMCTTransformer() {
        return this.mMCTContextMachinery;
    }


    @Override
    public Thread getAffiliateThread() {
        return this.getMessageNode().getAffiliateThread();
    }

    @Override
    public ArchProcessum setThreadAffinity( Thread affinity ) {
        this.getMessageNode().setThreadAffinity( affinity );
        return super.setThreadAffinity(affinity);
    }

    @Override
    public boolean isTerminated() {
        return this.getMessageNode().isTerminated();
    }

    @Override
    public void interrupt() {
        this.getMessageNode().interrupt();
    }

    @Override
    public void kill() {
        this.getMessageNode().kill();
    }

    @Override
    public Processum parentExecutum() {
        return (Processum) this.getMessageNode().parentExecutum();
    }

    @Override
    public void apoptosis() {
        this.getMessageNode().apoptosis();
    }

    @Override
    public GenericMasterTaskManager getTaskManager() {
        return (GenericMasterTaskManager) this.getMessageNode().getTaskManager();
    }

    @Override
    public void execute() throws ServiceException {
        try{
            ( (Servgram) this.getMessageNode() ).execute();
        }
        catch ( Exception e ) {
            throw new ServiceException( e );
        }
    }



    @Override
    public ClassDigest queryClassDigest( String name ) {
        return this.mMCTContextMachinery.queryClassDigest( name );
    }

    @Override
    public MethodDigest queryMethodDigest( String name ) {
        return this.mMCTContextMachinery.queryMethodDigest( name );
    }

    @Override
    public void addClassDigest( ClassDigest that ) {
        this.mMCTContextMachinery.addClassDigest( that );
    }

    @Override
    public void addMethodDigest( MethodDigest that ) {
        this.mMCTContextMachinery.addMethodDigest( that );
    }

    @Override
    public ClassDigest compile( Class<? > clazz, boolean bAsIface ) {
        return this.mMCTContextMachinery.compile( clazz, bAsIface );
    }

}
