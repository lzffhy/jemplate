package lzffhy.jemplate.core.utils;

import com.google.common.base.CaseFormat;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 数据操作常用工具类
 * @author yangjiajun
 */
public class JdbcUtil {

    /**
     * 需要过滤的特殊字符
     */
    private final static String REG = "[`~!@#$%^&*()+=|{}';',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

    /**
     * 下划线转驼峰
     *
     * @param s
     * @return
     */
    public static String lowerUnderscore(String s) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
    }

    /**
     * 驼峰转下划线
     *
     * @param s
     * @return
     */
    public static String lowerCamel(String s) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, s);
    }

    /**
     * 拼接entity属性
     *
     * @param clazz
     * @return
     */
    public static String getEntityFields(Class clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).map(field -> lowerCamel(field.getName())).collect(Collectors.joining(","));
    }

    public static String getEntityFields(Class clazz, String ... ignoreFields) {
        Map<String, String> ignoreFieldsMap = Arrays.stream(Optional.of(ignoreFields).orElse(new String[]{})).collect(Collectors.toMap(e -> e, e -> e));
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(e -> !ignoreFieldsMap.containsKey(e.getName()))
                .map(field -> lowerCamel(field.getName()))
                .collect(Collectors.joining(","));
    }

    /**
     * 拼接entity属性
     * @param clazz
     * @return
     */
    public static String getEntityFieldsIgnoreMysqlKeys(Class clazz) {
        return Arrays.stream(clazz.getDeclaredFields()).map(field -> ("`" + lowerCamel(field.getName())) + "`").collect(Collectors.joining(","));
    }

    /**
     * 拼接entity属性(忽略指定字段)
     * @param clazz
     * @param ignoreFields
     * @return
     */
    public static String getEntityFieldsIgnoreMysqlKeys(Class clazz, String ... ignoreFields) {
        Map<String, String> ignoreFieldsMap = Arrays.stream(Optional.of(ignoreFields).orElse(new String[]{})).collect(Collectors.toMap(e -> e, e -> e));
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(e -> !ignoreFieldsMap.containsKey(e.getName()))
                .map(field -> ("`" + lowerCamel(field.getName())) + "`")
                .collect(Collectors.joining(","));
    }


    /**
     * 根据列名返回查询的单列结果
     *
     * @param filedName
     * @return
     */
    public static List<String> getFiledList(String filedName, List<Map<String, Object>> queryResult) {
        List<String> res = new ArrayList<>();
        for (Map<String, Object> map : queryResult) {
            res.add(String.valueOf(map.get(filedName)));
        }
        return res;
    }

    /**
     * 特殊字符加上转义符
     * @param key
     * @return
     */
    public static String filterSearchKey(String key) {
        return Arrays.stream(key.split("")).map(e -> {
            if (Pattern.matches(REG, e)) {
                return "\\" + e;
            }
            return e;
        }).collect(Collectors.joining(""));
    }

    /**
     * 特殊字符加上转义符
     * @param args
     * @return
     */
    public static List<Object> filterSearchKeys(List<Object> args) {
        return args.stream().map(e -> {
            if (e instanceof String) {
                return filterSearchKey(String.valueOf(e));
            } else {
                return e;
            }
        }).collect(Collectors.toList());
    }
}
