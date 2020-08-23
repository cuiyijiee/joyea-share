<template>
  <div>
    <div style="margin: 10px;color: #1f1731;">请选择要上传的文件(最多可选9个文件)</div>
    <van-uploader v-model="filePreviewList" multiple accept="*" max-count="9" :after-read="afterRead">
    </van-uploader>
    <van-cell-group>
      <van-field v-for="(file,index) in filePreviewList" v-model="file.desc" :label="'文件 ' + (index + 1) + ' 的说明:'"
                 placeholder="请输入说明"/>
    </van-cell-group>
    <van-button type="info" round block style="margin-top: 100px" @click="handleClickUpload">开始上传</van-button>
  </div>
</template>

<script>
import {authUpload, realUpload} from "@/api/lenovo";
import {addUploadRecord} from "@/api";
import {mapGetters} from "vuex";
import LenovoDirSelector from "@/components/LenovoDirSelector";
import {genSrcPreviewSrc, replaceFileName} from "@/util/JoyeaUtil";
import {Toast} from "vant";

export default {
  name: "Index",
  components: {LenovoDirSelector},
  data() {
    return {
      pathSelectorVisible: false,
      uploadPath: "",
      uploadPathNeid: "",
      uploadFile: {},
      uploadFileName: "",
      preUploadLoading: false,
      filePreviewList: [],
    }
  },
  created() {
  },
  methods: {
    handleSelectPath(path) {
      this.uploadPath = "/" + path.path.join("/");
      this.uploadPathNeid = path.neid;
      this.pathSelectorVisible = false;
    },
    getPreviewUrl(item) {
      let previewType = 'pic';    // if video is av
      if (item.mime_type.startsWith("doc")) {
        previewType = 'doc'
      } else if (item.mime_type.startsWith("video")) {
        previewType = 'av'
      }
      return genSrcPreviewSrc(item.neid, item.hash, item.rev, previewType, this.userInfo.session);
    },
    afterRead(file) {
      this.filePreviewList.forEach(file => {
        file.tempFileName = replaceFileName(file.file.name);
      })
    },
    handleClickUpload() {
      const toast = Toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
        message: '拼命上传中...',
      });
      let toUploadNum = this.filePreviewList.length;
      let uploadProgress = 0;
      this.filePreviewList.forEach(file => {
        authUpload(file.file.size, this.userInfo.session).then(resp => {
          let realUploadDomain = resp.region;
          file.status = 'uploading';
          file.message = '上传中...';
          realUpload(realUploadDomain, file.file, file.tempFileName, this.userInfo.session).then(resp => {
            addUploadRecord(this.userInfo.email, file.tempFileName, resp.neid, resp.mime_type, resp.hash, resp.rev, file.desc, []).then(resp => {
              file.status = 'done';
              file.message = '上传完成';
              uploadProgress++;
              if (uploadProgress === toUploadNum) {
                toast.clear();
                this.$dialog.alert({
                  confirmButtonText: "继续上传",
                  cancelButtonText: "返回",
                  showCancelButton: true,
                  message: toUploadNum + '个资源已经上传成功，管理员审核后方可被使用！',
                  theme: 'round-button',
                }).then(() => {
                  this.filePreviewList = [];
                }).catch(() => {
                  this.$router.back();
                })
              }
            })
          })
        })
      })
    }
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ]),
  },
}
</script>

<style scoped>

</style>