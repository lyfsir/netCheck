<template>
  <div style="margin-bottom: 20px">
    <div class="title">
      <ul class="ul1">
        <li class="list-group-item" v-for="(item, index) in cateName" :key="index" :item="item" v-if="index<6">
          <a  href="javascript:void(0);" :class="{on : item.id === onId}" @click="key(item.id)">{{item.name}}</a>
        </li>
      </ul>
      <span style="line-height: 50px"><a href="javascript:void(0);" @click="gotocate">全部分类</a></span>
    </div>
    <ul class="ul2">
      <Item v-for="(food, id) in getNewFood" :key="id" :food="food" v-if="id<=12"></Item>
    </ul>

  </div>
</template>

<script>
    import Item from "./Item";
    import {CategoryTitle} from "../../api";
    import {reqSearchFood} from "../../api";


    export default {
        name: "Card",
        components: {
            Item
        },
        data() {
            return {
                cateName: [],
                getNewFood: [],
                onId: ''
            }
        },
        methods: {
            key(id){
                const cateId = id;
                this.onId = id
                reqSearchFood({cateId}).then((res)=>{
                    console.log("es",res)
                    this.getNewFood = res.data.food;
                    console.log("new",this.getNewFood)
                })
            },
            gotocate(){
                this.$router.push("category")
            }
        },
        created() {
            CategoryTitle().then((res)=>{
                this.cateName = res.data
                this.key(this.cateName[0].id)
            });

        }
    }
</script>

<style lang="less" scoped>
  @aColor: #ff6767;
  .title {
    height: 50px;
    display: flex;
    border-bottom: 1px solid #99a9bf;
    justify-content: space-between;
    .on{
      color: #ff6767;
    }
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

      .s1 {
        width: 85px;
      }

      .s2 {
        width: 165px;
      }

      .active2 {
        border-bottom: 3px solid @aColor;
      }
    }

    .ul1 {
      display: flex;
      height: 100%;
      align-items: center;

      .list-group-item {
        padding-right: 20px;
        border: 0px;
        font-size: 20px;
        :hover {
          color: @aColor;
        }
      }
    }
  }

  .ul2 {
    width: 103%;
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;

    .list-group-item {
      padding: 0;
      margin-right: 3%;
      width: 22%;
      height: 250px;
    }

  }
</style>
