package com.magictool.web.constants;


import org.apache.lucene.store.Directory;

/**
 * 全局Lucene内存目录实例
 * @Author ljf
 * @Date 2021/11/15 11:29
 */
public class InitDirectory {

    public static Directory directory = null;

    private InitDirectory(){}

    public static Directory getDirectory(){
        return directory;
    }

    public static void setDirectory(Directory directory){
        InitDirectory.directory = directory;
    }

}
