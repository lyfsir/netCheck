package com.lyf.check.food.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.lyf.check.food.annotation.Login;
import com.lyf.check.food.entity.*;
import com.lyf.check.food.fegin.SearchFeignService;
import com.lyf.check.food.service.*;
import com.lyf.check.food.vo.*;
import com.lyf.common.codeMesg.MyCodemsg;
import com.lyf.common.utils.PageUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.lyf.common.utils.R;

/**
 * @author lyf
 * @email 2653155409@qq.com
 * @date 2020-08-03 23:50:50
 */
@RestController
@RequestMapping("food/foodinfo")
public class FoodInfoController {
    @Autowired
    private FoodInfoService foodInfoService;

    @Autowired
    SearchFeignService searchFeignService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    //查询食物的详情信息
    @RequestMapping("/select/infomation")
    public R selects(@RequestParam Integer foodId) throws ExecutionException, InterruptedException {
        GetFoodInfomationVo infomationVo = foodInfoService.selectInfomation(foodId);
        if (infomationVo != null) {
            return R.ok().put("data", infomationVo);
        } else {
            return R.error(MyCodemsg.FOODNULL_EXCEPTION.getCode(), MyCodemsg.FOODNULL_EXCEPTION.getMsg());
        }
    }

    /**
     * 保存审核通过的信息
     */
    @RequestMapping("/save")
    public R save(@Validated @RequestBody UpdatefoodVo updatefoodVo) {
        foodInfoService.saveall(updatefoodVo);
        return R.ok();
    }

    /**
     * 保存待审核通过的信息
     */
    @Login
    @PostMapping("/save/toaud")
    public R savetoaut(@RequestAttribute("userId") Integer userId,
                       @Validated @RequestBody FoodInfomationVo infomationVo) throws Exception {
        infomationVo.setUserId(Integer.valueOf(userId));
        foodInfoService.savealltoaut(infomationVo);
        return R.ok();
    }

    /**
     * 获取auditing为1的分页列表(待审核) -> 后台管理系统
     * 获取auditing为0的分页列表(审核通过) -> 后台管理系统
     */
    @RequestMapping("/list/{num}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable Integer num) {

        PageUtils page = foodInfoService.queryPage(params, num);

        return R.ok().put("page", page);
    }

    /**
     * foodinfo信息
     */
    @GetMapping("/get/foodsInfo")
    public R getinfo(@RequestParam Map<String, Object> params) {
        PageUtils page = foodInfoService.queryInfo(params);
        return R.ok().put("page", page);
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R deletes(@RequestBody Integer[] ids) {
        foodInfoService.removeByIds(Arrays.asList(ids));
        rabbitTemplate.convertAndSend("es-exchange","es-foods-key",ids);
        return R.ok();
    }

    /**
     * 审核不通过 -》 后台管理系统
     */
    @RequestMapping("/del")
    public R delete(@RequestBody Integer[] ids) {

        foodInfoService.removeByIds(Arrays.asList(ids));
        return R.ok();

    }

    /**
     * 同步food信息到foods索引中
     *
     * @return
     */
    @RequestMapping("/tbu/foods")
    public R tbu() {
        int init = foodInfoService.init();
        return R.ok().put("num", init);
    }

}
