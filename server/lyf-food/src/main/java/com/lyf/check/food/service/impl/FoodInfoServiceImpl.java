package com.lyf.check.food.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyf.check.food.entity.*;
import com.lyf.check.food.fegin.SearchFeignService;
import com.lyf.check.food.fegin.UmsMemberService;
import com.lyf.check.food.service.*;
import com.lyf.check.food.vo.*;
import com.lyf.common.model.FoodInfosModel;
import com.lyf.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyf.common.utils.PageUtils;
import com.lyf.common.utils.Query;

import com.lyf.check.food.dao.FoodInfoDao;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("foodInfoService")
public class FoodInfoServiceImpl extends ServiceImpl<FoodInfoDao, FoodInfoEntity> implements FoodInfoService {
    @Autowired
    UmsMemberService umsMemberService;

    @Autowired
    FoodAttrGroupService foodAttrGroupService;

    @Autowired
    FoodStepService foodStepService;

    @Autowired
    FoodImagesService foodImagesService;


    @Autowired
    FoodAttrService foodAttrService;

    @Autowired
    FoodCategoryRelationService foodCategoryRelationService;

    @Autowired
    FoodSpecialService foodSpecialService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    SearchFeignService eSinitFeignService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 保存待审核的信息
     *
     * @param infomationVo
     * @return
     */
    @Override
    @Transactional
    public void savealltoaut(FoodInfomationVo infomationVo) {
        infomationVo.setAuditing(1);
        //保存基础信息
        FoodInfoEntity info = new FoodInfoEntity();
        BeanUtils.copyProperties(infomationVo, info);
        LocalDate now = LocalDate.now();
        info.setCreateTime(String.valueOf(now));
        this.save(info);
        //保存图片集
        List<FoodImagesVo> foodimages = infomationVo.getFoodimages();
        foodImagesService.saveImages(info.getId(), foodimages);

        //保存主料属性集
        List<FoodAttrEntity> foodAttrs = infomationVo.getDomains();
        List<FoodAttrEntity> collect = foodAttrs.stream().map(value -> {
            FoodAttrEntity attrEntity = new FoodAttrEntity();
            attrEntity.setAttrName(value.getAttrName());
            attrEntity.setAttrValue(value.getAttrValue());
            attrEntity.setFoodId(info.getId());
            attrEntity.setAttgroupId(1);
            return attrEntity;
        }).collect(Collectors.toList());
        foodAttrService.saveBatch(collect);

        //保存辅料料属性集
        List<FoodAttrEntity> foodAttrs2 = infomationVo.getDoless();
        if (foodAttrs2 != null && foodAttrs2.size() > 0) {
            List<FoodAttrEntity> collect2 = foodAttrs2.stream().map(value -> {
                FoodAttrEntity attrEntity = new FoodAttrEntity();
                attrEntity.setAttrName(value.getAttrName());
                attrEntity.setAttrValue(value.getAttrValue());
                attrEntity.setFoodId(info.getId());
                attrEntity.setAttgroupId(2);
                return attrEntity;
            }).collect(Collectors.toList());
            foodAttrService.saveBatch(collect2);
        }

        //保存其他属性集
        List<FoodAttrEntity> foodAttrs3 = infomationVo.getDomore();
        if (foodAttrs3 != null && foodAttrs3.size() > 0) {
            List<FoodAttrEntity> collect3 = foodAttrs3.stream().map(value -> {
                FoodAttrEntity attrEntity = new FoodAttrEntity();
                attrEntity.setAttrName(value.getAttrName());
                attrEntity.setAttrValue(value.getAttrValue());
                attrEntity.setFoodId(info.getId());
                attrEntity.setAttgroupId(3);
                return attrEntity;
            }).collect(Collectors.toList());
            foodAttrService.saveBatch(collect3);
        }

        //保存步骤集
        List<FoodStepVo> foodSteps = infomationVo.getFoodSteps();
        List<FoodStepEntity> collect1 = foodSteps.stream().map(value -> {
            FoodStepEntity entity = new FoodStepEntity();
            entity.setContent(value.getContent());
            entity.setFoodId(info.getId());
            return entity;
        }).collect(Collectors.toList());
        foodStepService.saveBatch(collect1);
    }


