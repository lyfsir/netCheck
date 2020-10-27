package com.lyf.check.topic.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.lyf.check.topic.entity.TopicInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyf.check.topic.vo.TopicEsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author lyf
 * @email 2653155409@qq.com
 * @date 2020-08-08 08:55:30
 */
@Mapper
public interface TopicInfoDao extends BaseMapper<TopicInfoEntity> {
    IPage<TopicInfoEntity> selectTopic(IPage<TopicInfoEntity> page, @Param(Constants.WRAPPER) QueryWrapper<TopicInfoEntity> wrapper);
    IPage<TopicEsVo> selectTopicToEs(IPage<TopicInfoEntity> page);


}
