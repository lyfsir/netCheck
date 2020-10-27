package com.lyf.essearch.controller;

import com.lyf.common.codeMesg.MyCodemsg;
import com.lyf.common.model.FoodInfosModel;
import com.lyf.common.model.FoodSpecialModel;
import com.lyf.common.model.TopicInfosModel;
import com.lyf.common.utils.R;
import com.lyf.essearch.service.ESInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author lyf
 * @date 2020/10/19-18:04
 */
@Slf4j
@RestController
public class ESInitController {
    @Autowired
    ESInitService esInitService;

    @PostMapping("/init")
    public R init(@RequestBody List<FoodInfosModel> models) {
        int b = 0;
        try {
            b = esInitService.initEs(models);
        } catch (IOException e) {
            //出现这种错误表示es客户端连接不上
            log.error("es批量保存失败",e);
            return R.error(MyCodemsg.ES_EXCEPTION.getCode(), MyCodemsg.ES_EXCEPTION.getMsg());
        }
        if(b!=0){
            return R.ok().put("num",b);
        }else {
            return R.error(MyCodemsg.ES_EXCEPTION.getCode(), MyCodemsg.ES_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/init/topic")
    public R inittopic(@RequestBody List<TopicInfosModel> models) {
        int b = 0;
        try {
            b = esInitService.initTopicsEs(models);
        } catch (IOException e) {
            //出现这种错误表示es客户端连接不上
            log.error("es批量保存失败",e);
            return R.error(MyCodemsg.ES_EXCEPTION.getCode(), MyCodemsg.ES_EXCEPTION.getMsg());
        }
        if(b!=0){
            return R.ok().put("num",b);
        }else {
            return R.error(MyCodemsg.ES_EXCEPTION.getCode(), MyCodemsg.ES_EXCEPTION.getMsg());
        }
    }

    @PostMapping("/init/specials")
    public R initspecials(@RequestBody List<FoodSpecialModel> models) {
        int b = 0;
        try {
            b = esInitService.initSpecialsEs(models);
        } catch (IOException e) {
            //出现这种错误表示es客户端连接不上
            log.error("es批量保存失败",e);
            return R.error(MyCodemsg.ES_EXCEPTION.getCode(), MyCodemsg.ES_EXCEPTION.getMsg());
        }
        if(b!=0){
            return R.ok().put("num",b);
        }else {
            return R.error(MyCodemsg.ES_EXCEPTION.getCode(), MyCodemsg.ES_EXCEPTION.getMsg());
        }
    }
}
