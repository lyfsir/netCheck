<template>
  <div class="from"  v-loading="loading">
    <div class="main">
      <el-form :model="ruleForm" status-icon label-width="100px" class="demo-ruleForm">
        <p>菜单名称(必须)</p>
        <el-form-item label-width="0" prop="text">
          <el-input type="text" v-model="ruleForm.text" size="small"></el-input>
        </el-form-item>
        <!-- 成品图片 -->
        <p>上传成品图片(至少1张，最多3张)</p>
        <Upload></Upload>
        <p>说明(必须)</p>
        <el-form-item label-width="0" prop="textarea">
          <el-input
            type="textarea"
            maxlength="100"
            :rows="5"
            placeholder="请输入说明内容/最多可以输入100字"
            show-word-limit
            v-model="ruleForm.textarea"
          ></el-input>
        </el-form-item>
        <p>主料(至少包含1条主料信息)</p>
        <el-form-item
          label-width="0"
          v-for="(domain) in ruleForm.domains"
          :key="domain.key"
          prop="domains"
        >
          <span class="spaninput">
            <el-input v-model="domain.attrName" size="small" placeholder="食材名"></el-input>
          </span>
          <span class="spaninput">
            <el-input v-model="domain.attrValue" size="small" placeholder="用量"></el-input>
          </span>

          <el-button @click.prevent="removeDomain(domain)" size="mini">
            <i class="el-icon-minus"></i>
          </el-button>
          <el-button @click="addDomain" size="mini">
            <i class="el-icon-plus"></i>
          </el-button>
        </el-form-item>

        <p>辅料(可选)</p>
        <el-form-item label-width="0" v-for="(domain) in ruleForm.doless" :key="domain.key">
          <span class="spaninput">
            <el-input v-model="domain.attrName" size="small" placeholder="食材名"></el-input>
          </span>
          <span class="spaninput">
            <el-input v-model="domain.attrValue" size="small" placeholder="用量"></el-input>
          </span>
          <el-button @click.prevent="removeDomaintoless(domain)" size="mini">
            <i class="el-icon-minus"></i>
          </el-button>
          <el-button @click="addDomaintoless" size="mini">
            <i class="el-icon-plus"></i>
          </el-button>
        </el-form-item>

        <p>调料(可选)</p>
        <el-form-item label-width="0" v-for="(domain) in ruleForm.domore" :key="domain.key">
          <span class="spaninput">
            <el-input v-model="domain.attrName" size="small" placeholder="食材名"></el-input>
          </span>
          <span class="spaninput">
            <el-input v-model="domain.attrValue" size="small" placeholder="用量"></el-input>
          </span>
          <el-button @click.prevent="removeDomaintomore(domain)" size="mini">
            <i class="el-icon-minus"></i>
          </el-button>
          <el-button @click="addDomore" size="mini">
            <i class="el-icon-plus"></i>
          </el-button>
        </el-form-item>

        <!-- 做法步骤 -->
        <p>做法步骤(至少填写3项步骤)</p>
        <el-form-item
          label-width="0"
          v-for="(domain) in ruleForm.dozuofa"
          :key="domain.key"
          prop="dozuofa"
        >
          <span class="zuofa">
            <el-input
              v-model="domain.content"
              maxlength="50"
              :rows="5"
              placeholder="请输入说明内容/最多可以输入50字"
              show-word-limit
              type="textarea"
            ></el-input>
          </span>
          <el-button @click.prevent="removeDozuofa(domain)" size="mini">
            <i class="el-icon-minus"></i>
          </el-button>
          <el-button @click="addDazuofa" size="mini">
            <i class="el-icon-plus"></i>
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="rig">
      <div class="right">
        <!-- 一个下拉框展示专题活动名称进行选择 -->
        <p>发布属于你自己的菜谱把！！！</p>
        <p>参与专题活动</p>

        <el-button class="btn" type="danger" @click="submitForm(ruleForm,fileList)">发布菜谱</el-button>
      </div>
    </div>
  </div>
</template>

