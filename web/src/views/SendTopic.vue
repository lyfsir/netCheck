<template>
  <div class="topic" v-loading="loading">
    <div class="title">发布话题</div>
    <div class="from">
      <p>上传图片(最多3张)</p>
      <UploadTopic></UploadTopic>
      <p style="margin-top: 20px">写话题(必须)</p>
      <el-input
        maxlength="50"
        :rows="5"
        v-model="from.content"
        placeholder="请输入话题内容/最多可以输入50字"
        show-word-limit
        type="textarea"
      ></el-input>
    </div>
    <el-button type="danger" style="margin-top: 20px" @click="sub(from,fileList)">发布话题</el-button>
  </div>
</template>

<script>
    import UploadTopic from "../components/SendTopic/UploadTopic";
    import { reqPolicyInfo } from "../api";
    import { sendTopic } from "../api"
    import axios from "axios";
    import PubSub from "pubsub-js";


    export default {
        name: "SendTopic",
        data() {
            return {
                loading: false,
                fileList: [],
                from: {
                    content: '',
                    images: []
                }
            }
        },
        components: {
            UploadTopic
        },
        mounted() {
            PubSub.subscribe("uptopic", (msg, fileList) => {
                this.fileList = fileList;
                console.log(this.fileList);
            });
        },
        methods: {

            sub(from, fileList) {
                if (from.content.trim() === '' || fileList.length == 0) {
                    this.$message.error({
                        offset: 50,
                        message: "请完善信息"
                    });
                } else {
                    this.loading = true
                    reqPolicyInfo().then((res) => {
                        this.policyInfo = res.data
                        console.log("pil", this.policyInfo)
                        console.log("--------------", this.fileList);
                        if (fileList.length > 0) {
                            fileList.map((el, key) => {
                                console.log("el", key);
                                if (this.policyInfo != null) {
                                    const res = this.policyInfo;
                                    console.log(res);
                                    //创建文件
                                    console.log("----jq-------");
                                    let imageurl = res.host + "/" + res.dir + el.name;
                                    let formData = new FormData();
                                    //表头信息
                                    let config = {
                                        headers: {
                                            "Content-Type": "multipart/form-data",
                                        },
                                    };
                                    //添加文件信息
                                    formData.append("policy", res.policy);
                                    formData.append("OSSAccessKeyId", res.accessid);
                                    formData.append("success_action_status", "200");
                                    formData.append("signature", res.signature);
                                    formData.append("key", res.dir + el.raw.name);
                                    formData.append("file", el.raw);
                                    axios
                                        .post(
                                            "https://netcheck.oss-cn-heyuan.aliyuncs.com",
                                            formData,
                                            config
                                        )
                                        .then((response) => {
                                            console.log("成功");
                                            let num = 0;
                                            if (key > 0) {
                                                num = 1;
                                            }
                                            this.from.images.push({
                                                image: imageurl,
                                            });
                                            console.log("img", this.from);
                                            //发送数据给后台
                                            if (this.fileList.length === this.from.images.length){
                                                const userInfos = this.$store.getters.getUser;
                                                const {
                                                    content,
                                                    images
                                                } = this.from;
                                                const data = {
                                                    content,
                                                    images
                                                };
                                                data.userName = userInfos.username;
                                                console.log("data", data);
                                                sendTopic(data)
                                                    .then((response) => {
                                                        const result = response;
                                                        if (result.code == 0) {
                                                            this.$alert('您的话题发布成功', '发布成功', {
                                                                confirmButtonText: '确定',
                                                                callback: action => {
                                                                    this.$router.push('/')
                                                                }
                                                            });
                                                        }
                                                    })
                                            }

                                        })
                                        .catch((err) => {
                                            console.log("失败", err);
                                            this.$message({
                                                message: "系统错误，请重新提交",
                                                type: "warning",
                                                offset: 50
                                            });
                                            this.$router.push('/')
                                        });
                                }
                            })
                        }

                    })
                }
            }
        }
    }
</script>

<style scoped>
  .title {
    height: 30px;
    font-size: 18px;
    border-bottom: 1px solid #99a9bf;
  }
  .from{
    margin-top: 20px;
  }
  p {
    color: #909399;
    font-size: 13px;
  }
</style>
