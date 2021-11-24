package com.magictool.web.util.lucene;

import java.io.IOException;
import java.nio.file.Paths;

import com.magictool.web.constants.SingletonLucene;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryTermScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * Lucene常用工具类
 * @Author ljf
 * @Date 2021/11/10 17:41
 */
@Slf4j
public class LuceneUtil {

    /**
     * 创建存储目录（磁盘）
     * @param filePath  索引存放在磁盘的路径
     */
    public static Directory buildFSDirectory(String filePath){
        try{
            return FSDirectory.open(Paths.get(filePath));
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建存储目录（内存）
     */
    public static Directory buildRAMDirectory() {
        return new RAMDirectory();
    }

    /**
     * 创建索引读取工具
     */
    public static IndexReader buildIndexReader(){
        if (SingletonLucene.getDirectory() == null){
            return null;
        }
        Directory directory = SingletonLucene.getDirectory();
        try{
            // 索引读取工具
            return DirectoryReader.open(directory);
        }catch (Exception e){
            log.warn("[401.build indexReader]: error={}, directory={}", e, directory);
        }
        return null;
    }

    /**
     * 创建索引搜索对象工具
     */
    public static IndexSearcher buildIndexSearcher(IndexReader reader){
        return new IndexSearcher(reader);
    }

    /**
     * 创建写入索引对象
     * @param directory 索引存放目录
     */
    public static IndexWriter buildIndexWriter(Directory directory) {
        IndexWriter indexWriter = null;
        try {
            IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
            // 设置索引打开方式
            config.setOpenMode(OpenMode.CREATE_OR_APPEND);
            // 设置关闭之前先提交
            config.setCommitOnClose(true);
            indexWriter = new IndexWriter(directory, config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexWriter;
    }

    /**
     * 关闭索引文件生成对象
     * @param indexWriter   写入索引对象
     */
    public static void close(IndexWriter indexWriter) {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException e) {
                log.warn("[indexWriter close]: error={}, indexWriter={}", e, indexWriter);
                indexWriter = null;
            }
        }
    }

    /**
     * 关闭索引文件读取对象
     * @param reader    索引读取工具
     */
    public static void close(IndexReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                reader = null;
            }
        }
    }

    /**
     * 高亮标签
     * @param query 查询对象
     * @param fieldName 关键字
     */
    public static Highlighter getHighlighter(Query query, String fieldName) {
        Formatter formatter = new SimpleHTMLFormatter("<span style='color:red'>", "</span>");
        Scorer fragmentScorer = new QueryTermScorer(query, fieldName);
        Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
        highlighter.setTextFragmenter(new SimpleFragmenter(200));
        return highlighter;
    }
}