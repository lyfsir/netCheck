<template>
  <div>正在登陆</div>
</template>

<script>
    import {authLoginInfo} from '../api/index'
    import cookie from 'vue-cookie'

    export default {
        created() {
            let code = this.$route.query.code;

            console.log(code);
            authLoginInfo({code})
                .then((reponse) => {
                    const result = reponse;
                    console.log("result",result)
                    if (result.code != 0) {
                        console.log("msg", result.msg);
                        this.$router.push("/logandres/login");
                    } else {
                        console.log("data", result);
                        const userInfo = result.data
                        const jwt = result.data.token
                        // this.$store.commit("SET_TOKEN", jwt)
                        cookie.set("token",jwt)
                        this.$store.commit("SET_USERINFO", userInfo)
                        this.$router.push("/");
                        this.$router.go(0)
                    }
                })
            // this.$addr.get(`sms/oauth/success?code=${code}`)
        },
    };
</script>

<style scoped>
  div {
    padding-top: 50px;
  }
</style>
