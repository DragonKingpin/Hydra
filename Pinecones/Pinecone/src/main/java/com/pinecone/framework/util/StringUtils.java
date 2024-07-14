package com.pinecone.framework.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.TreeSet;

public final class StringUtils {
    private static final String FOLDER_SEPARATOR = "/";
    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
    private static final String TOP_PATH = "..";
    private static final String CURRENT_PATH = ".";
    private static final char EXTENSION_SEPARATOR = '.';

    public StringUtils() {
    }

    public static boolean isEmpty( Object str ) {
        return str == null || "".equals(str);
    }

    public static boolean isEmpty( String str ) {
        return str == null || str.isEmpty();
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }

    public static boolean hasLength(String str) {
        return hasLength((CharSequence)str);
    }

    public static boolean hasText(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence)str);
    }

    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for(int i = 0; i < strLen; ++i) {
                if (Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence)str);
    }

    public static String trimWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
                sb.deleteCharAt(0);
            }

            while(sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();
        }
    }

    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        } else {
            int len = str.length();
            StringBuilder sb = new StringBuilder(str.length());

            for(int i = 0; i < len; ++i) {
                char c = str.charAt(i);
                if (!Character.isWhitespace(c)) {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
    }

    public static String trimLeadingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && Character.isWhitespace(sb.charAt(0))) {
                sb.deleteCharAt(0);
            }

            return sb.toString();
        }
    }

    public static String trimTrailingWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1))) {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();
        }
    }

    public static String trimLeadingCharacter(String str, char leadingCharacter) {
        if (!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && sb.charAt(0) == leadingCharacter) {
                sb.deleteCharAt(0);
            }

            return sb.toString();
        }
    }

    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        if (!hasLength(str)) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(str);

            while(sb.length() > 0 && sb.charAt(sb.length() - 1) == trailingCharacter) {
                sb.deleteCharAt(sb.length() - 1);
            }

            return sb.toString();
        }
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str != null && prefix != null) {
            if (str.startsWith(prefix)) {
                return true;
            } else if (str.length() < prefix.length()) {
                return false;
            } else {
                String lcStr = str.substring(0, prefix.length()).toLowerCase();
                String lcPrefix = prefix.toLowerCase();
                return lcStr.equals(lcPrefix);
            }
        } else {
            return false;
        }
    }

    public static boolean endsWithIgnoreCase(String str, String suffix) {
        if (str != null && suffix != null) {
            if (str.endsWith(suffix)) {
                return true;
            } else if (str.length() < suffix.length()) {
                return false;
            } else {
                String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
                String lcSuffix = suffix.toLowerCase();
                return lcStr.equals(lcSuffix);
            }
        } else {
            return false;
        }
    }

    public static boolean substringMatch(CharSequence str, int index, CharSequence substring) {
        for(int j = 0; j < substring.length(); ++j) {
            int i = index + j;
            if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
                return false;
            }
        }

        return true;
    }

    public static int countOccurrencesOf( String str, String sub ) {
        if ( str != null && sub != null && str.length() != 0 && sub.length() != 0 ) {
            int count = 0;

            int idx;
            for( int pos = 0; (idx = str.indexOf(sub, pos)) != -1; pos = idx + sub.length() ) {
                ++count;
            }

            return count;
        }
        else {
            return 0;
        }
    }

    public static int countOccurrencesOf( String str, String sub, int threshold ) {
        if ( str != null && sub != null && str.length() != 0 && sub.length() != 0 ) {
            int count = 0;

            int idx;
            for( int pos = 0; (idx = str.indexOf(sub, pos)) != -1; pos = idx + sub.length() ) {
                ++count;
                if( count >= threshold ) {
                    break;
                }
            }

            return count;
        }
        else {
            return 0;
        }
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        if (hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
            StringBuilder sb = new StringBuilder();
            int pos = 0;
            int index = inString.indexOf(oldPattern);

            for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                sb.append(inString.substring(pos, index));
                sb.append(newPattern);
                pos = index + patLen;
            }

            sb.append(inString.substring(pos));
            return sb.toString();
        } else {
            return inString;
        }
    }

    public static String delete(String inString, String pattern) {
        return replace(inString, pattern, "");
    }

    public static String deleteAny(String inString, String charsToDelete) {
        if (hasLength(inString) && hasLength(charsToDelete)) {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < inString.length(); ++i) {
                char c = inString.charAt(i);
                if (charsToDelete.indexOf(c) == -1) {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return inString;
        }
    }

    public static String quote(String str) {
        return str != null ? "'" + str + "'" : null;
    }

    public static Object quoteIfString(Object obj) {
        return obj instanceof String ? quote((String)obj) : obj;
    }

    public static String unqualify(String qualifiedName) {
        return unqualify(qualifiedName, '.');
    }

    public static String unqualify(String qualifiedName, char separator) {
        return qualifiedName.substring(qualifiedName.lastIndexOf(separator) + 1);
    }

    public static String capitalize(String str) {
        return StringUtils.changeFirstCharacterCase(str, true);
    }

    public static String uncapitalize(String str) {
        return StringUtils.changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase( String str, boolean bCapitalize ) {
        if (str != null && str.length() != 0) {
            char[] cs = str.toCharArray();
            if (bCapitalize) {
                cs[0] = Character.toUpperCase( cs[0] );
            } else {
                cs[0] = Character.toLowerCase( cs[0] );
            }
            return String.valueOf( cs );
        } else {
            return str;
        }
    }

    public static String getFilename(String path) {
        if (path == null) {
            return null;
        } else {
            int separatorIndex = path.lastIndexOf("/");
            return separatorIndex != -1 ? path.substring(separatorIndex + 1) : path;
        }
    }

    public static String getFilenameExtension(String path) {
        if (path == null) {
            return null;
        } else {
            int extIndex = path.lastIndexOf(46);
            if (extIndex == -1) {
                return null;
            } else {
                int folderIndex = path.lastIndexOf("/");
                return folderIndex > extIndex ? null : path.substring(extIndex + 1);
            }
        }
    }

    public static String stripFilenameExtension(String path) {
        if (path == null) {
            return null;
        } else {
            int extIndex = path.lastIndexOf(46);
            if (extIndex == -1) {
                return path;
            } else {
                int folderIndex = path.lastIndexOf("/");
                return folderIndex > extIndex ? path : path.substring(0, extIndex);
            }
        }
    }

    public static String applyRelativePath(String path, String relativePath) {
        int separatorIndex = path.lastIndexOf("/");
        if (separatorIndex != -1) {
            String newPath = path.substring(0, separatorIndex);
            if (!relativePath.startsWith("/")) {
                newPath = newPath + "/";
            }

            return newPath + relativePath;
        } else {
            return relativePath;
        }
    }

    public static String cleanPath(String path) {
        if (path == null) {
            return null;
        } else {
            String pathToUse = replace(path, "\\", "/");
            int prefixIndex = pathToUse.indexOf(":");
            String prefix = "";
            if (prefixIndex != -1) {
                prefix = pathToUse.substring(0, prefixIndex + 1);
                if (prefix.contains("/")) {
                    prefix = "";
                } else {
                    pathToUse = pathToUse.substring(prefixIndex + 1);
                }
            }

            if (pathToUse.startsWith("/")) {
                prefix = prefix + "/";
                pathToUse = pathToUse.substring(1);
            }

            String[] pathArray = delimitedListToStringArray(pathToUse, "/");
            List<String> pathElements = new LinkedList();
            int tops = 0;

            int i;
            for(i = pathArray.length - 1; i >= 0; --i) {
                String element = pathArray[i];
                if (!".".equals(element)) {
                    if ("..".equals(element)) {
                        ++tops;
                    } else if (tops > 0) {
                        --tops;
                    } else {
                        pathElements.add(0, element);
                    }
                }
            }

            for(i = 0; i < tops; ++i) {
                pathElements.add(0, "..");
            }

            return prefix + collectionToDelimitedString(pathElements, "/");
        }
    }

    public static boolean pathEquals(String path1, String path2) {
        return cleanPath(path1).equals(cleanPath(path2));
    }

    public static Locale parseLocaleString(String localeString) {
        String[] parts = tokenizeToStringArray(localeString, "_ ", false, false);
        String language = parts.length > 0 ? parts[0] : "";
        String country = parts.length > 1 ? parts[1] : "";
        validateLocalePart(language);
        validateLocalePart(country);
        String variant = "";
        if (parts.length > 2) {
            int endIndexOfCountryCode = localeString.indexOf(country, language.length()) + country.length();
            variant = trimLeadingWhitespace(localeString.substring(endIndexOfCountryCode));
            if (variant.startsWith("_")) {
                variant = trimLeadingCharacter(variant, '_');
            }
        }

        return language.length() > 0 ? new Locale(language, country, variant) : null;
    }

    private static void validateLocalePart(String localePart) {
        for(int i = 0; i < localePart.length(); ++i) {
            char ch = localePart.charAt(i);
            if (ch != '_' && ch != ' ' && !Character.isLetterOrDigit(ch)) {
                throw new IllegalArgumentException("Locale part \"" + localePart + "\" contains invalid characters");
            }
        }

    }

    public static String toLanguageTag(Locale locale) {
        return locale.getLanguage() + (hasText(locale.getCountry()) ? "-" + locale.getCountry() : "");
    }

    public static TimeZone parseTimeZoneString(String timeZoneString) {
        TimeZone timeZone = TimeZone.getTimeZone(timeZoneString);
        if ("GMT".equals(timeZone.getID()) && !timeZoneString.startsWith("GMT")) {
            throw new IllegalArgumentException("Invalid time zone specification '" + timeZoneString + "'");
        } else {
            return timeZone;
        }
    }

    public static String[] addStringToArray(String[] array, String str) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[]{str};
        } else {
            String[] newArr = new String[array.length + 1];
            System.arraycopy(array, 0, newArr, 0, array.length);
            newArr[array.length] = str;
            return newArr;
        }
    }

    public static String[] concatenateStringArrays(String[] array1, String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        } else if (ObjectUtils.isEmpty(array2)) {
            return array1;
        } else {
            String[] newArr = new String[array1.length + array2.length];
            System.arraycopy(array1, 0, newArr, 0, array1.length);
            System.arraycopy(array2, 0, newArr, array1.length, array2.length);
            return newArr;
        }
    }

    public static String[] mergeStringArrays(String[] array1, String[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        } else if (ObjectUtils.isEmpty(array2)) {
            return array1;
        } else {
            List<String> result = new ArrayList();
            result.addAll(Arrays.asList(array1));
            String[] arr = array2;
            int len = array2.length;

            for( int i = 0; i < len; ++i ) {
                String str = arr[i];
                if ( !result.contains(str) ) {
                    result.add(str);
                }
            }

            return toStringArray((Collection)result);
        }
    }

    public static String[] sortStringArray(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[0];
        } else {
            Arrays.sort(array);
            return array;
        }
    }

    public static String[] toStringArray(Collection<String> collection) {
        return collection == null ? null : (String[])collection.toArray(new String[collection.size()]);
    }

    public static String[] toStringArray(Enumeration<String> enumeration) {
        if (enumeration == null) {
            return null;
        } else {
            List<String> list = Collections.list(enumeration);
            return (String[])list.toArray(new String[list.size()]);
        }
    }

    public static String[] trimArrayElements(String[] array) {
        if (ObjectUtils.isEmpty(array)) {
            return new String[0];
        } else {
            String[] result = new String[array.length];

            for(int i = 0; i < array.length; ++i) {
                String element = array[i];
                result[i] = element != null ? element.trim() : null;
            }

            return result;
        }
    }

    public static String[] removeDuplicateStrings( String[] array ) {
        if ( ObjectUtils.isEmpty( array ) ) {
            return array;
        }
        else {
            Set<String> set = new TreeSet();
            String[] arr = array;
            int len = array.length;

            for( int i = 0; i < len; ++i ) {
                String element = arr[ i ];
                set.add(element);
            }

            return toStringArray( (Collection)set );
        }
    }

    public static String[] split(String toSplit, String delimiter) {
        if (hasLength(toSplit) && hasLength(delimiter)) {
            int offset = toSplit.indexOf(delimiter);
            if (offset < 0) {
                return null;
            } else {
                String beforeDelimiter = toSplit.substring(0, offset);
                String afterDelimiter = toSplit.substring(offset + delimiter.length());
                return new String[]{beforeDelimiter, afterDelimiter};
            }
        } else {
            return null;
        }
    }

    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter) {
        return splitArrayElementsIntoProperties(array, delimiter, (String)null);
    }

    public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {
        if ( ObjectUtils.isEmpty(array) ) {
            return null;
        }
        else {
            Properties result = new Properties();
            String[] arr = array;
            int len = array.length;

            for( int i = 0; i < len; ++i ) {
                String element = arr[i];
                if ( charsToDelete != null ) {
                    element = deleteAny(element, charsToDelete);
                }

                String[] splittedElement = split(element, delimiter);
                if ( splittedElement != null ) {
                    result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
                }
            }

            return result;
        }
    }

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        } else {
            StringTokenizer st = new StringTokenizer(str, delimiters);
            ArrayList tokens = new ArrayList();

            while(true) {
                String token;
                do {
                    if (!st.hasMoreTokens()) {
                        return toStringArray((Collection)tokens);
                    }

                    token = st.nextToken();
                    if (trimTokens) {
                        token = token.trim();
                    }
                } while(ignoreEmptyTokens && token.length() <= 0);

                tokens.add(token);
            }
        }
    }

    public static String[] delimitedListToStringArray(String str, String delimiter) {
        return delimitedListToStringArray(str, delimiter, (String)null);
    }

    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
        if (str == null) {
            return new String[0];
        } else if (delimiter == null) {
            return new String[]{str};
        } else {
            List<String> result = new ArrayList();
            int pos;
            if ("".equals(delimiter)) {
                for(pos = 0; pos < str.length(); ++pos) {
                    result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
                }
            } else {
                int delPos;
                for(pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
                    result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                }

                if (str.length() > 0 && pos <= str.length()) {
                    result.add(deleteAny(str.substring(pos), charsToDelete));
                }
            }

            return toStringArray((Collection)result);
        }
    }

    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    public static Set<String> commaDelimitedListToSet(String str) {
        Set<String> set = new TreeSet();
        String[] tokens = commaDelimitedListToStringArray(str);
        String[] ts = tokens;
        int len = tokens.length;

        for( int i = 0; i < len; ++i ) {
            String token = ts[i];
            set.add(token);
        }

        return set;
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
        if (CollectionUtils.isEmpty(coll)) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            Iterator it = coll.iterator();

            while(it.hasNext()) {
                sb.append(prefix).append(it.next()).append(suffix);
                if (it.hasNext()) {
                    sb.append(delim);
                }
            }

            return sb.toString();
        }
    }

    public static String collectionToDelimitedString(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim, "", "");
    }

    public static String collectionToCommaDelimitedString(Collection<?> coll) {
        return collectionToDelimitedString(coll, ",");
    }

    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if (ObjectUtils.isEmpty(arr)) {
            return "";
        } else if (arr.length == 1) {
            return ObjectUtils.nullSafeToString(arr[0]);
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < arr.length; ++i) {
                if (i > 0) {
                    sb.append(delim);
                }

                sb.append(arr[i]);
            }

            return sb.toString();
        }
    }

    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }





    /**
     *   prototype Pinecone C/C++ Ver 3.1
     *   Version: New add in Pinecone Java Ver 3.0
     */
    public static String hypertext2Text( String hypertext , boolean replaceBlankSpace , boolean replaceNewLine){
        String szRegex = "<.*?>";
        if (replaceBlankSpace){
            szRegex += "| ";
        }
        if (replaceNewLine){
            szRegex += "|\t" + "|\r" + "|\n";
        }

        hypertext = hypertext.replaceAll(szRegex,"");
        //hypertext = StringEscapeUtils.unescapeHtml( hypertext );

        return hypertext;
    }

    public static String nullThenEmpty ( String str ){
        return  str == null ? "" : str;
    }

    public static Writer addSlashes( String szBadString, Writer writer, boolean bJsonQuoteMode ) throws IOException {
        if ( szBadString != null && szBadString.length() != 0 ) {
            char c = 0;
            int len = szBadString.length();

            if( bJsonQuoteMode ){
                writer.write(34);
            }
            for( int i = 0; i < len; ++i ) {
                char b = c;
                c = szBadString.charAt(i);
                switch(c) {
                    case '\b': {
                        writer.write("\\b");
                        continue;
                    }
                    case '\t': {
                        writer.write("\\t");
                        continue;
                    }
                    case '\n': {
                        writer.write("\\n");
                        continue;
                    }
                    case '\f': {
                        writer.write("\\f");
                        continue;
                    }
                    case '\r': {
                        writer.write("\\r");
                        continue;
                    }
                    case '\'':{
                        if( bJsonQuoteMode ){
                            writer.write(c);
                            continue;
                        }
                    }
                    case '"':
                    case '\\': {
                        writer.write(92);
                        writer.write(c);
                        continue;
                    }
                    case '/': {
                        if (b == '<') {
                            writer.write(92);
                        }

                        writer.write(c);
                        continue;
                    }
                }

                if (c >= ' ' && (c < 128 || c >= 160) && (c < 8192 || c >= 8448)) {
                    writer.write(c);
                }
                else {
                    writer.write("\\u");
                    String szHexString = Integer.toHexString(c);
                    writer.write("0000", 0, 4 - szHexString.length() );
                    writer.write(szHexString);
                }
            }

            if( bJsonQuoteMode ){
                writer.write(34);
            }
            return writer;
        }
        else {
            if( bJsonQuoteMode ){
                writer.write("\"\"");
            }
            return writer;
        }
    }

    public static String addSlashes( String szBadString, boolean bJsonQuoteMode ) {
        StringWriter sw = new StringWriter();
        synchronized( sw.getBuffer()) {
            String s;
            try {
                s = StringUtils.addSlashes( szBadString, sw, bJsonQuoteMode ).toString();
            }
            catch ( IOException e ) {
                return "";
            }
            return s;
        }
    }

    public static String addSlashes( String szBadString ) {
        return StringUtils.addSlashes( szBadString, false );
    }

    public static String jsonQuote( String szBadString ) {
        return StringUtils.addSlashes( szBadString, true );
    }

    public static String sequencify ( String[] sequences, String szDelimiter, String szPrefix ){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for( String sequence : sequences ){
            sb.append( szPrefix ).append( sequence );
            if( ++i != sequences.length ){
                sb.append( szDelimiter );
            }
        }
        return sb.toString();
    }

    public static String sequencify ( String[] sequences, String szDelimiter ){
        return StringUtils.sequencify( sequences, szDelimiter, "" );
    }

    public static String[] trimEmptyElement( String[] strings ) {
        String[] buf = new String[ strings.length ];
        int j = 0;
        for ( int i = 0; i < strings.length; i++ ) {
            String each = strings[ i ];
            if( !StringUtils.isEmpty( each ) ) {
                buf[ j++ ] = each;
            }
        }
        return Arrays.copyOf( buf, j );
    }



    /**
     *   Version: New add in Pinecone Java Ver 20240624
     */
    public static boolean containsBoth( String target, String moreChars ) {
        boolean[] found = new boolean[ moreChars.length() ];
        Arrays.fill( found, false );

        for ( int i = 0; i < target.length(); ++i ) {
            char c = target.charAt( i );

            int index = moreChars.indexOf( c );
            if ( index != -1 ) {
                found[ index ] = true;
            }
        }

        for ( boolean isFound : found ) {
            if ( !isFound ) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsBoth( String target, char[] moreChars ) {
        boolean[] found = new boolean[ moreChars.length ];
        Arrays.fill( found, false );

        for ( int i = 0; i < target.length(); i++ ) {
            char c = target.charAt(i);

            for ( int j = 0; j < moreChars.length; ++j ) {
                if( found[j] ) {
                    continue;
                }
                if ( c == moreChars[j] ) {
                    found[j] = true;
                    break;
                }
            }
        }

        for ( boolean isFound : found ) {
            if ( !isFound ) {
                return false;
            }
        }
        return true;
    }

    public static boolean containsOnce( String target, String moreChars ) {
        for ( int i = 0; i < moreChars.length(); ++i ) {
            char c = moreChars.charAt( i );
            if ( target.indexOf(c) != -1 ) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsOnce( String target, char[] moreChars ) {
        for ( int i = 0; i < target.length(); i++ ) {
            char c = target.charAt(i);

            for ( int j = 0; j < moreChars.length; ++j ) {
                if ( c == moreChars[j] ) {
                    return true;
                }
            }
        }

        return false;
    }

    public static int countOccurrencesOf( String target, char specifiedChar, int threshold ) {
        int count = 0;
        for ( int i = 0; i < target.length(); ++i ) {
            if ( target.charAt(i) == specifiedChar ) {
                count++;
                if ( threshold > 0 && count >= threshold ) {
                    return count;
                }
            }
        }
        return count;
    }

    public static int countOccurrencesOf( String target, char specifiedChar ) {
        return StringUtils.countOccurrencesOf( target, specifiedChar, 0 );
    }
}
