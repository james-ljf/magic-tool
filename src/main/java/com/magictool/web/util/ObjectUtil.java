package com.magictool.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对象工具类
 */
public class ObjectUtil {

    private static final Logger logger = LoggerFactory.getLogger(ObjectUtil.class);

    /**
     * 将 object 对象转换为指定泛型对象
     *
     * @param obj 需要转换的对象
     * @param cla 转换的类型
     * @param <T> 泛型类型
     * @return 转换成 T 类型的对象, 如果转换失败则返回 {@link null}
     */
    public static <T> T objToT(Object obj, Class<T> cla) {
        try {
            return cla.cast(obj);
        } catch (ClassCastException e) {
            logger.error(" occur error to type conversion ", e);
        }
        return null;
    }

    /**
     * 检验某个对象中属性是否不为空
     *
     * @param target   被检验的对象
     * @param excludes 排除的属性名
     * @return {@code true} 不为空, {@code false} 为空
     */
    public static boolean checkFieldsNotEmptyExcludes(Object target, String... excludes) {
        return checkFieldsNotEmpty(target, 0, excludes);
    }

    /**
     * 检验某个对象中属性是否不为空
     *
     * @param target   被检验的对象
     * @param includes 包含的属性名
     * @return {@code true} 不为空, {@code false} 为空
     */
    public static boolean checkFieldsNotEmptyIncludes(Object target, String... includes) {
        return checkFieldsNotEmpty(target, 1, includes);
    }

    /**
     * 检验对象中属性是否不为空
     *
     * @param target     被检验的对象
     * @param strategy   策略，0为排除，1为包含
     * @param fieldNames 被检验的对象的属性名
     * @return {@code true} 不为空, {@code false} 为空
     */
    public static boolean checkFieldsNotEmpty(Object target, Integer strategy, String... fieldNames) {
        if (target == null) {
            return false;
        }
        Class<?> clz = target.getClass();
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (fit(field.getName(), strategy, fieldNames)) {
                try {
                    Object value = field.get(target);
                    if (value == null || (value instanceof String && ((String) value).trim().length() < 1)) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    static boolean fit(String fieldName, Integer strategy, String... fieldNames) {
        if (fieldNames == null || fieldNames.length < 1) {
            return true;
        }
        if (strategy == 0) {
            boolean flag = true;
            for (String name : fieldNames) {
                if (fieldName.equals(name)) {
                    flag = false;
                    break;
                }
            }
            return flag;
        } else if (strategy == 1) {
            for (String name : fieldNames) {
                if (fieldName.equals(name)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    /**
     * 替换对象中字符串属性的值（做高亮处理）
     *
     * @param target      目标对象
     * @param replacement 被替换的内容
     * @param fields      要被替换的参数
     */
    public static void highLightReplace(Object target, String replacement, String... fields) {
        Class<?> clz = target.getClass();
        Field[] fs = clz.getDeclaredFields();
        try {
            for (Field f : fs) {
                f.setAccessible(true);
                for (String field : fields) {
                    if (f.getName().equals(field)) {
                        String value = (String) f.get(target);
                        f.set(target, replaceAllIgnoreCase(value, replacement));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 忽略大小写的替换字符串中的特征字符串
     *
     * @param str   原字符串
     * @param regex 等待被替换的字符串
     * @return 替换后的字符串
     */
    public static String replaceAllIgnoreCase(String str, String regex) {
        Pattern p = Pattern.compile("((?i)" + regex + ")");
        Matcher m = p.matcher(str);
        if (m.find()) {
            StringBuilder result = new StringBuilder();
            String prefix = str.substring(0, m.start());
            String highLight = str.substring(m.start(), m.end());
            String suffix = str.substring(m.end());
            return result.append(prefix).append("<em>").append(highLight).append("</em>").append(replaceAllIgnoreCase(suffix, regex)).toString();
        }
        return str;
    }

    /**
     * 将 object 对象转换为指定泛型的 list 对象
     *
     * @param obj   需要转换的对象
     * @param cla   list 对象的泛型
     * @param <T>   list 对象的泛型类型
     * @return      转换的 list 对象
     */
    public static <T> List<T> objToList(Object obj, Class<T> cla){
        List<T> list = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                list.add(cla.cast(o));
            }
            return list;
        }
        return null;
    }

}
