package com.pinecone.hydra.entity.ibatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import com.pinecone.framework.util.uoi.UOI;


@MappedTypes(UOI.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class UOITypeHandler extends BaseTypeHandler<UOI > {
    @Override
    public void setNonNullParameter( PreparedStatement ps, int i, UOI parameter, JdbcType jdbcType ) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public UOI getNullableResult( ResultSet rs, String columnName ) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return new UOI( value );
    }

    @Override
    public UOI getNullableResult( ResultSet rs, int columnIndex ) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return new UOI( value );
    }

    @Override
    public UOI getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        return new UOI( value );
    }
}