package com.lyf.check.topic.service.impl;

import com.lyf.check.topic.fegin.SearchFeginService;
import com.lyf.check.topic.fegin.UmsMemberFegin;
import com.lyf.check.topic.service.AmqpTopicService;
import com.lyf.check.topic.vo.ImagesVo;
import com.lyf.check.topic.vo.TopicVo;
import com.lyf.common.exception.MyEsException;
import com.lyf.common.model.TopicInfosModel;
import com.lyf.common.utils.R;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lyf
 * @date 2020/10/22-0:02
 */
@Service
@RabbitListener(queues = {"es-topics-queue"})
public class AmqpTopicServiceImpl implements AmqpTopicService {

    @Autowired
    UmsMemberFegin umsMemberFegin;

    @Autowired
    SearchFeginService searchFeginService;

    @Override
    @RabbitHandler
    public void topicsEsQueue(TopicVo topicVo) {
        List<ImagesVo> images = topicVo.getImages();
        TopicInfosModel infosModel = new TopicInfosModel();
        infosModel.setId((long)topicVo.getId());
        infosModel.setContent(topicVo.getContent());
        // 获取头像信息
        // return R.ok().put("username", user[0]).put("logo",user[1]);
        R r = umsMemberFegin.username(topicVo.getUid());
        String logo = (String) r.get("logo");
        if (StringUtils.isEmpty(logo)){
            infosModel.setLogo("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");
        }else {
            infosModel.setLogo(logo);
        }
        infosModel.setCreateTime(topicVo.getCreateTime());
        infosModel.setUsername(topicVo.getUserName());
        infosModel.setUId((long)topicVo.getUid());
        List<TopicInfosModel.Image> collect = images.stream().map(value -> {
            TopicInfosModel.Image image = new TopicInfosModel.Image();
            String image1 = value.getImage();
            image.setImageurl(image1);
            return image;
        }).collect(Collectors.toList());
        infosModel.setImage(collect);

        searchFeginService.topicsave(infosModel);
    }

    @Override
    @RabbitHandler
    public void delTopicsEsQueue(Integer[] ids) {
        searchFeginService.deltopic(ids);
    }
}
