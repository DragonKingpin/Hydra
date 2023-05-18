package Pinecone.Framework.Util.Summer.util;

import Pinecone.Framework.System.util.Assert;
import Pinecone.Framework.System.util.CollectionUtils;
import Pinecone.Framework.Unit.LinkedCaseInsensitiveMap;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import java.util.Map.Entry;

public class MimeType implements Comparable<MimeType>, Serializable {
    private static final long serialVersionUID = 4085923477777865903L;
    protected static final String WILDCARD_TYPE = "*";
    private static final BitSet TOKEN;
    private static final String PARAM_CHARSET = "charset";
    private final String type;
    private final String subtype;
    private final Map<String, String> parameters;

    public MimeType(String type) {
        this(type, "*");
    }

    public MimeType(String type, String subtype) {
        this(type, subtype, Collections.emptyMap());
    }

    public MimeType(String type, String subtype, Charset charSet) {
        this(type, subtype, Collections.singletonMap("charset", charSet.name()));
    }

    public MimeType(MimeType other, Map<String, String> parameters) {
        this(other.getType(), other.getSubtype(), parameters);
    }

    public MimeType(String type, String subtype, Map<String, String> parameters) {
        Assert.hasLength(type, "type must not be empty");
        Assert.hasLength(subtype, "subtype must not be empty");
        this.checkToken(type);
        this.checkToken(subtype);
        this.type = type.toLowerCase(Locale.ENGLISH);
        this.subtype = subtype.toLowerCase(Locale.ENGLISH);
        if (!CollectionUtils.isEmpty(parameters)) {
            Map<String, String> map = new LinkedCaseInsensitiveMap<>(parameters.size(), Locale.ENGLISH);
            Iterator var5 = parameters.entrySet().iterator();

            while(var5.hasNext()) {
                Entry<String, String> entry = (Entry)var5.next();
                String attribute = (String)entry.getKey();
                String value = (String)entry.getValue();
                this.checkParameters(attribute, value);
                map.put(attribute, value);
            }

            this.parameters = Collections.unmodifiableMap(map);
        } else {
            this.parameters = Collections.emptyMap();
        }

    }

    private void checkToken(String token) {
        for(int i = 0; i < token.length(); ++i) {
            char ch = token.charAt(i);
            if (!TOKEN.get(ch)) {
                throw new IllegalArgumentException("Invalid token character '" + ch + "' in token \"" + token + "\"");
            }
        }

    }

    protected void checkParameters(String attribute, String value) {
        Assert.hasLength(attribute, "parameter attribute must not be empty");
        Assert.hasLength(value, "parameter value must not be empty");
        this.checkToken(attribute);
        if ("charset".equals(attribute)) {
            value = this.unquote(value);
            Charset.forName(value);
        } else if (!this.isQuotedString(value)) {
            this.checkToken(value);
        }

    }

    private boolean isQuotedString(String s) {
        if (s.length() < 2) {
            return false;
        } else {
            return s.startsWith("\"") && s.endsWith("\"") || s.startsWith("'") && s.endsWith("'");
        }
    }

    protected String unquote(String s) {
        if (s == null) {
            return null;
        } else {
            return this.isQuotedString(s) ? s.substring(1, s.length() - 1) : s;
        }
    }

    public boolean isWildcardType() {
        return "*".equals(this.getType());
    }

    public boolean isWildcardSubtype() {
        return "*".equals(this.getSubtype()) || this.getSubtype().startsWith("*+");
    }

    public boolean isConcrete() {
        return !this.isWildcardType() && !this.isWildcardSubtype();
    }

    public String getType() {
        return this.type;
    }

    public String getSubtype() {
        return this.subtype;
    }

    public Charset getCharSet() {
        String charSet = this.getParameter("charset");
        return charSet != null ? Charset.forName(this.unquote(charSet)) : null;
    }

    public String getParameter(String name) {
        return (String)this.parameters.get(name);
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public boolean includes(MimeType other) {
        if (other == null) {
            return false;
        } else if (this.isWildcardType()) {
            return true;
        } else {
            if (this.getType().equals(other.getType())) {
                if (this.getSubtype().equals(other.getSubtype())) {
                    return true;
                }

                if (this.isWildcardSubtype()) {
                    int thisPlusIdx = this.getSubtype().indexOf(43);
                    if (thisPlusIdx == -1) {
                        return true;
                    }

                    int otherPlusIdx = other.getSubtype().indexOf(43);
                    if (otherPlusIdx != -1) {
                        String thisSubtypeNoSuffix = this.getSubtype().substring(0, thisPlusIdx);
                        String thisSubtypeSuffix = this.getSubtype().substring(thisPlusIdx + 1);
                        String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
                        if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && "*".equals(thisSubtypeNoSuffix)) {
                            return true;
                        }
                    }
                }
            }

            return false;
        }
    }

