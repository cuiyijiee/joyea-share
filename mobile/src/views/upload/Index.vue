<template>
  <div>
    <van-field label="上传路径" placeholder="点击选择" rows="1" autosize
               :value="uploadPath" readonly type="textarea"
               @click="pathSelectorVisible = true"/>
    <van-cell is-link>
      <!-- 使用 title 插槽来自定义标题 -->
      <template #title>
        <span class="custom-title">选择上传文件</span>
      </template>
      <!-- 使用 right-icon 插槽来自定义右侧图标 -->
      <template #right-icon>
        <van-uploader v-model="filePreviewList" :max-count="1" accept="*" :after-read="afterRead">
        </van-uploader>
      </template>
    </van-cell>
    <van-field label="文件名" placeholder="点击选择" v-model="uploadFileName"/>
    <lenovo-dir-selector :visible="pathSelectorVisible" @selectPath="handleSelectPath">
    </lenovo-dir-selector>
    <van-button type="info" round block style="margin-top: 100px" @click="handleClickUpload">上传</van-button>
  </div>
</template>

<script>
import {authUpload, realUpload} from "@/api/lenovo";
import {addUploadRecord} from "@/api";
import {mapGetters, mapState} from "vuex";
import LenovoDirSelector from "@/components/LenovoDirSelector";
import {genSrcPreviewSrc} from "@/util/JoyeaUtil";
import {Toast} from "vant";

export default {
  name: "Index",
  components: {LenovoDirSelector},
  data() {
    return {
      pathSelectorVisible: false,
      uploadPath: "",
      uploadPathNeid: "",
      filePreviewList: [],
      uploadFile: {},
      uploadFileName: "",
      preUploadLoading: false
    }
  },
  methods: {
    onSubmit() {

    },
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
      this.uploadFile = file.file;
      this.uploadFileName = this.uploadFile.name;
    },
    handleClickUpload() {
      if (this.uploadPath.length <= 0 || this.uploadPathNeid.length <= 0 || this.uploadFileName.length <= 0) {
        this.$notify("请指定和上传路径和文件！")
        return;
      }
      const toast = Toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
        message: '拼命上传中...',
      });
      authUpload(this.uploadFile.size, this.userInfo.session).then(resp => {
        let realUploadDomain = resp.region;
        realUpload(realUploadDomain, this.uploadFile, this.uploadFileName, this.userInfo.session).then(resp => {
          this.preUploadLoading = false;
          addUploadRecord(this.userInfo.email, this.uploadFileName, resp.neid, resp.mime_type, resp.hash, resp.rev,
              this.uploadPath, this.uploadPathNeid, []).then(resp => {
            toast.clear();
            this.$dialog.alert({
              confirmButtonText: "继续上传",
              cancelButtonText: "返回",
              showCancelButton: true,
              message: '上传成功，管理员审核后方可被使用！',
              theme: 'round-button',
            }).then(() => {
              this.uploadPath = "";
              this.uploadPathNeid = "";
              this.filePreviewList = [];
              this.uploadFile = {};
              this.uploadFileName = "";
            }).catch(() => {
              this.$router.back();
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