package com.pinecone.hydra.storage.file.journal;

import com.pinecone.framework.system.prototype.Pinenut;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JournalPayloadParser implements Pinenut {
    public String stringValue( String payload, String key ) {
        Matcher matcher = Pattern.compile( "\"" + Pattern.quote( key ) + "\"\\s*:\\s*(null|\"((?:\\\\.|[^\"])*)\")" ).matcher( payload == null ? "" : payload );
        if ( !matcher.find() || "null".equals( matcher.group( 1 ) ) ) {
            return null;
        }
        return matcher.group( 2 ).replace( "\\\"", "\"" ).replace( "\\\\", "\\" );
    }

    public long longValue( String payload, String key, long defaultValue ) {
        Matcher matcher = Pattern.compile( "\"" + Pattern.quote( key ) + "\"\\s*:\\s*(-?\\d+)" ).matcher( payload == null ? "" : payload );
        if ( !matcher.find() ) {
            return defaultValue;
        }
        return Long.parseLong( matcher.group( 1 ) );
    }
}
