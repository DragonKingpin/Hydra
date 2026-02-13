package com.pinecone.hydra.umct.husky.compiler;

import java.util.List;

import com.pinecone.hydra.umct.stereotype.Iface;
import com.pinecone.hydra.umct.stereotype.IfaceUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public abstract class ArchIfaceCompiler extends ArchIfaceInspector implements IfaceCompiler {

    public ArchIfaceCompiler(ClassPool classPool, ClassLoader classLoader ) {
        super( classPool, classLoader );
    }

    protected IfaceMethodSignature resolveMethodSignature( CtMethod method ) {
        try{
            Class<? >[] parameters = this.getParameters( method );

            CtClass retType;
            try {
                retType = method.getReturnType();
            }
            catch ( NotFoundException e ) {
                retType = null;
            }

            Class<? > returnType;
            if ( retType != null ) {
                returnType = this.reinterpretClass( retType.getName() );
            }
            else {
                returnType = null;
            }

            String[] parameterTypes = ArchIfaceCompiler.evalGenericParameterTypes( method );
            String   returnGType    = ArchIfaceCompiler.evalGenericReturnType( method );

            return new IfaceMethodSignature(
                    parameters, parameterTypes, returnType, returnGType
            );
        }
        catch ( ClassNotFoundException e ) {
            throw new CompileException( e );
        }
    }

    protected MethodDigest compile ( ClassDigest classDigest, CtMethod method ) {
        try {

            IfaceMethodSignature signature = this.resolveMethodSignature( method );


            Class<? >[] parameters  = signature.getParameters();
            Class<? >   returnType  = signature.getReturnType();
            String[] parameterTypes = signature.getParameterGenericTypes();
            String   returnGType    = signature.getReturnGenericType();

            MethodDigest ret = new GenericMethodDigest(
                    classDigest, this.getIfaceMethodName( method ), method.getName(), parameters, parameterTypes, returnType, returnGType, null
            );

            List<IfaceParamsDigest> ifaceParamsDigests = this.inspectArgIfaceParams( ret, method );
            ret.apply(ifaceParamsDigests);
            return ret;
        }
        catch ( ClassNotFoundException e ) {
            throw new CompileException( e );
        }
    }

    protected String evalLogicClassName( String className ) throws NotFoundException {
        String szLogicClassName = className;
        CtClass ctClass = this.mClassPool.get( className );
        if ( ctClass != null ) {
            Iface cIface     = this.getAnnotation( ctClass, Iface.class );
            String szLogicCN = IfaceUtils.queryIfaceLogicClassName( cIface );
            if ( szLogicCN != null ) {
                szLogicClassName = szLogicCN;
            }
        }
        return szLogicClassName;
    }

    @Override
    public ClassDigest compile( Class<?> clazz, boolean bAsIface ) {
        return this.compile( clazz.getName(), bAsIface );
    }

    @Override
    public ClassDigest compile( String className, boolean bAsIface ) {
        try {
            List<CtMethod > ifaceMethods = this.inspect( className, bAsIface );
            if ( ifaceMethods.isEmpty() ) {
                return null;
            }

            String szLogicClassName = this.evalLogicClassName( className );
            ClassDigest classDigest = new GenericClassDigest( szLogicClassName, className );
            for ( CtMethod ctMethod : ifaceMethods ) {
                MethodDigest methodDigest = this.compile( classDigest, ctMethod );
                classDigest.addMethod( methodDigest );
            }

            return classDigest;
        }
        catch ( NotFoundException e ) {
            throw new CompileException( e );
        }
    }

    public static final class IfaceMethodSignature {
        protected final Class<?>[] mParameters;
        protected final String[]   mParameterGenericTypes;

        protected final Class<?>   mReturnType;
        protected final String     mszReturnGenericType;

        public IfaceMethodSignature(
                Class<?>[] parameters, String[] parameterGenericTypes, Class<?> returnType, String returnGenericType
        ) {
            this.mParameters = parameters;
            this.mParameterGenericTypes = parameterGenericTypes;
            this.mReturnType = returnType;
            this.mszReturnGenericType = returnGenericType;
        }

        public Class<?>[] getParameters() {
            return this.mParameters;
        }

        public String[] getParameterGenericTypes() {
            return this.mParameterGenericTypes;
        }

        public Class<?> getReturnType() {
            return this.mReturnType;
        }

        public String getReturnGenericType() {
            return this.mszReturnGenericType;
        }
    }

}
