package Pinecone.Framework.Util.RDB.MySQL;

import Pinecone.Framework.Debug.Debug;
import Pinecone.Framework.Util.RDB.Prototype.RDBHost;

import java.sql.*;

public class MySQLHost implements RDBHost {
    private String      mszLocation;

    private String      mszUsername;

    private String      mszPassword;

    private String      mszCharset;

    private Connection  mGlobalConnection;


    public MySQLHost( String dbLocation, String dbUsername, String dbPassword ) throws SQLException {
        this.mszLocation = dbLocation;
        this.mszUsername = dbUsername;
        this.mszPassword = dbPassword;
        this.mszCharset = "UTF-8";
        this.connect();
    }

    public MySQLHost( String dbLocation, String dbUsername, String dbPassword, String dbCharset ) throws SQLException {
        this.mszLocation = dbLocation;
        this.mszUsername = dbUsername;
        this.mszPassword = dbPassword;
        this.mszCharset = dbCharset;
        this.connect();
    }

    @Override
    public boolean isClosed() {
        if( this.mGlobalConnection == null ) {
            return true;
        }

        try {
            return this.mGlobalConnection.isClosed();
        }
        catch ( SQLException e ) {
            Debug.cerr( e );
            return false;
        }
    }

    @Override
    public void connect() throws SQLException {
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch ( ClassNotFoundException e ){
            throw new SQLException( "JDBC Driver is not found.", "CLASS_NOT_FOUND", e );
        }

        this.mGlobalConnection = DriverManager.getConnection("jdbc:mysql://" + this.mszLocation + "?characterEncoding="+ this.mszCharset +"&useSSL=false",this.mszUsername,this.mszPassword);
    }

    @Override
    public void close() throws SQLException {
        if( this.mGlobalConnection != null ) {
            this.mGlobalConnection.close();
        }
    }

    @Override
    public Connection getConnection() {
        return this.mGlobalConnection;
    }

    @Override
    public Statement  spawnStatement() throws SQLException {
        if( this.isClosed() ){
            this.connect();
        }

        return this.mGlobalConnection.createStatement();
    }


}
