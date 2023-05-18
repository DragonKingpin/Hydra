package Pinecone.Framework.Util.RDB;

import Pinecone.Framework.Util.RDB.Prototype.SQLSplicer;

import java.util.Map;
import java.util.Vector;

public class MappedSQLSplicer implements SQLSplicer {
    public String spliceSingleKeyValueSequence( Object szKey, Object szValue, String szGlue ){
        return String.format(" `%s` = '%s' %s ",szKey,szValue, szGlue );
    }

    public String spliceSimpleKeyValuesSequence( Vector<Map.Entry> keyValues, String szGlue ) {
        if ( keyValues!= null ) {
            StringBuilder sqlStream = new StringBuilder();
            int i = 0, mapSize = keyValues.size();
            for ( Map.Entry item : keyValues ) {
                sqlStream.append( this.spliceSingleKeyValueSequence( item.getKey(),item.getValue(), (i++ != mapSize - 1) ? szGlue : "" ) );
            }
            return sqlStream.toString();
        }
        return "";
    }

    public String spliceSimpleKeyValuesSequence( Map keyValues, String szGlue ) {
        if ( keyValues!= null ) {
            StringBuilder sqlStream = new StringBuilder();
            int i = 0, mapSize = keyValues.size();

            for ( Object each : keyValues.entrySet() ) {
                Map.Entry item = (Map.Entry) each;
                sqlStream.append(this.spliceSingleKeyValueSequence(item.getKey(), item.getValue(), (i++ != mapSize - 1) ? szGlue : ""));
            }
            return sqlStream.toString();
        }
        return "";
    }

    public String spliceInsertSQL ( String szFullNameTable, Map dataMap, boolean bReplace ) {
        if ( dataMap != null ) {
            StringBuilder sqlStream = new StringBuilder();
            sqlStream.append( String.format( bReplace ? "REPLACE INTO `%s` " : "INSERT INTO `%s` ", szFullNameTable ) )  ;
            int i = 0, mapSize = dataMap.size();
            StringBuilder sql_key = new StringBuilder();
            StringBuilder sql_value = new StringBuilder();

            for ( Object each : dataMap.entrySet() ) {
                Map.Entry item = (Map.Entry) each;
                sql_key .append( "`" ) .append( item.getKey() ).append( "`" ).append((i != mapSize - 1) ? "," : "");
                sql_value .append("'" ) .append( item.getValue() ).append( "'" ).append ((i++ != mapSize - 1) ? "," : "");
            }
            sqlStream .append( " ( " ).append( sql_key.toString() ).append( " ) VALUES ( " ).append( sql_value.toString() ).append( " )" );
            return sqlStream.toString();
        }
        return "";
    }




    public String spliceNoConditionUpdateSQL( String szFullNameTable, Map dataMap ) {
        if ( dataMap != null ) {
            return String.format( "UPDATE `%s` SET %s ", szFullNameTable, this.spliceSimpleKeyValuesSequence(dataMap, ",") );
        }
        return "";
    }

    public String spliceUpdateSQL ( String szFullNameTable, Map dataMap, Vector<Map.Entry> conditionKeyValues, String szConditionGlue ) {
        if ( dataMap != null ) {
            String szConditionSQL = this.spliceSimpleKeyValuesSequence( conditionKeyValues, szConditionGlue );
            if( !szConditionSQL.isEmpty() ){
                return this.spliceNoConditionUpdateSQL( szFullNameTable, dataMap ) + " WHERE " + szConditionSQL;
            }
        }
        return "";
    }

    public String spliceUpdateSQL ( String szFullNameTable, Map dataMap, Map conditionKeyValues, String szConditionGlue ) {
        if ( dataMap != null ) {
            String szConditionSQL = this.spliceSimpleKeyValuesSequence( conditionKeyValues, szConditionGlue );
            if( !szConditionSQL.isEmpty() ){
                return this.spliceNoConditionUpdateSQL( szFullNameTable, dataMap ) + " WHERE  " + szConditionSQL;
            }
        }
        return "";
    }



    public String spliceDeleteSQL ( String szFullNameTable, Vector<Map.Entry> conditionKeyValues, String szConditionGlue ) {
        StringBuilder sqlStream = new StringBuilder();
        sqlStream .append( String.format( "DELETE FROM `%s` ", szFullNameTable ) );
        if ( conditionKeyValues != null ) {
            sqlStream.append( " WHERE " );
            sqlStream.append( this.spliceSimpleKeyValuesSequence( conditionKeyValues, szConditionGlue ) );
        }
        return sqlStream.toString();
    }

    public String spliceDeleteSQL ( String szFullNameTable, Map conditionKeyValues, String szConditionGlue ) {
        StringBuilder sqlStream = new StringBuilder();
        sqlStream .append( String.format( "DELETE FROM `%s` ", szFullNameTable ) );
        if ( conditionKeyValues != null ) {
            sqlStream.append( " WHERE  " );
            sqlStream.append( this.spliceSimpleKeyValuesSequence( conditionKeyValues, szConditionGlue ) );
        }
        return sqlStream.toString();
    }


}
