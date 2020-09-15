package lzffhy.jemplate.core.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import lzffhy.jemplate.core.exception.JdbcException;
import lzffhy.jemplate.core.rowMapper.EntityRowMapper;
import lzffhy.jemplate.core.utils.JdbcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * 封装JdbcTemplate 常用增删改查操作
 * @author yangjiajun
 * @param
 */
@Slf4j
public class BaseRepository<R> implements BaseMapper<R> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void initJdbcTemplate(DriverManagerDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public R queryOne(String sql, Object[] args, Class<R> clazz) throws Exception {
        try {
            return jdbcTemplate.queryForObject(sql, filterArgs(args), new EntityRowMapper<>(clazz));
        } catch (EmptyResultDataAccessException e) {
            //未查询到数据时会抛出该异常
            return null;
        }
    }

    @Override
    public <K> K queryOneTarget(String sql, Object[] args, Class<K> kClass) throws Exception {
        try {
            return jdbcTemplate.queryForObject(sql, filterArgs(args), new EntityRowMapper<>(kClass));
        } catch (EmptyResultDataAccessException e) {
            //未查询到数据时会抛出该异常
            return null;
        }
    }

    @Override
    public List<R> queryList(String sql, Object[] args, Class<R> clazz) throws Exception {
        return Optional.of(jdbcTemplate.query(sql, filterArgs(args), new EntityRowMapper<>(clazz))).orElse(Collections.emptyList());
    }

    @Override
    public <K> List<K> queryListTarget(String sql, Object[] args, Class<K> clazz) throws Exception {
        return Optional.of(jdbcTemplate.query(sql, filterArgs(args), new EntityRowMapper<>(clazz))).orElse(Collections.emptyList());
    }

    @Override
    public BasePager<R> queryPage(String whereConditions, String tableName, Object[] args, Class<R> clazz, Integer page, Integer rows) throws Exception {
        String dataSql = "SELECT " + JdbcUtil.getEntityFieldsIgnoreMysqlKeys(clazz) + " FROM " + tableName +
                " WHERE 1 = 1 " + whereConditions + " LIMIT " + (page - 1) * rows + "," + rows;
        String countSql = "SELECT COUNT(1) FROM " + tableName + " WHERE 1 = 1 " + whereConditions;
        return BasePager.build(Optional.ofNullable(this.queryList(dataSql, Optional.of(args).orElse(new Object[]{}), clazz)).orElse(Collections.emptyList()),
                Optional.ofNullable(jdbcTemplate.queryForObject(countSql, Optional.of(args).orElse(new Object[]{}), Long.class)).orElse(0L));
    }

    @Override
    public Integer insert(R r, Class<R> clazz, String tableName) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        JSONObject entity = JSON.parseObject(JSON.toJSONString(r));
        List<Object> values = Arrays.stream(fields).map(e -> entity.getObject(e.getName(), e.getGenericType())).collect(Collectors.toList());
        return jdbcTemplate.update(buildInsertSql(clazz, tableName), values.toArray());
    }

    
    @Override
    public Integer batchInsert(R[] rs, Class<R> clazz, String tableName) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        List<Object[]> params = new ArrayList<>();
        for (R r : rs) {
            JSONObject entity = JSON.parseObject(JSON.toJSONString(r));
            List<Object> values = Arrays.stream(fields).map(e -> entity.getObject(e.getName(), e.getGenericType())).collect(Collectors.toList());
            params.add(values.toArray());
        }
        int[] results = jdbcTemplate.batchUpdate(buildInsertSql(clazz, tableName), params);
        return Arrays.stream(results).sum();
    }

    @Override
    public Integer insertReturnKey(R r, Class<R> clazz, String tableName) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        JSONObject entity = JSON.parseObject(JSON.toJSONString(r));
        String keys = Arrays.stream(fields)
                .filter(e -> !e.getName().equals("id"))
                .filter(e -> entity.getObject(e.getName(), e.getGenericType()) != null)
                .map(e -> "`" + JdbcUtil.lowerCamel(e.getName()) + "`")
                .collect(Collectors.joining(","));
        List<Object> values = Arrays.stream(fields)
                .filter(e -> !e.getName().equals("id"))
                .filter(e -> entity.getObject(e.getName(), e.getGenericType()) != null)
                .map(e -> entity.getObject(e.getName(), e.getGenericType()))
                .collect(Collectors.toList());
        sb.append("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(keys)
                .append(") VALUES (")
                .append(values.stream().map(e -> "'" + e + "'").collect(Collectors.joining(",")))
                .append(")");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS), keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    /**
     * 拼接新增sql
     * @param clazz
     * @param tableName
     * @return
     * @throws Exception
     */
    private String buildInsertSql (Class clazz, String tableName) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ")
                .append(tableName)
                .append(" (")
                .append(JdbcUtil.getEntityFieldsIgnoreMysqlKeys(clazz))
                .append(") VALUES (")
                .append(Arrays.stream(fields).map(e -> "?").collect(Collectors.joining(",")))
                .append(")");
        return sb.toString();
    }

    @Override
    public Integer update(R r, Class<R> clazz, String tableName) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        JSONObject entity = JSON.parseObject(JSON.toJSONString(r));
        sb.append("UPDATE ")
                .append(tableName)
                .append(" SET ")
                .append(Arrays.stream(fields)
                        .filter(e -> !e.getName().equals("id"))
                        .map(e -> "`" + JdbcUtil.lowerCamel(e.getName()) + "`" + " = ? ")
                        .collect(Collectors.joining(",")))
                .append(" WHERE id = ? ");
        List<Object> values = Arrays.stream(fields).filter(e -> !e.getName().equals("id")).map(e -> entity.getObject(e.getName(), e.getGenericType())).collect(Collectors.toList());
        values.add(Arrays.stream(fields)
                .filter(e -> e.getName().equals("id"))
                .map(e -> entity.getObject(e.getName(), e.getGenericType()))
                .findFirst()
                .orElseThrow(() -> new JdbcException("unknown id")));
        return jdbcTemplate.update(sb.toString(), values.toArray());
    }

    @Override
    public Integer updateIgnoreBlank(R r, Class<R> clazz, String tableName) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        JSONObject entity = JSON.parseObject(JSON.toJSONString(r));
        sb.append("UPDATE ")
                .append(tableName)
                .append(" SET ")
                .append(Arrays.stream(fields)
                        .filter(e -> !e.getName().equals("id"))
                        .filter(e -> entity.getObject(e.getName(), e.getGenericType()) != null)
                        .map(e -> "`" + JdbcUtil.lowerCamel(e.getName()) + "`" + " = ? ")
                        .collect(Collectors.joining(",")))
                .append(" WHERE id = ? ");
        List<Object> values = Arrays.stream(fields)
                .filter(e -> !e.getName().equals("id"))
                .filter(e -> entity.getObject(e.getName(), e.getGenericType()) != null)
                .map(e -> entity.getObject(e.getName(), e.getGenericType()))
                .collect(Collectors.toList());
        values.add(Arrays.stream(fields)
                .filter(e -> e.getName().equals("id"))
                .map(e -> entity.getObject(e.getName(), e.getGenericType()))
                .findFirst()
        .orElseThrow(() -> new JdbcException("unknown id")));
        return jdbcTemplate.update(sb.toString(), values.toArray());
    }

    @Override
    public Integer delete(String key, String tableName) throws Exception {
        String sql = "DELETE FROM " + tableName + " WHERE id = ? ";
        return jdbcTemplate.update(sql, key);
    }

    @Override
    public Integer delete(Integer key, String tableName) throws Exception {
        String sql = "DELETE FROM " + tableName + " WHERE id = ? ";
        return jdbcTemplate.update(sql, key);
    }

    @Override
    public Integer batchDelete(String[] keys, String tableName) throws Exception {
        String sql = "DELETE FROM " + tableName + " WHERE id IN ( " + Arrays.stream(keys).map(e -> "'" + e + "'").collect(Collectors.joining(",")) + ")";
        return jdbcTemplate.update(sql);
    }

    @Override
    public Integer batchDelete(Integer[] keys, String tableName) throws Exception {
        String sql = "DELETE FROM " + tableName + " WHERE id IN ( " + Arrays.stream(keys).map(e -> e + "").collect(Collectors.joining(",")) + ")";
        return jdbcTemplate.update(sql);
    }

    private Object[] filterArgs(Object[] args) {
        return JdbcUtil.filterSearchKeys(Arrays.asList(args)).toArray();
    }
}
