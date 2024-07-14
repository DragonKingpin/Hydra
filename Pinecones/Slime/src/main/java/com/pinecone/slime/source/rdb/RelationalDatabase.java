package com.pinecone.slime.source.rdb;

import com.pinecone.framework.system.prototype.Pinenut;

public interface RelationalDatabase extends Pinenut {
    String getHost();
    void setHost( String host );

    String getUsername();
    void setUsername( String username );

    String getPassword();
    void setPassword( String password );

    String getDatabase();
    void setDatabase( String database );

    int getPort();
    void setPort( int port );

    String getCharset();
    void setCharset( String charset );

    String getTablePrefix();
    void setTablePrefix( String tablePrefix );

    boolean isEnabled();
    void setEnabled( boolean enabled );

    String getDBType();
    void setDBType( String dbType );

    String toJDBCURL();
    void fromJDBCURL( String jdbcUrl );
}