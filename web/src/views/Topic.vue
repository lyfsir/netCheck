<template>
  <div class="topic">
    <el-container>
      <el-header height="70px">
        <Nav></Nav>
      </el-header>
      <el-main>
        <div><img src="../../static/images/topic.jpg" alt="topic" width="100%"></div>
        <div class="topic_main">
          <div class="topic_left">
            <span class="topic_title">话题</span>
          </div>
          <div class="topic_right">
            <el-button style="background-color: #ff6767;width: 100%;height: 50px;color: white;font-size: 18px" @click="$router.push('/user/sendtopic')">发布话题
            </el-button>
          </div>
        </div>
        <div class="topic_content">
          <div class="ul2" v-loading="dataListLoading">
            <ul>
              <TopicItem v-for="(topic, id) in searchTopic" :key="id" :topic="topic"></TopicItem>
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

          <img src="../../static/images/topic2.png" alt="topic2" style="width: 30%;height: 500px"/>
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
    import TopicItem from "../components/First/TopicItem";
    import {getTopicInfo} from "../api";

    export default {
        name: "Topic",
        components: {
            Nav,
            Footer,
            TopicItem
        },
        data() {
            return {
                hasA: true,
                sort: "",
                searchTopic: [],
                total: 0,
                pageIndex: 1,
                dataListLoading: true
            }
        },
        methods: {
            getData() {
                var params = {}
                params.page = this.pageIndex
                params.limit = 12
                getTopicInfo(params).then((res) => {
                    this.dataListLoading = false
                    this.searchTopic = res.page.list;
                    this.total = res.page.totalCount

                })
            },
            handleCurrentChange(c) {
                console.log("page", c)
                this.pageIndex = c
                this.dataListLoading = true
                this.getData()
            }
        },
        created() {
            this.getData()
        }
    }
</script>

<style lang="less" scoped>
  .topic {
    padding-top: 40px;
    width: 1000px;
    margin: 0 auto;
  }

  .topic_main {
    height: 50px;
    width: 100%;
    margin-top: 25px;
    display: flex;
    line-height: 50px;
    justify-content: space-between;
  }

  .topic_left {
    width: 60%;
    border-bottom: 1px solid #99a9bf;
    display: flex;
    justify-content: space-between;
  }

  .topic_right {
    width: 30%;
  }

  .topic_title {
    font-size: 19px;
    color: #ff6767;
    font-weight: bold;
    border-bottom: 3px solid #ff6767;
  }

  .topic_type {
    display: flex;

    li {
      margin-left: 20px;
    }
  }

  .topic_content {
    display: flex;
    justify-content: space-between;
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

  .on {
    color: #ff6767;
  }

  .on2 {
    color: #ff6767;
  }

  .ul2 {
    width: 60%;
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;
    flex-direction: column;

    .list-group-item {
      padding: 0;
      margin-top: 10px;
      margin-bottom: 20px;
    }
  }
</style>
