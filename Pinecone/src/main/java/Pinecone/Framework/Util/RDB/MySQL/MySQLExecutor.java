package Pinecone.Framework.Util.RDB.MySQL;
import Pinecone.Framework.Debug.Debug;
import Pinecone.Framework.Util.JSON.JSONArray;
import Pinecone.Framework.Util.JSON.JSONObject;
import Pinecone.Framework.Util.RDB.Prototype.JSONBasedRDBExecutor;
import Pinecone.Framework.Util.RDB.MappedSQLSplicer;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

public class MySQLExecutor implements JSONBasedRDBExecutor {
    private MySQLHost   mySQLHost;

    private String      mszTableNamespace;

    private MappedSQLSplicer mSimpleSQLSpawner = null ;

    private String      mszLastSQLSentence     = ""   ;

    private Statement   mCurrentStatement             ;


    private void init() {
        this.mSimpleSQLSpawner = new MappedSQLSplicer();
    }


    public MySQLHost getMySQLHost(){
        this.init();
        return this.mySQLHost;
    }

    public MySQLExecutor( MySQLHost mySQLHost ){
        this.init();
        this.mySQLHost = mySQLHost;
    }


    public void setTableNamespace( String szTableNamespace ){
        this.mszTableNamespace = szTableNamespace;
    }

    public String getTableNamespace() {
        return this.mszTableNamespace;
    }

    private void affirmCurrentStatement0() throws SQLException {
        ///[Warning] Multi-thread has error ! Notice framework change.
        if( this.mCurrentStatement == null || this.mySQLHost.isClosed() ) {
            this.mCurrentStatement = this.mySQLHost.spawnStatement();
        }
    }

    private Statement spawnStatement() throws SQLException {
        return this.mySQLHost.spawnStatement();
    }

    public ResultSet query( String szSQL ) throws SQLException {
        this.mszLastSQLSentence = szSQL;
        //this.affirmCurrentStatement();
        return this.spawnStatement().executeQuery( szSQL );
    }

    public long execute( String szSQL, boolean bIgnoreNoAffected ) throws SQLException {
        this.mszLastSQLSentence = szSQL;
        //this.affirmCurrentStatement();
        Statement statement = this.spawnStatement();
        statement.execute( szSQL );
        if( bIgnoreNoAffected ){
            return 1;
        }
        return statement.getUpdateCount();
    }

    public long execute( String szSQL ) throws SQLException {
        return this.execute( szSQL, false );
    }

    public String getLastSQLSentence() {
        return this.mszLastSQLSentence;
    }


    public String tableName( String szSimpleTable ){
        return this.mszTableNamespace + szSimpleTable;
    }

    public int getSumFromTable(String szSimpleTable){
        try{
            ResultSet resultSet = this.query("SELECT COUNT(*) FROM " + this.tableName(szSimpleTable));
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        }
        catch ( Exception E ){
            return 0;
        }
    }

    public int countFromTable( String szSQL ){
        try{
            ResultSet resultSet = this.query(szSQL);
            resultSet.next();
            return resultSet.getInt("COUNT(*)");
        }
        catch ( Exception E ){
            System.err.println( this.mszLastSQLSentence );
            return 0;
        }
    }




    /** Fetch Function **/
    public String[] fetchAllColumn ( String szTable ) throws SQLException {
        String szSQL = "SHOW COLUMNS FROM `" + this.tableName( szTable ) + "`";
        ResultSet resultSet = this.query( szSQL );
        resultSet.last();
        int nRow = resultSet.getRow();
        resultSet.beforeFirst();
        String[] columns = new String[ nRow ];

        int j = 0;
        while ( resultSet.next() ){
            columns[ j++ ] = resultSet.getString( 1 );
        }
        return columns;
    }

    public static String[] column2Array( ResultSet rResult )throws SQLException {
        ResultSetMetaData metaData = rResult.getMetaData();
        int nColumnCount = metaData.getColumnCount();
        String[] columns = new String[nColumnCount];

        for ( int i = 1, j = 0; i <= nColumnCount; i++ ) {
            columns[j++] = metaData.getColumnLabel(i);
        }
        return columns;
    }

    public ArrayList<Map<String,Object > > fetchAssoc( String szSQL ) throws SQLException {
        ResultSet resultSet = this.query(szSQL);
        String[] columnIndexMap = MySQLExecutor.column2Array(resultSet);
        ArrayList<Map<String,Object > > queryResult = new ArrayList<>();
        int sizeofRowSet = columnIndexMap.length;

        int jc = 0;
        while ( resultSet.next() ){
            queryResult.add( new LinkedHashMap<>() );
            for (int i = 1, j = 0; i <= sizeofRowSet; i++) {
                queryResult.get(jc).put(
                        columnIndexMap[ j++ ], resultSet.getObject(i)
                );
            }
            jc++;
        }

        return queryResult;
    }

    public JSONArray fetch     ( String szSQL ) throws SQLException {
        ResultSet resultSet = this.query( szSQL );
        String[] columnIndexMap = MySQLExecutor.column2Array(resultSet);
        JSONArray queryResult = new JSONArray();

        int sizeofRowSet = columnIndexMap.length;
        int jc = 0;
        while (resultSet.next()){
            queryResult.put(new JSONObject());
            for (int i = 1, j = 0; i <= sizeofRowSet; i++) {
                queryResult.getJSONObject(jc).put(
                        columnIndexMap[ j++ ], resultSet.getObject(i)
                );
            }
            jc++;
        }

        return queryResult;
    }

