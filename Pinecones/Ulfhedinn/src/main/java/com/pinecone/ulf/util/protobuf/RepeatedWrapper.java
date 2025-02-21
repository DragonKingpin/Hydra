package com.pinecone.ulf.util.protobuf;

import java.util.Collection;
import java.util.Set;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.unit.Units;
import com.pinecone.framework.util.ClassUtils;

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

    public Descriptors.Descriptor transform( BeanProtobufEncoder encoder ) {
        return RepeatedWrapper.transform( this.values.getClass(), this.componentType, encoder );
    }

    public static Descriptors.Descriptor transform( Class<?> elemClass, Class<?> componentType, BeanProtobufEncoder encoder ) {
        try{
            DescriptorProtos.DescriptorProto.Builder descriptorBuilder = DescriptorProtos.DescriptorProto.newBuilder();
            String szEntityName = RepeatedWrapper.class.getSimpleName() + "_" + elemClass.getSimpleName();
            szEntityName = szEntityName.replace( ClassUtils.ARRAY_SUFFIX, WolfProtobufConstants.ArrayTransformedName );
            descriptorBuilder.setName( szEntityName );

            DescriptorProtos.FieldDescriptorProto.Type fieldType = BeanProtobufEncoder.DefaultEncoder.reinterpret( componentType );

            DescriptorProtos.FieldDescriptorProto.Builder fieldBuilder = DescriptorProtos.FieldDescriptorProto.newBuilder()
                    .setName( RepeatedWrapper.FieldName )
                    .setNumber( 1 )
                    .setType( fieldType )
                    .setLabel( DescriptorProtos.FieldDescriptorProto.Label.LABEL_REPEATED );

            Descriptors.FileDescriptor[] objectDep = new Descriptors.FileDescriptor[0];
            if ( fieldType == DescriptorProtos.FieldDescriptorProto.Type.TYPE_MESSAGE ) {
                Descriptors.Descriptor nestedDescriptor = encoder.transform( componentType, null, Units.emptySet() );

                if ( nestedDescriptor != null ) {
                    fieldBuilder.setTypeName( nestedDescriptor.getFullName() );
                    objectDep = new Descriptors.FileDescriptor[] { nestedDescriptor.getFile() };
                }
            }
            descriptorBuilder.addField( fieldBuilder );

            Descriptors.FileDescriptor fileDescriptor = Descriptors.FileDescriptor.buildFrom(
                    DescriptorProtos.FileDescriptorProto.newBuilder()
                            .setName( szEntityName + "$REPEATED_FILE" )
                            .addMessageType( descriptorBuilder.build() )
                            .build(),
                    objectDep);

            return fileDescriptor.findMessageTypeByName( szEntityName );
        }
        catch ( Descriptors.DescriptorValidationException e ) {
            return null;
        }
    }

}
