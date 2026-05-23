package com.pinecone.framework.util.config;

import com.pinecone.framework.unit.LinkedTreeMap;

import java.util.HashMap;
import java.util.Map;

public class GenericStartupCommandParser implements StartupCommandParser {
    private String[] mValueStartSymbols = { "--", "-", "/", "\\", "" };
    private String[] mAssignmentSymbols = { "=", ":", "=>", "->" };

    public GenericStartupCommandParser() {}

    public GenericStartupCommandParser( String[] valueStartSymbols, String[] assignmentSymbols, String[] valueSeparators ) {
        this.mValueStartSymbols = valueStartSymbols;
        this.mAssignmentSymbols = assignmentSymbols;
    }

    @Override
    public Map<String, String > parse( String[] args ) {
        Map<String, String > result = new LinkedTreeMap<>();

        for ( String arg : args ) {
            String key   = null;
            String value = null;

            for ( String startSymbol : this.mValueStartSymbols ) {
                if ( arg.startsWith( startSymbol ) ) {
                    int assignmentIndex = -1;
                    String matchedAssignmentSymbol = null;
                    for ( String assignmentSymbol : this.mAssignmentSymbols ) {
                        int index = arg.indexOf(assignmentSymbol, startSymbol.length());
                        if ( index > 0 ) {
                            assignmentIndex = index;
                            matchedAssignmentSymbol = assignmentSymbol;
                            break;
                        }
                    }

                    if ( assignmentIndex > 0 ) {
                        key   = arg.substring(startSymbol.length(), assignmentIndex);
                        value = arg.substring(assignmentIndex + matchedAssignmentSymbol.length());
                    }
                    else {
                        key   = arg.substring(startSymbol.length());
                        value = "";
                    }
                    break;
                }
            }

            if ( key != null ) {
                result.put(key, value);
            }
        }

        return result;
    }

    @Override
    public Map<String, String> parse( Map<String, String> args ) {
        Map<String, String> map = new HashMap<>( args.size() );

        for ( String key : args.keySet() ) {
            String value = args.get(key);
            if ( value == null ) {
                value = "";
            }
            map.put( key, value );
        }

        return map;
    }

}
