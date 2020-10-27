<template>
  <div class="nav">
    <span style="margin-top: 14px;width: 250px"><img src="../assets/logo.png" alt="logo"></span>
    <ul>
      <li><a href="javascript:void(0);" :class="{on : '/'===$route.path}" @click="goTo('/')">首页</a></li>
      <li><a href="javascript:void(0);" :class="{on : '/menu'===$route.path}" @click="goTo('/menu')">菜谱</a></li>
      <li><a href="javascript:void(0);" :class="{on : '/category'===$route.path}" @click="goTo('/category')">分类</a></li>
      <li><a href="javascript:void(0);" :class="{on : '/topic'===$route.path}" @click="goTo('/topic')">社区</a></li>
      <li><a href="javascript:void(0);" :class="{on : '/special'===$route.path}" @click="goTo('/special')">专题</a></li>

    </ul>
    <span style="width: 250px">
      <el-input placeholder="请输入内容" class="input-with-select" v-model="key">
        <el-button slot="append" icon="el-icon-search" @click="goTosearch('/search')"></el-button>
      </el-input>
    </span>
  </div>
</template>

<script>
    export default {
        name: "Nav",
        data(){
            return{
                key: ' '
            }
        },
        methods:{
            goTo(path){
                this.$router.push(path)
            },
            goTosearch(path){
                if (this.key.trim()===''){
                    this.$router.push({
                        path: path
                    })
                    return
                }
                const that = this;
                this.$router.push({
                    path: path,
                    query: {
                        key: that.key
                    }
                })

            }
        }
    }
</script>

<style scoped>
  .nav {
    display: flex;
    justify-content: space-between;
    border-bottom: 2px solid #ff6767;
  }

  ul {
    display: flex;
    line-height: 80px;
  }
  .on{
    color: #ff6767;
  }

  li {
    flex: 1;
    width: 80px;
    font-size: 18px;

  }

</style>
