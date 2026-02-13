package com.pinecone.hydra.umct.husky.machinery;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pinecone.framework.lang.field.FieldEntity;
import com.pinecone.hydra.express.Deliver;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.umct.MessageDeliver;
import com.pinecone.hydra.umct.MessageExpress;
import com.pinecone.hydra.umct.MessageHandler;
import com.pinecone.hydra.umct.MessageJunction;
import com.pinecone.hydra.umct.ProtoletMsgDeliver;
import com.pinecone.hydra.umct.UMCTExpress;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.pinecone.hydra.uma.AppointServer;
import com.pinecone.hydra.umct.husky.compiler.BytecodeIfaceCompiler;
import com.pinecone.hydra.umct.husky.compiler.ClassDigest;
import com.pinecone.hydra.umct.husky.compiler.CompilerEncoder;
import com.pinecone.hydra.umct.husky.compiler.DynamicMethodPrototype;
import com.pinecone.hydra.umct.husky.compiler.IfaceMappingDigest;
import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.husky.compiler.MethodDigest;
import com.pinecone.hydra.umct.mapping.BytecodeControllerInspector;
import com.pinecone.hydra.umct.mapping.ControllerInspector;
import com.pinecone.hydra.umct.mapping.InspectException;
import com.pinecone.hydra.umct.mapping.MappingDigest;
import com.pinecone.hydra.umct.stereotype.IfaceUtils;
import com.pinecone.ulf.util.protobuf.GenericFieldProtobufDecoder;

import javassist.ClassPool;
import javassist.NotFoundException;

public class HuskyRouteDispatcher extends ArchRouteDispatcher implements ProtoRouteDispatcher {

    protected void applyExpress( ProtoInterfacialCompiler compiler, UMCTExpress express ) {
        this.mUMCTExpress = express;

        this.mDefaultDeliver      = new ProtoletMsgDeliver( AppointServer.DefaultEntityName, this.mUMCTExpress, this.getContextMachinery(), compiler.getCompilerEncoder() );
        this.mUMCTExpress.register( this.mDefaultDeliver  );
    }

    protected HuskyRouteDispatcher( ProtoInterfacialCompiler compiler, ControllerInspector controllerInspector ) {
        super();
        this.mMCTContextMachinery = new HuskyContextMachinery( compiler, controllerInspector, new GenericFieldProtobufDecoder() );
    }

    public HuskyRouteDispatcher( PMCTContextMachinery machinery, UMCTExpress express ) {
        super();
        this.mMCTContextMachinery = machinery;
        this.applyExpress( machinery.getInterfacialCompiler(), express );
    }

    public HuskyRouteDispatcher( ProtoInterfacialCompiler compiler, ControllerInspector controllerInspector, UMCTExpress express ) {
        this( compiler, controllerInspector );
        this.applyExpress( compiler, express );
    }

    public HuskyRouteDispatcher( CompilerEncoder encoder, UMCTExpress express, ClassLoader classLoader ) {
        this( new BytecodeIfaceCompiler(
                ClassPool.getDefault(), classLoader, encoder
        ), new BytecodeControllerInspector(
                ClassPool.getDefault(), classLoader
        ), express );
    }

    public HuskyRouteDispatcher( UMCTExpress express, ClassLoader classLoader ) {
        this( new BytecodeIfaceCompiler(
                ClassPool.getDefault(), classLoader
        ), new BytecodeControllerInspector(
                ClassPool.getDefault(), classLoader
        ), express );
    }

    public HuskyRouteDispatcher( Class<?> expressType, MessageJunction junction, ClassLoader classLoader ) {
        this(
                new BytecodeIfaceCompiler( ClassPool.getDefault(), classLoader ),
                new BytecodeControllerInspector( ClassPool.getDefault(), classLoader )
        );

        try {
            Constructor<?> constructor = expressType.getConstructor( String.class, MessageJunction.class, Logger.class );
            Logger logger ;
            if ( junction instanceof Slf4jTraceable ) {
                logger = ((Slf4jTraceable) junction).getLogger();
            }
            else {
                logger = LoggerFactory.getLogger( this.getClass().getName() );
            }

            UMCTExpress express = (UMCTExpress) constructor.newInstance( AppointServer.DefaultEntityName, junction, logger );

            this.applyExpress(
                    this.getInterfacialCompiler(), express
            );
        }
        catch ( NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            throw new IllegalArgumentException( "`" + expressType.getSimpleName() + "` is not UMCTExpress calibre qualified." );
        }
    }

    public HuskyRouteDispatcher( ClassLoader classLoader, boolean delayExpress ) {
        this(
                new BytecodeIfaceCompiler( ClassPool.getDefault(), classLoader ),
                new BytecodeControllerInspector( ClassPool.getDefault(), classLoader )
        );
    }

    public HuskyRouteDispatcher( MessageJunction junction, ClassLoader classLoader ) {
        this( WolfMCExpress.class, junction, classLoader );
    }




    @Override
    public PMCTContextMachinery getContextMachinery() {
        return (PMCTContextMachinery) super.getContextMachinery();
    }

    @Override
    public ProtoInterfacialCompiler getInterfacialCompiler() {
        return (ProtoInterfacialCompiler) super.getInterfacialCompiler();
    }


}
