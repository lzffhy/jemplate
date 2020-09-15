package lzffhy.jemplate.core.base;

import java.util.List;
import lzffhy.jemplate.core.exception.UnsupportedOperationException;

/**
 * 常用增删改查接口
 * @author yangjiajun
 */
public interface BaseMapper<R> {

    String UNSUPPORTED_MESSAGE = "Not implemented for this";

    /**
     * 查询单挑数据（类型R）
     * @param sql
     * @param args
     * @param rClass
     * @param
     * @return
     */
    default R queryOne(String sql, Object[] args, Class<R> rClass) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 查询单条数据（其它类型）
     * @param sql
     * @param args
     * @param kClass
     * @param <K>
     * @return
     */
    default <K> K queryOneTarget(String sql, Object[] args, Class<K> kClass) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 查询多条数据（类型R）
     * @param sql
     * @param args
     * @param rClass
     * @param
     * @return
     */
    default List<R> queryList(String sql, Object[] args, Class<R> rClass) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 查询多条数据（其它类型）
     * @param sql
     * @param args
     * @param kClass
     * @param <K>
     * @return
     */
    default <K> List<K> queryListTarget(String sql, Object[] args, Class<K> kClass) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 分页查询(类型R)
     * @param whereConditions
     * @param args
     * @param page
     * @param rows
     * @param
     * @return
     * @throws Exception
     */
    default BasePager<R> queryPage(String whereConditions, String tableName, Object[] args, Class<R> clazz, Integer page, Integer rows) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     *分页查询(其它类型)
     * @param whereConditions
     * @param args
     * @param clazz
     * @param page
     * @param rows
     * @param <K>
     * @return
     * @throws Exception
     */
    default <K> BasePager<K> queryPageTarget(String whereConditions, String tableName, Object[] args, Class<K> clazz, Integer page, Integer rows) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 新增
     * @param r
     * @param rClass
     * @param tableName
     * @return
     */
    default Integer insert(R r, Class<R> rClass, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 批量新增
     * @param rs
     * @param rClass
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer batchInsert(R[] rs, Class<R> rClass, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 新增（返回自增主键）
     * @param r
     * @param rClass
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer insertReturnKey(R r, Class<R> rClass, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 修改
     * @param r
     * @param rClass
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer update(R r, Class<R> rClass, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 修改（忽略null）
     * @param r
     * @param rClass
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer updateIgnoreBlank(R r, Class<R> rClass, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 批量修改
     * @param r
     * @param rClass
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer batchUpdate(R[] r, Class<R> rClass, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 删除
     * @param key
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer delete(String key, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 删除
     * @param key
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer delete(Integer key, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 批量删除
     * @param keys
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer batchDelete(String[] keys, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }

    /**
     * 批量删除
     * @param keys
     * @param tableName
     * @return
     * @throws Exception
     */
    default Integer batchDelete(Integer[] keys, String tableName) throws Exception {
        throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
    }
}
