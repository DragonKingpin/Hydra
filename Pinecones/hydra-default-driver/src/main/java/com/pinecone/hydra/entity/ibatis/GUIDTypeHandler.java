package com.pinecone.hydra.entity.ibatis;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.id.GUID72;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(GUID.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class GUIDTypeHandler extends BaseTypeHandler<GUID> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, GUID parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public GUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return new GUID72( value );
    }

    @Override
    public GUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return new GUID72( value );
    }

    @Override
    public GUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return new GUID72( value );
    }
}