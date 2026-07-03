package com.auto.proto;

import java.util.Arrays;
import java.util.Map;

import com.google.protobuf.Descriptors;

public final class ProtoMapAssertions {
    private ProtoMapAssertions() {
    }

    public static void assertTrue( boolean value, String message ) {
        if ( !value ) {
            throw new IllegalStateException( message );
        }
    }

    public static void assertNotNull( Object value, String message ) {
        if ( value == null ) {
            throw new IllegalStateException( message );
        }
    }

    public static void assertEquals( Object expected, Object actual, String message ) {
        if ( expected == null && actual == null ) {
            return;
        }
        if ( expected != null && expected.equals( actual ) ) {
            return;
        }
        throw new IllegalStateException( message + " expected => " + expected + ", actual => " + actual );
    }

    public static void assertBytesEquals( byte[] expected, byte[] actual, String message ) {
        if ( !Arrays.equals( expected, actual ) ) {
            throw new IllegalStateException( message );
        }
    }

    public static void assertMapEquals( Map<?, ?> expected, Map<?, ?> actual, String message ) {
        assertNotNull( actual, message + " actual map is null." );
        assertEquals( expected.size(), actual.size(), message + " map size mismatch." );
        for ( Map.Entry<?, ?> entry : expected.entrySet() ) {
            assertTrue( actual.containsKey( entry.getKey() ), message + " missing key: " + entry.getKey() );
            assertEquals( entry.getValue(), actual.get( entry.getKey() ), message + " value mismatch for key: " + entry.getKey() );
        }
    }

    public static Descriptors.FieldDescriptor assertMapField( Descriptors.Descriptor descriptor, String fieldName ) {
        assertNotNull( descriptor, "Descriptor is null." );
        Descriptors.FieldDescriptor fieldDescriptor = descriptor.findFieldByName( fieldName );
        assertNotNull( fieldDescriptor, "Map field not found: " + fieldName );
        assertTrue( fieldDescriptor.isMapField(), "Field should be protobuf map: " + fieldName );
        return fieldDescriptor;
    }

    public static Descriptors.FieldDescriptor assertRepeatedMessageField( Descriptors.Descriptor descriptor, String fieldName ) {
        assertNotNull( descriptor, "Descriptor is null." );
        Descriptors.FieldDescriptor fieldDescriptor = descriptor.findFieldByName( fieldName );
        assertNotNull( fieldDescriptor, "Repeated message field not found: " + fieldName );
        assertTrue( fieldDescriptor.isRepeated(), "Field should be repeated: " + fieldName );
        assertEquals(
                Descriptors.FieldDescriptor.Type.MESSAGE,
                fieldDescriptor.getType(),
                "Field should be repeated message: " + fieldName
        );
        return fieldDescriptor;
    }
}
