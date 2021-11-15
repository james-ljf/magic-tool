package com.magictool.web.util.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.wltea.analyzer.lucene.IKAnalyzer;


/**
 * Lucene索引查询工具类
 * @Author ljf
 * @Date 2021/11/11 10:50
 */
@Slf4j
public class QuerySearchUtils {

    /**
     *  创建查询对象（单字段）
     * @param field   查询的字段名称
     * @param keyword 查询的关键字
     */
    public static Query buildSingleQuery(String field, String keyword){
        Query query = null;
        try{
            // 创建查询解析器，默认适用IK分词器
            QueryParser parser = new QueryParser(field, new IKAnalyzer());
            // 创建查询对象
            query = parser.parse(keyword);
            return query;
        }catch (ParseException e){
            log.warn("[402.build query]: error={}, field={}, value={}", e, field, keyword);
        }
        return null;
    }

    /**
     * 创建查询对象（多字段）
     * @param fields  要查询的字段数组
     * @param keyword 查询的关键字
     */
    public static Query buildMultiFieldQuery(String[] fields, String keyword){
        Query query = null;
        try{
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new IKAnalyzer());
            query = parser.parse(keyword);
            return query;
        }catch (Exception e){
            log.warn("[403.build query]: error={}, fields={}, value={}", e, fields, keyword);
        }
        return null;
    }

    /**
     * 创建词条查询对象
     * 注意：Term(词条)是搜索的最小单位，不可再分词。值必须是字符串
     * 场景：一个字段不需要分词时，使用词条查询
     * @param field   查询的字段
     * @param keyword   查询的关键字
     */
    public static Query buildTermQuery(String field, String keyword){
        return new TermQuery(new Term(field, keyword));
    }

    /**
     * 创建通配符查询对象
     * ?代表一个任意字符
     * *代表一个或多个任意字符
     * @param field   查询的字段
     * @param keyword   查询的关键字
     */
    public static Query buildWildcardQuery(String field, String keyword){
        return new WildcardQuery(new Term(field, keyword));
    }

//    /**
//     * 创建范围查询对象
//     * @param field 查询的字段
//     * @param lowerTerm 最小值
//     * @param upperTerm 最大值
//     */
   /* public static <T> Query buildRangeQuery(String field, T lowerTerm, T upperTerm){
        Query query = null;
        if (lowerTerm instanceof Integer && upperTerm instanceof Integer) {
            Integer lower = (Integer) lowerTerm;
            Integer upper = (Integer) upperTerm;
            query = IntPoint.newRangeQuery(field, lower, upper);
        }
        if (lowerTerm instanceof Long && upperTerm instanceof Long){
            long lower = (long) lowerTerm;
            long upper = (long) upperTerm;
            query = LongPoint.newRangeQuery(field, lower, upper);
        }
        if (lowerTerm instanceof Float && upperTerm instanceof Float){
            float lower = (float) lowerTerm;
            float upper = (float) upperTerm;
            query = FloatPoint.newRangeQuery(field, lower, upper);
        }
        if (lowerTerm instanceof Double && upperTerm instanceof Double){
            double lower = (double) lowerTerm;
            double upper = (double) upperTerm;
            query = DoublePoint.newRangeQuery(field, lower, upper);
        }
        return query;
    }*/

    /**
     * 创建模糊查询对象
     * @param field   查询的字段
     * @param keyword   查询的关键字
     * @param distance  允许用户输错的最大编辑距离(必须在0~2之间)
     */
    public static Query buildFuzzyQuery(String field, String keyword, int distance){
        return new FuzzyQuery(new Term(field, keyword), distance);
    }

    /**
     * 查询内存索引获取结果
     * @param query 查询对象
     * @param n   要查询的最大结果条数
     * @return  按照匹配度排名得分前n名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
     */
    public static TopDocs searchQuery(Query query, int n){
        IndexReader reader = LuceneUtil.buildIndexReader();
        IndexSearcher searcher = LuceneUtil.buildIndexSearcher(reader);
        try{
            return searcher.search(query, n);
        }catch (Exception e){
            log.warn("[410.search query]: error={}, query={}, num={}", e, query, n);
        }
        return null;
    }

    /**
     * 查询内存索引获取经过排序的结果
     * @param query 查询对象
     * @param n   要查询的最大结果条数
     * @param sort  排序策略
     * @return  按照匹配度排名得分前n名的文档信息（包含查询到的总条数信息、所有符合条件的文档的编号信息）。
     */
    public static TopDocs searchQuery(Query query, int n, Sort sort){
        IndexReader reader = LuceneUtil.buildIndexReader();
        IndexSearcher searcher = LuceneUtil.buildIndexSearcher(reader);
        try{
            return searcher.search(query, n, sort);
        }catch (Exception e){
            log.warn("[410.search query]: error={}, query={}, num={}, sort={}", e, query, n, sort);
        }
        return null;
    }
}
