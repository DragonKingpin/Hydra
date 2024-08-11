package com.pinecone.ulf.util.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.prototype.Objectom;
import com.pinecone.framework.util.template.UniformTemplateRenderer;

public class UTRFreeMarkerProvider implements UniformTemplateRenderer {
    private Configuration configuration;

    public UTRFreeMarkerProvider( Configuration configuration ) {
        this.configuration = configuration;
    }

    public UTRFreeMarkerProvider() {
        this( new Configuration(Configuration.VERSION_2_3_31) );
    }

    protected Map<String, Object> makeContext( Objectom vars ) {
        Object proto = vars.prototype().proto();
        if (proto instanceof Map) {
            return (Map<String, Object>) proto;
        }
        else {
            return vars.toMap();
        }
    }

    @Override
    public String render(String tpl, Objectom vars) {
        try ( StringWriter writer = new StringWriter() ) {
            this.render( tpl, vars, writer );
            return writer.toString();
        }
        catch ( IOException e ) {
            throw new ProxyProvokeHandleException( "Error while rendering template", e );
        }
    }

    @Override
    public void render( String tpl, Objectom vars, Writer writer ) {
        try {
            Template template = new Template( "anonymous", tpl, this.configuration );
            Map<String, Object> context = this.makeContext(vars);
            template.process( context, writer );
        }
        catch ( TemplateException | IOException e ) {
            throw new ProxyProvokeHandleException( "Error while rendering template", e );
        }
    }
}