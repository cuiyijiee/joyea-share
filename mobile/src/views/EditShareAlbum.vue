<template>
  <div style="padding: 10px">
    <van-steps :active="active">
      <van-step>选清单</van-step>
      <van-step>配封面</van-step>
      <van-step>打标签</van-step>
    </van-steps>
    <div v-if="active === 0">
      <van-empty v-if="myAlbumList.length === 0" class="custom-image"
                 image="https://img.yzcdn.cn/vant/custom-empty-image.png" description="您暂时没有清单"/>
      <div v-else>
        <van-cell v-for="(item,index) in myAlbumList" :title="item.albumName">
          <template #right-icon>
            <!--                        <van-tag v-if="item.shared" style="margin-right: 20px" type="danger">已分享</van-tag>-->
            <van-button :type="item.shared ? 'danger':'primary'" @click="handleSelectShareAlbum(item)">
              {{ item.shared ? "取消分享" : "编辑分享" }}
            </van-button>
          </template>
        </van-cell>
      </div>
    </div>
    <div v-else-if="active===1">

      <van-divider content-position="left">选择封面</van-divider>
      <van-empty
          v-if="this.editAlbum.srcList.filter(item => {return item.local || item.srcType.startsWith('image');}).length === 0"
          class="custom-image"
          image="https://img.yzcdn.cn/vant/custom-empty-image.png" description="该清单中暂没有图片可做封面"/>
      <van-grid :column-num="3">
        <van-grid-item
            v-for="(item,index) in this.editAlbum.srcList.filter(item => {return item.local || item.srcType.startsWith('image');})"
            :key="item.id" style="padding: 10px" @click="handleSelectCover(item)">
          <div v-show="!(index >= 6 && onlySixPic)">
            <img style="width: 100%;max-height: 500px" v-if="item.local" :src="item.url">
            <img style="width: 100%;max-height: 500px" v-else
                 :src="genPreviewUrl(item.srcNeid,item.srcHash,item.srcRev,item.srcType)">
            <div style="position: absolute;right: 5px;bottom: 5px"
                 v-if="item.local && item.value === selectedLocalCoverId">
              <van-icon name="success" color="red"/>
            </div>
            <div style="position: absolute;right: 5px;bottom: 5px"
                 v-else-if="item.srcNeid === selectedCoverNeid">
              <van-icon name="success" color="red"/>
            </div>
          </div>
        </van-grid-item>
      </van-grid>
      <van-button type="primary" block
                  v-show="onlySixPic && this.editAlbum.srcList.filter(item => {return item.local || item.srcType.startsWith('image');}).length > 6"
                  @click="onlySixPic = false">显示全部封面
      </van-button>

      <van-divider content-position="left">修改信息</van-divider>

      <van-cell-group>
        <template #title>
          <div style="color: black;font-size: 16px">清单名称</div>
        </template>
        <van-field v-model="editAlbum.albumName" type="textarea" placeholder="请填写清单名称"/>
        <div style="padding:2px 15px;color: #8c939d;font-size: 0.5em">
          tips:时间+客户+出差/接待+产品<br>
          例:0829出差扬子江交流蓝芩口服液项目
        </div>
      </van-cell-group>
      <br>
      <van-cell-group>
        <template #title>
          <div style="color: black;font-size: 16px">清单简介</div>
        </template>
        <van-field v-model="editAlbum.shareDesc" rows="2"
                   placeholder="请填写清单简介"
                   type="textarea" maxlength="200" show-word-limit/>
        <div style="padding:2px 15px;color: #8c939d;font-size: 0.5em">
          tips:项目所处状态、背景、素材使用的目的<br>
          例:目前客户还在考虑四边封，了解到参与的同行有龙威，本次出差的目的是为了让客户掌握stick对比四边封的优势，同时给客户stick的标尺<br>
        </div>
      </van-cell-group>

      <van-button type="danger" block @click="active = 0">上一步</van-button>
      <div style="margin: 5px"></div>
      <van-button type="primary" block @click="handleCompleteStepTwo()">下一步</van-button>
    </div>
    <div v-else-if="active===2">
      <van-cell-group>
        <van-cell title="行业">
          <template #right-icon>
            <van-dropdown-menu>
              <van-dropdown-item v-model="selectedHangyeId" :options="hangyeOption"/>
            </van-dropdown-menu>
          </template>
        </van-cell>
        <van-cell title="剂型">
          <template #right-icon>
            <van-dropdown-menu>
              <van-dropdown-item v-model="selectedJixingId" :options="jixingOption"/>
            </van-dropdown-menu>
          </template>
        </van-cell>
        <van-cell title="线别">
          <template #right-icon>
            <van-dropdown-menu>
              <van-dropdown-item v-model="selectedXianbieId" :options="xianbieOption"/>
            </van-dropdown-menu>
          </template>
        </van-cell>
        <van-cell title="阶段">
          <template #right-icon>
            <van-dropdown-menu>
              <van-dropdown-item v-model="selectedJieduanId" :options="jieduanOption"/>
            </van-dropdown-menu>
          </template>
        </van-cell>
        <van-cell title="市场">
          <template #right-icon>
            <van-dropdown-menu>
              <van-dropdown-item v-model="selectedShichangId" :options="shichangOption"/>
            </van-dropdown-menu>
          </template>
        </van-cell>
      </van-cell-group>
      <van-button type="danger" block @click="active = 1">上一步</van-button>
      <div style="margin: 5px"></div>
      <van-button type="primary" block @click="handleShareAlbum">立即分享</van-button>
    </div>
  </div>
