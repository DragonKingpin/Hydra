package com.pinecone.hydra.device.generic;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.framework.util.json.JSONObject;

public class GenericDeviceSchemaValidator implements Pinenut {
    protected final GenericDeviceSchemaTransformer schemaTransformer = new GenericDeviceSchemaTransformer();

    public void validate( GenericDeviceSchema schema, String schemaDataJson ) {
        if ( schema != null && !schema.isEnabled() ) {
            throw new IllegalArgumentException( "Generic device schema is disabled." );
        }

        GenericDeviceSchemaDesigner designer = null;
        if ( schema != null ) {
            designer = this.schemaTransformer.decode( schema.getSchemaJson() );
        }
        JSONObject joData = this.decodeData( schemaDataJson, designer );
        if ( designer == null || designer.getFields() == null || designer.getFields().isEmpty() ) {
            return;
        }

        for ( GenericDeviceSchemaField field : designer.getFields() ) {
            if ( field != null && field.isEnabled() ) {
                this.validateField( field, joData );
            }
        }
    }

    protected JSONObject decodeData( String schemaDataJson, GenericDeviceSchemaDesigner designer ) {
        if ( schemaDataJson == null || schemaDataJson.trim().isEmpty() ) {
            if ( this.hasRequiredField( designer ) ) {
                throw new IllegalArgumentException( "Generic device schema data is required." );
            }
            return null;
        }

        Object raw = JSON.parse( schemaDataJson );
        if ( !( raw instanceof JSONObject ) ) {
            throw new IllegalArgumentException( "Generic device schema data should be JSON object text." );
        }
        return (JSONObject) raw;
    }

    protected boolean hasRequiredField( GenericDeviceSchemaDesigner designer ) {
        if ( designer == null || designer.getFields() == null ) {
            return false;
        }
        for ( GenericDeviceSchemaField field : designer.getFields() ) {
            if ( field != null && field.isEnabled() && field.isRequired() ) {
                return true;
            }
        }
        return false;
    }

    protected void validateField( GenericDeviceSchemaField field, JSONObject joData ) {
        Object value = null;
        if ( joData != null ) {
            value = joData.opt( field.getCode() );
        }
        if ( this.isEmptyValue( value ) ) {
            if ( field.isRequired() ) {
                throw new IllegalArgumentException( "Generic device field is required: " + field.getCode() );
            }
            return;
        }

        if ( field.isMultiple() ) {
            if ( !( value instanceof Collection ) ) {
                throw new IllegalArgumentException( "Generic device field should be array: " + field.getCode() );
            }
            for ( Object item : (Collection) value ) {
                this.validateSingleValue( field, item );
            }
            return;
        }

        this.validateSingleValue( field, value );
    }

    protected void validateSingleValue( GenericDeviceSchemaField field, Object value ) {
        GenericDeviceFieldType fieldType = field.getFieldType();
        if ( fieldType == null ) {
            fieldType = GenericDeviceFieldType.TEXT;
        }

        switch ( fieldType ) {
            case INTEGER: {
                this.assertNumber( field, value );
                if ( this.toDouble( value ) % 1 != 0 ) {
                    throw new IllegalArgumentException( "Generic device field should be integer: " + field.getCode() );
                }
                this.validateNumberRange( field, value );
                break;
            }
            case DECIMAL: {
                this.assertNumber( field, value );
                this.validateNumberRange( field, value );
                break;
            }
            case BOOLEAN: {
                if ( !( value instanceof Boolean ) ) {
                    throw new IllegalArgumentException( "Generic device field should be boolean: " + field.getCode() );
                }
                break;
            }
            case SELECT:
            case MULTI_SELECT: {
                this.validateOptionValue( field, value );
                break;
            }
            case JSON: {
                if ( !( value instanceof Collection ) && !( value instanceof JSONObject ) ) {
                    throw new IllegalArgumentException( "Generic device field should be JSON object or array: " + field.getCode() );
                }
                break;
            }
            default: {
                this.validateText( field, value );
            }
        }
    }

    protected void validateText( GenericDeviceSchemaField field, Object value ) {
        String text = String.valueOf( value );
        if ( field.getMinLength() != null && text.length() < field.getMinLength() ) {
            throw new IllegalArgumentException( "Generic device field is shorter than minLength: " + field.getCode() );
        }
        if ( field.getMaxLength() != null && text.length() > field.getMaxLength() ) {
            throw new IllegalArgumentException( "Generic device field is longer than maxLength: " + field.getCode() );
        }
        if ( field.getPattern() != null && !field.getPattern().trim().isEmpty() && !text.matches( field.getPattern() ) ) {
            throw new IllegalArgumentException( "Generic device field does not match pattern: " + field.getCode() );
        }
    }

    protected void assertNumber( GenericDeviceSchemaField field, Object value ) {
        if ( value instanceof Number ) {
            return;
        }
        try {
            Double.parseDouble( value.toString() );
        }
        catch ( NumberFormatException e ) {
            throw new IllegalArgumentException( "Generic device field should be number: " + field.getCode(), e );
        }
    }

    protected void validateNumberRange( GenericDeviceSchemaField field, Object value ) {
        double nValue = this.toDouble( value );
        if ( field.getMinValue() != null && nValue < field.getMinValue() ) {
            throw new IllegalArgumentException( "Generic device field is less than minValue: " + field.getCode() );
        }
        if ( field.getMaxValue() != null && nValue > field.getMaxValue() ) {
            throw new IllegalArgumentException( "Generic device field is greater than maxValue: " + field.getCode() );
        }
    }

    protected double toDouble( Object value ) {
        if ( value instanceof Number ) {
            return ( (Number) value ).doubleValue();
        }
        return Double.parseDouble( value.toString() );
    }

    protected void validateOptionValue( GenericDeviceSchemaField field, Object value ) {
        if ( field.getOptions() == null || field.getOptions().isEmpty() ) {
            return;
        }
        for ( GenericDeviceSchemaFieldOption option : field.getOptions() ) {
            if ( option != null && option.isEnabled() && this.sameValue( option.getValue(), value ) ) {
                return;
            }
        }
        throw new IllegalArgumentException( "Generic device field option is invalid: " + field.getCode() );
    }

    protected boolean sameValue( Object left, Object right ) {
        if ( left == null ) {
            return right == null;
        }
        if ( right == null ) {
            return false;
        }
        return left.toString().equals( right.toString() );
    }

    protected boolean isEmptyValue( Object value ) {
        if ( value == null || value == JSON.NULL ) {
            return true;
        }
        if ( value instanceof String ) {
            return ( (String) value ).trim().isEmpty();
        }
        if ( value instanceof Collection ) {
            return ( (Collection) value ).isEmpty();
        }
        return false;
    }
}
