package com.pinecone.framework.util.json;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pinecone.framework.system.prototype.ObjectiveEvaluator;
import com.pinecone.framework.util.ReflectionUtils;
import com.pinecone.framework.util.UnitHelper;

public class ObjectJSONCursorUnmarshal extends ArchCursorParser {
    public static JSONDecoder INNER_OBJECT_DECODER = new JSONObjectDecoder() {
        @Override
        protected void set( Object self, String key, Object val ) {
            ObjectiveEvaluator.MapStructures.set( self, key, val );
        }
    };

    public static JSONDecoder INNER_ARRAY_DECODER = new JSONArrayDecoder() {
        @Override
        @SuppressWarnings( "unchecked" )
        protected void add( Object self, Object parent, Object indexKey, Object val ) {
            if( self.getClass().isArray() ) {
                Object[] ref = (Object[])self; // Fuck java, no pointer.
                ref[ 0 ] = UnitHelper.append( (Object[])ref[ 0 ], val );
            }
            else if( self instanceof Collection ) {
                ((Collection) self).add( val );
            }
        }
    };


    protected Class<? >         mClassType   ;
    protected Type              mRootType    ;
    protected final TypeContext mTypeContext = new TypeContext();

    public ObjectJSONCursorUnmarshal( Reader reader, Class<? > classType, Type rootType ) throws JSONParseException {
        super(reader);
        this.mRootType   = rootType;
        this.mClassType  = classType;
    }

    public ObjectJSONCursorUnmarshal( Reader reader, Type rootType ) throws JSONParseException {
        this( reader, ObjectiveEvaluator.resolveRawClass( rootType ), rootType );
    }

    public ObjectJSONCursorUnmarshal( Reader reader, Class<? > classType ) throws JSONParseException {
        this( reader, (Type) classType );
    }

    public ObjectJSONCursorUnmarshal( InputStream inputStream, Class<? > classType ) throws JSONParseException {
        this((Reader)( new InputStreamReader(inputStream)), classType );
    }

    public ObjectJSONCursorUnmarshal( InputStream inputStream, Type rootType ) throws JSONParseException {
        this((Reader)( new InputStreamReader(inputStream)), ObjectiveEvaluator.resolveRawClass( rootType ), rootType );
    }

    public ObjectJSONCursorUnmarshal( String s, Class<? > classType ) throws JSONParseException {
        this((Reader)( new StringReader(s)), classType );
    }

    public ObjectJSONCursorUnmarshal( String s, Type rootType ) throws JSONParseException {
        this((Reader)( new StringReader(s)), ObjectiveEvaluator.resolveRawClass( rootType ), rootType );
    }


    protected GenericTypeContext nextGenericTypeContext( Object parent, Object indexKey ) throws JSONParseException {
        Class<? > thisType    ;
        Type elemGenericType  ;

        if ( parent == null ) {
            if ( this.mTypeContext.isEmpty() ) {
                this.mTypeContext.push(
                        this.mRootType,
                        ObjectiveEvaluator.extractGenericElementType( this.mRootType )
                );
            }

            TypeContext.Frame frame = this.mTypeContext.peek();

            thisType        = ObjectiveEvaluator.resolveRawClass( frame.mContainerType );
            elemGenericType = frame.mElementType;
        }
        else {
            TypeContext.Frame frame = this.mTypeContext.peek();

            if ( frame != null && frame.mElementType != null ) {
                elemGenericType = frame.mElementType;
                thisType        = ObjectiveEvaluator.resolveRawClass( frame.mElementType );
            }
            else {
                elemGenericType = ObjectiveEvaluator.MapStructures.getElementGenericType( parent, indexKey.toString() );
                thisType        = ObjectiveEvaluator.MapStructures.getType( parent, indexKey );
            }
        }

        GenericTypeContext context = new GenericTypeContext();
        context.thisType = thisType;
        context.elemGenericType = elemGenericType;

        return context;
    }

