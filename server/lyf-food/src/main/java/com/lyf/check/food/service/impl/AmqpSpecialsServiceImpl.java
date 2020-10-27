package com.lyf.check.food.service.impl;

import com.lyf.check.food.fegin.SearchFeignService;
import com.lyf.check.food.service.AmqpSpecialsService;
import com.lyf.check.food.vo.FoodSpecialVo;
import com.lyf.common.model.FoodSpecialModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lyf
 * @date 2020/10/21-23:46
 */
@Slf4j
@RabbitListener(queues = {"es-specials-queue"})
@Service
public class AmqpSpecialsServiceImpl implements AmqpSpecialsService {

    @Autowired
    SearchFeignService searchFeignService;

    /**
     * 接收FoodSpecialVo消息进行es添加和修改操作(如果本次id的es添加失败（发送异常），应该把此队列的此条消息不要删除)
     * 属于一个类型的索引放在一个队列中，保证队列的顺序执行
     * @param vo
     */
    @Override
    @RabbitHandler
    public void specialEsQueue(FoodSpecialVo vo) {
        // 将数据发送给es进行保存，远程调用lyf--search服务
        FoodSpecialModel foodSpecialModel = new FoodSpecialModel();
        foodSpecialModel.setId((long)vo.getId());
        foodSpecialModel.setName(vo.getName());
        foodSpecialModel.setImgurl(vo.getImgurl());
        foodSpecialModel.setCreateTime(vo.getCreateTime());
        searchFeignService.foodSave(foodSpecialModel);
//        if ((int) r1.get("code") != 0) {
//            // ES失败，抛出异常
//            throw new MyEsException();
//        }
    }

    /**
     * 接收ids信息，给指定的specials索引删除信息
     * @param ids
     */
    @Override
    @RabbitHandler
    public void delspecialEsQueue(Integer[] ids) {
        searchFeignService.delspecial(ids);
    }

}
