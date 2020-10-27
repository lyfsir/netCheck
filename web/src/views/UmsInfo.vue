<template>
  <div class="ums">
    <el-container>
      <el-header height="230px">
        <img src="http://static.meishichina.com/v6/img/center/bg.jpg" alt="img" width="100%" height="100%"
             style="position: absolute"/>
        <div class="header">
          <el-avatar v-if="user.header==null" :size="120"
                     src="https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png"></el-avatar>
          <el-avatar v-if="user.header!=null" :size="120" :src="user.header"></el-avatar>
          <p style="font-size: 22px;font-weight: bold;padding-left: 20px">{{user.username}}</p>
        </div>
      </el-header>
      <el-main>
        <div style="margin-top: 40px" v-if="searchFood.length>0" v-loading="dataListLoading">
          <div style="display: flex;justify-content: space-between">
            <span
              style="display: inline-block;margin-bottom: 10px;color: #ff6767;border-bottom: 3px solid #ff6767;padding-bottom: 10px">{{user.username}}的菜谱</span>
            <span style="color: #666666;">共{{total}}篇</span>
          </div>
          <ul class="ul2">
            <FoodItem v-for="(foods, id) in searchFood" :key="id" :foods="foods"></FoodItem>
          </ul>
          <el-pagination
            style="text-align: center;margin-top: 30px;margin-bottom: 20px"
            background
            :page-size="12"
            :current-page="pageIndex"
            layout="pager"
            :total="total"
            @current-change="handleCurrentChange"
          >
          </el-pagination>
        </div>
        <div style="margin-top: 50px" v-if="topicList.length>0" v-loading="dataListLoading2">
          <div style="display: flex;justify-content: space-between">
            <span
              style="display: inline-block;margin-bottom: 10px;color: #ff6767;border-bottom: 3px solid #ff6767;padding-bottom: 10px">{{user.username}}的话题</span>
            <span style="color: #666666;">共{{total2}}个</span>
          </div>
          <ul class="ul3">
            <Item v-for="(topic, index) in topicList" :key="index" :topic="topic"></Item>
          </ul>
          <el-pagination
            style="text-align: center;margin-top: 30px;margin-bottom: 20px"
            background
            :page-size="12"
            :current-page="pageIndex2"
            layout="pager"
            :total="total2"
            @current-change="handleCurrentChange2"
          ></el-pagination>
        </div>
        <div style="margin-top: 40px;text-align: center;height: 400px;line-height: 400px" v-if="searchFood.length==0&&topicList.length==0">
          <p style="color: #ff6767;font-size: 30px">他还在发芽中</p>
        </div>
      </el-main>
      <el-footer height="140px">
        <Footer></Footer>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
    import Footer from "../components/Footer";
    import {getfoods, getTopicInfo, reqUmsInfo} from "../api";
    import {reqSearchFood} from "../api";
    import FoodItem from "../components/SpecialIndomation/FoodItem";
    import Item from "../components/First/TopicItem";
    import {reqSearchTopic} from "../api";

    export default {
        name: "UmsInfo",
        components: {
            Footer,
            FoodItem,
            Item
        },
        data() {
            return {
                user: {},
                list: {},
                searchFood: [],
                total: 0,
                pageIndex: 1,
                pageIndex2: 1,
                total2: 0,
                topicList: [],
                dataListLoading: true,
                dataListLoading2: true

            }
        },
        methods: {
            getData() {
                const param = {}
                param.limit = 12
                param.page = this.pageIndex
                param.uid = this.$route.query.uid
                getfoods(param).then((res) => {
                    this.dataListLoading = false;
                    this.searchFood = res.page.list;
                    this.total = res.page.totalCount
                })
            },
            handleCurrentChange(c) {
                this.pageIndex = c
                this.dataListLoading = true;
                this.getData()
            },
            getData2(){
                const param = {}
                param.uId = this.$route.query.uid
                param.page = this.pageIndex2
                param.limit = 12
                getTopicInfo(param).then((res) => {
                    this.dataListLoading2 = false;
                    this.topicList = res.page.list;
                    this.total2 = res.page.totalCount
                })
            },
            handleCurrentChange2(c) {
                this.pageIndex2 = c
                this.dataListLoading2 = true;
                this.getData2()
            }
        },
        created() {
            if (this.$route.query.uid == null) {
                this.$router.push('/')
            } else {
                var uid = this.$route.query.uid
                reqUmsInfo({uid}).then((res) => {
                    if (res.data == null) {
                        this.$router.push('/')
                    }
                    this.user = res.data
                    this.getData()
                    this.getData2()
                    console.log("user", this.user)
                })
            }
        }
    }
</script>

<style lang="less" scoped>
  .ums {
    padding-top: 40px;

  }

  .el-header {
    padding: 0;
    line-height: 70px;
    position: relative;
  }

  .ul2 {
    width: 103%;
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;
  }

  .list-group-item {
    padding: 0;
    margin-right: 3%;
    width: 22%;
    height: 250px;
  }

  .el-footer {
    padding: 0;
    padding-top: 20px;
    width: 1000px;
    margin: 0 auto;
  }

  .el-main {
    margin-top: 20px;
    padding: 0;
    overflow-x: hidden;
    border-bottom: 1px solid #e8e8e8;
    width: 1000px;
    margin: 0 auto;
  }

  .header {
    display: flex;
    position: absolute;
    top: 60px;
    left: 300px;
  }
  .ul3 {
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;

    .list-group-item {
      margin-bottom: 20px;
      padding: 0;
      width: 47%;

    }

  }
</style>
