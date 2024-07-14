package com.pinecone.framework.util.config;

import com.pinecone.framework.unit.LinkedTreeMap;

import java.util.Map;

public class GenericStartupCommandParser implements StartupCommandParser {
    private String[] mValueStartSymbols = { "", "--", "-", "/", "\\" };
    private String[] mAssignmentSymbols = { "=", ":", "=>", "->" };
    private String[] mValueSeparators   = { ",", ";", "|" };

    public GenericStartupCommandParser() {}

    public GenericStartupCommandParser( String[] valueStartSymbols, String[] assignmentSymbols, String[] valueSeparators ) {
        this.mValueStartSymbols = valueStartSymbols;
        this.mAssignmentSymbols = assignmentSymbols;
        this.mValueSeparators = valueSeparators;
    }

    @Override
    public Map<String, String[] > parse( String[] args ) {
        Map<String, String[] > result = new LinkedTreeMap<>();

        for ( String arg : args ) {
            String key   = null;
            String value = null;

            for ( String startSymbol : this.mValueStartSymbols ) {
                if ( arg.startsWith( startSymbol ) ) {
                    int assignmentIndex = -1;
                    for ( String assignmentSymbol : this.mAssignmentSymbols ) {
                        int index = arg.indexOf(assignmentSymbol, startSymbol.length());
                        if ( index > 0 ) {
                            assignmentIndex = index;
                            break;
                        }
                    }

                    if ( assignmentIndex > 0 ) {
                        key   = arg.substring(startSymbol.length(), assignmentIndex);
                        value = arg.substring(assignmentIndex + 1);
                    }
                    else {
                        key   = arg.substring(startSymbol.length());
                        value = "";
                    }
                    break;
                }
            }

            if ( key != null ) {
                String[] values = this.splitValues( value );
                result.put(key, values);
            }
        }

        return result;
    }

    private String[] splitValues( String value ) {
        if ( value.isEmpty() ) {
            return new String[0];
        }

        for ( String separator : this.mValueSeparators ) {
            if ( value.contains( separator ) ) {
                return value.split(java.util.regex.Pattern.quote(separator));
            }
        }
        return new String[]{ value };
    }

}
