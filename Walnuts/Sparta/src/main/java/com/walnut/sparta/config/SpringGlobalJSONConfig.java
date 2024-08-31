package com.walnut.sparta.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pinecone.framework.util.id.GUID;

@Configuration
public class SpringGlobalJSONConfig {
    @Bean
    public ObjectMapper objectMapper( Jackson2ObjectMapperBuilder builder ) {
        ObjectMapper mapper = builder.createXmlMapper( false ).build();
        SimpleModule module = new SimpleModule();
        module.addSerializer( GUID.class, new JsonSerializer<GUID> () {
            @Override
            public void serialize( GUID value, JsonGenerator gen, SerializerProvider serializers ) throws IOException {
                gen.writeString(value.toString());
            }
        });

        mapper.registerModule(module);
        return mapper;
    }
}