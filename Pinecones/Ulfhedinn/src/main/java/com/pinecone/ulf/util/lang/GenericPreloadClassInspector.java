package com.pinecone.ulf.util.lang;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;

public class GenericPreloadClassInspector implements HierarchyClassInspector {
    protected ClassPool     mClassPool;

    public GenericPreloadClassInspector( ClassPool classPool ) {
        this.mClassPool = classPool;
    }

    @Override
    public CtClass preloadClass( String szClassName ) throws NotFoundException {
        return this.mClassPool.get( szClassName );
    }

    @Override
    public boolean isImplementedDirectly( CtClass clazz, Class<?> interf ) throws NotFoundException {
        CtClass[] interfaces = clazz.getInterfaces();
        for ( CtClass iface : interfaces ) {
            if ( iface.getName().equals( interf.getName() ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isImplemented( CtClass clazz, Class<?> interf ) throws NotFoundException {
        String szInterfaceName = interf.getName();
        while ( clazz != null && !clazz.getName().equals( Object.class.getName() ) ) {
            CtClass[] interfaces = clazz.getInterfaces();
            for ( CtClass iface : interfaces ) {
                if ( this.isInterfaceExtended( iface, szInterfaceName ) ) {
                    return true;
                }
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    @Override
    public boolean isExtendedDirectly( CtClass clazz, Class<?> parent ) throws NotFoundException {
        CtClass superClass = clazz.getSuperclass();
        if ( superClass != null && superClass.getName().equals( parent.getName() ) ) {
            return true;
        }

        if( clazz.isInterface() ) {
            return this.isImplementedDirectly( clazz, parent );
        }
        return false;
    }

    @Override
    public boolean isExtended( CtClass clazz, Class<?> parent ) throws NotFoundException {
        if( clazz.isInterface() ) {
            return this.isInterfaceExtended( clazz, parent.getName() );
        }

        while ( clazz != null && !clazz.getName().equals( Object.class.getName() ) ) {
            CtClass superClass = clazz.getSuperclass();
            if (superClass != null && superClass.getName().equals(parent.getName())) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }

        return false;
    }

    private boolean isInterfaceExtended( CtClass clazz, String interfaceName ) throws NotFoundException {
        if ( clazz == null ) {
            return false;
        }

        CtClass[] interfaces = clazz.getInterfaces();
        for ( CtClass interfaceClass : interfaces ) {
            if ( interfaceClass.getName().equals( interfaceName ) ) {
                return true;
            }
            if ( this.isInterfaceExtended( interfaceClass, interfaceName ) ) {
                return true;
            }
        }

        CtClass superClass = clazz.getSuperclass();
        if ( superClass != null ) {
            return this.isInterfaceExtended( superClass, interfaceName );
        }
        return false;
    }

    @Override
    public Annotation[] queryVisibleAnnotations( CtClass clazz ) {
        ClassFile classFile = clazz.getClassFile();
        AnnotationsAttribute visible = (AnnotationsAttribute) classFile.getAttribute( AnnotationsAttribute.visibleTag );
        if ( visible != null ) {
            return visible.getAnnotations();
        }
        return null;
    }

    @Override
    public boolean hasOwnAnnotation( CtClass clazz, Class<?> annotationClass ) {
        Annotation[] annotations = this.queryVisibleAnnotations( clazz );
        if( annotations == null ) {
            return false;
        }

        for ( Annotation annotation : annotations ) {
            if ( annotation.getTypeName().equals( annotationClass.getName() ) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasOwnAnnotations( CtClass clazz, Class<?>[] annotationClasses ) {
        Annotation[] annotations = this.queryVisibleAnnotations( clazz );
        if( annotations == null ) {
            return false;
        }

        return this.hasOwnAnnotations( annotations, annotationClasses );
    }

    @Override
    public boolean hasOwnMethod( CtClass clazz, String methodName ) {
        try {
            clazz.getDeclaredMethod( methodName );
            return true;
        }
        catch ( NotFoundException e ) {
            return false;
        }
    }

    @Override
    public boolean hasOwnMethods( CtClass clazz, String[] methodNames ) {
        for ( String methodName : methodNames ) {
            if ( !this.hasOwnMethod(clazz, methodName) ) {
                return false;
            }
        }
        return true;
    }

    protected boolean hasOwnAnnotations( Annotation[] annotations, Class<?>[] annotationClasses ) {
        for ( Class<?> annotationClass : annotationClasses ) {
            boolean found = false;
            for ( Annotation annotation : annotations ) {
                if ( annotation.getTypeName().equals( annotationClass.getName() ) ) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public boolean methodHasAnnotations( CtMethod method, Class<?>[] annotationClasses ) {
        MethodInfo methodInfo = method.getMethodInfo();
        AnnotationsAttribute attr = (AnnotationsAttribute) methodInfo.getAttribute( AnnotationsAttribute.visibleTag );
        if ( attr == null ) {
            return false;
        }

        return this.hasOwnAnnotations( attr.getAnnotations(), annotationClasses );
    }

    public boolean methodHasAnnotations( CtMethod method, String[] annotationNames ) {
        MethodInfo methodInfo = method.getMethodInfo();
        AnnotationsAttribute attr = (AnnotationsAttribute) methodInfo.getAttribute( AnnotationsAttribute.visibleTag );
        if ( attr == null ) {
            return false;
        }

        for ( String annotationName : annotationNames ) {
            boolean found = false;
            for ( Annotation annotation : attr.getAnnotations() ) {
                if ( annotation.getTypeName().equals( annotationName ) ) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return false;
    }

    public boolean methodHasAnnotation( CtMethod method, Class<?> annotationClass ) {
        return this.methodHasAnnotation( method, annotationClass.getName() );
    }

    public boolean methodHasAnnotation( CtMethod method, String annotationName ) {
        MethodInfo methodInfo = method.getMethodInfo();
        AnnotationsAttribute attr = (AnnotationsAttribute) methodInfo.getAttribute( AnnotationsAttribute.visibleTag );
        if ( attr != null ) {
            for ( Annotation annotation : attr.getAnnotations() ) {
                if ( annotation.getTypeName().equals( annotationName ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasOwnField( CtClass clazz, String fieldName ) {
        try {
            clazz.getDeclaredField( fieldName );
            return true;
        }
        catch ( NotFoundException e ) {
            return false;
        }
    }

    @Override
    public boolean hasOwnFields( CtClass clazz, String[] fieldNames ) {
        for ( String fieldName : fieldNames ) {
            if ( !this.hasOwnField( clazz, fieldName ) ) {
                return false;
            }
        }
        return true;
    }



    public static String[] parseGenericParameterTypes( CtMethod method ) throws NotFoundException, BadBytecode {
        SignatureAttribute.MethodSignature methodSignature = GenericPreloadClassInspector.getMethodSignature( method );
        if ( methodSignature == null ) {
            CtClass[] ps = method.getParameterTypes();
            String[] result = new String[ ps.length ];
            for ( int i = 0; i < ps.length; ++i ) {
                result[ i ] = ps[ i ].getName();
            }
            return result;
        }

        SignatureAttribute.Type[] paramTypes = methodSignature.getParameterTypes();
        String[] result = new String[ paramTypes.length ];

        for ( int i = 0; i < paramTypes.length; ++i ) {
            result[ i ] = GenericPreloadClassInspector.typeToString( paramTypes[ i ] );
        }

        return result;
    }

    public static String parseGenericReturnType( CtMethod method ) throws NotFoundException, BadBytecode {
        SignatureAttribute.MethodSignature methodSignature = GenericPreloadClassInspector.getMethodSignature( method );
        if ( methodSignature == null ) {
            return method.getReturnType().getName();
        }

        return GenericPreloadClassInspector.typeToString( methodSignature.getReturnType() );
    }

    public static String[] evalGenericParameterTypes( CtMethod method ) {
        try {
            return GenericPreloadClassInspector.parseGenericParameterTypes( method );
        }
        catch ( NotFoundException | BadBytecode e ) {
            return null;
        }
    }

    public static String getGenericReturnType( CtMethod method ) {
        try {
            SignatureAttribute.MethodSignature methodSignature = GenericPreloadClassInspector.getMethodSignature( method );
            if ( methodSignature == null ) {
                return null;
            }

            return GenericPreloadClassInspector.typeToString( methodSignature.getReturnType() );
        }
        catch ( BadBytecode e ) {
            return null;
        }
    }

    public static String evalGenericReturnType( CtMethod method ) {
        try {
            return GenericPreloadClassInspector.parseGenericReturnType( method );
        }
        catch ( NotFoundException | BadBytecode e ) {
            return null;
        }
    }

    protected static SignatureAttribute.MethodSignature getMethodSignature( CtMethod method ) throws BadBytecode {
        SignatureAttribute signature = (SignatureAttribute) method.getMethodInfo().getAttribute( SignatureAttribute.tag );
        if ( signature == null ) {
            return null;
        }
        return SignatureAttribute.toMethodSignature( signature.getSignature() );
    }

    public static String typeToString( SignatureAttribute.Type type ) {
        if ( type instanceof SignatureAttribute.ClassType ) {
            SignatureAttribute.ClassType classType = (SignatureAttribute.ClassType) type;
            if ( classType.getTypeArguments() != null && classType.getTypeArguments().length > 0 ) {
                StringBuilder sb = new StringBuilder(classType.getName());
                sb.append( "<" );
                for ( int i = 0; i < classType.getTypeArguments().length; ++i ) {
                    if ( i > 0 ) {
                        sb.append( ", " );
                    }
                    sb.append(classType.getTypeArguments()[i].toString());
                }
                sb.append( ">" );
                return sb.toString();
            }
            return classType.getName();
        }
        return type.toString();
    }

}
