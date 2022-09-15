package com.magictool.web.util;

import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * 集合处理工具
 * @author lijf
 */
public class ListUtils {

    /**
     * 根据集合范型的属性删除集合中的重复元素
     * @param list  集合
     * @param function  such as：User::getUId
     */
    public static <T,E> void removeElement(List<T> list, Function<T,E> function){
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        Set<E> set = new HashSet<>();
        list.removeIf(e -> function.apply(e) != null && !set.add(function.apply(e)));
    }

}

