package com.pinecone.ulf.util.template;

import java.io.Writer;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import com.pinecone.framework.system.prototype.Objectom;
import com.pinecone.framework.util.template.UniformTemplateRenderer;

public class UTRThymeleafProvider implements UniformTemplateRenderer {
    protected TemplateEngine templateEngine;

    public UTRThymeleafProvider( TemplateEngine engine ) {
        this.templateEngine = engine;
    }

    public UTRThymeleafProvider( TemplateEngine engine, ITemplateResolver resolver ) {
        this( engine );
        this.templateEngine.setTemplateResolver( resolver );
    }

    public UTRThymeleafProvider() {
        this( new TemplateEngine(), new StringTemplateResolver() );
    }

    protected Context makeContext( Objectom vars ) {
        Context context = new Context();

        Object proto = vars.prototype().proto();
        if( proto instanceof Map ) {
            context.setVariables( (Map)proto );
        }
        else {
            context.setVariables( vars.toMap() );
        }

        return context;
    }

    @Override
    public String render( String tpl, Objectom vars ) {
        Context context = this.makeContext( vars );
        return this.templateEngine.process( tpl, context );
    }

    @Override
    public void render( String tpl, Objectom vars, Writer writer ) {
        Context context = this.makeContext( vars );
        this.templateEngine.process( tpl, context, writer );
    }
}
