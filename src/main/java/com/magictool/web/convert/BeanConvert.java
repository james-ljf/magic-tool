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
     * 集合数据的拷贝
     * @param sources: 数据源类
     * @param target: 目标类::new
     * @return  List<T>
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
        }
        return list;
    }
}