    /**
     *  Using java class to store query result if these data operated particular frequently (> 1e6)
     *  According to trail result, if calculation scale beyond (1e6) there is a significant
     *  performance gap between java native object and the HashMap based Pinecone::JSONObject.
     *  *****************************************************************************************
     *  Experiment At [Intel(R) Core(TM) i7-9750H CPU @ 2.60GHz (Single Thread)]:
     *  Trail At 1e6 : HashMap [11ms], Java Native Object [<10ms] the difference is tiny.
     *  Trail At 1e7 : HashMap [~100ms], Java Native Object [10ms ~ 20ms] the difference is huge but still acceptable.
     *  Trail At 1e8 : HashMap [>1000ms], Java Native Object [80ms ~ 150ms] the difference is huge but unacceptable.
     *  *****************************************************************************************
     *  JSONObject Mode was recommended to be used in temporary query object or normal condition.
     *  NativeObject Mode was recommended to be used in the query result will be manipulated frequently.
     */
//    public Object selectJavaify ( String szSQL, Class<?> antetype ) throws SQLException {
//
//    }


    /** Insert Function **/
    public long insertWithArray ( String szSimpleTable, Map dataMap, boolean bReplace ) throws SQLException {
        if ( dataMap != null ) {
            return this.execute( this.mSimpleSQLSpawner.spliceInsertSQL( this.tableName( szSimpleTable ), dataMap, bReplace ) );
        }
        return -1;
    }

    public long insertWithArray ( String szSimpleTable, Map dataMap ) throws SQLException {
        return insertWithArray(szSimpleTable,dataMap,false);
    }



    /** Update Function **/
    public long updateWithArray ( String szSimpleTable, Map dataMap, Vector<Map.Entry> conditionMap, String szConditionGlue ) throws SQLException {
        if ( dataMap != null ) {
            return this.execute(
                    this.mSimpleSQLSpawner.spliceUpdateSQL (
                            this.tableName( szSimpleTable ),
                            dataMap,
                            conditionMap,
                            szConditionGlue
                    ),
                    true
            );
        }
        return -1;
    }

    public long updateWithArray ( String szSimpleTable, Map dataMap, Vector<Map.Entry> conditionMap ) throws SQLException {
        return this.updateWithArray( szSimpleTable, dataMap, conditionMap, "AND" );
    }

    public long updateWithArray ( String szSimpleTable, Map dataMap, Map conditionMap, String szConditionGlue ) throws SQLException {
        if ( dataMap != null ) {
            return this.execute(
                    this.mSimpleSQLSpawner.spliceUpdateSQL (
                            this.tableName( szSimpleTable ),
                            dataMap,
                            conditionMap,
                            szConditionGlue
                    ),
                    true
            );
        }
        return -1;
    }

    public long updateWithArray ( String szSimpleTable, Map dataMap, Map conditionMap ) throws SQLException {
        return this.updateWithArray( szSimpleTable, dataMap, conditionMap, "AND" );
    }

    public long updateWithArray ( String szSimpleTable, Map dataMap, String szConditionSQL ) throws SQLException {
        if ( dataMap != null ) {
            StringBuilder sqlStream = new StringBuilder();
            sqlStream.append( this.mSimpleSQLSpawner.spliceNoConditionUpdateSQL( this.tableName( szSimpleTable ),dataMap ) );

            if ( szConditionSQL!= null ) {
                if( !szConditionSQL.toLowerCase().contains("where")){
                    sqlStream.append(" WHERE ");
                }
                sqlStream.append( szConditionSQL );
            }
            return this.execute( sqlStream.toString(), true );
        }
        return -1;
    }

    public long updateWithArray ( String szSimpleTable, Map dataMap ) throws SQLException {
        return updateWithArray( szSimpleTable, dataMap, (Map) null, "AND" );
    }



    /** Delete Function **/
    public long deleteWithArray ( String szSimpleTable, Vector<Map.Entry> conditionMap,  String szConditionGlue ) throws SQLException {
        if ( conditionMap != null ) {
            return this.execute( this.mSimpleSQLSpawner.spliceDeleteSQL( this.tableName(szSimpleTable), conditionMap, szConditionGlue ) );
        }
        return this.execute("TRUNCATE `" + tableName(szSimpleTable) + '`');
    }

    public long deleteWithArray ( String szSimpleTable, Map conditionMap,  String szConditionGlue ) throws SQLException {
        if ( conditionMap != null ) {
            return this.execute( this.mSimpleSQLSpawner.spliceDeleteSQL( this.tableName(szSimpleTable), conditionMap, szConditionGlue ) );
        }
        return this.execute("TRUNCATE  `" + tableName(szSimpleTable) + '`');
    }

    public long deleteWithArray ( String szSimpleTable, Vector<Map.Entry> conditionMap ) throws SQLException {
        return this.deleteWithArray( szSimpleTable,conditionMap,"AND" );
    }

    public long deleteWithArray ( String szSimpleTable, Map conditionMap ) throws SQLException {
        return this.deleteWithArray( szSimpleTable,conditionMap,"AND" );
    }

    public long deleteWithSQL   ( String szSimpleTable, String szConditionSQL ) throws SQLException {
        StringBuilder sqlStream = new StringBuilder();
        sqlStream .append( "DELETE FROM `" ).append( tableName(szSimpleTable) ).append( "`" );
        if ( szConditionSQL!= null ) {
            if( !szConditionSQL.toLowerCase().contains("where")){
                sqlStream.append(" WHERE ");
            }
            sqlStream.append( szConditionSQL );
        }
        return this.execute( sqlStream.toString() );
    }
}
