package com.lyf.check.food.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lyf.check.food.vo.FoodInfomationVo;
import com.lyf.check.food.vo.GetFoodInfomationVo;
import com.lyf.check.food.vo.UpdatefoodVo;
import com.lyf.common.model.FoodInfosModel;
import com.lyf.common.utils.PageUtils;
import com.lyf.check.food.entity.FoodInfoEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 
 *
 * @author lyf
 * @email 2653155409@qq.com
 * @date 2020-08-03 23:50:50
 */
public interface FoodInfoService extends IService<FoodInfoEntity> {


    PageUtils queryPage(Map<String, Object> params, Integer num);

    GetFoodInfomationVo selectInfomation(Integer foodId) throws ExecutionException, InterruptedException;

    void saveall(UpdatefoodVo updatefoodVo);

    void savealltoaut(FoodInfomationVo infomationVo);


    int init();

    PageUtils queryInfo(Map<String, Object> params);
}

