package com.lyf.check.food.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyf.check.food.entity.FoodSpecialInfomationEntity;
import com.lyf.check.food.fegin.SearchFeignService;
import com.lyf.check.food.service.FoodSpecialInfomationService;
import com.lyf.check.food.vo.FoodSpecialVo;
import com.lyf.check.food.vo.GetSpecialNamesVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.lyf.check.food.entity.FoodSpecialEntity;
import com.lyf.check.food.service.FoodSpecialService;
import com.lyf.common.utils.PageUtils;
import com.lyf.common.utils.R;

/**
 * @author lyf
 * @email 2653155409@qq.com
 * @date 2020-08-05 18:03:22
 */
@RestController
@RequestMapping("food/foodspecial")
public class FoodSpecialController {
    @Autowired
    private FoodSpecialService foodSpecialService;

    @Autowired
    private SearchFeignService searchFeignService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    FoodSpecialInfomationService foodSpecialInfomationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R listto(@RequestParam Map<String, Object> params) {
        PageUtils page = foodSpecialService.queryPage(params);
        return R.ok().put("page", page);
    }

    /**
     * 获取使用专题名字信息
     */
    @GetMapping("/info/name")
    public R doGet() {
        List<GetSpecialNamesVo> names = foodSpecialService.queryName();
        return R.ok().put("data", names);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id) {
        FoodSpecialVo vo = foodSpecialService.getInfomatinoById(id);

        return R.ok().put("foodSpecial", vo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody FoodSpecialVo vo) {
        foodSpecialService.add(vo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody FoodSpecialVo vo) {
        foodSpecialService.updateBySid(vo);
        return R.ok();
    }

    /**
     * 删除
     */
    @Transactional
    @RequestMapping("/delete")
    public R deletesp(@RequestBody Integer[] ids) {
        foodSpecialService.removeByIds(Arrays.asList(ids));
        foodSpecialInfomationService.getBaseMapper().delete(new QueryWrapper<FoodSpecialInfomationEntity>().in("s_id",Arrays.asList(ids)));
        rabbitTemplate.convertAndSend("es-exchange","es-specials-key",ids);
        return R.ok();
    }


    /**
     * 同步special信息到specials索引中
     *
     * @return
     */
    @RequestMapping("/tbu/special")
    public R tbu() {
        int init = foodSpecialService.init();
        return R.ok().put("num", init);
    }

}
