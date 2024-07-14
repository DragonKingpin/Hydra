package com.pinecone.framework.util.rdb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DirectResultSession implements ResultSession {
    protected RDBHost mHost;

    protected Statement mStatement;

    protected ResultSet mResultSet;

    public DirectResultSession( RDBHost host, Statement statement, ResultSet resultSet ) {
        this.mHost      = host;
        this.mStatement = statement;
        this.mResultSet = resultSet;
    }

    @Override
    public RDBHost getHost() {
        return this.mHost;
    }

    @Override
    public Statement getStatement() {
        return this.mStatement;
    }

    @Override
    public ResultSet getResultSet() {
        return this.mResultSet;
    }

    @Override
    public void close() throws SQLException {
        this.mStatement.close();
        this.mResultSet.close();
        this.mHost = null;
    }
}
