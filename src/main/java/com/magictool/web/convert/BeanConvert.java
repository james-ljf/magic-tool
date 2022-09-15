package com.magictool.web.convert;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


/**
 * 集合类型实体类bean的转换工具
 * @author lijf
 */
public class BeanConvert extends BeanUtils {
    /**
     * 集合数据的拷贝,需字段类型对应
     * @param sources: 数据源类
     * @param target: 目标类::new
     * @return  List<T>
     */
    public static <S, T> List<T> copyList(List<S> sources, Supplier<T> target) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
        }
        return list;
    }

    /**
     * 范型不同集合类转换
     * @param list  需要被转换的集合
     * @param clazz 需要转换成的类(如：User.class)
     * @return  List<T>
     */
    public static <T> List<T> copyList(List<?> list, Class<T> clazz) {
        List<T> result = new ArrayList<>(list.size());
        for (Object source : list) {
            T target;
            try {
                target = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException();
            }
            BeanUtils.copyProperties(source, target);
            result.add(target);
        }
        return result;
    }
}
