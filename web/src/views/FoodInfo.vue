<template>
  <div class="info">
    <el-container>
      <el-header height="70px">
        <Nav></Nav>
      </el-header>
      <el-main>
        <div><img src="../../static/images/topic.jpg" alt="topic" width="100%"></div>
        <div v-if="foodInfo.title==null" style="margin-top: 100px;height: 200px" v-loading="true"></div>
        <div v-if="foodInfo.title!=null" style="display: flex">
          <div style="margin-top: 30px;width: 65%">
            <p style="font-size: 13px">您的位置 : {{foodInfo.userName}}的菜谱 - <span v-for="(cate,id) in foodInfo.cateName"> > {{cate}}</span></p>
            <div class="main" style="margin-bottom: 30px">
              <div style="display: flex;justify-content: space-between">
                <p style="font-size: 25px;">{{foodInfo.title}}</p>
                <div>
                  <a href="javascript:void(0);" @click="gouser(foodInfo.userId)"><el-avatar size="large" :src="foodInfo.logo"></el-avatar></a>
                </div>
              </div>
              <div v-for="(img,key) in foodInfo.selfoodimages" style="margin-top: 20px">
                <img :src="img.imagesUrl" alt="img" width="100%">
              </div>
              <p style="margin-top: 30px">&nbsp;&nbsp;&nbsp;&nbsp;<span class="start">“</span>{{foodInfo.descrit}}<span
                class="start">”</span></p>
              <div style="margin-top: 30px">
                <i class="el-icon-s-promotion" style="font-size: 22px;color: #FF6767">
                  食材明细
                </i>
                <div class="shicai">
                  <div style="color: #ff6e67;font-size: 18px;">主料</div>
                  <span v-for="(zuliao,index) in foodInfo.foodAttrs" v-if="zuliao.attgroupId==1">
                    <span style="margin-left: 20px;display: inline-block;height: 50px;line-height: 50px">
                      <span>{{zuliao.attrName}}</span>
                      <span style="font-size: 13px;color: #99a9bf">{{zuliao.attrValue}}</span>
                    </span>
                  </span>
                  <div style="color: #ff6e67;font-size: 18px;margin-top: 20px">辅料</div>
                  <span v-for="(fuliao,index2) in foodInfo.foodAttrs" v-if="fuliao.attgroupId==2">
                    <span style="margin-left: 20px;display: inline-block;height: 50px;line-height: 50px">
                      <span>{{fuliao.attrName}}</span>
                      <span style="font-size: 13px;color: #99a9bf">{{fuliao.attrValue}}</span>
                    </span>
                  </span>
                  <div style="color: #ff6e67;font-size: 18px;margin-top: 20px">调料</div>
                  <span v-for="(tiaoliao,index3) in foodInfo.foodAttrs" v-if="tiaoliao.attgroupId==3">
                    <span style="margin-left: 20px;display: inline-block;height: 50px;line-height: 50px">
                      <span>{{tiaoliao.attrName}}</span>
                      <span style="font-size: 13px;color: #99a9bf">{{tiaoliao.attrValue}}</span>
                    </span>
                  </span>
                </div>
                <i class="el-icon-s-promotion" style="font-size: 22px;color: #FF6767;margin-top: 40px">
                  做法步骤
                </i>
                <div class="shicai">
                  <ul v-for="(step,key2) in foodInfo.selfoodSteps" style="padding: 10px">
                    <li>{{key2+1}}、{{step.content}}</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
          <div style="margin-left: 8%;margin-top: 80px;width: 27%">

          </div>
        </div>
      </el-main>
      <el-footer height="140px">
        <Footer></Footer>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
    import Nav from "../components/Nav";
    import Footer from "../components/Footer";
    import {reqFoodInfo} from "../api";

    export default {
        name: "FoodInfo",
        components: {
            Nav,
            Footer,
        },
        data() {
            return {
                foodInfo: {}
            }
        },
        methods: {
            gouser(uid) {
                this.$router.push({
                    path: '/umsinfo',
                    query: {
                        uid: uid
                    }
                })
            },
        },
        created() {
            if (this.$route.query.id == null) {
                this.$router.push('/')
            } else {
                const foodId = this.$route.query.id
                reqFoodInfo({foodId}).then((res) => {
                    if (res.code != 0) {
                        this.$router.push('/')
                    }
                    this.foodInfo = res.data;
                })
            }
        }
    }
</script>

<style scoped>
  .info {
    padding-top: 40px;
    width: 1000px;
    margin: 0 auto;
  }

  .el-header {
    padding: 0;
    line-height: 70px;
  }

  .el-footer {
    padding: 0;
    padding-top: 20px;
  }

  .el-main {
    margin-top: 20px;
    padding: 0;
    overflow-x: hidden;
    border-bottom: 1px solid #e8e8e8;
    overflow-y: hidden;
  }

  .main {
    margin-top: 20px;
  }

  .start {

    font-size: 30px;
    font-family: 宋体, "Hiragino Sans GB", STHeiti, 微软雅黑, "Microsoft YaHei";
    color: rgb(191, 191, 191);
    height: 16px;
    line-height: 100%;
    width: 30px;
    display: inline-block;
    overflow: hidden;
  }

  .shicai {
    margin-left: 10px;
    margin-top: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    padding: 20px;
    border-radius: 30px;
  }

</style>
