<template>
  <div>
    <p v-if="search" class="p">请输入话题搜索关键词</p>
    <div class="list" v-if="!search">
      <div class="title">
        <span class="left2">{{essData.key}}<span style="font-size: 12px;margin-left: 15px;color: #666666">大约有：{{essData.total}}条关于{{essData.key}}的专题数据</span></span>
        <span class="right"><a href="javascript:void(0);" :class="{on : on}" @click="newsort">最新</a></span>
      </div>
      <div v-if="essData.topicInfosModels.length>0">
        <ul>
          <li v-for="(topic, index) in essData.topicInfosModels" class="list-group-item" style="margin-top: 30px">
            <div style="display: flex">
              <div>
                <a href="javascript:void(0);" @click="gouser(topic.uid)">
                  <el-avatar size="large" :src="topic.header?topic.header:'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'"></el-avatar>
                </a>
              </div>
              <div style="margin-left: 10px;">
              <span style="font-size: 13px"><a href="javascript:void(0);"
                                         @click="gouser(topic.uid)">{{topic.username}}</a></span><br/>
                <span style="font-size: 12px">{{topic.createTime}}</span>
              </div>
            </div>
            <p style="margin-top: 10px;font-size: 15px;color: #666666" v-html="topic.content"></p>
            <div style="margin-top: 15px;display: flex;flex-wrap: wrap">
              <div v-for="(item,id) in topic.image" style="padding-right: 10px;padding-bottom: 10px;">
                <img :src="item.imageurl"/>
              </div>
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
           v-if="essData.topicInfosModels.length===0">找不到相关数据
      </div>
    </div>
  </div>
</template>

<script>
  import {reqSearchTopic} from "../api";

  export default {
        name: "SearchTopic",
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
            PubSub.subscribe("ess3", (msg, essData) => {
                this.essData = essData;
                this.total = essData.total
                this.search = false
            });
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
            newsort() {
                this.on = true
                const searchParam = {}
                searchParam.keyword = this.essData.key
                searchParam.sort = "id"
                searchParam.pageNum = this.pageIndex
                const key = this.essData.key
                reqSearchTopic(searchParam).then((res) => {
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
                reqSearchTopic(searchParam).then((res)=>{
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
        }

    }
</script>

<style lang="less" scoped>
  img {
    width: 120px;
    height: 120px;
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
