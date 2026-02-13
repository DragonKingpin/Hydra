package com.pinecone.hydra.umb.wolf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.pinecone.framework.lang.field.FieldEntity;
import com.pinecone.hydra.umb.broadcast.BroadcastControlNode;
import com.pinecone.hydra.umct.husky.compiler.CompilerEncoder;
import com.pinecone.hydra.umct.husky.compiler.DynamicMethodPrototype;
import com.pinecone.hydra.umct.husky.compiler.MethodPrototype;
import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.husky.machinery.PMCTContextMachinery;
import com.pinecone.ulf.util.protobuf.FieldProtobufDecoder;
import com.pinecone.ulf.util.protobuf.FieldProtobufEncoder;

public abstract class ArchUlfBroadcastControlAgent extends ArchBroadcastControlAgent implements UlfBroadcastControlAgent {

    public ArchUlfBroadcastControlAgent( BroadcastControlNode controlNode ) {
        super( controlNode );
    }

    @Override
    public ProtoInterfacialCompiler getInterfacialCompiler() {
        return this.broadcastControlNode().getInterfacialCompiler();
    }

    @Override
    public PMCTContextMachinery getMCTTransformer() {
        return (PMCTContextMachinery) this.mMCTContextMachinery;
    }

    @Override
    public FieldProtobufEncoder getFieldProtobufEncoder() {
        return this.broadcastControlNode().getFieldProtobufEncoder();
    }

    @Override
    public FieldProtobufDecoder getFieldProtobufDecoder() {
        return this.broadcastControlNode().getFieldProtobufDecoder();
    }

    @Override
    public UlfBroadcastControlNode broadcastControlNode() {
        return (UlfBroadcastControlNode) super.broadcastControlNode();
    }




    protected CompilerEncoder getCompilerEncoder() {
        return this.getInterfacialCompiler().getCompilerEncoder();
    }

    protected DynamicMessage reinterpretMsg(MethodPrototype prototype, Object[] args ) {
        FieldProtobufEncoder encoder = this.getFieldProtobufEncoder();
        Descriptors.Descriptor descriptor = prototype.getArgumentsDescriptor();

        FieldEntity[] types = prototype.getArgumentTemplate().getSegments();
        for ( int i = 0; i < args.length; ++i ) {
            Object v = args [ i ]; // Fuck duplicated codes.
            types[ i + 1 ].setValue( v );
        }

        return encoder.encode(
                descriptor, types, this.getCompilerEncoder().getExceptedKeys(), this.getCompilerEncoder().getOptions()
        );
    }

    protected DynamicMethodPrototype queryMethodPrototype(String szMethodAddress ) {
        DynamicMethodPrototype method = (DynamicMethodPrototype) this.queryMethodDigest( szMethodAddress );
        if ( method == null ) {
            throw new IllegalArgumentException( "Method address: `" + szMethodAddress + "` is invalid." );
        }

        return method;
    }

}
