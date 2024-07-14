package com.sauron.radium.heistron.chronic;

import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;

import javassist.bytecode.annotation.Annotation;

public class RaiderletAnnotationValueFilter implements AnnotationValueFilter {
    public boolean match( Annotation that, String destinationName ) {
        if( that.getTypeName().equals( com.sauron.radium.heistron.chronic.Raiderlet.class.getName() ) ) {
            String szAN = that.getMemberValue( Raiderlet.ValueKey ).toString();
            if( szAN.startsWith( "\"" ) ){
                return !szAN.equals("\"" + destinationName + "\"");
            }
            return !szAN.equals( destinationName );
        }

        return true;
    }
}
