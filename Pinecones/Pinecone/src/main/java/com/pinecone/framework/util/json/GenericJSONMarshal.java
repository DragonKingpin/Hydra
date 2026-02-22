package com.pinecone.framework.util.json;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.ReflectionUtils;
import com.pinecone.framework.util.json.handler.EncodeHandlerRegistry;
import com.pinecone.framework.util.json.handler.GenericEncodeHandlerRegistry;
import com.pinecone.framework.util.json.handler.JSONObjectEncodeHandler;
import com.pinecone.framework.util.json.homotype.AnnotatedJSONInjector;
import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;
import com.pinecone.framework.util.json.homotype.GenericBeanJSONEncoder;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GenericJSONMarshal extends GenericJSONEncoder implements JSONMarshal {

    protected long                   mnMode;
    protected BeanJSONEncoder        mBeanEncoder;
    protected EncodeHandlerRegistry  mEncodeHandlerRegistry;


    public GenericJSONMarshal( long mode, @Nullable BeanJSONEncoder beanEncoder, @Nullable EncodeHandlerRegistry registry ) {
        super();
        this.mnMode = mode;

        if ( beanEncoder == null ) {
            this.mBeanEncoder = new RecursiveBeanJSONEncoder( this );
        }
        else {
            this.mBeanEncoder = beanEncoder;
        }

        if ( registry == null ) {
            this.mEncodeHandlerRegistry = new GenericEncodeHandlerRegistry();
        }
        else {
            this.mEncodeHandlerRegistry = registry;
        }
    }

    public GenericJSONMarshal( long mode ) {
        this( mode, null, null );
    }

    public GenericJSONMarshal() {
        this( JSONMarshalMode.MODE_DEFAULT );
    }


    @Override
    public void setMode( long mode ) {
        this.mnMode = mode;
    }

    @Override
    public long getMode() {
        return this.mnMode;
    }

    @Override
    public void setBeanEncoder( BeanJSONEncoder encoder ) {
        this.mBeanEncoder = encoder;
    }

    @Override
    public BeanJSONEncoder getBeanEncoder() {
        return this.mBeanEncoder;
    }

    @Override
    public void setEncodeHandlerRegistry( EncodeHandlerRegistry registry ) {
        this.mEncodeHandlerRegistry = registry;
    }

    @Override
    public EncodeHandlerRegistry getEncodeHandlerRegistry() {
        return this.mEncodeHandlerRegistry;
    }


    public <T> void registerEncodeHandler( Class<T> type, JSONObjectEncodeHandler<? super T> handler ) {
        this.mEncodeHandlerRegistry.register( type, handler );
    }

    protected boolean tryCustomEncodeHandler( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( that == null ) {
            return false;
        }

        JSONObjectEncodeHandler<Object> handler = this.mEncodeHandlerRegistry.get( that.getClass() );
        if ( handler == null ) {
            return false;
        }

        handler.serialize( that, writer, nIndentFactor, nIndentBlankNum, this );

        return true;
    }

    protected boolean tryBeanMode( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( ( this.mnMode & JSONMarshalMode.MODE_BEAN_GETTER ) == 0L ) {
            return false;
        }

        this.mBeanEncoder.encode( that, writer, nIndentFactor, nIndentBlankNum );

        return true;
    }

    protected void collectAnnotatedFields( Object that, List<Object[]> list ) {
        Field[] fields = that.getClass().getDeclaredFields();

        for ( Field field : fields ) {
            ReflectionUtils.makeAccessible( field );
            String szKey = AnnotatedJSONInjector.getAnnotatedKey( field );
            if ( szKey == null ) {
                continue;
            }

            if ( szKey.isEmpty() ) {
                szKey = field.getName();
            }

            Object value;
            try {
                value = field.get( that );
            }
            catch ( IllegalAccessException e ) {
                value = null;
            }

            list.add( new Object[]{ szKey, field, value } );
        }
    }

    protected void collectAnyFields( Object that, List<Object[]> list ) {
        Field[] fields = that.getClass().getDeclaredFields();
        for ( Field field : fields ) {
            ReflectionUtils.makeAccessible( field );

            String szKey = field.getName();

            Object value;
            try {
                value = field.get( that );
            }
            catch ( IllegalAccessException e ) {
                value = null;
            }

            list.add( new Object[]{ szKey, field, value } );
        }
    }

    protected void collectPublicFields( Object that, List<Object[]> list ) {
        Field[] fields = that.getClass().getFields();

        for ( Field field : fields ) {
            String szKey = field.getName();

            Object value;
            try {
                value = field.get( that );
            }
            catch ( IllegalAccessException e ) {
                value = null;
            }

            list.add( new Object[]{ szKey, field, value } );
        }
    }


    @Override
    public Writer writeUnidentifiedObject( Object that, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
        if ( this.tryCustomEncodeHandler( that, writer, nIndentFactor, nIndentBlankNum ) ) {
            return writer;
        }

        if ( this.tryBeanMode( that, writer, nIndentFactor, nIndentBlankNum ) ) {
            return writer;
        }

        List<Object[]> list = new ArrayList<>();
        if ( ( this.mnMode & JSONMarshalMode.MODE_ANNOTATED_FIELD ) != 0L ) {
            this.collectAnnotatedFields( that, list );
        }
        if ( ( this.mnMode & JSONMarshalMode.MODE_ANY_FIELD ) != 0L ) {
            this.collectAnyFields( that, list );
        }
        if ( ( this.mnMode & JSONMarshalMode.MODE_PUBLIC_FIELD ) != 0L ) {
            this.collectPublicFields( that, list );
        }

        if ( list.isEmpty() ) {
            return super.writeUnidentifiedObject( that, writer, nIndentFactor, nIndentBlankNum);
        }

        writer.write( '{' );
        boolean bHasNextElement = false;
        int nNewIndent = nIndentBlankNum + nIndentFactor;

        int i = 0;
        for ( Object[] item : list ) {
            GenericJSONEncoder.beforeJsonElementWrote( writer, nIndentFactor, nNewIndent, bHasNextElement );
            this.writeKeyValue( writer, item[0], item[2], nIndentFactor, nIndentBlankNum );
            bHasNextElement = true;
            ++i;
        }

        if ( nIndentFactor > 0 ) {
            writer.write( '\n' );
        }

        GenericJSONEncoder.indentBlank( writer, nIndentBlankNum);
        writer.write( '}' );
        return writer;
    }


    public static class RecursiveBeanJSONEncoder extends GenericBeanJSONEncoder {
        protected GenericJSONMarshal mJSONMarshal;

        public RecursiveBeanJSONEncoder( GenericJSONMarshal marshal ) {
            this.mJSONMarshal = marshal;
        }

        @Override
        public String valueJsonify( Object val ) {
            StringWriter w = new StringWriter();
            try {
                synchronized( w.getBuffer() ) {
                    this.valueJsonify( val, w, 0,0 );
                    return w.toString();
                }
            }
            catch ( IOException e ){
                return null;
            }
        }

        @Override
        public void valueJsonify( Object val, Writer writer, int nIndentFactor, int nIndentBlankNum ) throws IOException {
            this.mJSONMarshal.write( val, writer, nIndentFactor, nIndentBlankNum );
        }
    }

}
