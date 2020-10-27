<template>
  <el-form :label-position="labelPosition" label-width="100px" :model="formLabelAlign" >
    <el-form-item label="手机号/昵称">
      <el-input v-model="formLabelAlign.user"></el-input>
    </el-form-item>
    <el-form-item label="密码">
      <el-input type="password" v-model="formLabelAlign.password"></el-input>
    </el-form-item>
    <el-button style="background-color: #ff6767;color: white;width: 150px" @click="login">登陆</el-button>
  </el-form>
</template>

<script>
  import {sendLogin} from '../api/index'
  import cookie from 'vue-cookie'
    export default {
        name: "Login",
        data() {
            return {
                labelPosition: 'right',
                formLabelAlign: {
                    user: '',
                    password: ''
                }
            };
        },
        methods:{
            login(){
                if (this.formLabelAlign.user.trim()===''||this.formLabelAlign.password.trim()===''){
                    this.$message({
                        message: '用户名或密码为空',
                        type: 'warning',
                        offset: 50
                    });
                }else {
                    const {user,password} = this.formLabelAlign;
                    // this.$axios.post("ums/ums/umsmember/login/ums",data).then((res)=>{
                    //     const result = response;
                    //     console.log(result)
                    // })
                    const data = {user,password}
                    sendLogin(data).then((response) => {
                        console.log(response)
                        if (response.code != 0) {
                            this.$message({
                                message: response.msg,
                                type: 'error',
                                offset: 50
                            });
                        } else {
                            const jwt = response.data.token
                            const userInfo = response.data
                            cookie.set('token', jwt)
                            this.$store.commit("SET_USERINFO", userInfo)
                            this.$router.push("/");
                            this.$router.go(0)
                        }
                    });
                }
            }
        }
    }
</script>

<style scoped>
.login{

}
</style>
