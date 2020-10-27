<template>
  <el-upload
    ref="upload"
    action
    :on-change="handlePreview"
    :on-remove="handleRemove"
    :file-list="fileList"
    :auto-upload="false"
    :multiple="true"
    :limit="3"
    :on-exceed="fileExceed"
    accept=".jpg, .jpeg, .png, .JPG, .JPEG"
    list-type="picture-card"
  >
    <i slot="default" class="el-icon-plus"></i>

  </el-upload>
</template>

<script>
    import PubSub from "pubsub-js";
    export default {
        name: "Upload",
        data() {
            return {
                fileList: []
            };
        },

        watch:{
            fileList(){
                //console.log("----------",this.fileList);
                PubSub.publish("updata", this.fileList);
            }
        },
        methods: {
            handleRemove(file, fileList) {
                this.fileList = fileList;
            },

            handlePreview(file, fileList) {
                let that = this;
                if (file.raw.size > 1024 * 1024 * 2) {
                    that.$message.error("上传文件大小不能超过 2MB!");
                    return;
                }
                that.fileList = fileList;
            },

            fileExceed: function(files, fileList) {
                this.$message.warning(
                    `当前限制选择 3 个文件，本次选择了 ${
                        files.length
                    } 个文件，共选择了 ${files.length + fileList.length} 个文件`
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

</style>
