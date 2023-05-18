package Pinecone.Framework.Util.Summer.http;

import Pinecone.Framework.System.util.StringUtils;
import Pinecone.Framework.Util.JSON.JSONArray;
import Pinecone.Framework.Util.JSON.JSONObject;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 *  Pinecone For Java HttpEntityParser
 *  Copyright © Bean Nuts Foundation All rights reserved. [Mr.A.R.B / WJH]
 *  Tip:
 *  *****************************************************************************************
 *  Java Servlet is incredible stupid !!!
 *  What fuck did author think of merge $_GET and $_POST to ParameterMap ???
 *  And there is no separation method to separate it.
 *  So many scenarios, we need to separate those.
 *  Oh my goodness !!! Jesus fucking christ !!!
 *  *****************************************************************************************
 */
public class CommonHttpEntityParser implements HttpEntityParser {
    protected String    mszCharset = "UTF-8";

    public CommonHttpEntityParser( String szCharset ){
        if( szCharset != null ){
            this.mszCharset = szCharset;
        }
    }


    protected String getCharset(){
        return this.mszCharset;
    }

    private Object realValue( Object value, boolean bSafe ){
        if( value instanceof String ){
            String szValue = (String) value;
            return bSafe ? this.valueSafety( szValue ) : szValue;
        }
        return value;
    }

    private LinkedHashSet<String >  parameterMapStrings2Set( String[] strings ) {
        if( strings != null ){
            return new LinkedHashSet<>( Arrays.asList(strings) );
        }
        return null;
    }

    private Object  linkedHashSet2JSONValue( LinkedHashSet<String > set, boolean bSafe ) {
        JSONArray jsonArray = new JSONArray();
        if( set != null ){
            int nSetSize = set.size();
            for ( Object row : set ) {
                row = this.realValue( row, bSafe );
                if( nSetSize != 1 ){
                    jsonArray.put( row );
                }
                else {
                    return row;
                }
            }
        }
        return jsonArray;
    }

    private Object parameterMapValue2JSONValue( Object parameterMapValue, boolean bSafe ){
        Object jsonValue;

        if( null == parameterMapValue ){
            jsonValue = null;
        }
        else if ( parameterMapValue instanceof String[] ) {
            String[] strings = (String[]) parameterMapValue;
            jsonValue = new JSONArray();
            if( strings.length == 1 ){
                jsonValue = this.realValue( strings[0], bSafe );
            }
            else {
                for ( String str : strings ) {
                    ((JSONArray) jsonValue).put( this.realValue( str, bSafe ) );
                }
            }
        }
        else {
            jsonValue = this.realValue( parameterMapValue, bSafe );
        }

        return jsonValue;
    }



    /** And java have no pointer, fuck! **/
    public Object valueSafety( Object value ) {
        if( value instanceof String ){
            return StringUtils.addSlashes( (String) value );
        }
        return value;
    }

    public JSONObject parseQueryString  ( String szQueryString, boolean bSafe ){
        int nParseAt = 0;
        JSONObject hObject = new JSONObject();

        if( szQueryString != null && !szQueryString.isEmpty() ){
            while ( nParseAt < szQueryString.length() ) {
                StringBuilder hKeyBuf   = new StringBuilder();
                StringBuilder hValueBuf = new StringBuilder();

                while ( nParseAt < szQueryString.length() ) {
                    if( szQueryString.charAt(nParseAt) == '&' ){
                        break;
                    }

                    if( szQueryString.charAt(nParseAt) == '=' ){
                        nParseAt++;
                        break;
                    }
                    hKeyBuf.append( szQueryString.charAt(nParseAt) );
                    nParseAt++;
                }

                while ( nParseAt < szQueryString.length() ) {
                    if( szQueryString.charAt(nParseAt) == '&' ){
                        nParseAt++;
                        break;
                    }
                    hValueBuf.append( szQueryString.charAt(nParseAt) );
                    nParseAt++;
                }

                try {
                    String szDecodedValueBuf = HttpURLParser.decode( hValueBuf.toString(), this.getCharset() );
                    hValueBuf = new StringBuilder( bSafe ? (String) this.valueSafety(szDecodedValueBuf) : szDecodedValueBuf ) ;
                }
                catch ( UnsupportedEncodingException e ){
                    e.printStackTrace();
                }

                if( hKeyBuf.length() != 0 ){
                    try {
                        hKeyBuf = new StringBuilder( HttpURLParser.decode(hKeyBuf.toString(), this.getCharset()) );
                    }
                    catch ( UnsupportedEncodingException e ){
                        e.printStackTrace();
                    }

                    if( hKeyBuf.length() >= 2 && hKeyBuf.charAt( hKeyBuf.length() - 2 ) == '[' && hKeyBuf.charAt( hKeyBuf.length() - 1 ) == ']' ){
                        /** Notice: Java Servlet Key of Array Value is different with PHP. { Such as 'key[]' would not trim to 'key' }**/
                        /*hKeyBuf.replace( hKeyBuf.length() - 1,hKeyBuf.length(), "" );
                        hKeyBuf.replace( hKeyBuf.length() - 1,hKeyBuf.length(), "" );*/

                        String szKeyBuf = hKeyBuf.toString();
                        Object rRow = hObject.opt( szKeyBuf );
                        if( !(rRow instanceof JSONArray) ){
                            hObject.remove( szKeyBuf );
                            hObject.put( szKeyBuf, new JSONArray() );
                        }
                        hObject.optJSONArray( szKeyBuf ).put( hValueBuf.toString() );
                    }
                    else{
                        hObject.put( hKeyBuf.toString(), hValueBuf.toString() );
                    }
                }
            }
        }

        return hObject;
    }

