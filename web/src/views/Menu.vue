<template>
  <div class="menuclass">
    <el-container>
      <el-header height="70px">
        <Nav></Nav>
      </el-header>
      <el-main  v-loading="dataListLoading">
        <ul class="ul2">
          <MenuItem v-for="(food, id) in searchFood" :key="id" :food="food"></MenuItem>
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
      </el-main>
      <el-footer height="140px">
        <Footer></Footer>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
    import Nav from "../components/Nav";
    import MenuItem from "../components/Menu/MenuItem";
    import Footer from "../components/Footer";
    import {reqSearchFood} from "../api";
    import {getfoods} from "../api";

    export default {
        name: "Menu",
        components: {
            Nav,
            MenuItem,
            Footer
        },
        data(){
            return{
                searchFood: [],
                total: 0,
                pageIndex: 1,
                dataListLoading: true
            }
        },
        methods:{
            getData(){
                const param = {}
                param.limit = 12
                param.page = this.pageIndex
                getfoods(param).then((res)=>{
                    console.log(res)
                    this.dataListLoading = false
                    this.searchFood = res.page.list;
                    this.total = res.page.totalCount
                })
            },
            handleCurrentChange(c){
                this.dataListLoading = true
                this.pageIndex = c
                this.getData()
            }
        },
        created() {
            this.getData()
        }
    }
</script>

<style lang="less" scoped>
  .menuclass {
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
  }

  .ul2 {
    width: 104%;
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;
  }

  .list-group-item {
    padding: 0;
    margin-right: 2.8%;
    width: 30%;
    height: 250px;
  }


</style>
