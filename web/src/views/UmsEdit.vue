<template>
    <div class="edit">
      <div style="color: #ff6767;font-size: 20px;height: 40px;border-bottom: 1px solid #99a9bf">个人资料</div>
      <div class="main">
        <p>头像</p>
        <el-avatar v-if="user.header==null" shape="square" :size="120" src="https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png"></el-avatar>
        <el-avatar v-if="user.header!=null" shape="square" :size="120" :src="user.header"></el-avatar>
        <p>选择头像</p>
        <el-upload
          ref="upload"
          action
          :on-change="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false"
          :multiple="true"
          :limit="1"
          :on-exceed="fileExceed"
          accept=".jpg, .jpeg, .png, .JPG, .JPEG"
          list-type="picture-card"
        >
          <i slot="default" class="el-icon-plus"></i>
        </el-upload>
        <p>昵称</p>
        <el-input v-model="from.nickName" placeholder="请输入昵称" maxlength="10" show-word-limit style="width: 300px;"></el-input>
      </div>
      <el-button type="danger" style="width: 200px;margin-top: 30px">保存修改</el-button>
    </div>
</template>

<script>
    export default {
        name: "UmsEdit",
        data() {
            return {
                user: {
                    id: '',
                    username: '',
                    nickname: '',
                    header: ''
                },
                fileList: [],
                from: {
                    nickname: '',
                    fileimages: ''
                }
            };
        },
        created() {
            if (this.$store.getters.getUser) {
                this.user.id = this.$store.getters.getUser.id
                this.user.username = this.$store.getters.getUser.username
                this.user.nickname = this.$store.getters.getUser.nickname
                this.user.header = this.$store.getters.getUser.header
                this.hasLogin = true
            }
        },
        methods: {
            handleRemove(file, fileList) {
                this.fileList = fileList;
            },

            handlePreview(file, fileList) {
                let that = this;
                if (file.raw.size > 1024 * 1024 * 2) {
                    that.$message.error({
                        message:"上传文件大小不能超过 2MB!",
                        offset: 50,
                    });
                    return;
                }
                that.fileList = fileList;
            },

            fileExceed: function(files, fileList) {
                this.$message.warning(
                    {message: `当前限制选择 1 个文件，本次选择了 ${
                            files.length
                        } 个文件，共选择了 ${files.length + fileList.length} 个文件`,
                      offset: 50
                    }

                );
            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            }
        }

    }
</script>

<style scoped>
  .main{
    margin-top: 30px;
  }
  p{
    font-size: 13px;
    color: #666666;
    margin-bottom: 10px;
    margin-top: 10px;
  }
</style>
