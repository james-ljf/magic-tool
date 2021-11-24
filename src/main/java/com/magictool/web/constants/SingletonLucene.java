package com.magictool.web.constants;


import com.magictool.web.util.lucene.LuceneUtil;
import org.apache.lucene.store.Directory;

/**
 * 单例Lucene内存目录对象
 * @Author ljf
 * @Date 2021/11/15 11:29
 */
public class SingletonLucene {

    private SingletonLucene(){}

    private static class SingleDirectory{
        private static final Directory DIRECTORY = LuceneUtil.buildRAMDirectory();
    }

    /**
     * 内部类方法获取单例
     */
    public static Directory getDirectory(){
        return SingleDirectory.DIRECTORY;
    }

}
