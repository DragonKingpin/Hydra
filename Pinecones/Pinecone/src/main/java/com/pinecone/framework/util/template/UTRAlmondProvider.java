package com.pinecone.framework.util.template;

import java.io.IOException;
import java.io.Writer;

import com.pinecone.framework.system.ProxyProvokeHandleException;
import com.pinecone.framework.system.prototype.Objectom;

public class UTRAlmondProvider implements UniformTemplateRenderer {
    @Override
    public String render( String tpl, Objectom context ){
        TemplateParser parser = new TemplateParser( tpl, context );
        return parser.eval();
    }

    @Override
    public void render( String tpl, Objectom context, Writer writer ) {
        // TODO
        try{
            writer.write( this.render( tpl, context ) );
        }
        catch ( IOException e ) {
            throw new ProxyProvokeHandleException( e );
        }
    }
}
