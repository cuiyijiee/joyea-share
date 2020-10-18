<template>
  <div style="text-align: center" v-loading="uploadLoading">
    <div style="padding: 10px 150px 0 150px;">
      <el-upload ref="upload" action="upload" list-type="picture-card" multiple :auto-upload="false"
                 :on-change="handleUploadFileChange">
        <i slot="default" class="el-icon-plus"></i>
        <div slot="file" slot-scope="{file}">
          <div>
            <el-image style="width: 146px; height: 146px" fit="cover" :src="file.url">
              <div slot="error" class="image-slot">
                <div>无法提供预览图</div>
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
          </div>
          <span class="el-upload-list__item-actions">
            <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
              <i class="el-icon-zoom-in"></i>
            </span>
            <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
              <i class="el-icon-delete"></i>
            </span>
          </span>
        </div>
      </el-upload>
      <div v-for="(file,index) in fileList">
        <div style="text-align: left;margin: 15px 0 5px 0;">文件{{ index + 1 }}:【 {{ file.name }} 】审核说明:</div>
        <el-input v-model="file.joyeaDesc" placeholder="请输入素材内容、素材使用场景、素材使用说明等。"></el-input>
      </div>
      <el-dialog :visible.sync="dialogVisible">
        <el-image class="el-upload-list__item-thumbnail" fit="scale-down"
                  :src="dialogImageUrl">
          <div slot="error" class="image-slot">
            <i class="el-icon-picture-outline"></i>
          </div>
        </el-image>
      </el-dialog>
      <el-button type="primary" style="margin: 20px" @click="handleUpload">开始上传
        <i class="el-icon-upload el-icon--right"></i></el-button>
    </div>
  </div>
</template>

<script>
import {uploadFile} from "../../../api";

export default {
  name: "UploadIndex",
  data() {
    return {
      dialogImageUrl: '',
      dialogVisible: false,
      disabled: false,
      fileList: [],
      uploadLoading: false
    }
  },
  methods: {
    handleRemove(file) {
      this.$refs.upload.uploadFiles = this.$refs.upload.uploadFiles.filter(tmpFile => tmpFile.uid !== file.uid);
      this.fileList = this.$refs.upload.uploadFiles;
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleUploadFileChange(file, fileList) {
      this.fileList = fileList;
    },
    handleUpload() {
      if (this.$refs.upload.uploadFiles.length === 0) {
        this.$message.error("待上传文件列表为空！")
        return;
      }
      if (this.$refs.upload.uploadFiles.filter(file => {
        return !file.joyeaDesc || file.joyeaDesc.length === 0
      }).length > 0) {
        this.$message.error("请完善待上传文件的审核说明！")
        return;
      }
      this.uploadLoading = true;
      let toUploadFiles = this.$refs.upload.uploadFiles;
      const originToUploadFileSize = toUploadFiles.length;
      let successNum = 0;
      let doneNum = 0;
      this.$refs.upload.uploadFiles.forEach(file => {
        uploadFile(file.raw, file.joyeaDesc).then(resp => {
          toUploadFiles = toUploadFiles.filter(tmpFile => {
            return tmpFile.name !== resp.fileName;
          });
          successNum++;
        }).finally(() => {
          doneNum++;
          if (doneNum >= originToUploadFileSize) {
            this.uploadLoading = false;
            this.$alert(`上传完毕,成功上传${successNum}个文件!`, '上传提示', {
              confirmButtonText: '确定',
              callback: action => {
                this.$refs.upload.uploadFiles = toUploadFiles;
                this.fileList = toUploadFiles;
              }
            });
          }
        });
      })
    }
  },
  created() {

  }
}
</script>

<style scoped>

</style>