package com.lyf.check.food.service;

import com.lyf.check.food.vo.FoodSpecialVo;

/**
 * @author lyf
 * @date 2020/10/21-23:46
 */
public interface AmqpSpecialsService {
    void specialEsQueue(FoodSpecialVo vo);
    void delspecialEsQueue(Integer[] ids);
}
