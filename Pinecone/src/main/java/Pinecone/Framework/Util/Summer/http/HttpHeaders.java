package Pinecone.Framework.Util.Summer.http;


import Pinecone.Framework.System.util.Assert;
import Pinecone.Framework.System.util.StringUtils;
import Pinecone.Framework.Unit.LinkedCaseInsensitiveMap;
import Pinecone.Framework.Unit.MultiValueMap;

import java.io.Serializable;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class HttpHeaders implements MultiValueMap<String, String>, Serializable {
    private static final long serialVersionUID = -8578554704772377436L;
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_CHARSET = "Accept-Charset";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String AGE = "Age";
    public static final String ALLOW = "Allow";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String CONNECTION = "Connection";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    public static final String CONTENT_LANGUAGE = "Content-Language";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_LOCATION = "Content-Location";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String COOKIE = "Cookie";
    public static final String DATE = "Date";
    public static final String ETAG = "ETag";
    public static final String EXPECT = "Expect";
    public static final String EXPIRES = "Expires";
    public static final String FROM = "From";
    public static final String HOST = "Host";
    public static final String IF_MATCH = "If-Match";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String IF_NONE_MATCH = "If-None-Match";
    public static final String IF_RANGE = "If-Range";
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LINK = "Link";
    public static final String LOCATION = "Location";
    public static final String MAX_FORWARDS = "Max-Forwards";
    public static final String ORIGIN = "Origin";
    public static final String PRAGMA = "Pragma";
    public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
    public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String RANGE = "Range";
    public static final String REFERER = "Referer";
    public static final String RETRY_AFTER = "Retry-After";
    public static final String SERVER = "Server";
    public static final String SET_COOKIE = "Set-Cookie";
    public static final String SET_COOKIE2 = "Set-Cookie2";
    public static final String TE = "TE";
    public static final String TRAILER = "Trailer";
    public static final String TRANSFER_ENCODING = "Transfer-Encoding";
    public static final String UPGRADE = "Upgrade";
    public static final String USER_AGENT = "User-Agent";
    public static final String VARY = "Vary";
    public static final String VIA = "Via";
    public static final String WARNING = "Warning";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";
    private static final String[] DATE_FORMATS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM dd HH:mm:ss yyyy"};
    private static TimeZone GMT = TimeZone.getTimeZone("GMT");
    private final Map<String, List<String>> headers;

    public HttpHeaders() {
        this(new LinkedCaseInsensitiveMap(8, Locale.ENGLISH), false);
    }

    private HttpHeaders(Map<String, List<String>> headers, boolean readOnly) {
        Assert.notNull(headers, "'headers' must not be null");
        if (readOnly) {
            Map<String, List<String>> map = new LinkedCaseInsensitiveMap(headers.size(), Locale.ENGLISH);
            Iterator var4 = headers.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, List<String>> entry = (Map.Entry)var4.next();
                List<String> values = Collections.unmodifiableList((List)entry.getValue());
                map.put(entry.getKey(), values);
            }

            this.headers = Collections.unmodifiableMap(map);
        } else {
            this.headers = headers;
        }

    }

    public void setAccept(List<MediaType> acceptableMediaTypes) {
        this.set("Accept", MediaType.toString(acceptableMediaTypes));
    }

    public List<MediaType> getAccept() {
        String value = this.getFirst("Accept");
        List<MediaType> result = value != null ? MediaType.parseMediaTypes(value) : Collections.emptyList();
        if (result.size() == 1) {
            List<String> acceptHeader = this.get("Accept");
            if (acceptHeader.size() > 1) {
                value = StringUtils.collectionToCommaDelimitedString(acceptHeader);
                result = MediaType.parseMediaTypes(value);
            }
        }

        return result;
    }

    public void setAcceptCharset(List<Charset> acceptableCharsets) {
        StringBuilder builder = new StringBuilder();
        Iterator iterator = acceptableCharsets.iterator();

        while(iterator.hasNext()) {
            Charset charset = (Charset)iterator.next();
            builder.append(charset.name().toLowerCase(Locale.ENGLISH));
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        this.set("Accept-Charset", builder.toString());
    }

    public List<Charset> getAcceptCharset() {
        List<Charset> result = new ArrayList();
        String value = this.getFirst("Accept-Charset");
        if (value != null) {
            String[] tokens = value.split(",\\s*");
            String[] var4 = tokens;
            int var5 = tokens.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String token = var4[var6];
                int paramIdx = token.indexOf(59);
                String charsetName;
                if (paramIdx == -1) {
                    charsetName = token;
                } else {
                    charsetName = token.substring(0, paramIdx);
                }

                if (!charsetName.equals("*")) {
                    result.add(Charset.forName(charsetName));
                }
            }
        }

        return result;
    }

    public void setAllow(Set<HttpMethod> allowedMethods) {
        this.set("Allow", StringUtils.collectionToCommaDelimitedString(allowedMethods));
    }

    public Set<HttpMethod> getAllow() {
        String value = this.getFirst("Allow");
        if (StringUtils.isEmpty(value)) {
            return EnumSet.noneOf(HttpMethod.class);
        } else {
            List<HttpMethod> allowedMethod = new ArrayList(5);
            String[] tokens = value.split(",\\s*");
            String[] var4 = tokens;
            int var5 = tokens.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String token = var4[var6];
                allowedMethod.add(HttpMethod.valueOf(token));
            }

            return EnumSet.copyOf(allowedMethod);
        }
    }

    public void setCacheControl(String cacheControl) {
        this.set("Cache-Control", cacheControl);
    }

    public String getCacheControl() {
        return this.getFirst("Cache-Control");
    }

    public void setConnection(String connection) {
        this.set("Connection", connection);
    }

    public void setConnection(List<String> connection) {
        this.set("Connection", this.toCommaDelimitedString(connection));
    }

    public List<String> getConnection() {
        return this.getFirstValueAsList("Connection");
    }

    public void setContentDispositionFormData(String name, String filename) {
        Assert.notNull(name, "'name' must not be null");
        StringBuilder builder = new StringBuilder("form-data; name=\"");
        builder.append(name).append('"');
        if (filename != null) {
            builder.append("; filename=\"");
            builder.append(filename).append('"');
        }

        this.set("Content-Disposition", builder.toString());
    }

    public void setContentLength(long contentLength) {
        this.set("Content-Length", Long.toString(contentLength));
    }

    public long getContentLength() {
        String value = this.getFirst("Content-Length");
        return value != null ? Long.parseLong(value) : -1L;
    }

    public void setContentType(MediaType mediaType) {
        Assert.isTrue(!mediaType.isWildcardType(), "'Content-Type' cannot contain wildcard type '*'");
        Assert.isTrue(!mediaType.isWildcardSubtype(), "'Content-Type' cannot contain wildcard subtype '*'");
        this.set("Content-Type", mediaType.toString());
    }

    public MediaType getContentType() {
        String value = this.getFirst("Content-Type");
        return StringUtils.hasLength(value) ? MediaType.parseMediaType(value) : null;
    }

    public void setDate(long date) {
        this.setDate("Date", date);
    }

    public long getDate() {
        return this.getFirstDate("Date");
    }

    public void setETag(String eTag) {
        if (eTag != null) {
            Assert.isTrue(eTag.startsWith("\"") || eTag.startsWith("W/"), "Invalid eTag, does not start with W/ or \"");
            Assert.isTrue(eTag.endsWith("\""), "Invalid eTag, does not end with \"");
        }

        this.set("ETag", eTag);
    }

    public String getETag() {
        return this.getFirst("ETag");
    }

    public void setExpires(long expires) {
        this.setDate("Expires", expires);
    }

    public long getExpires() {
        try {
            return this.getFirstDate("Expires");
        } catch (IllegalArgumentException var2) {
            return -1L;
        }
    }

    public void setIfModifiedSince(long ifModifiedSince) {
        this.setDate("If-Modified-Since", ifModifiedSince);
    }

    /** @deprecated */
    @Deprecated
    public long getIfNotModifiedSince() {
        return this.getIfModifiedSince();
    }

    public long getIfModifiedSince() {
        return this.getFirstDate("If-Modified-Since");
    }

    public void setIfNoneMatch(String ifNoneMatch) {
        this.set("If-None-Match", ifNoneMatch);
    }

    public void setIfNoneMatch(List<String> ifNoneMatchList) {
        this.set("If-None-Match", this.toCommaDelimitedString(ifNoneMatchList));
    }

    protected String toCommaDelimitedString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        Iterator iterator = list.iterator();

        while(iterator.hasNext()) {
            String ifNoneMatch = (String)iterator.next();
            builder.append(ifNoneMatch);
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }

        return builder.toString();
    }

    public List<String> getIfNoneMatch() {
        return this.getFirstValueAsList("If-None-Match");
    }

    protected List<String> getFirstValueAsList(String header) {
        List<String> result = new ArrayList();
        String value = this.getFirst(header);
        if (value != null) {
            String[] tokens = value.split(",\\s*");
            String[] var5 = tokens;
            int var6 = tokens.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                String token = var5[var7];
                result.add(token);
            }
        }

        return result;
    }

    public void setLastModified(long lastModified) {
        this.setDate("Last-Modified", lastModified);
    }

    public long getLastModified() {
        return this.getFirstDate("Last-Modified");
    }

    public void setLocation(URI location) {
        this.set("Location", location.toASCIIString());
    }

    public URI getLocation() {
        String value = this.getFirst("Location");
        return value != null ? URI.create(value) : null;
    }

    public void setOrigin(String origin) {
        this.set("Origin", origin);
    }

    public String getOrigin() {
        return this.getFirst("Origin");
    }

    public void setPragma(String pragma) {
        this.set("Pragma", pragma);
    }

    public String getPragma() {
        return this.getFirst("Pragma");
    }

    public void setUpgrade(String upgrade) {
        this.set("Upgrade", upgrade);
    }

    public String getUpgrade() {
        return this.getFirst("Upgrade");
    }

    public long getFirstDate(String headerName) {
        String headerValue = this.getFirst(headerName);
        if (headerValue == null) {
            return -1L;
        } else {
            String[] var3 = DATE_FORMATS;
            int var4 = var3.length;
            int var5 = 0;

            while(var5 < var4) {
                String dateFormat = var3[var5];
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
                simpleDateFormat.setTimeZone(GMT);

                try {
                    return simpleDateFormat.parse(headerValue).getTime();
                } catch (ParseException var9) {
                    ++var5;
                }
            }

            throw new IllegalArgumentException("Cannot parse date value \"" + headerValue + "\" for \"" + headerName + "\" header");
        }
    }

    public void setDate(String headerName, long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATS[0], Locale.US);
        dateFormat.setTimeZone(GMT);
        this.set(headerName, dateFormat.format(new Date(date)));
    }

    public String getFirst(String headerName) {
        List<String> headerValues = (List)this.headers.get(headerName);
        return headerValues != null ? (String)headerValues.get(0) : null;
    }

    public void add(String headerName, String headerValue) {
        List<String> headerValues = (List)this.headers.get(headerName);
        if (headerValues == null) {
            headerValues = new LinkedList();
            this.headers.put(headerName, headerValues);
        }

        ((List)headerValues).add(headerValue);
    }

    public void set(String headerName, String headerValue) {
        List<String> headerValues = new LinkedList();
        headerValues.add(headerValue);
        this.headers.put(headerName, headerValues);
    }

    public void setAll(Map<String, String> values) {
        Iterator var2 = values.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry)var2.next();
            this.set((String)entry.getKey(), (String)entry.getValue());
        }

    }

    public Map<String, String> toSingleValueMap() {
        LinkedHashMap<String, String> singleValueMap = new LinkedHashMap(this.headers.size());
        Iterator var2 = this.headers.entrySet().iterator();

        while(var2.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry)var2.next();
            singleValueMap.put(entry.getKey(), (String) ((List)entry.getValue()).get(0));
        }

        return singleValueMap;
    }

    public int size() {
        return this.headers.size();
    }

    public boolean isEmpty() {
        return this.headers.isEmpty();
    }

    public boolean containsKey(Object key) {
        return this.headers.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.headers.containsValue(value);
    }

    public List<String> get(Object key) {
        return (List)this.headers.get(key);
    }

    public List<String> put(String key, List<String> value) {
        return (List)this.headers.put(key, value);
    }

    public List<String> remove(Object key) {
        return (List)this.headers.remove(key);
    }

    public void putAll(Map<? extends String, ? extends List<String>> map) {
        this.headers.putAll(map);
    }

    public void clear() {
        this.headers.clear();
    }

    public Set<String> keySet() {
        return this.headers.keySet();
    }

    public Collection<List<String>> values() {
        return this.headers.values();
    }

    public Set<Map.Entry<String, List<String>>> entrySet() {
        return this.headers.entrySet();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof HttpHeaders)) {
            return false;
        } else {
            HttpHeaders otherHeaders = (HttpHeaders)other;
            return this.headers.equals(otherHeaders.headers);
        }
    }

    public int hashCode() {
        return this.headers.hashCode();
    }

    public String toString() {
        return this.headers.toString();
    }

    public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
        return new HttpHeaders(headers, true);
    }
}
