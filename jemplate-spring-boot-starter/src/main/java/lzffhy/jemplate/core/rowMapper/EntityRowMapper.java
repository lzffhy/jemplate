package lzffhy.jemplate.core.rowMapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lzffhy.jemplate.core.utils.JdbcUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 封装JdbcTemplate通用mapper
 * @author yangjiajun
 * @param <T>
 */
public class EntityRowMapper<T> implements RowMapper<T> {

    private final Class<T> clazz;

    public EntityRowMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T mapRow(ResultSet rs, int row)  {
        JSONObject result = new JSONObject();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                result.put(JdbcUtil.lowerUnderscore(JdbcUtils.lookupColumnName(metaData, i)), JdbcUtils.getResultSetValue(rs, i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(result.toJSONString(), (Type) clazz);
    }
}
