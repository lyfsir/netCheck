<template>
  <div>
    <div class="title">
      <span>
        <span class="s1 active2">
          <a href="javascript:void(0);" class="active">最新话题</a>
        </span>
      </span>
      <ul class="ul1">
        <li class="list-group-item">
          <a href="javascript:void(0);" @click="goTopic">全部话题</a>
        </li>
      </ul>
    </div>
      <ul class="ul2">
        <Item v-for="(topic, id) in topicList" :key="id" :topic="topic"></Item>
      </ul>
    </div>

</template>

<script>
  import Item from "./TopicItem";
  import {getTopicInfo} from "../../api";

  export default {
        name: "TopicCard",
        components:{
            Item
        },
        data(){
            return{
                topicList: []
            }
        },
        methods:{
            goTopic(){
                this.$router.push('/topic')
            }
        },
        created() {
            var params = {}
            params.page = 1
            params.limit = 12
            getTopicInfo(params).then((res)=>{
                console.log("data",res)
                this.topicList = res.page.list;
            })
        }
    }
</script>

<style lang="less" scoped>
  @aColor: #ff6767;
  .title {
    height: 50px;
    border-bottom: 1px solid #99a9bf;
    display: flex;
    justify-content: space-between;

    span {
      display: inline-block;
      height: 100%;

      .s1,
      .s2 {
        font-size: 20px;
        margin-right: 20px;
        line-height: 50px;

        .active {
          color: @aColor;
        }
      }

      .s1,.s2 {
        width: 85px;
      }
    }
    .ul1 {
      display: flex;
      height: 100%;
      align-items: center;
      .list-group-item {
        padding: 10px;
        border: 0px;
        padding-right: 0;
        :hover {
          color: @aColor;
        }
      }
    }
  }
  .ul2 {
    width: 100%;
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;

    .list-group-item {
      margin-bottom: 20px;
      padding: 0;
      width: 50%;

    }

  }
</style>
