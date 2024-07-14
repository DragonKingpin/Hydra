package com.pinecone.framework.util.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class ArchDynamicFactory implements DynamicFactory {
    protected static final Map<Class<? >, Class<? > > PrimitiveToWrapper = new HashMap<>();

    static {
        ArchDynamicFactory.PrimitiveToWrapper.put( boolean.class,  Boolean.class   );
        ArchDynamicFactory.PrimitiveToWrapper.put( byte.class,     Byte.class      );
        ArchDynamicFactory.PrimitiveToWrapper.put( char.class,     Character.class );
        ArchDynamicFactory.PrimitiveToWrapper.put( double.class,   Double.class    );
        ArchDynamicFactory.PrimitiveToWrapper.put( float.class,    Float.class     );
        ArchDynamicFactory.PrimitiveToWrapper.put( int.class,      Integer.class   );
        ArchDynamicFactory.PrimitiveToWrapper.put( long.class,     Long.class      );
        ArchDynamicFactory.PrimitiveToWrapper.put( short.class,    Short.class     );
        ArchDynamicFactory.PrimitiveToWrapper.put( void.class,     Void.class      );
    }

    protected ClassLoader             mClassLoader      ;
    protected ClassScope              mClassScope       ;

    protected ArchDynamicFactory( ClassLoader classLoader, ClassScope classScope ) {
        this.mClassLoader       = classLoader       ;
        this.mClassScope        = classScope        ;
    }

    @Override
    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    @Override
    public ClassScope getClassScope() {
        return this.mClassScope;
    }


    @Override
    public Object newInstance ( Class<? > that, Class<?>[] stereotypes, Object[] args ) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?>[] constructors = that.getConstructors();
        boolean bUsingSetAccess = false;
        if( constructors.length == 0 ) {
            constructors = that.getDeclaredConstructors();
            bUsingSetAccess = true;
        }
        for ( Constructor<?> constructor : constructors ) {
            Class<?>[] paramTypes = constructor.getParameterTypes();
            int nArgsLength = 0;
            if( args != null ) {
                nArgsLength = args.length;
            }

            if ( paramTypes.length == nArgsLength ) {
                boolean matches = true;
                for ( int i = 0; i < paramTypes.length; ++i ) {
                    if( stereotypes != null ) {
                        if ( !paramTypes[i].isAssignableFrom( stereotypes[i] ) ) {
                            matches = false;
                            break;
                        }
                    }
                    else {
                        Class<?> paramType = paramTypes[i];
                        if ( !paramType.isInstance( args[i] ) ) {
                            if( paramType.isPrimitive() ) {
                                Class<?> wrapperType = ArchDynamicFactory.PrimitiveToWrapper.get( paramType );
                                if ( wrapperType != null && wrapperType.isInstance( args[i] ) ) {
                                    continue;
                                }
                            }
                            matches = false;
                            break;
                        }
                    }
                }

                if ( matches ) {
                    if( bUsingSetAccess ) {
                        try{
                            return constructor.newInstance( args );
                        }
                        catch ( IllegalAccessException e ) {
                            constructor.setAccessible( true );
                            Object ins = constructor.newInstance( args );
                            constructor.setAccessible( false );
                            return ins;
                        }
                    }
                    else {
                        return constructor.newInstance( args );
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Object loadInstance( String szClassFullName, Class<?>[] stereotypes, Object[] args ) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = this.mClassLoader.loadClass( szClassFullName );
        return this.newInstance( clazz, stereotypes, args );
    }
}
