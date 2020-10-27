/*
包含n个接口请求函数的模块
函数的返回值: promise对象
 */
// import ajax from './ajax'
// const BASE_URL = 'http://localhost:88/api'
// const userInfo = JSON.parse(localStorage.getItem('userInfo'));
// const uId = userInfo.id;

// // 1、根据经纬度获取位置详情
// export const reqPolicyInfo = () => ajax(`${BASE_URL}/oss/oss/doget`);

// // 2, 获取分类信息标题
// export const reqCateName = () => ajax(`${BASE_URL}/food/food/sort/list/tree/0`);

import {get, post } from '../axios'

export const reqPolicyInfo = p => get('renren/oss/oss/doget', p); //获取服务端oss签名数据
export const CategoryTitle = p => get('food/food/sort/list/tree/0', p); //获取分类信息第一条数据
export const sendFood = p => post('food/food/foodinfo/save/toaud', p); //发送食物数据提交服务端进行食物保存
export const sendPhoneCode = p => get('sms/sms/sendCode', p) //获取sms的手机验证码
export const sendRegister = p => post('sms/register/user', p) //注册信息提交服务端
export const sendLogin = p => post('ums/ums/umsmember/login/ums', p) //登陆信息提交服务端
export const authLoginInfo = p => get('sms/oauth/success', p) //获取社交登陆成功的code码
export const reqCategory = p => get('food/food/sort/list/treefromredis', p) //获取分类信息
export const sendTopic = p => post('topic/topic/topicinfo/save/topic', p) //保存话题
export const reqSwiper = p => get("food/food/foodspecialinfomation/swiper",p) // 获取轮播图
export const reqSpecialinfomation = p => get("food/food/foodspecialinfomation/doGetBySid" , p) // 根据sid获取对应专题信息
export const reqUmsInfo = p => get("ums/ums/umsmember/getuserinfo" , p) // 根据usernam获取用户信息
export const reqFoodInfo = p => get("http://localhost:88/api/food/food/foodinfo/select/infomation" , p) // 根据fid获取对应食物详情信息


export const getTopicInfo = p => get('topic/topic/topicinfo/topic/list' , p)
export const getfoods = p => get('food/food/foodinfo/get/foodsInfo' , p)
export const getspecialsInfo = p => get('food/food/foodspecial/list' , p)

// 检索
export const reqSearchFood = p => get('ess/search/doget/doget/esfood' , p) //按条件检索食谱信息
export const reqSearchTopic = p => get('ess/search/doget/doget/topic' , p) // 按条件检索话题信息
export const reqSearchSpecial = p => get('ess/search/doget/doget/special', p) // 按条件检索专题信息


