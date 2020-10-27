package com.lyf.check.food.service;

import com.lyf.check.food.vo.FoodSpecialVo;
import com.lyf.check.food.vo.UpdatefoodVo;

/**
 * @author lyf
 * @date 2020/10/21-21:44
 */
public interface AmqpFoodsService {
    void foodEsQueue(UpdatefoodVo vo);
    void delFoodsEsQueue(Integer[] ids);
}
