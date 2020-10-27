<template>
    <div class="first">
      <el-container>
        <el-header height="70px">
          <Nav></Nav>
        </el-header>
        <el-main>
          <Swiper></Swiper>
          <Card></Card>
          <TopicCard></TopicCard>
        </el-main>
        <el-footer height="140px">
          <Footer></Footer>
        </el-footer>
      </el-container>
    </div>
</template>

<script>
  import Nav from "../components/Nav";
  import Swiper from "../components/First/Swiper";
  import Card from "../components/First/Card";
  import Footer from "../components/Footer";
  import TopicCard from "../components/First/TopicCard";
    export default {
        name: "First",
        components:{
            Nav,
            Swiper,
            Card,
            Footer,
            TopicCard
        }
    }
</script>

<style scoped>
  .first{
    padding-top: 40px;
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
    border-bottom: 1px solid #e8e8e8;
  }

  .first{
    width: 1000px;
    margin: 0 auto;
  }
</style>
