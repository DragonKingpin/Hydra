package com.walnut.sparta.config.mybatisConfig;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.ulf.util.id.GUID72;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(GUID72.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class GUID72TypeHandler extends BaseTypeHandler<GUID72> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, GUID72 parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public GUID72 getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String value = rs.getString(columnName);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        String[] parts = value.split("-");
        // 将十六进制字符串转换为十进制整数
        long deltaSecondsDec = new BigInteger(parts[0], 16).longValue();
        long workerIdDec = new BigInteger(parts[1], 16).longValue();
        long sequenceDec = new BigInteger(parts[2], 16).longValue();
        long nanoSeedDec = new BigInteger(parts[3], 16).longValue();

        // 创建一个新的GUID64实例
        return new GUID72(deltaSecondsDec, workerIdDec, sequenceDec, nanoSeedDec);
    }

    @Override
    public GUID72 getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String value = rs.getString(columnIndex);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        String[] parts = value.split("-");
        // 将十六进制字符串转换为十进制整数
        long deltaSecondsDec = new BigInteger(parts[0], 16).longValue();
        long workerIdDec = new BigInteger(parts[1], 16).longValue();
        long sequenceDec = new BigInteger(parts[2], 16).longValue();
        long nanoSeedDec = new BigInteger(parts[3], 16).longValue();

        // 创建一个新的GUID64实例
        return new GUID72(deltaSecondsDec, workerIdDec, sequenceDec, nanoSeedDec);
    }

    @Override
    public GUID72 getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String value = cs.getString(columnIndex);
        if (value == null) {
            return null; // 如果值为 null，则直接返回 null
        }
        String[] parts = value.split("-");
        // 将十六进制字符串转换为十进制整数
        long deltaSecondsDec = new BigInteger(parts[0], 16).longValue();
        long workerIdDec = new BigInteger(parts[1], 16).longValue();
        long sequenceDec = new BigInteger(parts[2], 16).longValue();
        long nanoSeedDec = new BigInteger(parts[3], 16).longValue();

        // 创建一个新的GUID64实例
        return new GUID72(deltaSecondsDec, workerIdDec, sequenceDec, nanoSeedDec);
    }
}
