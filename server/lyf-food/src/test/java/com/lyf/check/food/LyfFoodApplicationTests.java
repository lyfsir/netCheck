package com.lyf.check.food;

import com.lyf.check.food.entity.FoodAttrEntity;
import com.lyf.check.food.entity.FoodAttrGroupEntity;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LyfFoodApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void sendMessageMq() {
        FoodAttrEntity attrEntity = new FoodAttrEntity();
        attrEntity.setId(1);
        attrEntity.setAttrValue("公共");
        attrEntity.setAttrName("dsd");
        attrEntity.setAttgroupId(4);
        attrEntity.setFoodId(2);
        FoodAttrGroupEntity foodAttrGroupEntity = new FoodAttrGroupEntity();
        foodAttrGroupEntity.setGroupName("dsd");
        foodAttrGroupEntity.setId(1);
        foodAttrGroupEntity.setSearch(1);
        rabbitTemplate.convertAndSend("es-exchange","es-key",attrEntity);
        rabbitTemplate.convertAndSend("es-exchange","es-key",foodAttrGroupEntity);

    }

    @Test
    void testmq() {
        DirectExchange directExchange = new DirectExchange("es-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
    }

    @Test
    void testqueue() {
        Queue queue = new Queue("es-foods-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
    }

    @Test
    void testqueue2() {
        Queue queue = new Queue("es-specials-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
    }

    @Test
    void testqueue3() {
        Queue queue = new Queue("es-topics-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
    }

    @Test
    void testBinding() {
        Binding binding = new Binding("es-foods-queue",Binding.DestinationType.QUEUE,"es-exchange","es-foods-key",null);
        amqpAdmin.declareBinding(binding);
    }

    @Test
    void testBinding2() {
        Binding binding = new Binding("es-specials-queue",Binding.DestinationType.QUEUE,"es-exchange","es-specials-key",null);
        amqpAdmin.declareBinding(binding);
    }

    @Test
    void testBinding3() {
        Binding binding = new Binding("es-topics-queue",Binding.DestinationType.QUEUE,"es-exchange","es-topics-key",null);
        amqpAdmin.declareBinding(binding);
    }

}
