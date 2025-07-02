package com.pinecone.hydra.entity.ibatis;

import java.net.URI;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(URI.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class URITypeHandler extends BaseTypeHandler<URI> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, URI parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public URI getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return URI.create( value );
    }

    @Override
    public URI getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return URI.create( value );
    }

    @Override
    public URI getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return URI.create( value );
    }

}