<script>
    import Upload from "./Upload";
    import axios from "axios";
    import PubSub from "pubsub-js";
    import { reqPolicyInfo } from "../../api";
    import { sendFood } from "../../api";

    export default {
        name: "From.vue",
        components: {
            Upload,
        },
        data() {
            /**
             * 数据
             */
            return {
                loading: false,
                items: 1,
                fileList: [],
                policyInfo: {},
                /**
                 * 表单数据
                 */
                ruleForm: {
                    /**
                     * 菜谱名称
                     */
                    text: "",
                    /**
                     * 菜谱说明
                     */
                    textarea: "",
                    /**
                     * 成品图数据
                     */
                    foodimages: [],
                    /**
                     * 主料数据
                     */
                    domains: [
                        {
                            attrName: "",
                            attrValue: "",
                        },
                    ],
                    /**
                     * 辅料料数据
                     */
                    doless: [
                        {
                            attrName: "",
                            attrValue: "",
                        },
                    ],
                    /**
                     * 其他数据
                     */
                    domore: [
                        {
                            attrName: "",
                            attrValue: "",
                        },
                    ],
                    /**
                     * 做法步骤
                     */
                    dozuofa: [
                        {
                            content: "",
                        },
                    ],
                },
            };
        },
        mounted() {
            PubSub.subscribe("updata", (msg, fileList) => {
                this.fileList = fileList;
                console.log(this.fileList.length);
            });
        },
        methods: {
            removeDozuofa(item) {
                var index = this.ruleForm.dozuofa.indexOf(item);
                if (index < 1) {
                    return;
                }
                if (index !== -1) {
                    this.ruleForm.dozuofa.splice(index, 1);
                }
            },
            removeDomaintomore(item) {
                var index = this.ruleForm.domore.indexOf(item);
                if (index < 1) {
                    return;
                }
                if (index !== -1) {
                    this.ruleForm.domore.splice(index, 1);
                }
            },
            removeDomain(item) {
                var index = this.ruleForm.domains.indexOf(item);
                if (index < 1) {
                    return;
                }
                if (index !== -1) {
                    this.ruleForm.domains.splice(index, 1);
                }
            },
            removeDomaintoless(item) {
                var index = this.ruleForm.doless.indexOf(item);
                if (index < 1) {
                    return;
                }
                if (index !== -1) {
                    this.ruleForm.doless.splice(index, 1);
                }
            },
            addDazuofa() {
                this.ruleForm.dozuofa.push({
                    content: "",
                });
            },
            addDomore() {
                this.ruleForm.domore.push({
                    attrName: "",
                    attrValue: "",
                });
            },

            addDomain() {
                this.ruleForm.domains.push({
                    attrName: "",
                    attrValue: "",
                });
            },

            addDomaintoless() {
                this.ruleForm.doless.push({
                    attrName: "",
                    attrValue: "",
                });
            },
            /**
             * 提交表单数据
             */
            submitForm(ruleFrom, fileList) {
                if (
                    fileList.length == 0 ||
                    ruleFrom.text.trim() == "" ||
                    ruleFrom.textarea.trim() == "" ||
                    ruleFrom.domains.length == 0 ||
                    ruleFrom.domains[0].attrName.trim() == "" ||
                    ruleFrom.dozuofa.length < 3 ||
                    ruleFrom.dozuofa[0].content.trim() == "" ||
                    ruleFrom.dozuofa[1].content.trim() == "" ||
                    ruleFrom.dozuofa[2].content.trim() == ""
                ) {
                    this.$message.error({
                        offset: 50,
                        message: "请完善信息"
                    });
                } else {
                    this.loading = true
                    reqPolicyInfo().then((res)=>{
                        this.policyInfo = res.data
                        console.log("pil",this.policyInfo)
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
                                            this.ruleForm.foodimages.push({
                                                imagesUrl: imageurl,
                                                type: num,
                                            });
                                            console.log("img", this.ruleForm);
                                            //发送数据给后台
                                            if (this.fileList.length === this.ruleForm.foodimages.length) {
                                                // let userInfos = JSON.parse(localStorage.getItem("userInfo"));
                                                const {
                                                    // userId,
                                                    title,
                                                    descrit,
                                                    foodimages,
                                                    domains,
                                                    doless,
                                                    domore,
                                                    foodSteps,
                                                } = this.ruleForm;
                                                const data = {
                                                    // userId,
                                                    title,
                                                    descrit,
                                                    foodimages,
                                                    domains,
                                                    doless,
                                                    domore,
                                                    foodSteps,
                                                };
                                                // data.userId = userInfos.id;
                                                data.title = this.ruleForm.text;
                                                data.descrit = this.ruleForm.textarea;
                                                data.foodSteps = this.ruleForm.dozuofa;
                                                console.log("data", data);
                                                sendFood(data)
                                                    .then((response) => {
                                                        const result = response;
                                                        if (result.code == 0) {
                                                            this.$alert('您的菜谱发布成功，需要2-3个工作日完成审核', '发布成功', {
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
                                    // this.$addr
                                    //   .post("food/food/foodinfo/save/toaud", data)
                                }
                            });
                        } else {
                            this.$message.warning({
                                offset: 50,
                                message: "当前没有合适图片可以上传"
                            });
                        }
                    })

                }
            },
        },
    }

</script>

<style lang="less" scoped>
  @pColor: #909399;
  .from {
    display: flex;
  }

  .main {
    width: 70%;
  }

  p {
    color: @pColor;
    font-size: 13px;
  }

  span {
    display: inline-block;
  }

  .spaninput {
    width: 300px;
  }

  .mes {
    color: #f56c6c;
    font-size: 12px;
  }

  .zuofa {
    width: 600px;
  }

  .rig{
    width: 30%;
    height: 200px;
    text-align: center;

  }
  .right {
    position: fixed;
    padding-left: 100px;
    p {
      color: #ff6767;
    }

    .btn {
      margin-top: 50px;
    }
  }
</style>
