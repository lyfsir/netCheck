<template>
  <li class="list-group-item" v-if="special">
    <a href="javascript:void(0);" @click="goinfo(special.id)">
      <img :src="special.imgurl" alt="img" width="100%" height="85%"/>
    </a>
    <p style="font-weight: bold"><a href="javascript:void(0);" @click="goinfo(special.id)">{{special.name}}</a></p>
  </li>
</template>

<script>
    export default {
        name: "SpecialItem",
        props: ['special'],
        props: {
            'special': Object,
        },
        methods:{
            goinfo(id){
                this.$router.push({
                    path: '/specialinfo',
                    query: {
                        id: id
                    }
                })
            },

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
