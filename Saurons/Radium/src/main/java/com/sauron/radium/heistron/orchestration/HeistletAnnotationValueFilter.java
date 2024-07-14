package com.sauron.radium.heistron.orchestration;

import com.pinecone.hydra.servgram.filters.AnnotationValueFilter;
import javassist.bytecode.annotation.Annotation;

public class HeistletAnnotationValueFilter implements AnnotationValueFilter {
    public boolean match(Annotation that, String destinationName ) {
        if( that.getTypeName().equals( com.sauron.radium.heistron.orchestration.Heistlet.class.getName() ) ) {
            String szAN = that.getMemberValue( Heistlet.ValueKey ).toString();
            if( szAN.startsWith( "\"" ) ){
                return !szAN.equals("\"" + destinationName + "\"");
            }
            return !szAN.equals( destinationName );
        }

        return true;
    }
}
