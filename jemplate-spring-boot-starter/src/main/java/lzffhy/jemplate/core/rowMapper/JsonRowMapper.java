package lzffhy.jemplate.core.rowMapper;

import com.alibaba.fastjson.JSONObject;
import lzffhy.jemplate.core.utils.JdbcUtil;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 封装JdbcTemplate查询结果转json通用类
 * @author yangjiajun
 */
public class JsonRowMapper implements RowMapper<JSONObject> {

    //是否下划线转驼峰
    private boolean caseFormat = true;

    public JsonRowMapper(boolean caseFormat) {
        this.caseFormat = caseFormat;
    }

    public JsonRowMapper() {}

    @Override
    public JSONObject mapRow(ResultSet rs, int rowNum) {
        JSONObject result = new JSONObject();
        try {
            ResultSetMetaData metaData = rs.getMetaData();
            int count = metaData.getColumnCount();
            for (int i = 1; i <= count; i++) {
                if (caseFormat) {
                    result.put(JdbcUtil.lowerUnderscore(JdbcUtils.lookupColumnName(metaData, i)), JdbcUtils.getResultSetValue(rs, i));
                } else {
                    result.put(JdbcUtils.lookupColumnName(metaData, i), JdbcUtils.getResultSetValue(rs, i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
