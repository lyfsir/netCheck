<template>
  <div>
    <p v-if="search" class="p">请输入专题搜索关键词</p>
    <div class="list" v-if="!search">
      <div class="title">
        <span class="left2">{{essData.key}}<span style="font-size: 12px;margin-left: 15px;color: #666666">大约有：{{essData.total}}条关于{{essData.key}}的专题数据</span></span>
        <span class="right"><a href="javascript:void(0);" :class="{on : on}" @click="newsort">最新</a></span>
      </div>
      <div v-if="essData.foodSpecialModels.length>0">
        <ul>
          <li v-for="(item, index) in essData.foodSpecialModels"
              style="height: 140px;display: flex;margin-top: 20px;">
            <a href="javascript:void(0);" @click="goinfo(item.id)" style="width: 15%">
              <img :src="item.imgurl" alt="img" width="100%" height="100%">
            </a>
            <div style="margin-left: 20px;line-height: 120px">
              <p style="font-size: 20px"><a href="javascript:void(0);" @click="goinfo(item.id)"  v-html="item.name"></a></p>
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
           v-if="essData.foodSpecialModels.length===0">找不到相关数据
      </div>
    </div>
  </div>
</template>

<script>
    import PubSub from "pubsub-js";
    import {reqSearchSpecial} from "../api";

    export default {
        name: "SearchSpecial",
        data() {
            return {
                search: true,
                essData: [],
                on: false,
                total: 0,
                pageIndex: 1
            }
        },
        mounted() {
            PubSub.subscribe("ess2", (msg, essData) => {
                this.essData = essData;
                this.total = essData.total
                this.search = false
            });
        },
        methods: {
            newsort() {
                this.on = true
                const searchParam = {}
                searchParam.keyword = this.essData.key
                searchParam.sort = "id"
                searchParam.pageNum = this.pageIndex
                const key = this.essData.key
                reqSearchSpecial(searchParam).then((res) => {
                    this.essData = res.data
                    this.essData.key = key
                    this.total = res.data.total
                    this.pageIndex = 1
                    this.getData()
                    console.log("es",res)
                })
            },
            getData(){
                const searchParam = {}
                if(this.on == true){
                    searchParam.sort = "id"
                }
                searchParam.pageNum = this.pageIndex
                searchParam.keyword = this.essData.key
                const key = this.essData.key
                reqSearchSpecial(searchParam).then((res)=>{
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
            goinfo(id){
                this.$router.push({
                    path: '/specialinfo',
                    query: {
                        id: id
                    }
                })
            }
        }
    }
</script>

<style lang="less" scoped>
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
    height: 50px;
    display: flex;
    justify-content: space-between;

  span {
    display: inline-block;
    height: 40px;

    line-height: 40px;
    margin-top: 7px;
  }

  .left2 {
    font-size: 18px;
    color: #ff6767;
    border-bottom: 2px solid #ff6767;
  }

  .right {
    font-size: 14px;
  }

  }
</style>
