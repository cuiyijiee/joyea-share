<template>
  <div>
    <van-card
        v-for="item in recordList"
        :desc="item.uploadPath"
        :title="item.srcName"
        :thumb="getPreviewUrl(item)"
    >
      <template #tags>
        <van-tag type="primary" v-if="!item.checkedAt">待审核</van-tag>
        <van-tag type="success" v-if="item.checkedAt && item.checked">通过</van-tag>
        <van-tag type="danger" v-if="item.checkedAt && !item.checked">未通过</van-tag>
        <div v-if="item.checkedAt && !item.checked">审核意见:{{item.refuseReason}}</div>
      </template>
    </van-card>
  </div>
</template>

<script>

import {getMyUploadRecord} from "@/api";
import {genSrcPreviewSrc} from "@/util/JoyeaUtil";
import {mapGetters} from "vuex";

export default {

  name: "MyUpload",
  data() {
    return {
      recordList: []
    }
  },
  methods: {
    getMyUploadRecord() {
      getMyUploadRecord().then(resp => {
        console.log(resp)
        this.recordList = resp.data;
      })
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
  computed: {
    ...mapGetters([
      'userInfo'
    ]),
  },
  created() {
    this.getMyUploadRecord()
  }
}
</script>

<style scoped>

</style>