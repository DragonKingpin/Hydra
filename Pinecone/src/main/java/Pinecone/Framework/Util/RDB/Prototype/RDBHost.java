package Pinecone.Framework.Util.RDB.Prototype;

import java.sql.*;

public interface RDBHost {
    Connection getConnection();

    boolean isClosed() ;

    void connect() throws SQLException;

    void close() throws SQLException;

    Statement  spawnStatement() throws SQLException;

}
