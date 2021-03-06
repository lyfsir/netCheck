// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store'
import Element from 'element-ui'
import axios from 'axios'
import "./axios"
import "./permission"
import "element-ui/lib/theme-chalk/index.css"
import VueCookie from 'vue-cookie'            // api: https://github.com/alfhen/vue-cookie

Vue.use(VueCookie)
Vue.config.productionTip = false
Vue.use(Element)
Vue.prototype.$axios = axios
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
