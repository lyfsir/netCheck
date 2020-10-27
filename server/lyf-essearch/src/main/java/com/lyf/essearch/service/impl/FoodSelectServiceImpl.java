package com.lyf.essearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.lyf.common.model.FoodInfosModel;
import com.lyf.common.model.FoodSpecialModel;
import com.lyf.essearch.config.LyfEsConfig;
import com.lyf.essearch.constant.EsConstant;
import com.lyf.essearch.service.FoodSelectService;
import com.lyf.essearch.vo.SearchParam;
import com.lyf.essearch.vo.SearchResponseVo;
import com.lyf.essearch.vo.SpecialParam;
import com.lyf.essearch.vo.SpecialResponseVo;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.eclipse.jetty.util.StringUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyf
 * @date 2020/7/19-2:18
 */

@Service
public class FoodSelectServiceImpl implements FoodSelectService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    //检索es
    @Override
    public SearchResponseVo search(SearchParam searchParam) {
        //动态构建出查询需要的dsl语句
        SearchResponseVo searchResponseVo = null;
        //1，准备检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        try {
            //2，执行检索请求
            SearchResponse search = restHighLevelClient.search(searchRequest, LyfEsConfig.COMMON_OPTIONS);

            //3，分析响应数据封装成我们需要的格式
            searchResponseVo = buildSearchResponse(search,searchParam);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResponseVo;
    }

    /**
     * 准备检索请求
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();//构建DSL语句的

        /**
         * 查询：过滤
         */
        //1,构建bool - query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //1.1 must- 模糊匹配
        if(!StringUtils.isEmpty(searchParam.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("title",searchParam.getKeyword()));
        }
        if (!StringUtils.isEmpty(searchParam.getUserName())){
            boolQuery.filter(QueryBuilders.termQuery("username", searchParam.getUserName()));
        }
        //1.2 bool- filter --按照分类id
        if(searchParam.getCateId()!=null){

            NestedQueryBuilder nestedQuery = new NestedQueryBuilder(
                    "category", new TermQueryBuilder("category.cId",searchParam.getCateId()), ScoreMode.None);
            boolQuery.filter(nestedQuery);
        }

        //1.2 bool- filter --按照专题id
        if(searchParam.getSid()!=null){
            boolQuery.filter(QueryBuilders.termQuery("sId",searchParam.getSid()));
        }

        //1.2 bool- filter --属性
        if (!StringUtils.isEmpty(searchParam.getAttrs())){

            NestedQueryBuilder nestedQuery = new NestedQueryBuilder(
                    "attr", new TermQueryBuilder("attr.attrName",searchParam.getAttrs()), ScoreMode.None);
            boolQuery.filter(nestedQuery);
        }
        searchSourceBuilder.query(boolQuery);


        /**
         * 排序，分页，高亮
         */
        //2.1 排序(按最新)
        if(!StringUtils.isEmpty(searchParam.getSort())){
            //sort=id/hotScore
            searchSourceBuilder.sort(searchParam.getSort(), SortOrder.DESC);
        }
        //2.2 分页 页码*每页数量得出从第几页开始
        searchSourceBuilder.from((searchParam.getPageNum()-1)*EsConstant.FOODINFO_SIZE);
        searchSourceBuilder.size(EsConstant.FOODINFO_SIZE);

        //2.3 高亮
        if (!StringUtils.isEmpty(searchParam.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title");
            highlightBuilder.preTags("<b style='color:#ff6767'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }

//        String s = searchSourceBuilder.toString();
//        System.out.println("DSL语句"+s);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.FOODINFO_INDEX}, searchSourceBuilder);
        return searchRequest;
    }

    /**
     * 构建结果数据
     * @param search
     * @return
     */
    private SearchResponseVo buildSearchResponse(SearchResponse search,SearchParam searchParam) {
        SearchResponseVo searchResponseVo = new SearchResponseVo();
        SearchHits hits = search.getHits();
        //遍历每一条命中的记录
        List<FoodInfosModel> models = new ArrayList<>();
        if(hits.getHits()!=null&&hits.getHits().length>0){
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                FoodInfosModel infosModel = JSON.parseObject(sourceAsString, FoodInfosModel.class);
                if(!StringUtils.isEmpty(searchParam.getKeyword())){
                    HighlightField title = hit.getHighlightFields().get("title");
                    String string = title.getFragments()[0].string();
                    infosModel.setTitle(string);
                }
                models.add(infosModel);
            }
        }
        searchResponseVo.setFood(models);

        //封装当前页码
        searchResponseVo.setPageNum(searchParam.getPageNum());
        //封装总记录数
        long totla = hits.getTotalHits().value;
        searchResponseVo.setTotal(totla);
        //计算总页码
        int totalPages = (int) totla%EsConstant.FOODINFO_SIZE == 0?(int)totla/EsConstant.FOODINFO_SIZE:((int) totla/EsConstant.FOODINFO_SIZE+1);
        searchResponseVo.setTotalPages(totalPages);
        return searchResponseVo;
    }


}
