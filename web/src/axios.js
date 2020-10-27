import axios from 'axios'
import Element from 'element-ui'
import router from './router'
import store from './store'
import Vue from 'vue'
import cookie from 'vue-cookie'
import QS from 'qs';


axios.defaults.baseURL = "http://localhost:88/api/"
axios.defaults.headers = {'Content-Type': 'application/json; charset=utf-8',"token": cookie.get('token')}


/**
 * get方法，对应get请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function get(url, params) {
  return new Promise((resolve, reject) => {
    axios.get(url, {
      params: params
    })
      .then(res => {
        if (res.data && res.data.code === 401) { // 401, token失效
          store.commit("REMOVE_INFO")
          router.push('/logandres/login')
        }
        resolve(res.data);
      }).catch((error) => {
      console.log(error)
      if (error.response.data) {
        error.message = error.response.data.msg
        Element.Message.error({
          message: "系统错误",
          offset: 50
        })
        router.push('/')
      }

    })

  });
}

/**
 * post方法，对应post请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function post(url, params) {
  return new Promise((resolve, reject) => {
    axios.post(url, JSON.stringify(params))
      .then(res => {
        if (res.data && res.data.code === 401) { // 401, token失效
          store.commit("REMOVE_INFO")
          router.push('/logandres/login')
        }
        resolve(res.data);
      }).catch((error) => {
      console.log(error)
      if (error.response.data) {
        error.message = error.response.data.msg
        Element.Message.error({
          message: "系统错误",
          offset: 50
        })
        router.push('/')
      }

    })

  });
}
