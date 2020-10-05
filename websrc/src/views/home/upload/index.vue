<template>
  <div style="text-align: center">
    <div style="padding: 10px 150px 0 150px;">
      <el-upload ref="upload" action="upload" list-type="picture-card" multiple :auto-upload="false"
                 :on-change="handleUploadFileChange">
        <i slot="default" class="el-icon-plus"></i>
        <div slot="file" slot-scope="{file}">
          <el-image style="width: 146px; height: 146px" fit="cover" :src="file.url">
            <div slot="error" class="image-slot">
              <i class="el-icon-picture-outline"></i>
            </div>
          </el-image>
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
        <el-divider content-position="left">文件{{ index + 1 }}审核说明:</el-divider>
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
    }
  },
  methods: {
    handleRemove(file) {
      this.$refs.upload.uploadFiles = this.$refs.upload.uploadFiles.filter(tmpFile => tmpFile.uid !== file.uid);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    handleUploadFileChange(file, fileList) {
      this.fileList = fileList;
    },
    handleUpload() {
      let toUploadFiles = this.$refs.upload.uploadFiles;
      this.$refs.upload.uploadFiles.forEach(file => {
        uploadFile(file.raw, file.joyeaDesc).then(resp => {
          toUploadFiles = toUploadFiles.filter(tmpFile => {
            return tmpFile.name !== resp.fileName;
          });
          if (toUploadFiles.length === 0) {
            this.$alert('上传完毕!', '上传提示', {
              confirmButtonText: '确定',
              callback: action => {
                this.$refs.upload.uploadFiles = [];
              }
            });
          }
        })
      })
    }
  },
  created() {

  }
}
</script>

<style scoped>

</style>