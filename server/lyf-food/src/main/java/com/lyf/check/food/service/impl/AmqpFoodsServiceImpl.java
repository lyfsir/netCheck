package com.lyf.check.food.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyf.check.food.entity.FoodAttrEntity;
import com.lyf.check.food.entity.FoodAttrGroupEntity;
import com.lyf.check.food.entity.FoodImagesEntity;
import com.lyf.check.food.entity.FoodInfoEntity;
import com.lyf.check.food.fegin.SearchFeignService;
import com.lyf.check.food.fegin.UmsMemberService;
import com.lyf.check.food.service.*;
import com.lyf.check.food.vo.FoodCateVo;
import com.lyf.check.food.vo.UpdatefoodVo;
import com.lyf.common.model.FoodInfosModel;
import com.lyf.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyf
 * @date 2020/10/21-21:44
 */
@Slf4j
@Service
@RabbitListener(queues = {"es-foods-queue"})
public class AmqpFoodsServiceImpl implements AmqpFoodsService {

    @Autowired
    FoodInfoService foodInfoService;

    @Autowired
    FoodAttrService foodAttrService;

    @Autowired
    FoodAttrGroupService foodAttrGroupService;

    @Autowired
    FoodImagesService foodImagesService;

    @Autowired
    UmsMemberService umsMemberService;

    @Autowired
    SearchFeignService searchFeignService;

    /**
     * 接收UpdatefoodVo消息进行es添加操作
     * @param vo
     */
    @Override
    @RabbitHandler
    public void foodEsQueue(UpdatefoodVo vo) {
        log.info(vo.toString());
        //得到分类名list
        List<FoodCateVo> category1 = vo.getCategory();
        //修改数据库成功得到完整数据再进行es的保存工作
        //组装需要的es数据
        FoodInfosModel infosModel = new FoodInfosModel();
        //根据vo的id查询数据库的foodinfo表信息，通过id查到分类信息、可以被用来检索的属性信息、默认图片信息以及用户名信息，按热度来排序，因为还没弄，默认0
        FoodInfoEntity entity2 = foodInfoService.getBaseMapper().selectById(vo.getId());
        infosModel.setUserId((long) entity2.getUserId());
        infosModel.setId((long) entity2.getId());
        infosModel.setTitle(entity2.getTitle());
        List<FoodInfosModel.Category> collect = category1.stream().map(value -> {
            FoodInfosModel.Category category = new FoodInfosModel.Category();
            category.setCId((long) value.getCategoryId());
            category.setCName(value.getCateName());
            return category;
        }).collect(Collectors.toList());
        infosModel.setCategory(collect);
        //可以被用来检索的属性信息
        FoodAttrGroupEntity search = foodAttrGroupService.getBaseMapper().selectOne(new QueryWrapper<FoodAttrGroupEntity>().eq("search", "1"));
        if (search != null) {
            List<FoodAttrEntity> attrEntities = foodAttrService.selectAttrByFId(entity2.getId(), search.getId());
            List<FoodInfosModel.Attr> collect1 = attrEntities.stream().map(value -> {
                FoodInfosModel.Attr attrs = new FoodInfosModel.Attr();
                attrs.setAttrIid((long) value.getId());
                attrs.setAttrName(value.getAttrName());
                attrs.setAttrValue(value.getAttrValue());
                return attrs;
            }).collect(Collectors.toList());
            infosModel.setAttr(collect1);
        }

        //默认图片信息
        FoodImagesEntity imagesEntity = foodImagesService.getBaseMapper().selectOne(new QueryWrapper<FoodImagesEntity>().eq("food_id", entity2.getId())
                .eq("type", 0));
        String url = imagesEntity.getImagesUrl();
        infosModel.setImgurl(url);

        //用户名信息
        R r = umsMemberService.username(entity2.getUserId());
        String username = (String) r.get("username");
        infosModel.setUsername(username);

        //所属专题
        if (vo.getSid() != null) {
            infosModel.setSId((long) vo.getSid());
        }
        infosModel.setCreateTime(vo.getCreateTime());
        log.info("model-----" + infosModel.toString());

        // 将数据发送给es进行保存，远程调用lyf--search服务
        searchFeignService.foodSave(infosModel);
    }

    @Override
    @RabbitHandler
    public void delFoodsEsQueue(Integer[] ids) {
        searchFeignService.delfoods(ids);
    }


}
