package com.pinecone.hydra.umct.husky.machinery;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pinecone.framework.lang.field.FieldEntity;
import com.pinecone.hydra.express.Deliver;
import com.pinecone.hydra.umct.MessageDeliver;
import com.pinecone.hydra.umct.MessageExpress;
import com.pinecone.hydra.umct.MessageHandler;
import com.pinecone.hydra.umct.UMCTExpress;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;
import com.pinecone.hydra.umct.husky.compiler.DynamicMethodPrototype;
import com.pinecone.hydra.umct.husky.compiler.IfaceMappingDigest;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;
import com.pinecone.hydra.umct.husky.compiler.MethodDigest;
import com.pinecone.hydra.umct.mapping.InspectException;
import com.pinecone.hydra.umct.mapping.MappingDigest;
import com.pinecone.hydra.umct.stereotype.IfaceUtils;

import javassist.NotFoundException;

public abstract class ArchRouteDispatcher implements RouteDispatcher {

    protected MCTContextMachinery     mMCTContextMachinery;
    protected UMCTExpress             mUMCTExpress;
    protected MessageDeliver          mDefaultDeliver;

    protected ArchRouteDispatcher() {

    }



    protected void registerInstance( MessageDeliver deliver, Object instance, Class<?> iface ) {
        if ( !iface.isInterface() ) {
            throw new IllegalArgumentException( "The provided class is not an interface: " + iface.getName() );
        }

        List<MethodDigest> digests = this.compile( iface, false ).getMethodDigests();
        Map<String, MethodDigest > digestMap = digests.stream()
                .collect( Collectors.toMap(MethodDigest::getName, digest -> digest) );

        Method[] methods = iface.getMethods();
        for ( Method method : methods ) {
            String methodName = IfaceUtils.getIfaceMethodName( method );

            DynamicMethodPrototype digest = (DynamicMethodPrototype)digestMap.get( methodName );

            String fullPath = digest.getFullName();

            MessageHandler handler = new MessageHandler() {
                @Override
                public String getAddressMapping() {
                    return digest.getFullName();
                }

                @Override
                public Object invoke( Object... args ) throws Exception {
                    return method.invoke( instance, args );
                }

                @Override
                public List<String> getArgumentsKey() {
                    return digest.getArgumentsKey();
                }

                @Override
                public Object getReturnDescriptor() {
                    return digest.getReturnDescriptor();
                }

                @Override
                public String getReturnGenericLabel() {
                    return digest.getGenericReturnTypeLabel();
                }

                @Override
                public Object getArgumentsDescriptor() {
                    return digest.getArgumentsDescriptor();
                }

                @Override
                public FieldEntity[] getArgumentTemplate() {
                    return digest.getArgumentTemplate().getSegments();
                }

            };

            deliver.registerHandler( fullPath, handler );
            this.mMCTContextMachinery.getMessageHandlerMap().put( fullPath, handler );
        }
    }

    @Override
    public void setUMCTExpress( UMCTExpress handler ) {
        this.mUMCTExpress = handler;
    }

    @Override
    public MCTContextMachinery getContextMachinery() {
        return this.mMCTContextMachinery;
    }

    @Override
    public UMCTExpress getUMCTExpress() {
        return this.mUMCTExpress;
    }

    @Override
    public MessageExpress register( Deliver deliver ) {
        return this.mUMCTExpress.register( deliver );
    }

    @Override
    public MessageExpress  fired   ( Deliver deliver ) {
        return this.mUMCTExpress.fired( deliver );
    }

    @Override
    public MessageDeliver getDeliver( String name ) {
        return this.mUMCTExpress.getDeliver( name );
    }

    @Override
    public MessageDeliver getDefaultDeliver() {
        return this.mDefaultDeliver;
    }

    @Override
    public InterfacialCompiler getInterfacialCompiler() {
        return this.mMCTContextMachinery.getInterfacialCompiler();
    }


    @Override
    public void registerInstance( String deliverName, Object instance, Class<?> iface ) {
        MessageDeliver deliver = this.getDeliver( deliverName );
        if ( deliver == null ) {
            throw new IllegalArgumentException( "No such deliver: " + deliverName );
        }

        this.registerInstance( deliver, instance, iface );
    }

    @Override
    public void registerInstance( Object instance, Class<?> iface ) {
        this.registerInstance( this.mDefaultDeliver, instance, iface );
    }

    protected void registerController( MessageDeliver deliver, Object instance, Class<?> controllerType ) {
        try {
            List<MappingDigest> digests   = this.mMCTContextMachinery.getControllerInspector().characterize( controllerType );
            List<IfaceMappingDigest>  ifs = this.getInterfacialCompiler().compile( digests );

            for ( IfaceMappingDigest imd : ifs ) {
                String[] addresses = imd.getAddresses();
                for ( int i = 0; i < addresses.length; ++i ) {
                    String address = addresses[ i ];

                    MessageHandler handler = new MessageHandler() {
                        @Override
                        public String getAddressMapping() {
                            return address;
                        }

                        @Override
                        public Object invoke( Object... args ) throws Exception {
                            return imd.getMappedMethod().invoke( instance, args );
                        }

                        @Override
                        public List<String> getArgumentsKey() {
                            return imd.getArgumentsKey();
                        }

                        @Override
                        public Object getReturnDescriptor() {
                            return imd.getReturnDescriptor();
                        }

                        @Override
                        public String getReturnGenericLabel() {
                            return imd.getReturnGenericTypeLabel();
                        }

                        @Override
                        public Object getArgumentsDescriptor() {
                            return imd.getArgumentsDescriptor();
                        }

                        @Override
                        public FieldEntity[] getArgumentTemplate() {
                            return imd.getArgumentTemplate().getSegments();
                        }
                    };

                    deliver.registerHandler( address, handler );
                    this.mMCTContextMachinery.getMessageHandlerMap().put( address, handler );
                }
            }
        }
        catch ( NotFoundException e ) {
            throw new InspectException( e );
        }
    }

    @Override
    public void registerController( String deliverName, Object instance, Class<?> controllerType ) {
        MessageDeliver deliver = this.getDeliver( deliverName );
        if ( deliver == null ) {
            throw new IllegalArgumentException( "No such deliver: " + deliverName );
        }

        this.registerController( deliver, instance, controllerType );
    }

    @Override
    public void registerController( Object instance, Class<?> controllerType ) {
        this.registerController( this.mDefaultDeliver, instance, controllerType );
    }

    @Override
    public ClassDigest queryClassDigest(String name ) {
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
