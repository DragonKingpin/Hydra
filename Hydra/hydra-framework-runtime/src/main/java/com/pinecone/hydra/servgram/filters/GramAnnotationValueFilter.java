package com.pinecone.hydra.servgram.filters;

import com.pinecone.hydra.servgram.Gram;
import javassist.bytecode.annotation.Annotation;

public class GramAnnotationValueFilter implements AnnotationValueFilter {
    public boolean match( Annotation that, String destinationName ) {
        if( that.getTypeName().equals( com.pinecone.hydra.servgram.Gram.class.getName() ) ) {
            String szAN = that.getMemberValue( Gram.ValueKey ).toString();
            if( szAN.startsWith( "\"" ) ){
                return !szAN.equals("\"" + destinationName + "\"");
            }
            return !szAN.equals( destinationName );
        }

        return true;
    }
}
