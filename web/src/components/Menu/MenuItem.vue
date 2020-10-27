<template>
  <li class="list-group-item" v-if="food">
    <a href="javascript:void(0);" @click="gofoodInfo(food.id)">
      <img :src="food.images" alt="img" width="100%" height="80%"/>
    </a>
    <p style="font-weight: bold"><a href="javascript:void(0);" @click="gofoodInfo(food.id)">{{food.title}}</a></p>
    <p style="font-size: 13px;"><a href="javascript:void(0);" @click="gouser(food.userId)" style="color: #666666">{{food.userName}}</a>
    </p>
  </li>
</template>

<script>
    export default {
        props: ['food'],
        props: {
            'food': Object,
        },
        methods: {
            gouser(uid) {
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

  p {
    margin-top: 5px;
  }
</style>
