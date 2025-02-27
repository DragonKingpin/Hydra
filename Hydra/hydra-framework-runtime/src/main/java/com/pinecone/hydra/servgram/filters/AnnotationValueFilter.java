package com.pinecone.hydra.servgram.filters;

import com.pinecone.framework.system.prototype.Pinenut;
import javassist.bytecode.annotation.Annotation;

public interface AnnotationValueFilter extends Pinenut {
    boolean match( Annotation that, String destinationName );
}
