package com.pinecone.hydra.uma;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pinecone.framework.lang.field.FieldEntity;
import com.pinecone.hydra.servgram.Servgramium;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umct.IlleagalResponseException;
import com.pinecone.hydra.umct.husky.compiler.CompilerEncoder;
import com.pinecone.hydra.umct.husky.compiler.DynamicMethodPrototype;
import com.pinecone.hydra.umct.husky.compiler.MethodPrototype;
import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.husky.machinery.MCTContextMachinery;
import com.pinecone.hydra.umct.husky.machinery.PMCTContextMachinery;
import com.pinecone.ulf.util.protobuf.FieldProtobufDecoder;
import com.pinecone.ulf.util.protobuf.FieldProtobufEncoder;

public abstract class ArchUlfAppointNode extends ArchAppointNode implements UlfAppointNode {
    protected ArchUlfAppointNode( Servgramium sharded ) {
        super( sharded );
    }

    protected ArchUlfAppointNode( Servgramium sharded, MCTContextMachinery machinery ) {
        super( sharded, machinery );
    }

    @Override
    public ProtoInterfacialCompiler getInterfacialCompiler() {
        return (ProtoInterfacialCompiler) super.getInterfacialCompiler();
    }

    @Override
    public PMCTContextMachinery getMCTTransformer() {
        return (PMCTContextMachinery) super.getMCTTransformer();
    }

    @Override
    public FieldProtobufEncoder getFieldProtobufEncoder() {
        return this.getMCTTransformer().getFieldProtobufEncoder();
    }

    @Override
    public FieldProtobufDecoder getFieldProtobufDecoder() {
        return this.getMCTTransformer().getFieldProtobufDecoder();
    }




    protected CompilerEncoder getCompilerEncoder() {
        return this.getInterfacialCompiler().getCompilerEncoder();
    }

    protected DynamicMessage reinterpretMsg(MethodPrototype prototype, Object[] args ) {
        FieldProtobufEncoder encoder = this.getFieldProtobufEncoder();
        Descriptors.Descriptor descriptor = prototype.getArgumentsDescriptor();

        FieldEntity[] types = prototype.getArgumentTemplate().getSegments();
        for ( int i = 0; i < args.length; ++i ) {
            types[ i + 1 ].setValue( args [ i ] );
        }

        return encoder.encode(
                descriptor, types, this.getCompilerEncoder().getExceptedKeys(), this.getCompilerEncoder().getOptions()
        );
    }

    public Object unmarshalResponse( MethodPrototype digest, byte[] raw ) throws IlleagalResponseException {
        try{
            Descriptors.Descriptor retDes = digest.getReturnDescriptor();
            if ( retDes == null ) {
                // undefined response for `void` type-return.
                if ( digest.getReturnType() == void.class || digest.getReturnType() == Void.class ) {
                    return null;
                }

                throw new IlleagalResponseException( "Illegal undefined return type, what => " + digest.getReturnType() );
            }
            DynamicMessage rm = DynamicMessage.parseFrom( retDes, raw );
            FieldProtobufDecoder decoder = this.getMCTTransformer().getFieldProtobufDecoder();
            return decoder.decode(
                    digest.getReturnType(), digest.getGenericReturnTypeLabel(), retDes, rm, this.getCompilerEncoder().getExceptedKeys(), this.getCompilerEncoder().getOptions()
            );
        }
        catch ( InvalidProtocolBufferException e ) {
            throw new IlleagalResponseException( e );
        }
    }

    public Object unmarshalResponse( MethodPrototype digest, UMCMessage msg ) throws IlleagalResponseException {
        return this.unmarshalResponse( digest, (byte[]) msg.getHead().getExtraHead() );
    }

    protected DynamicMethodPrototype queryMethodPrototype(String szMethodAddress ) {
        DynamicMethodPrototype method = (DynamicMethodPrototype) this.queryMethodDigest( szMethodAddress );
        if ( method == null ) {
            throw new IllegalArgumentException( "Method address `" + szMethodAddress + "` is invalid." );
        }

        return method;
    }

}
