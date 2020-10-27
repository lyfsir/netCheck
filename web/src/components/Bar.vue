<template>
  <div>
    <div class="bar">
      <span><a href="javascript:void(0);" @click="goTo('/')">美食网 首页</a></span>
      <span><a href="javascript:void(0);" @click="goTo('/menu')">菜谱</a></span>
      <span><a href="javascript:void(0);" @click="goTo('/topic')">社区</a></span>
      <span><a href="javascript:void(0);" @click="goTo('/special')">专题</a></span>
      <span><a href="javascript:void(0);" @click="goTo('/search')"><i class="el-icon-search i"><span
        class="search">搜索</span></i></a></span>

      <a href="javascript:void(0);" class="af" @click="goTo('/user/sendfood')"><span class="fabu">发布</span></a>

      <span class="hearder" v-if="!hasLogin"><a href="javascript:void(0);" class="ah" @click="goTo('/logandres/login')">登陆/注册</a></span>
      <span class="img" v-if="hasLogin">
        <a href="javascript:void(0);" @click="gouser(user.id)">
          <el-dropdown>
            <img :src="user.header" style="width: 60%;border-radius: 50px"/>
<!--            <img v-if="user.header==null" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"-->
<!--                 style="width: 60%;border-radius: 50px"/>-->
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item><a href="javascript:void(0);" @click="goTo('/user/umsedit')"
                                   style="color: #ff6767">管理</a></el-dropdown-item>
              <el-dropdown-item>收藏</el-dropdown-item>
              <el-dropdown-item><a href="javascript:void(0);" @click="logout"
                                   style="color: #ff6767">退出</a></el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>

      </a></span>

    </div>
  </div>
</template>

<script>
    export default {
        name: "Bar",
        data() {
            return {
                hasLogin: false,
                user: { }
            }
        },
        methods: {
            goTo(path) {
                this.$router.push(path)
            },
            logout() {
                this.$store.commit("REMOVE_INFO")
                this.$router.push("/logandres/login")
                this.$router.go(0)
            },
            gouser(uid){
                this.$router.push({
                    path: '/umsinfo',
                    query: {
                        uid: uid
                    }
                })
            }
        },

        created() {
            if (this.$store.getters.getUser) {
                // this.user = JSON.parse(this.$store.getters.getUser)
                // console.log(this.$store.getters.getUser)
                // this.user.id = this.$store.getters.getUser.id
                // this.user.username = this.$store.getters.getUser.username
                // this.user.nickname = this.$store.getters.getUser.nickname
                // this.user.header = this.$store.getters.getUser.header
                this.user = this.$store.getters.getUser
                console.log("token",localStorage.getItem("token"))
                console.log("log", this.user)
                if (this.user.header==null){
                    this.user.header = "https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"
                }
                this.hasLogin = true
            }
        }
    }
</script>

<style scoped>
  .bar {
    width: 100%;
    height: 40px;
    background-color: #333333;
    position: fixed;
    z-index: 10000;
  }

  span {
    display: inline-block;
    padding-left: 10px;
    line-height: 40px;
    font-size: 14px;
    font-weight: bold;
  }

  .search {
    padding-left: 5px;
  }

  .fabu {
    background-color: #59c3d1;
    font-weight: normal;
    font-size: 10px;
    width: 50px;
    line-height: 40px;
    float: right;
    text-align: center;
  }

  .hearder {
    background-color: #f1f1f1;
    width: 80px;
    font-size: 12px;
    font-weight: normal;
    float: right;
    text-align: center;
  }

  .img {
    width: 60px;
    float: right;
  }

  .ah {
    color: black;
  }

  .ah:hover {
    color: black;
  }

  .af {
    color: white;
  }


  a {
    text-decoration: none;
    color: #c4c4c4;
  }

  a:hover {
    color: white;
  }

  .i {
    font-weight: bold;
  }

  .el-dropdown {
    vertical-align: top;
  }

  .el-dropdown + .el-dropdown {
    margin-left: 15px;
  }

  .el-icon-arrow-down {
    font-size: 12px;
  }

</style>
