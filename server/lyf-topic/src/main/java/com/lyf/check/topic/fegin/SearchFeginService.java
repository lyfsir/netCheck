package com.lyf.check.topic.fegin;

import com.lyf.common.model.TopicInfosModel;
import com.lyf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lyf
 * @date 2020/8/24-5:23
 */
@FeignClient("lyf-essearch")
public interface SearchFeginService {
    @PostMapping("/search/save/topic")
    R topicsave(@RequestBody TopicInfosModel topicInfosModel);

    @DeleteMapping("/search/del/deltopic")
    R deltopic(@RequestBody Integer[] ids);

    @PostMapping("/init/topic")
    R inittopic(@RequestBody List<TopicInfosModel> models);
}
