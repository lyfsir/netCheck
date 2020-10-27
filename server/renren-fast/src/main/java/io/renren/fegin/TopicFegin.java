package io.renren.fegin;

import io.renren.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author lyf
 * @date 2020/8/8-9:23
 */
@FeignClient("lyf-topic")
public interface TopicFegin {
    /**
     * 信息
     */
    /**
     * 列表
     */
    @RequestMapping("topic/topicinfo/list")
    R list(@RequestParam Map<String, Object> params);

    /**
     * 删除
     */
    @RequestMapping("topic/topicinfo/delete")
    R delete(@RequestBody Integer[] ids);

    /**
     * 同步topics到es
     */
    @RequestMapping("topic/topicinfo/tbu/topics")
    R init();
}
