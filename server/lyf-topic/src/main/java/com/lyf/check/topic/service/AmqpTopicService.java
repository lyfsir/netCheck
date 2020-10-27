package com.lyf.check.topic.service;

import com.lyf.check.topic.vo.TopicVo;

/**
 * @author lyf
 * @date 2020/10/22-0:01
 */
public interface AmqpTopicService {
    void topicsEsQueue(TopicVo topicVo);
    void delTopicsEsQueue(Integer[] ids);
}
