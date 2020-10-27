package com.lyf.check.food.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyf.check.food.entity.FoodInfoEntity;
import com.lyf.check.food.entity.FoodSpecialInfomationEntity;
import com.lyf.check.food.fegin.SearchFeignService;
import com.lyf.check.food.service.FoodSpecialInfomationService;
import com.lyf.check.food.vo.FoodInfosVo;
import com.lyf.check.food.vo.FoodSpecialVo;
import com.lyf.check.food.vo.GetSpecialNamesVo;
import com.lyf.common.exception.MyEsException;
import com.lyf.common.model.FoodInfosModel;
import com.lyf.common.model.FoodSpecialModel;
import com.lyf.common.utils.R;
import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyf.common.utils.PageUtils;
import com.lyf.common.utils.Query;

import com.lyf.check.food.dao.FoodSpecialDao;
import com.lyf.check.food.entity.FoodSpecialEntity;
import com.lyf.check.food.service.FoodSpecialService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("foodSpecialService")
public class FoodSpecialServiceImpl extends ServiceImpl<FoodSpecialDao, FoodSpecialEntity> implements FoodSpecialService {

    @Autowired
    FoodSpecialInfomationService foodSpecialInfomationService;

    @Autowired
    SearchFeignService searchFeignService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String specialName = (String)params.get("specialName");
        IPage<FoodSpecialEntity> page = this.page(
                new Query<FoodSpecialEntity>().getPage(params),
                new QueryWrapper<FoodSpecialEntity>().like(StringUtils.isNotBlank(specialName),"name",specialName)
        );

        return new PageUtils(page);
    }

    @Override
    public List<GetSpecialNamesVo> queryName() {
        List<FoodSpecialEntity> entities = this.getBaseMapper().selectList(null);
        List<GetSpecialNamesVo> collect = entities.stream().map(value -> {
            GetSpecialNamesVo vo = new GetSpecialNamesVo();
            vo.setId(value.getId());
            vo.setName(value.getName());
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    @Transactional
    public void add(FoodSpecialVo vo) {
        FoodSpecialEntity foodSpecialEntity = new FoodSpecialEntity();
        BeanUtils.copyProperties(vo,foodSpecialEntity);
        LocalDate now = LocalDate.now();
        vo.setCreateTime(String.valueOf(now));
        foodSpecialEntity.setCreateTime(String.valueOf(now));
        this.save(foodSpecialEntity);
        vo.setId(foodSpecialEntity.getId());
        FoodSpecialInfomationEntity foodSpecialInfomationEntity = new FoodSpecialInfomationEntity();
        BeanUtils.copyProperties(vo,foodSpecialInfomationEntity);
        foodSpecialInfomationEntity.setSId(foodSpecialEntity.getId());
        foodSpecialInfomationEntity.setImgurl(vo.getImgurlBig());
        foodSpecialInfomationService.save(foodSpecialInfomationEntity);

        // 把FoodSpecialVo
        rabbitTemplate.convertAndSend("es-exchange","es-specials-key",vo);
    }

    @Transactional
    @Override
    public void updateBySid(FoodSpecialVo vo) {
        FoodSpecialEntity foodSpecialEntity = new FoodSpecialEntity();
        BeanUtils.copyProperties(vo,foodSpecialEntity);
        LocalDate now = LocalDate.now();
        vo.setCreateTime(String.valueOf(now));
        foodSpecialEntity.setCreateTime(String.valueOf(now));
        this.updateById(foodSpecialEntity);
        vo.setId(foodSpecialEntity.getId());
        FoodSpecialInfomationEntity foodSpecialInfomationEntity = new FoodSpecialInfomationEntity();
        BeanUtils.copyProperties(vo,foodSpecialInfomationEntity);
        foodSpecialInfomationEntity.setImgurl(vo.getImgurlBig());
        foodSpecialInfomationEntity.setSId(foodSpecialEntity.getId());
        foodSpecialInfomationService.getBaseMapper().update(foodSpecialInfomationEntity,
                new QueryWrapper<FoodSpecialInfomationEntity>().eq("s_id",foodSpecialEntity.getId()));

        rabbitTemplate.convertAndSend("es-exchange","es-specials-key",vo);
    }

    @Override
    public FoodSpecialVo getInfomatinoById(Integer id) {
        FoodSpecialEntity byId = this.getById(id);
        FoodSpecialVo vo = new FoodSpecialVo();
        BeanUtils.copyProperties(byId,vo);
        FoodSpecialInfomationEntity foodSpecialInfomationEntity = foodSpecialInfomationService.getBaseMapper().selectOne(
                new QueryWrapper<FoodSpecialInfomationEntity>().eq("s_id", byId.getId()));
        vo.setImgurlBig(foodSpecialInfomationEntity.getImgurl());
        vo.setContent(foodSpecialInfomationEntity.getContent());
        vo.setSId(foodSpecialInfomationEntity.getSId());
        return vo;
    }

    // 同步special信息到specials索引中
    @Override
    public int init() {
//        long startTime = System.currentTimeMillis();    //获取开始时间
        boolean flag = true;
        int size = 1000;
        Page page = new Page();
        page.setSize(size);
        int num = 0;
        int i = 1;
        while (true) {
            page.setCurrent(i);
            IPage<FoodSpecialEntity> models = this.page(page);
            if (models.getRecords().isEmpty() || models.getRecords() == null) {
                break;
            }
            List<FoodSpecialModel> collect = models.getRecords().stream().map(value -> {
                FoodSpecialModel specialModel = new FoodSpecialModel();
                BeanUtils.copyProperties(value, specialModel);
                specialModel.setId((long) value.getId());
                return specialModel;
            }).collect(Collectors.toList());
            int esnum = this.esinit(collect);
            num += esnum;
            if (models.getRecords().size() < size) {
                break;
            }
            i++;
        }
        return num;
    }


    private int esinit(List<FoodSpecialModel> update) {
        if (update == null || update.isEmpty()) {
            return 0;
        }
        R init = searchFeignService.initspecials(update);
        if ((int) init.get("code") == 0) {
            int num = (Integer) init.get("num");
            log.info("一共同步了specials[{}]条",String.valueOf(num));
            return num;
        } else {
            return 0;
        }
    }


}