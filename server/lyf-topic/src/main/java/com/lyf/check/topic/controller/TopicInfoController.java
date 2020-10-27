package com.lyf.check.topic.controller;

import java.util.Arrays;
import java.util.Map;

import com.lyf.check.topic.annotation.Login;
import com.lyf.check.topic.fegin.SearchFeginService;
import com.lyf.check.topic.vo.TopicVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lyf.check.topic.entity.TopicInfoEntity;
import com.lyf.check.topic.service.TopicInfoService;
import com.lyf.common.utils.PageUtils;
import com.lyf.common.utils.R;


/**
 * @author lyf
 * @email 2653155409@qq.com
 * @date 2020-08-08 08:55:30
 */
@RestController
@RequestMapping("topic/topicinfo")
public class TopicInfoController {
    @Autowired
    private TopicInfoService topicInfoService;
    @Autowired
    SearchFeginService searchFeginService;

    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * 列表 (后台管理系统)
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = topicInfoService.queryPage(params);

        return R.ok().put("page", page);
    }

    // toipc列表
    @GetMapping("/topic/list")
    public R tlist(@RequestParam Map<String, Object> params) {
        PageUtils paging = topicInfoService.paging(params);
        return R.ok().put("page", paging);
    }


    // 保存topic信息
    @Login
    @PostMapping("/save/topic")
    public R save(@RequestAttribute("userId") Integer userId, @RequestBody TopicVo topicVo) throws Exception {
        System.out.println(userId);
        topicVo.setUid(Integer.valueOf(userId));
        topicInfoService.savetopic(topicVo);
        // TODO 为空则报401
        return R.ok();
    }


    /**
     * 删除 (后台管理系统)
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids) {
        topicInfoService.removeByIds(Arrays.asList(ids));
        rabbitTemplate.convertAndSend("es-exchange","es-topics-key",ids);
        return R.ok();
    }

    /**
     * 同步topics到es
     */
    @RequestMapping("/tbu/topics")
    public R init() {
        int num = topicInfoService.init();
        return R.ok().put("num",num);
    }

}
