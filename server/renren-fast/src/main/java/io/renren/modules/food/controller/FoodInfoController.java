package io.renren.modules.food.controller;

import io.renren.common.utils.R;
import io.renren.fegin.FoodSortFegin;
import io.renren.modules.food.vo.UpdatefoodVo;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lyf
 * @date 2020/8/4-18:39
 */
@RestController
@RequestMapping("/food/info")
public class FoodInfoController {
    @Autowired
    FoodSortFegin foodSortFegin;
    @GetMapping("doget/info/{foodId}")
    public R doget(@PathVariable Integer foodId){
        R r = foodSortFegin.selects(foodId);
        return R.ok().put("data",r.get("data"));
    }

    @PostMapping("save/food")
    public R save(@RequestBody UpdatefoodVo updatefoodVo){

        System.out.println("----testf---"+updatefoodVo.toString());
        foodSortFegin.save(updatefoodVo);
        return R.ok();
    }

    @DeleteMapping("del/food")
    public R del(@RequestBody Integer[] ids){
        foodSortFegin.deletes(ids);
        return R.ok();
    }

    @DeleteMapping("delete/food")
    public R delete(@RequestBody Integer[] ids){
        foodSortFegin.del(ids);
        return R.ok();
    }

    @PostMapping("tongbu/foods")
    public R tb() {
        R tbu = foodSortFegin.tbu();
        int num =(Integer) tbu.get("num");
        return R.ok().put("num",num);
    }
}
