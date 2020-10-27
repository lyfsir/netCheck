import Vue from 'vue'
import Router from 'vue-router'
import First from "../views/First";
import Menu from "../views/Menu";
import CateGory from "../views/CateGory";
import Topic from "../views/Topic";
import Special from "../views/Special";
import Search from "../views/Search";
import SearchFood from "../views/SearchFood";
import User from "../views/User";
import SendFood from "../views/SendFood";
import SendTopic from "../views/SendTopic";
import LoginAndRegister from "../views/LoginAndRegister";
import Login from "../views/Login";
import Register from "../views/Register";
import Success from "../views/Success";
import UmsEdit from "../views/UmsEdit";
import CateInfo from "../views/CateInfo";
import SearchSpecial from "../views/SearchSpecial";
import SpecialInfomation from "../views/SpecialInfomation";
import UmsInfo from "../views/UmsInfo";
import FoodInfo from "../views/FoodInfo";
import SearchTopic from "../views/SearchTopic";
Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'First',
      component: First
    },
    {
      path: '/menu',
      name: 'Menu',
      component: Menu
    },
    {
      path: "/category",
      name: "CateGory",
      component: CateGory
    },
    {
      path: "/topic",
      name: "Topic",
      component: Topic
    },
    {
      path: '/special',
      name: 'Special',
      component: Special
    },
    {
      path: '/search',
      name: 'Search',
      component: Search,
      redirect: "/search/food",
      children: [{
        path: 'food',
        component: SearchFood
      },
        {
          path: 'special', // path 的‘/’永远代表根路由
          component: SearchSpecial
        },
        {
          path: 'topic', // path 的‘/’永远代表根路由
          component: SearchTopic
        }
      ]
    },
    {
      path: '/user',
      name: 'User',
      component: User,
      children: [{
        path: 'sendfood',
        component: SendFood
      },
        {
          path: 'sendtopic',
          component: SendTopic
        },
        {
          path: 'umsedit',
          component: UmsEdit
        }
      ],
      meta: {
        requireAuth: true
      }
    },
    {
      path: '/logandres',
      component: LoginAndRegister,
      children: [{
        path: 'login',
        component: Login
      },
        {
          path: 'register',
          component: Register
        }
      ],

    },
    {
      path: '/success',
      component: Success
    },
    {
      path: '/cateinfo',
      name: 'CateInfo',
      component: CateInfo
    },
    {
      path: '/specialinfo',
      name: 'SpecialInfomation',
      component: SpecialInfomation
    },
    {
      path: '/umsinfo',
      name: 'UmsInfo',
      component: UmsInfo
    },
    {
      path: '/foodinfo',
      name: 'FoodInfo',
      component: FoodInfo
    },

  ]
})

// const router = new VueRouter({
//   mode: 'history',
//   base: process.env.BASE_URL,
//   routes
// })
