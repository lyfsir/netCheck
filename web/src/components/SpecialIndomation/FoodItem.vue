<template>
  <li class="list-group-item" v-if="foods">
    <a href="javascript:void(0);" @click="gofoodInfo(foods.id)">
      <img :src="foods.imgurl?foods.imgurl:foods.images" alt="img" width="100%" height="80%" style="border-radius: 50px" />
    </a>
    <p style="font-weight: bold"><a href="javascript:void(0);" @click="gofoodInfo(foods.id)">{{foods.title}}</a></p>
    <p style="font-size: 13px;"><a href="javascript:void(0);" @click="gouser(foods.userId)" style="color: #666666">{{foods.username}}</a></p>
  </li>
</template>

<script>
    export default {
        name: "FoodItem",
        props: ['foods'],
        props: {
            'foods': Object,
        },
        methods:{
            gouser(uid){
                this.$router.push({
                    path: '/umsinfo',
                    query: {
                        uid: uid
                    }
                })
            },
            gofoodInfo(id){
                this.$router.push({
                    path: '/foodinfo',
                    query: {
                        id: id
                    }
                })
            }
        }
    }
</script>

<style scoped>
  li {
    text-align: center;
    margin-bottom: 20px;
  }

  p{
    margin-top: 5px;
  }
</style>
