<template>
  <div style="padding: 10px 150px 0 150px;">
    <h1 v-if="recordList.length === 0">暂时没有需要审核的上传文件！</h1>
    <el-row>
      <el-col :span="6" v-for="(record, index) in recordList" :key="index">
        <el-card style="margin: 20px">
          <el-image v-if="record.srcType.startsWith('image')"
                    :src="genSrcPreviewSrc(record.srcNeid,record.srcHash,record.srcRev,'pic',userInfo.session)"
                    class="image" style="height: 400px" fit="contain"></el-image>
          <el-image v-else :src="getDocumentImage(record.srcType)" class="image" style="height: 400px"
                    fit="contain"></el-image>
          <div style="padding: 14px;">
            <span></span>
            <div>
              <div>上传到路径: {{ record.uploadPath.replace("/营销素材展示","首页") }}</div>
              <div>文件名:{{ record.srcName }}</div>
              <div>上传者:{{ record.uploaderName }}</div>
              <time class="time">{{ record.createdAt }}</time>
            </div>
            <div class="bottom clearfix">
              <el-button type="danger" @click="handleRefuseUpload(record)">不通过</el-button>
              <el-button type="primary" @click="handleAllowUpload(record)">通过</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>

import {findUploadRecord, uploadRecordManage} from "../../api"
import genSrcPreviewSrc from "../../utils";
import {mapGetters} from "vuex";
import {getDocumentImage} from "../../utils/JoyeaUtil";
import {moveFile} from "../../api/lenovo";

export default {
  name: "UploadManage",
  data() {
    return {
      recordList: []
    }
  },
  computed: {
    ...mapGetters({
      'userInfo': 'userInfo/userInfo'
    })
  },
  methods: {
    genSrcPreviewSrc,
    getDocumentImage,
    handleAllowUpload(record) {
      uploadRecordManage(record.id, true).then(resp => {
        if (resp.data) {
          this.$message.success("审核成功！");
          this.getUnCheckRecord();
        }
      })
    },
    handleRefuseUpload(record) {
      this.$prompt('给点建议吧！', '拒绝提示', {
        confirmButtonText: '拒绝',
        cancelButtonText: '我再想想',
      }).then(({value}) => {
        uploadRecordManage(record.id, false, value).then(resp => {
          if (resp.data) {
            this.$message.success("审核成功！");
            this.getUnCheckRecord();
          }
        })
      });
    },
    getUnCheckRecord() {
      findUploadRecord(1, 9999).then(resp => {
        resp.data.record.map(record => {
          record.uploaderName = resp.data.user[record.uploader];
        })
        this.recordList = resp.data.record;
      })
    }
  },
  created() {
    this.getUnCheckRecord();
  }
}
</script>

<style scoped>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>