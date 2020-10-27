<template>
  <div class="search">
    <el-container>
      <el-header height="100px">
        <div style="height: 50px;width: 70%;margin: 0 auto;font-size: 14px">
          <span><a href="javascript:void(0);" :class="{on : '/search/food'==$route.path}" @click="goTo('/search/food')">菜谱</a></span>
          <span><a href="javascript:void(0);" :class="{on : '/search/special'==$route.path}" @click="goTo('/search/special')">专题</a></span>
          <span><a href="javascript:void(0);" :class="{on : '/search/topic'==$route.path}" @click="goTo('/search/topic')">话题</a></span>

        </div>
        <div style="width: 70%;margin: 0 auto">
          <el-input placeholder="请输入内容" v-model="key">
            <el-button slot="append" @click="search">搜索</el-button>
          </el-input>
        </div>
      </el-header>
      <el-main>
        <div v-if="loading" style="height: 300px" v-loading="loading" :class="{lod : loading}"></div>
        <router-view v-if="!loading"></router-view>
      </el-main>
      <el-footer height="140px">
        <Footer></Footer>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
    import Footer from "../components/Footer";
    import {reqSearchFood} from "../api";
    import {reqSearchSpecial} from "../api";
    import {reqSearchTopic} from "../api";
    import PubSub from "pubsub-js";


    export default {
        name: "Search",
        components: {
            Footer
        },
        data() {
            return {
                key: '',
                esData: [],
                loading: false
            }
        },
        methods:{
            goTo(path){
                this.$router.push(path)
            },
            search(){
                if(this.key==null){
                    this.$message({
                        message: '请填入搜索条件',
                        type: 'warning',
                        offset: 50
                    });
                    return
                }
                if (this.$route.path=='/search/food'){
                    const keyword = this.key
                    if(keyword.trim()===''){
                        this.$message({
                            message: '请填入搜索条件',
                            type: 'warning',
                            offset: 50
                        });
                        return;
                    }
                    this.loading = true
                    reqSearchFood({keyword}).then((res)=>{
                        this.loading = false
                        this.esData = res.data
                        this.esData.key = keyword
                        PubSub.publish("ess", this.esData);
                    }).catch((err)=>{
                        this.loading = false
                        this.$message({
                            message: '系统错误',
                            type: 'error',
                            offset: 50
                        });
                    })
                }
                if(this.$route.path=='/search/topic'){
                    const keyword = this.key
                    if(keyword.trim()===''){
                        this.$message({
                            message: '请填入搜索条件',
                            type: 'warning',
                            offset: 50
                        });
                        return;
                    }
                    this.loading = true
                    reqSearchTopic({keyword}).then((res)=>{
                        console.log("dsdsa",res)

                        this.loading = false
                        this.esData = res.data
                        this.esData.key = keyword
                        PubSub.publish("ess3", this.esData);
                    }).catch((err)=>{
                        this.loading = false
                        this.$message({
                            message: '系统错误',
                            type: 'error',
                            offset: 50
                        });
                    })
                }
                if (this.$route.path=='/search/special'){
                    const keyword = this.key
                    if(keyword.trim()===''){
                        this.$message({
                            message: '请填入搜索条件',
                            type: 'warning',
                            offset: 50
                        });
                        return;
                    }
                    this.loading = true
                    reqSearchSpecial({keyword}).then((res)=>{
                        this.loading = false
                        this.esData = res.data
                        this.esData.key = keyword
                        PubSub.publish("ess2", this.esData);
                    }).catch((err)=>{
                        this.loading = false
                        this.$message({
                            message: '系统错误',
                            type: 'error',
                            offset: 50
                        });
                    })
                }

            }
        },
        created() {
            console.log("key",this.$route.query.key)
            this.key = this.$route.query.key;
            if (this.key!=null){
                const keyword = this.key
                reqSearchFood({keyword}).then((res)=>{
                    console.log("res",res)
                    this.esData = res.data
                    this.esData.key = keyword
                    PubSub.publish("ess", this.esData);
                })
            }
        }
    }
</script>

<style lang="less" scoped>
  .search {
    padding-top: 40px;
    width: 1000px;
    margin: 0 auto;
  }


  .el-header {
    margin-top: 50px;
    padding: 0;
    line-height: 70px;

    span {
      padding-right: 20px;
      .on{
        color: #ff6767;
      }
    }

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


  }
</style>
