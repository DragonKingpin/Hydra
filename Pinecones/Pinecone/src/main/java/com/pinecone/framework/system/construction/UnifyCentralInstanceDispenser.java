package com.pinecone.framework.system.construction;

import com.pinecone.framework.system.BadAllocateException;
import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.ReflectionUtils;
import com.pinecone.framework.util.StringUtils;
import com.pinecone.framework.util.lang.DynamicFactory;
import com.pinecone.framework.util.lang.GenericDynamicFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UnifyCentralInstanceDispenser implements StructureInstanceDispenser {
    protected final Map<Class<?>, Object >                 mSingletonObjects   = new ConcurrentHashMap<>();
    protected final Map<Class<?>, StructureDefinition >    mObjectDefinitions  = new ConcurrentHashMap<>();
    protected final Map<Class<?>, InstancePool<? > >       mObjectInstancer    = new ConcurrentHashMap<>(); // Pool is immutable.
    protected final Map<StructureKey, Class<?> >           mObjectKeys         = new ConcurrentHashMap<>();
    protected final Map<String, Object >                   mObjectRegister     = new ConcurrentHashMap<>();
    protected final DynamicFactory                         mCentralFactory     ;

    public UnifyCentralInstanceDispenser( DynamicFactory factory ) {
        this.mCentralFactory = factory;
    }

    public UnifyCentralInstanceDispenser() {
        this( new GenericDynamicFactory() );
    }

    /**
     * update
     * The pool is immutable.
     * @param type the object`s type
     * @param definition the object`s definition
     * @return null for nonsexist or definition which just inserted.
     */
    @Override
    public StructureDefinition update( Class<?> type, StructureDefinition definition ) {
        if( this.mObjectDefinitions.containsKey( type ) ) {
            return this.mObjectDefinitions.put( type, definition );
        }
        return null;
    }

    @Override
    public InstanceDispenser register( Class<?> type, StructureDefinition definition, InstancePool<? > pool ) {
        if( pool == null ) {
            pool = this.defaultInstancePool( type, definition );
        }
        this.mObjectDefinitions.putIfAbsent( type, definition );
        this.mObjectInstancer.putIfAbsent( type, pool );
        this.mObjectKeys.putIfAbsent( new StructureKey( type, definition ), type );
        return this;
    }

    @Override
    public InstanceDispenser register( Class<?> type, StructureDefinition definition ) {
        return this.register( type, definition, this.defaultInstancePool( type, definition ) );
    }

    protected StructureDefinition defaultDefinition( Class<?> type, Structure structure ) {
        StructureDefinition definition = new GenericStructureDefinition( structure );
        if( definition.getType() == Object.class && type != Object.class ) {
            definition.setType( type );
        }

        return definition;
    }

    @Override
    public InstanceDispenser register( Class<?> type, Structure structure ) {
        return this.register( type, structure, null );
    }

    @Override
    public InstanceDispenser register( Class<?> type, Structure structure, @Nullable InstancePool<?> pool ) {
        StructureDefinition definition = this.defaultDefinition( type, structure );
        if( pool == null ) {
            pool = this.defaultInstancePool( type, definition );
        }
        return this.register( type, definition, pool );
    }

    @Override
    public InstanceDispenser register( Class<?> type ) {
        return this.register( type, (InstancePool<?>) null );
    }

    protected Structure foundClassDeclaredStructure( Class<?> type ) {
        Annotation[] annotations = type.getAnnotations();
        for( Annotation annotation : annotations ) {
            if( annotation instanceof Structure ) {
                return (Structure)annotation;
            }
        }

        return null;
    }

    @Override
    public InstanceDispenser register( Class<?> type, @Nullable InstancePool<?> pool ) {
        Structure target = this.foundClassDeclaredStructure( type );
        if( target != null ) {
            return this.register( type, target, pool );
        }

        StructureDefinition definition = new GenericStructureDefinition( type );
        return this.register( type, definition, pool );
    }

    protected InstancePool<? > defaultInstancePool( Class<?> type, StructureDefinition definition ) {
        if( definition.getProvide() != void.class && definition.getProvide() != Object.class ) {
            Object o = this.tryInstancingFromProvider( type, definition, null );
            if( o instanceof InstancePool ) {
                return (InstancePool)o;
            }
        }

        if( definition.getCycle() == ReuseCycle.Disposable || definition.getCycle().isSingleton() ) {
            return new GenericDynamicInstancePool<>( this.mCentralFactory, 0, type );
        }
        return new GenericDynamicInstancePool<>( this.mCentralFactory, 4, type );
    }

    @Override
    public InstanceDispenser register( StructureDefinition definition ) {
        return this.register( definition.getType(), definition );
    }

    @Override
    public InstanceDispenser registerByImplicitFirstFound( Class<?> type, @Nullable Structure structure ) {
        return this.registerByImplicitFirstFound( type, structure, null );
    }

    @Override
    public InstanceDispenser registerByImplicitFirstFound( Class<?> type, @Nullable Structure structure, @Nullable InstancePool<?> pool ) {
        Structure target = this.foundClassDeclaredStructure( type );
        if( target == null ) {
            target = structure;
        }

        if( target == null ) {
            StructureDefinition definition = new GenericStructureDefinition( type );
            return this.register( type, definition, pool );
        }
        return this.register( type, target, pool );
    }

    @Override
    public InstanceDispenser registerByImplicitFirstFound( Class<?> type ) {
        return this.registerByImplicitFirstFound( type, null, null );
    }

    @Override
    public boolean hasRegistered( Class<? > type ) {
        return this.mObjectDefinitions.containsKey( type );
    }

    protected Object invokeInstancingProvider( Class<? > provider, String szMethodName ) {
        Object provide = this.mCentralFactory.optNewInstance( provider, null );
        if ( provide == null ) {
            throw new StructureProviderException( "Failed to instantiate structure provider: " + provider.getName() );
        }
        Method pm;
        try{
            pm = provide.getClass().getMethod( szMethodName );
        }
        catch ( NoSuchMethodException nme ) {
            throw new StructureProviderException( "Structure provider method not found: " + provider.getName() + "." + szMethodName, nme );
        }

        try {
            return ReflectionUtils.tryAccessibleInvoke( pm, provide );
        }
        catch ( InvocationTargetException | IllegalArgumentException e ) {
            throw new StructureProviderException( "Failed to invoke structure provider method: " + provider.getName() + "." + szMethodName, e );
        }
    }

    protected Object tryInstancingFromProvider( Class<?> type, StructureDefinition definition, @Nullable Structure instanceStructure ) {
        if( instanceStructure != null ) {
            Class<? > provider = instanceStructure.provider();
            if( DynamicInstancePool.class.isAssignableFrom( provider ) ) {
                if( instanceStructure.cycle() == ReuseCycle.Disposable || instanceStructure.cycle().isSingleton() ) {
                    return (InstancePool<?>) this.mCentralFactory.optNewInstance( provider, new Object[]{ this.mCentralFactory, 0, type } );
                }
                return (InstancePool<?>) this.mCentralFactory.optNewInstance( provider, new Object[]{ this.mCentralFactory, 4, type } );
            }
            else if( InstancePool.class.isAssignableFrom( provider ) ) {
                return (InstancePool<?>) this.mCentralFactory.optNewInstance( provider, null );
            }
            else if( instanceStructure.type() != void.class && instanceStructure.type() != Object.class && !instanceStructure.providerMethod().isEmpty() ) {
                Object ret = this.invokeInstancingProvider( provider, instanceStructure.providerMethod() );
                if( ret != null ) {
                    return ret;
                }
            }
        }

        Class<? > provider = definition.getProvide();
        if( DynamicInstancePool.class.isAssignableFrom( definition.getProvide() ) ) {
            if( definition.getCycle() == ReuseCycle.Disposable || definition.getCycle().isSingleton() ) {
                return (InstancePool<?>) this.mCentralFactory.optNewInstance( provider, new Object[]{ this.mCentralFactory, 0, type } );
            }
            return (InstancePool<?>) this.mCentralFactory.optNewInstance( provider, new Object[]{ this.mCentralFactory, 4, type } );
        }
        else if( InstancePool.class.isAssignableFrom( definition.getProvide() ) ) {
            return (InstancePool<?>) this.mCentralFactory.optNewInstance( provider, null );
        }
        else if( definition.getType() != void.class && definition.getType() != Object.class && !definition.getProvideMethod().isEmpty() ) {
            Object ret = this.invokeInstancingProvider( provider, definition.getProvideMethod() );
            if( ret != null ) {
                return ret;
            }
        }
        return null;
    }


    @Override
    public <T> T allotInstance( Class<T> type, @Nullable Structure instanceStructure ) {
        StructureDefinition definition = this.mObjectDefinitions.get( type );
        if( definition == null ) {
            return null; // Unregistered.
        }
        Class<? > innerType = definition.getType();
        if( innerType == Object.class ) {
            innerType = type;
        }

        Object t = this.tryInstancingFromProvider( type, definition, instanceStructure );
        if( t != null ) {
            return type.cast( t );
        }

        Object b = this.mSingletonObjects.get( innerType );
        if ( b != null ) {
            if( instanceStructure != null && !instanceStructure.cycle().isSingleton() ) {
                return type.cast( this.allocateFromPool( innerType, this.mObjectInstancer.get( innerType ) ) );
            }
            return type.cast( b );
        }

        InstancePool<? > pool = this.mObjectInstancer.get( innerType );
        if ( pool != null ) {
            if(
                    definition.getCycle() == ReuseCycle.Disposable ||
                    ( instanceStructure != null && instanceStructure.cycle() == ReuseCycle.Disposable )
            ) {
                return type.cast( this.allocateFromPool( innerType, pool ) );
            }

            if ( definition.getCycle().isSingleton() ) {
                return type.cast( this.mSingletonObjects.computeIfAbsent(
                        innerType,
                        key -> this.allocateFromPool( key, pool )
                ) );
            }
            return type.cast( this.allocateFromPool( innerType, pool ) );
        }

        String name = instanceStructure == null ? "" : instanceStructure.name();
        if ( StringUtils.isEmpty(name) ) {
            name = type.getSimpleName();
            name = Character.toLowerCase( name.charAt(0) ) + name.substring(1);
        }

        if ( StringUtils.isNoneEmpty(name) ) {
            Object o = this.getRegisteredInstance( name );
            if( o != null && type.isAssignableFrom( o.getClass() ) ) {
                return type.cast( o );
            }
        }

        return null;
    }

    protected Object allocateFromPool( Class<?> type, InstancePool<?> pool ) {
        if ( pool == null ) {
            throw new StructureResolutionException( "Instance pool is undefined: " + type.getName() );
        }

        try {
            return pool.allocate();
        }
        catch ( BadAllocateException e ) {
            throw new StructureResolutionException( "Failed to allocate instance: " + type.getName(), e );
        }
    }

    @Override
    public <T > T allotInstance( Class<T> type ) {
        return this.allotInstance( type, null );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void free( Class<?> type, Object instance ) {
        InstancePool pool = this.mObjectInstancer.get( type );
        if( pool != null ) {
            pool.free( instance );
        }
        else {
            throw new IllegalArgumentException( type.getName() + " is not owned instance." );
        }
    }

    @Override
    public void free( Object instance ) {
        this.free( instance.getClass(), instance );
    }

    @Override
    public StructureDefinition getStructureDefinition( Class<?> type ) {
        return this.mObjectDefinitions.get( type );
    }

    @Override
    public InstancePool<? > getInstancePool( Class<?> type ) {
        return this.mObjectInstancer.get( type );
    }

    @Override
    public DynamicFactory getCentralFactory() {
        return this.mCentralFactory;
    }


    @Override
    public Object registerInstance( String name, Object instance ) {
        return this.mObjectRegister.put( name, instance );
    }

    @Override
    public Object getRegisteredInstance( String name ) {
        return this.mObjectRegister.get( name );
    }

    @Override
    public Object removeRegisteredInstance( String name ) {
        return this.mObjectRegister.remove( name );
    }

    public UnifyCentralInstanceDispenser prepare() {
        for ( Class<?> type : this.mObjectDefinitions.keySet() ) {
            this.prepare( type );
        }
        return this;
    }

    public UnifyCentralInstanceDispenser prepare( Class<?> type ) {
        StructureDefinition definition = this.mObjectDefinitions.get( type );
        if ( definition == null ) {
            throw new StructureResolutionException( "Structure is not registered: " + type.getName() );
        }
        if ( definition.getCycle() == ReuseCycle.PreSingleton ) {
            this.allotInstance( type );
        }
        else if ( definition.getCycle() == ReuseCycle.PreRecyclable ) {
            InstancePool<?> pool = this.mObjectInstancer.get( type );
            if ( pool == null && definition.getType() != Object.class ) {
                pool = this.mObjectInstancer.get( definition.getType() );
            }
            if ( pool != null ) {
                pool.preAllocate( 4 );
            }
        }
        return this;
    }

    public Class<?> getRegisteredType( StructureKey key ) {
        return this.mObjectKeys.get( key );
    }

}
