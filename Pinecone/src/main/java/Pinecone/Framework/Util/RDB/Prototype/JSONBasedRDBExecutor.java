package Pinecone.Framework.Util.RDB.Prototype;

import Pinecone.Framework.Util.JSON.JSONArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

public interface JSONBasedRDBExecutor {
    String tableName( String szSimpleTable );

    long execute(String szSQL, boolean bIgnoreNoAffected) throws SQLException;

    ResultSet query(String szSQL) throws SQLException;



    JSONArray fetch     ( String szSQL ) throws SQLException ;

    long insertWithArray ( String szSimpleTable, Map dataMap ) throws SQLException;

    long updateWithArray ( String szSimpleTable, Map dataMap, Vector<Map.Entry> conditionMap, String szConditionGlue ) throws SQLException;

    long updateWithArray ( String szSimpleTable, Map dataMap, Map conditionMap, String szConditionGlue ) throws SQLException;



    long deleteWithArray (String szSimpleTable, Vector<Map.Entry> conditionMap, String szConditionGlue ) throws SQLException;

    long deleteWithArray ( String szSimpleTable, Map conditionMap,  String szConditionGlue ) throws SQLException;
}