    public boolean isCompatibleWith(MimeType other) {
        if (other == null) {
            return false;
        } else if (!this.isWildcardType() && !other.isWildcardType()) {
            if (this.getType().equals(other.getType())) {
                if (this.getSubtype().equals(other.getSubtype())) {
                    return true;
                }

                if (this.isWildcardSubtype() || other.isWildcardSubtype()) {
                    int thisPlusIdx = this.getSubtype().indexOf(43);
                    int otherPlusIdx = other.getSubtype().indexOf(43);
                    if (thisPlusIdx == -1 && otherPlusIdx == -1) {
                        return true;
                    }

                    if (thisPlusIdx != -1 && otherPlusIdx != -1) {
                        String thisSubtypeNoSuffix = this.getSubtype().substring(0, thisPlusIdx);
                        String otherSubtypeNoSuffix = other.getSubtype().substring(0, otherPlusIdx);
                        String thisSubtypeSuffix = this.getSubtype().substring(thisPlusIdx + 1);
                        String otherSubtypeSuffix = other.getSubtype().substring(otherPlusIdx + 1);
                        if (thisSubtypeSuffix.equals(otherSubtypeSuffix) && ("*".equals(thisSubtypeNoSuffix) || "*".equals(otherSubtypeNoSuffix))) {
                            return true;
                        }
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public int compareTo(MimeType other) {
        int comp = this.getType().compareToIgnoreCase(other.getType());
        if (comp != 0) {
            return comp;
        } else {
            comp = this.getSubtype().compareToIgnoreCase(other.getSubtype());
            if (comp != 0) {
                return comp;
            } else {
                comp = this.getParameters().size() - other.getParameters().size();
                if (comp != 0) {
                    return comp;
                } else {
                    TreeSet<String> thisAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
                    thisAttributes.addAll(this.getParameters().keySet());
                    TreeSet<String> otherAttributes = new TreeSet(String.CASE_INSENSITIVE_ORDER);
                    otherAttributes.addAll(other.getParameters().keySet());
                    Iterator<String> thisAttributesIterator = thisAttributes.iterator();
                    Iterator otherAttributesIterator = otherAttributes.iterator();

                    do {
                        if (!thisAttributesIterator.hasNext()) {
                            return 0;
                        }

                        String thisAttribute = (String)thisAttributesIterator.next();
                        String otherAttribute = (String)otherAttributesIterator.next();
                        comp = thisAttribute.compareToIgnoreCase(otherAttribute);
                        if (comp != 0) {
                            return comp;
                        }

                        String thisValue = (String)this.getParameters().get(thisAttribute);
                        String otherValue = (String)other.getParameters().get(otherAttribute);
                        if (otherValue == null) {
                            otherValue = "";
                        }

                        comp = thisValue.compareTo(otherValue);
                    } while(comp == 0);

                    return comp;
                }
            }
        }
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof MimeType)) {
            return false;
        } else {
            MimeType otherType = (MimeType)other;
            return this.type.equalsIgnoreCase(otherType.type) && this.subtype.equalsIgnoreCase(otherType.subtype) && this.parameters.equals(otherType.parameters);
        }
    }

    public int hashCode() {
        int result = this.type.hashCode();
        result = 31 * result + this.subtype.hashCode();
        result = 31 * result + this.parameters.hashCode();
        return result;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        this.appendTo(builder);
        return builder.toString();
    }

    protected void appendTo(StringBuilder builder) {
        builder.append(this.type);
        builder.append('/');
        builder.append(this.subtype);
        this.appendTo(this.parameters, builder);
    }

    private void appendTo(Map<String, String> map, StringBuilder builder) {
        Iterator var3 = map.entrySet().iterator();

        while(var3.hasNext()) {
            Entry<String, String> entry = (Entry)var3.next();
            builder.append(';');
            builder.append((String)entry.getKey());
            builder.append('=');
            builder.append((String)entry.getValue());
        }

    }

    public static MimeType valueOf(String value) {
        return MimeTypeUtils.parseMimeType(value);
    }

    static {
        BitSet ctl = new BitSet(128);

        for(int i = 0; i <= 31; ++i) {
            ctl.set(i);
        }

        ctl.set(127);
        BitSet separators = new BitSet(128);
        separators.set(40);
        separators.set(41);
        separators.set(60);
        separators.set(62);
        separators.set(64);
        separators.set(44);
        separators.set(59);
        separators.set(58);
        separators.set(92);
        separators.set(34);
        separators.set(47);
        separators.set(91);
        separators.set(93);
        separators.set(63);
        separators.set(61);
        separators.set(123);
        separators.set(125);
        separators.set(32);
        separators.set(9);
        TOKEN = new BitSet(128);
        TOKEN.set(0, 128);
        TOKEN.andNot(ctl);
        TOKEN.andNot(separators);
    }

    public static class SpecificityComparator<T extends MimeType> implements Comparator<T> {
        public SpecificityComparator() {
        }

        public int compare(T mimeType1, T mimeType2) {
            if (mimeType1.isWildcardType() && !mimeType2.isWildcardType()) {
                return 1;
            } else if (mimeType2.isWildcardType() && !mimeType1.isWildcardType()) {
                return -1;
            } else if (!mimeType1.getType().equals(mimeType2.getType())) {
                return 0;
            } else if (mimeType1.isWildcardSubtype() && !mimeType2.isWildcardSubtype()) {
                return 1;
            } else if (mimeType2.isWildcardSubtype() && !mimeType1.isWildcardSubtype()) {
                return -1;
            } else {
                return !mimeType1.getSubtype().equals(mimeType2.getSubtype()) ? 0 : this.compareParameters(mimeType1, mimeType2);
            }
        }

        protected int compareParameters(T mimeType1, T mimeType2) {
            int paramsSize1 = mimeType1.getParameters().size();
            int paramsSize2 = mimeType2.getParameters().size();
            return paramsSize2 < paramsSize1 ? -1 : (paramsSize2 == paramsSize1 ? 0 : 1);
        }
    }
}

