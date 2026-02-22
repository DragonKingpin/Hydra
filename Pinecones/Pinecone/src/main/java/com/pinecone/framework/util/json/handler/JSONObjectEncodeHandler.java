package com.pinecone.framework.util.json.handler;

import java.io.IOException;
import java.io.Writer;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.GenericJSONEncoder;

public interface JSONObjectEncodeHandler<T> extends Pinenut {

    void serialize( T object, Writer writer, int nIndentFactor, int nIndentBlankNum, GenericJSONEncoder encoder ) throws IOException;

}