</template>

<script>
import {listMineAlbum, switchShare} from "../api";
import {hangyeOption, jixingOption, xianbieOption, jieduanOption, shichangOption} from "../util/JoyeaTagUtil";
import {genSrcPreviewSrc} from "../util/JoyeaUtil";
import {mapGetters} from "vuex";
import {albumCoverOption} from "../util/JoyeaTagUtil";

export default {
  name: "EditShareAlbum",
  data() {
    return {
      collapseActiveName: '2',

      selectedHangyeId: -1,
      selectedXianbieId: -1,
      selectedJixingId: -1,
      selectedJieduanId: -1,
      selectedShichangId: -1,

      onlySixPic: true,

      hangyeOption: hangyeOption,
      xianbieOption: xianbieOption,
      jixingOption: jixingOption,
      jieduanOption: jieduanOption,
      shichangOption: shichangOption,

      prepareCover: [],

      active: 0,
      myAlbumList: [],

      selectedCoverNeid: "",
      selectedLocalCoverId: 0,
      editAlbum: {},

      albumCoverOption: albumCoverOption
    }
  },
  methods: {
    genPreviewUrl(srcNeid, srcHash, srcRev, srcType) {
      let previewType = 'pic';    // if video is av
      if (srcType.startsWith("doc")) {
        previewType = 'doc'
      } else if (srcType.startsWith("video")) {
        previewType = 'av'
      }
      return genSrcPreviewSrc(srcNeid, srcHash, srcRev, previewType, this.userInfo.session);
    },
    handleListMineAlbum() {
      listMineAlbum().then(resp => {
        this.myAlbumList = resp.data;
        let toEditAlbumId = this.$route.params.albumId;
        if (toEditAlbumId) {
          let editItem = this.myAlbumList.find(item => item.albumId === toEditAlbumId);
          if (editItem) {
            this.handleSelectShareAlbum(editItem);
          }
        }
      })
    },
    handleSelectCover(item) {
      if (item.local) {
        this.selectedCoverNeid = "";
        this.selectedLocalCoverId = item.value;
      } else {
        this.selectedLocalCoverId = 0;
        this.selectedCoverNeid = item.srcNeid;
      }
    },
    handleCompleteStepTwo() {
      if (this.selectedCoverNeid === "" && this.selectedLocalCoverId === 0) {
        this.$notify({
          type: "danger", message: "请先选择封面!"
        })
        return;
      } else if (this.editAlbum.shareDesc.length === 0 || this.editAlbum.albumName.length === 0) {
        this.$notify({
          type: "danger", message: "请填写清单名称和简介!"
        })
        return;
      }
      this.active = 2;
    },
    handleSelectShareAlbum(toHandleAlbum) {
      if (toHandleAlbum.shared) {
        switchShare(toHandleAlbum.albumId, false).then(resp => {
          if (resp.data) {
            this.$notify({
              type: "success",
              message: "取消分享成功！"
            })
            toHandleAlbum.shared = false;
          }
        })
      } else {
        if (!toHandleAlbum.shareDesc) {
          toHandleAlbum.shareDesc = "";
        }

        this.albumCoverOption.forEach(item => {
          item.local = true;
          toHandleAlbum.srcList.push(item);
        })

        console.log(toHandleAlbum);

        this.editAlbum = toHandleAlbum;

        this.selectedCoverNeid = "";
        this.active = 1;
      }
    },
    handleShareAlbum() {
      if (this.selectedHangyeId === -1 || this.selectedXianbieId === -1 || this.selectedJixingId === -1
          || this.selectedJieduanId === -1 || this.selectedShichangId === -1) {
        this.$notify({
          type: "danger",
          message: "请打完标签再分享哦!"
        })
        return;
      }
      switchShare(
          this.editAlbum.albumId, true, this.editAlbum.albumName,
          this.selectedCoverNeid, this.selectedLocalCoverId, this.editAlbum.shareDesc,
          this.selectedHangyeId,
          this.selectedXianbieId,
          this.selectedJixingId,
          this.selectedJieduanId,
          this.selectedShichangId
      )
          .then(resp => {
            if (resp.data) {
              this.$notify({
                type: "success",
                message: "分享成功！"
              });
              this.$router.back();
            }
          })
    },
    handleResetForm() {
      this.active = 0;
      this.editAlbum = {};
    },
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  created() {
    this.handleResetForm();
    this.handleListMineAlbum();
  }
}
</script>

<style scoped>
</style>