    @Override
    protected Object newJSONArray( Object indexKey, ArchCursorParser parser, Object parent, Object[] args ) {
        try {
            GenericTypeContext context = this.nextGenericTypeContext( parent, indexKey );
            Class<? > thisType    = context.thisType;
            Type elemGenericType  = context.elemGenericType;

//            if ( parent != null ){
//                elemGenericType = ObjectiveEvaluator.MapStructures.getElementGenericType( parent, indexKey.toString() );
//                thisType = ObjectiveEvaluator.MapStructures.getType( parent, indexKey );
//            }
//            else {
//                thisType = ObjectiveEvaluator.resolveRawClass( this.mRootType );
//                elemGenericType = ObjectiveEvaluator.extractGenericElementType( this.mRootType );
//            }

            Object    self;

            if ( thisType == null ) {
                self = new Object(); // Dummy
                ObjectJSONCursorUnmarshal.INNER_ARRAY_DECODER.decode( self, parent, indexKey,this, elemGenericType );
                return self;
            }

            if ( thisType.equals( List.class ) || thisType.equals( Void.class ) || thisType.equals( Object.class ) ) {
                thisType = JSONArraytron.class;
                if( elemGenericType != null ) {
                    String genericTypeName = elemGenericType.getTypeName();
                    if( !genericTypeName.equals( "?" ) && !genericTypeName.equals( Object.class.getSimpleName() ) ) {
                        thisType = ArrayList.class;
                    }
                }
            }
            else if ( thisType.equals( Set.class ) ) {
                thisType = LinkedHashSet.class;
            }

            if ( thisType.isArray() ) {
                Class<?> innerType = thisType.getComponentType();
                if ( innerType.equals( Object.class ) ) {
                    self = new Object[]{ new Object[ 0 ] };  // Object[]*, ptr -> Object[]
                }
                else {
                    elemGenericType   = innerType;
                    self = new Object[]{ Array.newInstance( innerType, 0 ) };
                    // Object[]*, ptr -> Object[]
                }
            }
            else {
                self = thisType.getConstructor().newInstance();
            }

            ObjectJSONCursorUnmarshal.INNER_ARRAY_DECODER.decode( self, parent, indexKey,this, elemGenericType );
            if ( self.getClass().isArray() ) {
                return Array.get( self, 0 );
            }
            return self;
        }
        catch ( NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            return null;
        }
        catch ( InstantiationException e1 ) {
            throw new JSONParseException( e1 );
        }
    }

    protected Class<?> findDirectJSONObjectAssignableType( Class<? > thisType ) {
        if( thisType == null || thisType.equals( Map.class ) || thisType.equals( Void.class ) || thisType.equals( Object.class ) ) {
            thisType = JSONMaptron.class;
        }
        else if( thisType.isInterface() &&  Map.class.isAssignableFrom( thisType ) ) {
            thisType = JSONMaptron.class;
        }
        else if( thisType.isInterface() &&  JSONObject.class.isAssignableFrom( thisType ) ) {
            thisType = JSONMaptron.class;
        }

        return thisType;
    }

    @Override
    protected Object newJSONObject( Object indexKey, ArchCursorParser parser, Object parent, Object[] args ) {
        try {
            GenericTypeContext context = this.nextGenericTypeContext( parent, indexKey );
            Class<? > thisType    = context.thisType;
            Type elemGenericType  = context.elemGenericType;
//            if( parent == null ){
//                thisType = ObjectiveEvaluator.resolveRawClass( this.mRootType );
//                elemGenericType = ObjectiveEvaluator.extractGenericElementType( this.mRootType );
//            }
//            else {
//                thisType = ObjectiveEvaluator.MapStructures.getType( parent, indexKey );
//                elemGenericType = ObjectiveEvaluator.MapStructures.getFieldGenericType( parent, indexKey.toString() );
//            }

            thisType = this.findDirectJSONObjectAssignableType( thisType );
            if( elemGenericType != null ) {
                String genericTypeName = elemGenericType.getTypeName();
                if( genericTypeName.contains( "<" )  && genericTypeName.contains( ">" ) ) {
                    thisType = LinkedHashMap.class;
                }
            }

            Object    self;

            if( thisType == null ) {
                self = new Object(); // Dummy
                ObjectJSONCursorUnmarshal.INNER_OBJECT_DECODER.decode( self, parent, indexKey,this, elemGenericType );
                return self;
            }

            if( args != null && args.length > 0 ) {
                Object dyType = args[ 0 ];
                Type eleType  = (Type) dyType;
                if( eleType != null ) {
                    if( parent != null && parent.getClass().isArray() ) {
                        if( !dyType.equals( Object[].class ) && !dyType.equals( Object.class ) && !dyType.equals( Map.class ) ) {
                            thisType = (Class<?>) eleType;
                        }
                    }
                    else {
                        String[] genericTypeNames = ReflectionUtils.extractGenericClassNames( eleType.getTypeName() );
                        if( genericTypeNames != null && genericTypeNames.length > 0 ) {
                            String genericTypeName;
                            if( genericTypeNames.length > 1 ) {
                                genericTypeName = genericTypeNames[ 1 ]; // Map value.
                            }
                            else {
                                genericTypeName = genericTypeNames[ 0 ]; // Collection value.
                            }

                            if( !genericTypeName.equals( "?" ) && !genericTypeName.equals( Object.class.getSimpleName() ) ) {
                                try{
                                    thisType = this.getClass().getClassLoader().loadClass( genericTypeName );
                                    thisType = this.findDirectJSONObjectAssignableType( thisType );
                                }
                                catch ( ClassNotFoundException e ) {
                                    thisType = JSONMaptron.class;
                                }
                            }
                        }
                    }
                }
            }
            self     = thisType.getConstructor().newInstance();

            ObjectJSONCursorUnmarshal.INNER_OBJECT_DECODER.decode( self, parent, indexKey, this, elemGenericType );
            return self;
        }
        catch ( NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            return null;
        }
        catch ( InstantiationException e1 ) {
            throw new JSONParseException( e1 );
        }
    }


    public static class GenericTypeContext {
        protected Class<? > thisType    ;
        protected Type elemGenericType  ;
    }
}


