<template>
  <div class="category">
    <el-container>
      <el-header height="70px">
        <Nav></Nav>
      </el-header>
      <el-main>
        <div v-if="category.length < 1" style="margin-top: 100px;" v-loading="loading" :class="{lod : loading}"></div>
        <div class="cate clear" v-for="(item, index) in category" :key="index">
          <h3>{{item.name}}</h3>
          <ul class="ul" v-if="item.children">
            <li v-for="(items, index) in item.children" :key="index">
              <a href="javascript:void(0);" @click="search(items.id,items.name)">{{items.name}}</a>
            </li>
          </ul>
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
    import {reqCategory} from "../api";

    export default {
        name: "CateGory",
        components: {
            Nav,
            Footer
        },
        data() {
            return {
                category: [],
                loading: true
            };
        },
        methods: {
            search(cid,name) {
                console.log(cid)
                this.$router.push({
                    path: '/cateinfo',
                    query: {
                        cid: cid,
                        cName: name
                    }
                })
            }
        },
        created() {
            reqCategory().then((res)=>{
                this.category = res.data
            })
        },
    }
</script>

<style lang="less" scoped>
  .category {
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
    overflow-y: hidden;
    .lod{
      height: 350px;
    }
    .cate {
      margin-top: 30px;
      width: 100%;
      overflow: hidden;
      background: #f8f8f8;
      position: relative;
      border-top: 1px solid #eaeaea;
      border-bottom: 1px solid #eaeaea;

      h3 {
        float: left;
        font-size: 24px;
        height: 40px;
        line-height: 40px;
        margin-top: -20px;
        position: absolute;
        text-align: center;
        top: 50%;
        width: 186px;
        color: #666;
      }

      ul {
        margin: 0;
        padding: 0;
        width: 804px;
        float: right;
        background: #fff;
        margin-bottom: -1px;
        overflow: hidden;

        li {
          float: left;
          position: relative;

          a {
            float: left;
            height: 70px;
            line-height: 64px;
            width: 160px;
            border-left: 1px solid #eaeaea;
            border-bottom: 1px solid #eaeaea;
            font-size: 20px;
            color: #333;
            text-decoration: none;
            text-decoration-line: none;
            text-decoration-style: initial;
            text-decoration-color: initial;
            text-align: center;
            position: relative;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          :hover {
            background-color: #f8f8f8;
          }
        }
      }
    }

    .clear {
      zoom: 1;
    }

    .clear:after {
      clear: both;
      content: " ";
      display: block;
      height: 0;
      overflow: hidden;
      visibility: hidden;
    }
  }
</style>
