package com.auto.proto;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.pinecone.framework.lang.field.FieldEntity;
import com.pinecone.framework.lang.field.GenericFieldEntity;
import com.pinecone.ulf.util.protobuf.GenericFieldProtobufDecoder;
import com.pinecone.ulf.util.protobuf.GenericFieldProtobufEncoder;
import com.pinecone.ulf.util.protobuf.Options;
import com.pinecone.ulf.util.protobuf.map.MapProtobufException;

public class ProtoMapDevilCase {
    protected final GenericFieldProtobufEncoder encoder = new GenericFieldProtobufEncoder();
    protected final GenericFieldProtobufDecoder decoder = new GenericFieldProtobufDecoder();

    public void run() {
        this.assertBeanMapRoundTrip();
        this.assertFieldEntityMapRoundTrip();
        this.assertDynamicMapCompatibility();
        this.assertIllegalMapsFail();
    }

    protected void assertBeanMapRoundTrip() {
        ProtoMapPayload sample = ProtoMapPayload.sample();

        Descriptors.Descriptor descriptor = this.encoder.transform( ProtoMapPayload.class, sample, null, Options.DefaultOptions );
        ProtoMapAssertions.assertMapField( descriptor, "metadata" );
        ProtoMapAssertions.assertMapField( descriptor, "longLabels" );
        ProtoMapAssertions.assertMapField( descriptor, "switchCounters" );
        ProtoMapAssertions.assertMapField( descriptor, "nestedPayloads" );
        ProtoMapAssertions.assertRepeatedMessageField( descriptor, "nestedList" );

        DynamicMessage message = this.encoder.encode( descriptor, sample, null, Options.DefaultOptions );
        ProtoMapPayload decoded = this.decoder.decode( ProtoMapPayload.class, descriptor, message, null, Options.DefaultOptions );

        ProtoMapAssertions.assertMapEquals( sample.getMetadata(), decoded.getMetadata(), "metadata should round trip." );
        ProtoMapAssertions.assertMapEquals( sample.getLongLabels(), decoded.getLongLabels(), "longLabels should round trip." );
        ProtoMapAssertions.assertMapEquals( sample.getSwitchCounters(), decoded.getSwitchCounters(), "switchCounters should round trip." );
        ProtoMapAssertions.assertEquals(
                sample.getNestedPayloads().get( "red" ).getName(),
                decoded.getNestedPayloads().get( "red" ).getName(),
                "nested map value name should round trip."
        );
        ProtoMapAssertions.assertEquals(
                sample.getNestedPayloads().get( "blue" ).getScore(),
                decoded.getNestedPayloads().get( "blue" ).getScore(),
                "nested map value score should round trip."
        );
        ProtoMapAssertions.assertEquals(
                sample.getNestedList().size(),
                decoded.getNestedList().size(),
                "nested list size should round trip."
        );
        ProtoMapAssertions.assertEquals(
                sample.getNestedList().get( 0 ).getName(),
                decoded.getNestedList().get( 0 ).getName(),
                "nested list first value name should round trip."
        );
        ProtoMapAssertions.assertEquals(
                sample.getNestedList().get( 1 ).getScore(),
                decoded.getNestedList().get( 1 ).getScore(),
                "nested list second value score should round trip."
        );
    }

    protected void assertFieldEntityMapRoundTrip() {
        Map<String, String> metadata = new LinkedHashMap<>();
        metadata.put( "node", "praetor" );
        metadata.put( "mode", "devil" );

        @SuppressWarnings( "unchecked" )
        FieldEntity[] fields = new FieldEntity[] {
                new GenericFieldEntity( "metadata", metadata, Map.class, "java.util.Map<java.lang.String, java.lang.String>" )
        };

        Descriptors.Descriptor descriptor = this.encoder.transform( fields, "ProtoMapFieldEntityPayload", null, Options.DefaultOptions );
        ProtoMapAssertions.assertMapField( descriptor, "metadata" );

        DynamicMessage message = this.encoder.encode( descriptor, fields, null, Options.DefaultOptions );
        FieldEntity[] decodedFields = new FieldEntity[] {
                new GenericFieldEntity( "metadata", null, Map.class, "java.util.Map<java.lang.String, java.lang.String>" )
        };
        Object[] values = this.decoder.decodeValues( decodedFields, descriptor, message, null, Options.DefaultOptions );
        ProtoMapAssertions.assertNotNull( values, "Decoded field values are null." );
        ProtoMapAssertions.assertMapEquals( metadata, (Map<?, ?>) values[ 0 ], "FieldEntity metadata should round trip." );
    }

    protected void assertDynamicMapCompatibility() {
        Map<String, Object> dynamic = new LinkedHashMap<>();
        dynamic.put( "name", "legacy" );
        dynamic.put( "count", 3 );

        Descriptors.Descriptor descriptor = this.encoder.transform( dynamic, null, Options.DefaultOptions );
        ProtoMapAssertions.assertTrue( !descriptor.findFieldByName( "name" ).isMapField(), "Legacy dynamic map should not become proto map." );

        DynamicMessage message = this.encoder.encode( descriptor, dynamic, null, Options.DefaultOptions );
        Map<String, Object> decoded = this.decoder.decodeMap( LinkedHashMap.class, descriptor, message, null, Options.DefaultOptions );
        ProtoMapAssertions.assertEquals( dynamic.get( "name" ), decoded.get( "name" ), "Legacy dynamic string should round trip." );
        ProtoMapAssertions.assertEquals( dynamic.get( "count" ), decoded.get( "count" ), "Legacy dynamic integer should round trip." );
    }

    protected void assertIllegalMapsFail() {
        this.assertFails( () -> this.encoder.transform( ProtoRawMapPayload.class, new ProtoRawMapPayload(), null, Options.DefaultOptions ), "Raw Map should fail." );
        this.assertFails( () -> this.encoder.transform( ProtoObjectMapPayload.class, new ProtoObjectMapPayload(), null, Options.DefaultOptions ), "Map<Object,Object> should fail." );
    }

    protected void assertFails( Runnable runnable, String message ) {
        try {
            runnable.run();
        }
        catch ( MapProtobufException | IllegalArgumentException expected ) {
            return;
        }
        catch ( RuntimeException expected ) {
            Throwable cause = expected.getCause();
            while ( cause != null ) {
                if ( cause instanceof MapProtobufException ) {
                    return;
                }
                cause = cause.getCause();
            }
            throw expected;
        }
        throw new IllegalStateException( message );
    }
}
