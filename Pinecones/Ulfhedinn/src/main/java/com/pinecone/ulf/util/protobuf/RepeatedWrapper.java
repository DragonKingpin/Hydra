package com.pinecone.ulf.util.protobuf;

import java.util.Collection;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.pinecone.framework.system.prototype.Pinenut;

public class RepeatedWrapper<T> implements Pinenut {
    public final static String FieldName = "values";

    private T values;

    private final Class<?> componentType;

    public RepeatedWrapper( T value, Class<?> componentType ) {
        if ( value == null || RepeatedWrapper.isSupportedRepeated(value) ) {
            this.values = value;
            this.componentType = componentType;
        }
        else {
            throw new IllegalArgumentException( "Unsupported repeated type: " + value.getClass() );
        }
    }

    public RepeatedWrapper( T value ) {
        this( value, value.getClass().getComponentType() );
    }

    public T getValues() {
        return this.values;
    }

    public void setValues( T values ) {
        this.values = values;
    }

    public Class<?> getComponentType() {
        return this.componentType;
    }

    public boolean isRepeated() {
        return this.values == null || RepeatedWrapper.isSupportedRepeated(this.values);
    }

    public static boolean isSupportedRepeated( Object obj ) {
        return RepeatedWrapper.isSupportedRepeated( obj.getClass() );
    }

    public static boolean isSupportedRepeated( Class<?> obj ) {
        return obj.isArray() || Collection.class.isAssignableFrom( obj ) || !BeanProtobufEncoder.DefaultEncoder.reinterpret( obj ).equals( DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE ) ;
    }

    public static <T> RepeatedWrapper<T> wrap( T val ) {
        return new RepeatedWrapper<>( val );
    }

    public static <T> RepeatedWrapper<T> wrap( T val, Class<?> componentType ) {
        return new RepeatedWrapper<>( val, componentType );
    }

    public Descriptors.Descriptor transform() {
        return RepeatedWrapper.transform( this.values.getClass(), this.componentType );
    }

    public static Descriptors.Descriptor transform( Class<?> elemClass, Class<?> componentType ) {
        try{
            DescriptorProtos.DescriptorProto.Builder descriptorBuilder = DescriptorProtos.DescriptorProto.newBuilder();
            String szEntityName = RepeatedWrapper.class.getSimpleName() + "_" + elemClass.getSimpleName();
            szEntityName = szEntityName.replace( "[]", "_ARRAY" );
            descriptorBuilder.setName( szEntityName );

            DescriptorProtos.FieldDescriptorProto.Type fieldType = BeanProtobufEncoder.DefaultEncoder.reinterpret( componentType );

            DescriptorProtos.FieldDescriptorProto.Builder fieldBuilder = DescriptorProtos.FieldDescriptorProto.newBuilder()
                    .setName( RepeatedWrapper.FieldName )
                    .setNumber( 1 )
                    .setType( fieldType )
                    .setLabel( DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED );

            descriptorBuilder.addField( fieldBuilder );

            Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(
                    DescriptorProtos.FileDescriptorProto.newBuilder()
                            .setName( szEntityName + "$REPEATED_FILE" )
                            .addMessageType( descriptorBuilder.build() )
                            .build(),
                    new Descriptors.FileDescriptor[0]);

            return fileDescriptor.findMessageTypeByName( szEntityName );
        }
        catch ( Descriptors.DescriptorValidationException e ) {
            return null;
        }
    }

}
