<template>
  <div id="yesterday-upload">
    <van-card
        v-for="item in yesterdayUploadRecordList">
      <template #thumb>
        <van-image width="80" height="80" v-if="item.srcType.startsWith('image')" :src="getPreviewUrl(item)"/>
        <van-image v-else width="80" height="80" :src="getDocumentImage(item.srcType)"></van-image>
      </template>
      <template #tags>
        <div>上传人:{{ item.uploaderName }}</div>
        <div v-if="item.checkedAt && item.checked">
          上传至:{{ item.uploadPath.replace("/营销素材展示", "首页") + "/" + item.srcName }}
        </div>
        <div>文件说明:{{ item.srcDesc }}</div>
        <div>上传时间:{{ item.createdAt }}</div>
      </template>
      <template #footer>
        <van-button icon="plus" size="small" type="danger" plain style="border: #ffffff;background: #fafafa"
                    @click="handleAddToOrderList(item)"/>
      </template>
    </van-card>
  </div>
</template>

<script>
import {yesterdayUpload} from "@/api";
import {genSrcPreviewSrc, getDocumentImage} from "@/util/JoyeaUtil";
import {mapActions} from "vuex";
import {convertItem} from "@/util/ImageViewUtil";

export default {
  name: "YesterdayUploadContainer",
  data() {
    return {
      yesterdayUploadRecordList: []
    }
  },
  methods: {
    getDocumentImage,
    ...mapActions([
      'clearFunc', 'addFunc', 'setOrderEditInfoFunc'
    ]),
    handleGetYesterdayUpload() {
      yesterdayUpload().then(resp => {
        this.yesterdayUploadRecordList = resp.data;
      })
    },
    handleAddToOrderList(item) {
      //this.clearFunc();
      item.srcPath = item.uploadPath + "/" + item.srcName;
      //console.log(JSON.stringify(convertItem(item)));
      this.addFunc(convertItem(item));
    },
    getPreviewUrl(item) {
      let previewType = 'pic';    // if video is av
      if (item.srcType.startsWith("doc")) {
        previewType = 'doc'
      } else if (item.srcType.startsWith("video")) {
        previewType = 'av'
      }
      return genSrcPreviewSrc(item.srcNeid, item.srcHash, item.srcRev, previewType, this.userInfo.session);
    },
  },
  created() {
    this.handleGetYesterdayUpload();
  }
}
</script>

<style scoped>
#yesterday-upload {
  margin: 5px 0 15px 0;
}
</style>