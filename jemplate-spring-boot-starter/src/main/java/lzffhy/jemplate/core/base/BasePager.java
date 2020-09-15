package lzffhy.jemplate.core.base;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 基础分页类
 * @author yangjiajun
 * @param <T>
 */
public class BasePager<T> {

    private final List<T> entities;

    private final long rows;

    private BasePager(List<T> entities, long rows) {
        this.entities = Optional.ofNullable(entities).orElse(Collections.emptyList());
        this.rows = rows;
    }

    public List<T> getEntities() {
        return entities;
    }

    public long getRows() {
        return rows;
    }

    public static <R> BasePager<R> build(List<R> entities, long rows) {
        return new BasePager<R>(entities, rows);
    }

    public static <R> BasePager<R> buildEmpty() {
        return new BasePager<R>(null, 0);
    }
}
