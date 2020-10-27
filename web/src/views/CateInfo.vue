<template>
    <div class="info">
      <el-container>
        <el-header height="70px">
          <Nav></Nav>
        </el-header>
        <el-main>
          <div style="display:flex;height: 50px;line-height: 50px;justify-content: space-between">
            <div>
              <p style="color: #ff6767;font-size: 22px;font-weight: bold">{{cateName}}</p>
            </div>
            <div class="title">
              <span class="right"><a href="javascript:void(0);" :class="{on : !on}" @click="allsort">全部</a></span>
              <span class="right"><a href="javascript:void(0);" :class="{on : on}" @click="newsort">最新</a></span>
            </div>
          </div>

          <div v-if="essData.food&&essData.food.length>0">
            <ul>
              <li v-for="(item, index) in essData.food"
                  style="height: 140px;display: flex;margin-top: 20px;">
                <a href="javascript:void(0);" @click="gofoodInfo(item.id)" style="width: 15%">
                  <img :src="item.imgurl" alt="img" width="100%" height="100%">
                </a>
                <div style="margin-left: 20px;margin-top: 20px;">
                  <p style="font-size: 20px"><a href="javascript:void(0);" @click="gofoodInfo(item.id)" v-html="item.title"></a></p>
                  <p style="font-size: 13px;"><a href="javascript:void(0);" style="color: #99a9bf" @click="gouser(item.userId)">{{item.username}}</a></p>
                  <p style="font-size: 15px;color: #99a9bf">主料：<span
                    v-for="(attr, id) in item.attr" :key="id">{{attr.attrName}}&nbsp;&nbsp;</span></p>
                </div>
              </li>
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
          <div style="height: 200px;text-align: center;line-height: 200px;font-size: 20px;color: #ff6767;font-weight: bold"
               v-if="essData.food.length === 0">找不到相关数据
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
    import {reqSearchFood} from "../api";

    export default {
        name: "FoodInfo",
        components:{
            Nav,
            Footer
        },
        data(){
            return{
                essData: [],
                on: false,
                total: 0,
                pageIndex: 1,
                cateName: ''
            }
        },
        methods: {
            gouser(uid){
                this.$router.push({
                    path: '/umsinfo',
                    query: {
                        uid: uid
                    }
                })
            },
            allsort(){
                this.on = false
                this.pageIndex = 1
                this.getData()
            },
            newsort() {
                this.on = true
                this.pageIndex = 1
                this.getData()
            },
            getData(){
                const cateId = this.$route.query.cid
                const searchParam = {}
                if(this.on == true){
                    searchParam.sort = "id"
                }
                searchParam.pageNum = this.pageIndex
                searchParam.keyword = this.essData.key
                searchParam.cateId = cateId
                const key = this.essData.key
                reqSearchFood(searchParam).then((res)=>{
                    console.log("res",res)
                    this.essData = res.data;
                    this.total = res.data.total;
                    this.essData.key = key
                })
            },
            handleCurrentChange(c){
                console.log("page",c)
                this.pageIndex = c
                this.getData()
            },
            gofoodInfo(id){
                this.$router.push({
                    path: '/foodinfo',
                    query: {
                        id: id
                    }
                })
            }
        },
        created() {
            this.cateName = this.$route.query.cName
            this.getData();
        }
    }
</script>

<style lang="less" scoped>
  .info{
    padding-top: 40px;
    width: 1000px;
    margin: 0 auto;
  }
  .el-header{
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
    overflow-x:hidden;
    overflow-y: hidden;
    border-bottom: 1px solid #e8e8e8;
  }
  .p {
    text-align: center;
    height: 350px;
    background-color: #d3dce6;
    margin-top: 20px;
    font-size: 20px;
    color: #ff6767;
    line-height: 350px;
    border-radius: 50px;
  }

  p {
    padding: 4px;
  }

  .on{
    color: #ff6767;
  }

  .title {


  span {
    display: inline-block;
    height: 40px;

    line-height: 40px;
    margin-top: 7px;
  }

  .left {
    font-size: 18px;
    color: #ff6767;

    border-bottom: 2px solid #ff6767;
  }

  .right {
    font-size: 14px;
    padding-left: 20px;
  }

  }
</style>
