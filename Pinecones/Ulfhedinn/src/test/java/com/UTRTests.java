package com;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.template.UniformTemplateRenderer;
import com.pinecone.ulf.util.template.UTRFreeMarkerProvider;
import com.pinecone.ulf.util.template.UTRThymeleafProvider;

import org.junit.jupiter.api.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;

class DummyBean {
    private String key1;

    public String getKey1() {
        return this.key1;
    }

    public void setKey1( String key1 ) {
        this.key1 = key1;
    }
}

public class UTRTests {
    @Test
    void testThymeleaf() {
        UniformTemplateRenderer renderer = new UTRThymeleafProvider();
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("key1", "Test");

        DummyBean variables = new DummyBean();
        variables.setKey1( "BeanTest" );

        String jsonTemplate = "{ \"name\": \"[(${key1})]\" }";
        Debug.trace( renderer.render( jsonTemplate, variables ) );
    }

    @Test
    void testAlmondUTR() {
        UniformTemplateRenderer renderer = UniformTemplateRenderer.DefaultRenderer;

        Map<String, Object> variables = new HashMap<>();
        variables.put("key1", "Test");
        Debug.trace( renderer.render( "{ \"name\": \"${key1}\" }", variables ) );
    }

    @Test
    void testFreemarker() {
        Configuration cfg = new Configuration( Configuration.VERSION_2_3_31 );
        cfg.setDefaultEncoding("UTF-8");

        UTRFreeMarkerProvider provider = new UTRFreeMarkerProvider( cfg );

        Map<String, Object> variables = new JSONMaptron( "{ name: test, age: 30, more: { key: 1.364 } }" );

        String templateContent = "{ \"name\": \"${name}\", \"age\": ${age}, \"more.key\": ${more.key} }";

        String result = provider.render(templateContent, variables);
        Debug.echo( result );
    }
}