    /**
     * 保存全部信息（保存后台评审的信息）
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public void saveall(UpdatefoodVo vo) {
        LocalDate now = LocalDate.now();
        vo.setCreateTime(String.valueOf(now));
        //因为数据库已经保存auditing为1的信息但是没有分类
        //修改auditing为0
        FoodInfoEntity entity = new FoodInfoEntity();
        //保存前台传来的分类信息
        foodCategoryRelationService.saveCategory(vo.getId(), vo.getTitle(), vo.getCategory());

        // 保存专题信息
        entity.setAuditing(0);
        if (vo.getSid() != null) {
            entity.setSId(vo.getSid());
        }
        entity.setCreateTime(String.valueOf(now));
        this.getBaseMapper().update(entity, new QueryWrapper<FoodInfoEntity>().eq("id", vo.getId()));

        // 发送添加food信息给foods索引的消息
        rabbitTemplate.convertAndSend("es-exchange","es-foods-key",vo);

    }

    /**
     * @param foodId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public GetFoodInfomationVo selectInfomation(Integer foodId) throws ExecutionException, InterruptedException {
//        long startTime = System.currentTimeMillis();    //获取开始时间
        GetFoodInfomationVo infomationVo = new GetFoodInfomationVo();
        //获取info信息
        FoodInfoEntity entity = this.baseMapper.selectOne(new QueryWrapper<FoodInfoEntity>().eq("id", foodId));

        if (entity != null) {
            CompletableFuture<Void> bean = CompletableFuture.runAsync(() -> {
                BeanUtils.copyProperties(entity, infomationVo);
            }, executor);

            CompletableFuture<Void> special = CompletableFuture.runAsync(() -> {
                //获取专题信息
                if (entity.getSId() != null) {
                    FoodSpecialEntity specialEntity = foodSpecialService.getById(entity.getSId());
                    if (specialEntity != null) {
                        infomationVo.setSpecialName(specialEntity.getName());
                    }
                }
            }, executor);

            CompletableFuture<Void> userInfo = CompletableFuture.runAsync(() -> {
                //获取用户信息
                R user = umsMemberService.username(entity.getUserId());
                String username = (String) user.get("username");
                String logo = (String) user.get("logo");
                infomationVo.setUserName(username);
                infomationVo.setLogo(logo);
            }, executor);

            CompletableFuture<Void> cate = CompletableFuture.runAsync(() -> {
                //获取所属分类信息
                List<String> list = new ArrayList<>();
                List<FoodCategoryRelationEntity> foodCategoryRelationEntities = foodCategoryRelationService.getBaseMapper().selectList(
                        new QueryWrapper<FoodCategoryRelationEntity>().eq("food_id", entity.getId()));

                for (FoodCategoryRelationEntity entity1 : foodCategoryRelationEntities) {
                    String categoryName = entity1.getCategoryName();
                    list.add(categoryName);
                }
                infomationVo.setCateName(list);
            }, executor);

            CompletableFuture<Void> imagesInfo = CompletableFuture.runAsync(() -> {
                //获取图片集
                List<FoodImagesEntity> images = foodImagesService.getBaseMapper().selectList(
                        new QueryWrapper<FoodImagesEntity>().eq("food_id", entity.getId())
                );
                infomationVo.setSelfoodimages(images);
            }, executor);

            CompletableFuture<Void> attr = CompletableFuture.runAsync(() -> {
                //获取属性分组以及属性值
                List<FoodAttrEntity> attList = new ArrayList<>();
                List<FoodAttrGroupEntity> groupEntities =
                        foodAttrGroupService.getBaseMapper().selectList(new QueryWrapper<FoodAttrGroupEntity>());
                for (FoodAttrGroupEntity entity1 : groupEntities) {
                    List<FoodAttrEntity> atts = foodAttrService.getBaseMapper().selectList(
                            new QueryWrapper<FoodAttrEntity>().eq("attgroup_id", entity1.getId())
                                    .eq("food_id", entity.getId())
                    );
                    for (FoodAttrEntity entity2 : atts) {
                        attList.add(entity2);
                    }
                }
                infomationVo.setFoodAttrs(attList);
            }, executor);

            CompletableFuture<Void> step = CompletableFuture.runAsync(() -> {
                //获取步骤
                List<FoodStepEntity> stepList = foodStepService.getBaseMapper().selectList(new QueryWrapper<FoodStepEntity>()
                        .eq("food_id", entity.getId()));
                infomationVo.setSelfoodSteps(stepList);
            }, executor);

            CompletableFuture.allOf(userInfo, bean, special, cate, imagesInfo, attr, step).get();
//            long endTime = System.currentTimeMillis();    //获取结束时间
//            System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
            return infomationVo;
        } else {
            return null;
        }

    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Integer num) {
        String title = (String) params.get("title");
        IPage<FoodInfoEntity> page = this.page(
                new Query<FoodInfoEntity>().getPage(params),
                new QueryWrapper<FoodInfoEntity>().like("title", title).eq("auditing", num)
        );
        List<FoodInfoEntity> list = page.getRecords().stream().map(value -> {
            //获取用户信息
            Integer userId = value.getUserId();
            R info = umsMemberService.username(userId);
            String username = (String) info.get("username");
            value.setUserName(username);
            FoodImagesEntity imagesEntity = foodImagesService.getBaseMapper().selectOne(new QueryWrapper<FoodImagesEntity>().eq("food_id", value.getId())
                    .eq("type", 0));
            String imagesUrl = imagesEntity.getImagesUrl();
            value.setImages(imagesUrl);
            return value;
        }).collect(Collectors.toList());
        page.setRecords(list);
        return new PageUtils(page);
    }


    // 同步foods信息到foods索引中
    @Override
    public int init() {
//        long startTime = System.currentTimeMillis();    //获取开始时间
        boolean flag = true;
        int size = 1000;
        Page page = new Page();
        page.setSize(size);
        int num = 0;
        int i = 1;
        while (true) {
            page.setCurrent(i);
            IPage<FoodInfosVo> models = this.getBaseMapper().findFoood(page);
            if (models.getRecords().isEmpty() || models.getRecords() == null) {
                break;
            }
            List<FoodInfosModel> collect2 = models.getRecords().stream().map(info -> {
                // 分类
                CompletableFuture<Void> categorys = CompletableFuture.runAsync(() -> {
                    List<FoodCategoryRelationEntity> cate = foodCategoryRelationService.getBaseMapper().selectList(
                            new QueryWrapper<FoodCategoryRelationEntity>().eq("food_id", info.getId()));
                    List<FoodInfosVo.Category> collect = cate.stream().map(cat -> {
                        FoodInfosVo.Category category = new FoodInfosVo.Category();
                        category.setCId((long) cat.getCategoryId());
                        category.setCName(cat.getCategoryName());
                        return category;
                    }).collect(Collectors.toList());
                    info.setCategory(collect);
                }, executor);


                // 属性
                CompletableFuture<Void> attr = CompletableFuture.runAsync(() -> {
                    FoodAttrGroupEntity search = foodAttrGroupService.getBaseMapper().selectOne(new QueryWrapper<FoodAttrGroupEntity>().eq("search", "1"));
                    if (search != null) {
                        List<FoodAttrEntity> attrEntities = foodAttrService.selectAttrByFId(info.getId().intValue(), search.getId());
                        List<FoodInfosVo.Attr> collect1 = attrEntities.stream().map(value -> {
                            FoodInfosVo.Attr attrs = new FoodInfosVo.Attr();
                            attrs.setAttrIid((long) value.getId());
                            attrs.setAttrName(value.getAttrName());
                            attrs.setAttrValue(value.getAttrValue());
                            return attrs;
                        }).collect(Collectors.toList());
                        info.setAttr(collect1);
                    }
                }, executor);


                //默认图片信息
                CompletableFuture<Void> img = CompletableFuture.runAsync(() -> {
                    FoodImagesEntity imagesEntity = foodImagesService.getBaseMapper().selectOne(new QueryWrapper<FoodImagesEntity>().eq("food_id", info.getId())
                            .eq("type", 0));
                    String url = imagesEntity.getImagesUrl();
                    info.setImgurl(url);
                }, executor);

                try {
                    CompletableFuture.allOf(categorys, attr, img).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                FoodInfosModel infosModel = new FoodInfosModel();
                BeanUtils.copyProperties(info, infosModel);
                return infosModel;
            }).collect(Collectors.toList());
            int esnum = this.esinit(collect2);
            num += esnum;
            if (models.getRecords().size() < size) {
                break;
            }
            i++;
        }
//        long endTime = System.currentTimeMillis();    //获取结束时间
//        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
        return num;
    }

    @Override
    public PageUtils queryInfo(Map<String, Object> params) {
        String uid = (String) params.get("uid");
        IPage<FoodInfoEntity> foods = this.page(
                new Query<FoodInfoEntity>().getPage(params),
                new QueryWrapper<FoodInfoEntity>().eq("auditing", 0)
                        .eq(StringUtils.isNotBlank(uid), "user_id", uid)
                        .orderByDesc(StringUtils.isNotBlank(uid), "id")
        );
        List<FoodInfoEntity> collect = foods.getRecords().stream().map(value -> {
            //获取用户信息
            CompletableFuture<Void> userName = CompletableFuture.runAsync(() -> {
                Integer userId = value.getUserId();
                R info = umsMemberService.username(userId);
                String username = (String) info.get("username");
                value.setUserName(username);
            }, executor);

            CompletableFuture<Void> image = CompletableFuture.runAsync(() -> {
                FoodImagesEntity imagesEntity = foodImagesService.getBaseMapper().selectOne(new QueryWrapper<FoodImagesEntity>().eq("food_id", value.getId())
                        .eq("type", 0));
                String imagesUrl = imagesEntity.getImagesUrl();
                value.setImages(imagesUrl);
            }, executor);

            try {
                CompletableFuture.allOf(userName, image).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return value;
        }).collect(Collectors.toList());
        foods.setRecords(collect);
        return new PageUtils(foods);
    }

    private int esinit(List<FoodInfosModel> update) {
        if (update == null || update.isEmpty()) {
            return 0;
        }
        R init = eSinitFeignService.init(update);
        if ((int) init.get("code") == 0) {
            int num = (Integer) init.get("num");
            log.info("一共同步了foods[{}]条",String.valueOf(num));
            return num;
        } else {
            return 0;
        }
    }


}