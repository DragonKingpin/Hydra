package com.pinecone.framework.util.rdb;

import java.sql.*;

public interface RDBHost {
    Connection getConnection();

    boolean isClosed() ;

    void connect() throws SQLException;

    void close() throws SQLException;

    Statement  createStatement() throws SQLException;

}
