package com.lyf.check.food.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyf.check.food.entity.FoodInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyf.check.food.vo.FoodInfosVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author lyf
 * @email 2653155409@qq.com
 * @date 2020-08-03 23:50:50
 */
@Mapper
public interface FoodInfoDao extends BaseMapper<FoodInfoEntity> {
    IPage<FoodInfosVo> findFoood(IPage<FoodInfoEntity> page);

}
