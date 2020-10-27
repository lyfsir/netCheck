<template>
  <div class="send">
    <el-container>
      <el-aside width="110px">
        <ul class="left">
          <a href="javascript:void(0);" :class="{on : '/user/umsedit'===$route.path}" @click="goTo('/user/umsedit')">
            <li>
              账户设置
            </li>
          </a>
          <a href="javascript:void(0);" :class="{on : '/user/sendfood'===$route.path}" @click="goTo('/user/sendfood')">
            <li>
              菜谱
            </li>
          </a>
          <a href="javascript:void(0);" :class="{on : '/user/sendtopic'===$route.path}" @click="goTo('/user/sendtopic')">
            <li>
              话题
            </li>
          </a>
          <a href="javascript:void(0);" :class="{on : '/user/info'===$route.path}" @click="goTo('/user/info')">
            <li>
              收藏
            </li>
          </a>

        </ul>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
    export default {
        methods: {
            goTo(path) {
                this.$router.push(path)
            }
        }
    }
</script>

<style lang="less" scoped>

  .send {
    padding-top: 40px;
  }

  .el-aside {
    background-color: #f8f8f8;
    position: fixed;
    height: 100%;

    .left {
      display: flex;
      flex-direction: column;

      :hover {
        background-color: #eeeeee;
      }

      li {
        height: 60px;
        text-align: center;
        line-height: 60px;
        border-bottom: 1px solid #d3dce6;
      }

      .on {
        background-color: #eeeeee;
        color: #ff6767;
      }
    }

  }

  .el-main {
    margin-left: 110px;
  }

</style>
