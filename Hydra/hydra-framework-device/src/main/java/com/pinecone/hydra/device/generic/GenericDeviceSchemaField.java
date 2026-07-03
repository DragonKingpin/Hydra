package com.pinecone.hydra.device.generic;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public class GenericDeviceSchemaField implements Pinenut {
    protected String code;
    protected String name;
    protected String description;
    protected GenericDeviceFieldType fieldType = GenericDeviceFieldType.TEXT;
    protected boolean required;
    protected boolean multiple;
    protected boolean enabled = true;
    protected int orderIndex;
    protected String placeholder;
    protected String unit;
    protected Object defaultValue;
    protected Integer minLength;
    protected Integer maxLength;
    protected Double minValue;
    protected Double maxValue;
    protected String pattern;
    protected List<GenericDeviceSchemaFieldOption> options = new ArrayList<>();

    public String getCode() {
        return this.code;
    }

    public void setCode( String code ) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public GenericDeviceFieldType getFieldType() {
        return this.fieldType;
    }

    public void setFieldType( GenericDeviceFieldType fieldType ) {
        this.fieldType = fieldType;
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired( boolean required ) {
        this.required = required;
    }

    public boolean isMultiple() {
        return this.multiple;
    }

    public void setMultiple( boolean multiple ) {
        this.multiple = multiple;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled( boolean enabled ) {
        this.enabled = enabled;
    }

    public int getOrderIndex() {
        return this.orderIndex;
    }

    public void setOrderIndex( int orderIndex ) {
        this.orderIndex = orderIndex;
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public void setPlaceholder( String placeholder ) {
        this.placeholder = placeholder;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit( String unit ) {
        this.unit = unit;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue( Object defaultValue ) {
        this.defaultValue = defaultValue;
    }

    public Integer getMinLength() {
        return this.minLength;
    }

    public void setMinLength( Integer minLength ) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength( Integer maxLength ) {
        this.maxLength = maxLength;
    }

    public Double getMinValue() {
        return this.minValue;
    }

    public void setMinValue( Double minValue ) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue( Double maxValue ) {
        this.maxValue = maxValue;
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern( String pattern ) {
        this.pattern = pattern;
    }

    public List<GenericDeviceSchemaFieldOption> getOptions() {
        return this.options;
    }

    public void setOptions( List<GenericDeviceSchemaFieldOption> options ) {
        this.options = options;
    }
}
