package com.pinecone.hydra.device.generic;

import java.util.ArrayList;
import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.JSONArraytron;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.json.JSONMaptron;

public class GenericDeviceSchemaTransformer implements Pinenut {

    public GenericDeviceSchemaDesigner decode( String schemaJson ) {
        GenericDeviceSchemaDesigner designer = new GenericDeviceSchemaDesigner();
        if ( this.isBlank( schemaJson ) ) {
            return designer;
        }

        Object raw = JSON.parse( schemaJson );
        if ( !( raw instanceof JSONObject ) ) {
            throw new IllegalArgumentException( "Generic device schema designer should be a JSON object." );
        }

        JSONObject joDesigner = (JSONObject) raw;
        designer.setVersion( joDesigner.optString( "version", "1.0.0" ) );
        designer.setFields( this.decodeFields( joDesigner.optJSONArray( "fields" ) ) );
        return designer;
    }

    public String encode( GenericDeviceSchemaDesigner designer ) {
        GenericDeviceSchemaDesigner schemaDesigner = designer;
        if ( schemaDesigner == null ) {
            schemaDesigner = new GenericDeviceSchemaDesigner();
        }

        JSONObject joDesigner = new JSONMaptron();
        joDesigner.put( "version", this.defaultString( schemaDesigner.getVersion(), "1.0.0" ) );
        joDesigner.put( "fields", this.encodeFields( schemaDesigner.getFields() ) );
        return joDesigner.toJSONString();
    }

    protected List<GenericDeviceSchemaField> decodeFields( JSONArray jaFields ) {
        List<GenericDeviceSchemaField> fields = new ArrayList<>();
        if ( jaFields == null ) {
            return fields;
        }

        for ( int i = 0; i < jaFields.size(); ++i ) {
            JSONObject joField = jaFields.optJSONObject( i );
            if ( joField != null ) {
                fields.add( this.decodeField( joField ) );
            }
        }
        return fields;
    }

    protected GenericDeviceSchemaField decodeField( JSONObject joField ) {
        GenericDeviceSchemaField field = new GenericDeviceSchemaField();
        field.setCode( joField.optString( "code", null ) );
        field.setName( joField.optString( "name", null ) );
        field.setDescription( joField.optString( "description", null ) );
        field.setFieldType( this.decodeFieldType( joField.optString( "fieldType", joField.optString( "type", "TEXT" ) ) ) );
        field.setRequired( joField.optBoolean( "required", false ) );
        field.setMultiple( joField.optBoolean( "multiple", false ) );
        field.setEnabled( joField.optBoolean( "enabled", true ) );
        field.setOrderIndex( joField.optInt( "orderIndex", 0 ) );
        field.setPlaceholder( joField.optString( "placeholder", null ) );
        field.setUnit( joField.optString( "unit", null ) );
        field.setDefaultValue( joField.opt( "defaultValue" ) );
        field.setMinLength( this.optInteger( joField, "minLength" ) );
        field.setMaxLength( this.optInteger( joField, "maxLength" ) );
        field.setMinValue( this.optDouble( joField, "minValue" ) );
        field.setMaxValue( this.optDouble( joField, "maxValue" ) );
        field.setPattern( joField.optString( "pattern", null ) );
        field.setOptions( this.decodeOptions( joField.optJSONArray( "options" ) ) );
        return field;
    }

    protected List<GenericDeviceSchemaFieldOption> decodeOptions( JSONArray jaOptions ) {
        List<GenericDeviceSchemaFieldOption> options = new ArrayList<>();
        if ( jaOptions == null ) {
            return options;
        }

        for ( int i = 0; i < jaOptions.size(); ++i ) {
            JSONObject joOption = jaOptions.optJSONObject( i );
            if ( joOption != null ) {
                GenericDeviceSchemaFieldOption option = new GenericDeviceSchemaFieldOption();
                option.setCode( joOption.optString( "code", null ) );
                option.setName( joOption.optString( "name", null ) );
                option.setValue( joOption.opt( "value" ) );
                option.setEnabled( joOption.optBoolean( "enabled", true ) );
                options.add( option );
            }
        }
        return options;
    }

    protected JSONArray encodeFields( List<GenericDeviceSchemaField> fields ) {
        JSONArray jaFields = new JSONArraytron();
        if ( fields == null ) {
            return jaFields;
        }

        for ( GenericDeviceSchemaField field : fields ) {
            if ( field != null ) {
                jaFields.put( this.encodeField( field ) );
            }
        }
        return jaFields;
    }

    protected JSONObject encodeField( GenericDeviceSchemaField field ) {
        JSONObject joField = new JSONMaptron();
        GenericDeviceFieldType fieldType = field.getFieldType();
        if ( fieldType == null ) {
            fieldType = GenericDeviceFieldType.TEXT;
        }
        joField.put( "code", field.getCode() );
        joField.put( "name", field.getName() );
        joField.put( "description", field.getDescription() );
        joField.put( "fieldType", fieldType.name() );
        joField.put( "required", field.isRequired() );
        joField.put( "multiple", field.isMultiple() );
        joField.put( "enabled", field.isEnabled() );
        joField.put( "orderIndex", field.getOrderIndex() );
        joField.put( "placeholder", field.getPlaceholder() );
        joField.put( "unit", field.getUnit() );
        joField.put( "defaultValue", field.getDefaultValue() );
        joField.put( "minLength", field.getMinLength() );
        joField.put( "maxLength", field.getMaxLength() );
        joField.put( "minValue", field.getMinValue() );
        joField.put( "maxValue", field.getMaxValue() );
        joField.put( "pattern", field.getPattern() );
        joField.put( "options", this.encodeOptions( field.getOptions() ) );
        return joField;
    }

    protected JSONArray encodeOptions( List<GenericDeviceSchemaFieldOption> options ) {
        JSONArray jaOptions = new JSONArraytron();
        if ( options == null ) {
            return jaOptions;
        }

        for ( GenericDeviceSchemaFieldOption option : options ) {
            if ( option != null ) {
                JSONObject joOption = new JSONMaptron();
                joOption.put( "code", option.getCode() );
                joOption.put( "name", option.getName() );
                joOption.put( "value", option.getValue() );
                joOption.put( "enabled", option.isEnabled() );
                jaOptions.put( joOption );
            }
        }
        return jaOptions;
    }

    protected GenericDeviceFieldType decodeFieldType( String value ) {
        if ( this.isBlank( value ) ) {
            return GenericDeviceFieldType.TEXT;
        }
        return GenericDeviceFieldType.valueOf( value.trim().toUpperCase() );
    }

    protected Integer optInteger( JSONObject jo, String key ) {
        Object value = jo.opt( key );
        if ( value == null || value == JSON.NULL ) {
            return null;
        }
        if ( value instanceof Number ) {
            return ( (Number) value ).intValue();
        }
        if ( this.isBlank( value.toString() ) ) {
            return null;
        }
        return Integer.parseInt( value.toString() );
    }

    protected Double optDouble( JSONObject jo, String key ) {
        Object value = jo.opt( key );
        if ( value == null || value == JSON.NULL ) {
            return null;
        }
        if ( value instanceof Number ) {
            return ( (Number) value ).doubleValue();
        }
        if ( this.isBlank( value.toString() ) ) {
            return null;
        }
        return Double.parseDouble( value.toString() );
    }

    protected String defaultString( String value, String defaultValue ) {
        if ( this.isBlank( value ) ) {
            return defaultValue;
        }
        return value;
    }

    protected boolean isBlank( String value ) {
        return value == null || value.trim().isEmpty();
    }
}
