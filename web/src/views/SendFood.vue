<template>
    <div class="food">
      <div class="title">发布菜谱</div>
      <From class="from"></From>
    </div>
</template>

<script>
  import From from "../components/SendFood/From";
    export default {
        name: "SendFood",
        components: {
            From
        }
    }
</script>

<style lang="less" scoped>

.title{
  height: 30px;
  font-size: 18px;
  border-bottom: 1px solid #99a9bf;
}
  .from{
    margin-top: 20px;
  }

</style>
