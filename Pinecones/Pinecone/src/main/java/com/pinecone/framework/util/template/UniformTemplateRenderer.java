package com.pinecone.framework.util.template;

import java.io.IOException;
import java.io.Writer;

import com.pinecone.framework.system.prototype.Objectom;
import com.pinecone.framework.system.prototype.Pinenut;

public interface UniformTemplateRenderer extends Pinenut {
    UniformTemplateRenderer DefaultRenderer = new UTRAlmondProvider();

    String render( String tpl, Objectom context );

    default String render( String tpl, Object context ) {
        return this.render( tpl, Objectom.wrap( context ) );
    }

    void render( String tpl, Objectom context, Writer writer ) ;

    default void render( String tpl, Object context, Writer writer ) {
        this.render( tpl, Objectom.wrap( context ), writer );
    }
}
