<template>
    <div class="sw">
      <div v-if="list.content==null" style="margin-top: 200px;" v-loading="true"></div>
      <el-container v-if="list.content!=null">
        <el-header height="300px">
          <img :src="list.imgurl" alt="img" width="100%" height="100%">
        </el-header>
        <el-main>
          <div class="name" style="margin-top: 20px">
            <p style="font-size: 20px;font-family: '仿宋';font-weight: bold;color: #475669">{{list.content}}</p>
          </div>
          <div style="margin-top: 50px">
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
        </el-main>
        <el-footer height="140px">
          <Footer></Footer>
        </el-footer>
      </el-container>
    </div>
</template>

<script>
    import Footer from "../components/Footer";
    import {reqSpecialinfomation} from "../api";
    import FoodItem from "../components/SpecialIndomation/FoodItem";
    import {reqSearchFood} from "../api";

    export default {
        name: "SpecialInfomation",
        components:{
            Footer,
            FoodItem
        },
        data(){
          return{
              list: {},
              searchFood: [],
              total: 0,
              pageIndex: 1
          }
        },
        methods:{
            getData(){
                const sort = "id";
                const searchParam = {}
                searchParam.sort = "id"
                searchParam.sid = this.$route.query.id
                searchParam.pageNum = this.pageIndex
                reqSearchFood(searchParam).then((res)=>{
                    console.log("resdsds",res)
                    this.searchFood = res.data.food;
                    this.total = res.data.total
                })
            },
            handleCurrentChange(c){
                console.log("page",c)
                this.pageIndex = c
                this.getData()
            }
        },
        created() {
            if (this.$route.query.id==null){
                this.$router.push('/special')
            }else {
                console.log(this.$route.query.id);
                const sid = this.$route.query.id
                reqSpecialinfomation({sid}).then((res)=>{
                    this.list = res.data;
                    console.log(this.list)
                    this.getData()
                })
            }

        }
    }
</script>

<style scoped>
  .sw{
    padding-top: 40px;
  }
  .el-header{
    padding: 0;
    line-height: 70px;
  }
  .el-footer {
    width: 1000px;
    margin: 0 auto;
    padding: 0;
    padding-top: 20px;
  }

  .el-main {
    width: 1000px;
    margin: 0 auto;
    margin-top: 20px;
    padding: 0;
    overflow-x:hidden;
    border-bottom: 1px solid #e8e8e8;
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

</style>