    public JSONObject parseFormData     ( HttpServletRequest request, boolean bSafe ){
        return this.siftPostFromParameterMap( request, bSafe );
    }

    public JSONObject requestMapJsonify ( HttpServletRequest request, boolean bSafe ) {
        Map<?, ?> properties = request.getParameterMap();
        JSONObject jsonObject = new JSONObject();

        Iterator<?> entries = properties.entrySet().iterator();
        Map.Entry entry;
        while ( entries.hasNext() ) {
            entry = (Map.Entry) entries.next();
            String szKey = (String) entry.getKey();

            jsonObject.put( szKey, this.parameterMapValue2JSONValue( entry.getValue(), bSafe ) );
        }
        return jsonObject;
    }

    public JSONObject siftPostFromParameterMap( HttpServletRequest request, boolean bSafe ){
        JSONObject queryMap = this.parseQueryString( request.getQueryString(), false );
        JSONObject postMap  = new JSONObject();
        Map<?, ?>  unionMap = request.getParameterMap();

        Iterator<?> entries = unionMap.entrySet().iterator();
        Map.Entry entry;
        while ( entries.hasNext() ) {
            entry = (Map.Entry) entries.next();
            String szKey = (String) entry.getKey();
            Object parameterMapValue   = entry.getValue();

            Object queryMapValue = queryMap.opt( szKey );
            if( queryMapValue != null ){
                if( parameterMapValue instanceof String[] ){
                    LinkedHashSet<String > hashSet = this.parameterMapStrings2Set( (String[])parameterMapValue );

                    if( queryMapValue instanceof String ){
                        hashSet.remove( (String) queryMapValue );
                    }
                    else if( queryMapValue instanceof JSONArray ){
                        for( Object row : ( (JSONArray)queryMapValue ).getArray() ){
                            hashSet.remove( (String) row );
                        }
                    }

                    if( !hashSet.isEmpty() ){
                        postMap.put( szKey, this.linkedHashSet2JSONValue( hashSet, bSafe ) );
                    }
                }
                else if( parameterMapValue instanceof String ){
                    boolean bQualified = true;
                    if( queryMapValue instanceof String && queryMap == parameterMapValue ){
                        bQualified = false;
                    }
                    else if( queryMapValue instanceof JSONArray ){
                        for( Object row : ( (JSONArray)queryMapValue ).getArray() ){
                            if( row == parameterMapValue ){
                                bQualified = false;
                                break;
                            }
                        }
                    }

                    if( bQualified ){
                        postMap.put( szKey, this.realValue( queryMapValue, bSafe ) );
                    }
                }
            }
            else {
                postMap.put( szKey, this.parameterMapValue2JSONValue( parameterMapValue, bSafe ) );
            }

        }
        return postMap;
    }

    public Map<String, Cookie > cookiesMapify ( Map<String, Cookie > map, HttpServletRequest request ) {
        Cookie[] cookies = request.getCookies();

        map.clear();
        if( cookies != null ) {
            for ( Cookie cookie : cookies ) {
                map.put( cookie.getName(), cookie );
            }
        }

        return map;
    }

}
