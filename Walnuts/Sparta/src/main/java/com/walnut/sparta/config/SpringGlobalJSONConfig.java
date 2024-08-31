package com.walnut.sparta.config;

import java.io.IOException;

import com.pinecone.framework.util.uoi.UOI;
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
        // 为 GUID 类添加序列化器
        module.addSerializer(GUID.class, new JsonSerializer<GUID>() {
            @Override
            public void serialize(GUID value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString());
            }
        });

        // 为 UOI 类添加序列化器
        module.addSerializer(UOI.class, new JsonSerializer<UOI>() {
            @Override
            public void serialize(UOI value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString()); // 假设 UOI 类也有 toString 方法
            }
        });

        mapper.registerModule(module);
        return mapper;
    }
}