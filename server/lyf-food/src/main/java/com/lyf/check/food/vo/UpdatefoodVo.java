package com.lyf.check.food.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author lyf
 * @date 2020/7/15-18:01
 */
@Data
public class UpdatefoodVo implements Serializable {
    /**
     * food_id
     */
    private Integer id;

    /**
     * food_name
     */
    private String title;

    /**
     * 分类信息
     */
    @NotNull
    private List<FoodCateVo> category;

    /**
     * 专题信息
     */
    private Integer sid;

    private String createTime;

    public UpdatefoodVo(){

    }


}
