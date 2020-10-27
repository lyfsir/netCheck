<template>
  <div class="login">
    <div class="title">
      <div class="nav">
        <a href="javascript:void(0);" :class="{on : '/logandres/login'===$route.path}" @click="goTo('/logandres/login')">登陆</a>
        <a href="javascript:void(0);" :class="{on : '/logandres/register'===$route.path}" @click="goTo('/logandres/register')">注册</a>
      </div>
      <div class="content">
        <router-view></router-view>
      </div>
      <div style="font-size: 13px;padding: 10px;padding-bottom: 30px" v-if="'/logandres/login'===$route.path">第三方登陆 :
        <a class="weibo" href="https://api.weibo.com/oauth2/authorize?client_id=2927018396&response_type=code&redirect_uri=
http://127.0.0.1:8080/success"><img src="../../static/images/weibo.jpg" alt="logo" width="30px" style="padding: 5px"/></a>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "LoginAndRegister",
        methods: {
            goTo(path) {
                this.$router.push(path)
            },
        }
    }
</script>

<style scoped>
  .login {
    margin: 0px;
    padding: 0px;
    position: fixed;
    left: 0px;
    top: 0px;
    width: 100%;
    height: 100%;
    margin-top: 40px;
    background: url("../../static/images/timg.jpg") no-repeat;
    background-size: 100% 100%;
    z-index: -1;
  }
  .on{
    color: white;
    background: rgba(0,0,0,.6);
  }
  .title {
    width: 40%;

    background: rgba(255, 255, 255, .8);
    margin: 0 auto;
    margin-top: 100px;

  }
  .nav{
    display: flex;
  }

  a {
    display: inline-block;
    flex: 1;
    height: 40px;
    text-align: center;
    line-height: 40px;
  }
  a:hover{
    background: rgba(0,0,0,.6);
    color: white;
  }
  .weibo:hover{
    background: none;
  }
  .content{
    text-align: center;
    width: 80%;
    padding-top: 20px;

  }

</style>
