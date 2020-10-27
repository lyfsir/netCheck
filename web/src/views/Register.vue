<template>
  <el-form :label-position="labelPosition" label-width="100px" :model="data" style="padding-bottom: 30px">
    <el-form-item label="用户名">
      <el-input v-model="data.username"></el-input>
    </el-form-item>
    <el-form-item label="昵称">
      <el-input v-model="data.nickname"></el-input>
    </el-form-item>
    <el-form-item label="密码">
      <el-input type="password" v-model="data.password"></el-input>
    </el-form-item>
    <el-form-item label="手机号">
      <el-input v-model="data.mobile"></el-input>
    </el-form-item>
    <el-form-item label="验证码">
      <el-input v-model="data.code">
        <el-button slot="append" v-if="verShow" @click="btn">发送验证码</el-button>
        <el-button slot="append" v-show="!verShow">{{timer}}后再发送</el-button>
      </el-input>
    </el-form-item>
    <el-button style="background-color: #ff6767;width: 200px;color: white" @click="res">注册</el-button>
  </el-form>
</template>

<script>
    import {sendPhoneCode} from "../api";
    import {sendRegister} from "../api"

    export default {
        name: "Register.vue",
        data() {
            return {
                labelPosition: 'right',
                data: {
                    username: '',
                    nickname: '',
                    password: '',
                    repwd: '',
                    mobile: '',
                    code: ''
                },
                verShow: true,
                timer: 60,

            };
        },
        methods: {
            btn() {
                var TEL_REGEXP = /^1([38]\d|5[0-35-9]|7[3678])\d{8}$/;
                if (this.data.mobile.trim() == "") {
                    this.$message({
                        message: '请输入手机号码',
                        type: 'warning',
                        offset: 50
                    });
                } else if (!TEL_REGEXP.test(this.data.mobile)) {
                    this.$message({
                        message: '手机号码格式错误',
                        type: 'warning',
                        offset: 50
                    });
                } else {
                    this.verShow = false;
                    var auth_timer = setInterval(() => {
                        this.timer--;
                        if (this.timer <= 0) {
                            this.verShow = true;
                            clearInterval(auth_timer);
                            this.timer = 60;
                        }
                    }, 1000);

                    const phone = this.data.mobile
                    sendPhoneCode({phone}).then((response) => {
                        const result = response;
                        console.log(result);
                        if (result.code != 0) {
                            alert(result.msg);
                        }
                    });
                    // this.$addr
                    //   .get(`sms/sms/sendCode?phone=${this.mobile}`)
                }
            },
            res() {
                if (this.data.username.trim() === '' || this.data.nickname.trim() === ''
                    || this.data.password.trim() === '' || this.data.mobile.trim() === '' || this.data.code.trim() === '') {
                    this.$message({
                        message: '请完善注册信息',
                        type: 'warning',
                        offset: 50
                    });
                }else{
                    const {username,nickname,password,mobile,code} = this.data
                    const data = {username,nickname,password,mobile,code}
                    sendRegister(data).then(
                        (response) => {
                            const result = response;
                            console.log("success", result);
                            if (result.code == 10004) {
                                alert("验证码不存在");
                            } else if (result.code == 10005) {
                                alert("验证码不正确");
                            } else if (result.code == 10006) {
                                alert("用户名已存在");
                            } else if (result.code == 10007) {
                                alert("昵称已存在");
                            } else if (result.code == 10008) {
                                alert("手机号码已存在");
                            } else {
                                this.$router.push("/logandres/login");
                            }
                        },
                        (error) => {
                            console.log("接口报错");
                        }
                    );
                }
            }
        }
    }
</script>

<style scoped>

</style>
