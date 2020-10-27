<template>
  <div id="app">
    <bar></bar>
    <router-view/>
  </div>
</template>

<script>
import Bar from "./components/Bar";

export default {
  name: 'App',
  components:{
      Bar
  }
}
</script>

<style lang="less">
  .el-input-group__append{
    background-color: #ff6767 !important;
    color: white;
  }
  .el-pager{
    li{
      color: #999999 !important;
      width: 40px;
      height: 40px;
      line-height: 40px;
    }
    .el-icon{
      line-height: 40px !important;
    }
    .active{
      background-color: #ff6767 !important;
      color: white !important;
    }
    :hover{
      border: 1px solid #ff6767 !important;
    }
  }
  *{
    margin: 0;
    padding: 0;
  }
  a{
    text-decoration: none;
    color: black;
    font-family: "微软雅黑";
  }
  a:hover{
    color: #ff6e67;
  }
  li{list-style:none}
  img{vertical-align:top;border:none}
</style>
