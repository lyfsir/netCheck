package com.lyf.essearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.lyf.common.model.FoodInfosModel;
import com.lyf.common.model.FoodSpecialModel;
import com.lyf.common.model.TopicInfosModel;
import com.lyf.essearch.config.LyfEsConfig;
import com.lyf.essearch.constant.EsConstant;
import com.lyf.essearch.service.ESInitService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author lyf
 * @date 2020/10/19-17:47
 */
@Slf4j
@Service
public class EsInitServiceImpl implements ESInitService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public int initEs(List<FoodInfosModel> models) throws IOException {
        int size = models.size();
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0 ;i<size;i++) {
            FoodInfosModel infosModel = models.get(i);
            String jsonString = JSON.toJSONString(infosModel);
            bulkRequest.add(new IndexRequest(EsConstant.FOODINFO_INDEX).id(infosModel.getId().toString()).source(jsonString, XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, LyfEsConfig.COMMON_OPTIONS);
        RestStatus status = bulk.status();
        int status1 = status.getStatus();
        if(status1 != 200 && status1 != 201){
            log.error("es批量保存失败");
            return 0;
        }else {
            log.info("es批量保存成功");
            return size;
        }
    }

    @Override
    public int initTopicsEs(List<TopicInfosModel> models) throws IOException {
        int size = models.size();
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0 ;i<size;i++) {
            TopicInfosModel infosModel = models.get(i);
            String jsonString = JSON.toJSONString(infosModel);
            bulkRequest.add(new IndexRequest(EsConstant.TOPICINFO_INDEX).id(infosModel.getId().toString()).source(jsonString, XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, LyfEsConfig.COMMON_OPTIONS);
        RestStatus status = bulk.status();
        int status1 = status.getStatus();
        if(status1 != 200 && status1 != 201){
            log.error("es批量保存失败");
            return 0;
        }else {
            log.info("es批量保存成功");
            return size;
        }
    }

    @Override
    public int initSpecialsEs(List<FoodSpecialModel> models) throws IOException {
        int size = models.size();
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0 ;i<size;i++) {
            FoodSpecialModel infosModel = models.get(i);
            String jsonString = JSON.toJSONString(infosModel);
            bulkRequest.add(new IndexRequest(EsConstant.FOODSPECIAL_INDEX).id(infosModel.getId().toString()).source(jsonString, XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, LyfEsConfig.COMMON_OPTIONS);
        RestStatus status = bulk.status();
        int status1 = status.getStatus();
        if(status1 != 200 && status1 != 201){
            log.error("es批量保存失败");
            return 0;
        }else {
            log.info("es批量保存成功");
            return size;
        }
    }
}
