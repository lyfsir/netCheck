package com.lyf.check.topic.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyf.check.topic.dao.TopicImagesDao;
import com.lyf.check.topic.fegin.SearchFeginService;
import com.lyf.check.topic.fegin.UmsMemberFegin;
import com.lyf.check.topic.service.TopicImagesService;
import com.lyf.check.topic.vo.ImagesVo;
import com.lyf.check.topic.vo.TopicEsVo;
import com.lyf.check.topic.vo.TopicVo;
import com.lyf.common.model.TopicInfosModel;
import com.lyf.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyf.common.utils.PageUtils;
import com.lyf.common.utils.Query;

import com.lyf.check.topic.dao.TopicInfoDao;
import com.lyf.check.topic.entity.TopicInfoEntity;
import com.lyf.check.topic.service.TopicInfoService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("topicInfoService")
public class TopicInfoServiceImpl extends ServiceImpl<TopicInfoDao, TopicInfoEntity> implements TopicInfoService {
    @Autowired
    UmsMemberFegin umsMemberFegin;
    @Autowired
    TopicImagesService topicImagesService;
    @Autowired
    SearchFeginService searchFeginService;

    @Autowired
    TopicImagesDao topicImagesDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TopicInfoEntity> page = this.page(
                new Query<TopicInfoEntity>().getPage(params),
                new QueryWrapper<TopicInfoEntity>()
        );
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void savetopic(TopicVo topicVo) {
        TopicInfoEntity infoEntity = new TopicInfoEntity();
        infoEntity.setContent(topicVo.getContent());
        LocalDate now = LocalDate.now();
        topicVo.setCreateTime(String.valueOf(now));
        infoEntity.setCreateTime(String.valueOf(now));
        infoEntity.setUId(topicVo.getUid());
        infoEntity.setUserName(topicVo.getUserName());
        this.save(infoEntity);
        topicVo.setId(infoEntity.getId());
        List<ImagesVo> images = topicVo.getImages();
        topicImagesService.saveImages(infoEntity.getId(), images);

        rabbitTemplate.convertAndSend("es-exchange", "es-topics-key", topicVo);
    }

    @Override
    public PageUtils paging(Map<String, Object> params) {
        String uId = (String) params.get("uId");
        IPage<TopicInfoEntity> ipage = this.getBaseMapper().selectTopic(
                new Query<TopicInfoEntity>().getPage(params),
                new QueryWrapper<TopicInfoEntity>().orderByDesc("topic.id")
                        .eq(StringUtils.isNotBlank(uId), "u_id", uId));
        List<TopicInfoEntity> collect = ipage.getRecords().stream().map(value -> {
            List<String> list = topicImagesDao.selectImages((long) value.getId());
            value.setImages(list);
            return value;
        }).collect(Collectors.toList());
        ipage.setRecords(collect);
        return new PageUtils(ipage);
    }

    @Override
    public int init() {
        boolean flag = true;
        int size = 1000;
        Page page = new Page();
        page.setSize(size);
        int num = 0;
        int i = 1;
        while (true) {
            page.setCurrent(i);
            IPage<TopicEsVo> models = this.getBaseMapper().selectTopicToEs(page);
            if (models.getRecords().isEmpty() || models.getRecords() == null) {
                break;
            }
            List<TopicInfosModel> collect = models.getRecords().stream().map(value -> {
                List<String> list = topicImagesDao.selectImages((long) value.getId());
                List<TopicInfosModel.Image> images = list.stream().map(img -> {
                    TopicInfosModel.Image image = new TopicInfosModel.Image();
                    image.setImageurl(img);
                    return image;
                }).collect(Collectors.toList());
                TopicInfosModel infosModel = new TopicInfosModel();
                BeanUtils.copyProperties(value, infosModel);
                infosModel.setLogo(value.getHeader());
                infosModel.setUsername(value.getUserName());
                infosModel.setImage(images);
                return infosModel;
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


    private int esinit(List<TopicInfosModel> update) {
        if (update == null || update.isEmpty()) {
            return 0;
        }
        R init = searchFeginService.inittopic(update);
        if ((int) init.get("code") == 0) {
            int num = (Integer) init.get("num");
            log.info("一共同步了topics[{}]条",String.valueOf(num));
            return num;
        } else {
            return 0;
        }
    }

}