<template>
  <div id="order">
    <van-empty v-if="orderList.length === 0" description="素材车空空如也"/>
    <div style="margin-bottom: 100px">
      <van-swipe-cell v-for="item in orderList" :key="item.path">
        <van-card @click="handleClickOrderItem(item)"
                  :desc="item.joyeaDesc !== undefined && item.joyeaDesc.length > 0 ? item.joyeaDesc :'暂未设置解说词'"
                  :title="item.path.substr(item.path.lastIndexOf('/')+1)"
                  :thumb="item.mime_type.startsWith('image') ? genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type):handleGetDocumentImage(item.mime_type)"/>
        <template #right>
          <van-button square text="删除" type="danger" class="delete-button"
                      @click="handleDeleteOrderList(item)"/>
        </template>
      </van-swipe-cell>
    </div>
    <!-- 底部占位 -->
    <van-submit-bar style="margin-bottom: 50px" :button-text="getOrderEditInfo.name ? '重新保存':'提交清单'"
                    :safe-area-inset-bottom="false"
                    @submit="handleSaveList">
      <div style="padding:0 20px;position: absolute;left: 0" v-if="editMode">
        <div>
          正在编辑:{{
            getOrderEditInfo.name.length < 10 ? getOrderEditInfo.name :
                getOrderEditInfo.name.substr(0, 10) + "..."
          }}
        </div>
        <div>
          <van-button type="primary" @click="handleExitEditMode" size="small">退出编辑</van-button>
        </div>
      </div>
      <div style="margin-right: 5px">{{ "合计：" + this.$store.getters.getStateOrderList.length + "个" }}
      </div>
    </van-submit-bar>
    <van-dialog v-model="submitDialogVisible" :title="editMode ? '确认保存编辑后新清单？':'确认提交新清单？'"
                @cancel="handleCancelSubmit"
                @confirm="handleSubmitNewAlbum" show-cancel-button>
      <van-field v-model="newAlbumName" label="清单名称" placeholder="请输入清单名"/>
    </van-dialog>
    <van-action-sheet v-model="orderItemEdit.visible" title="编辑">
      <van-field label="文件名" :value="orderItemEdit.item.path.substr(orderItemEdit.item.path.lastIndexOf('/') + 1)"
                 disabled/>
      <van-field
          v-model="orderItemEdit.item.joyeaDesc" rows="2"
          label="解说词" type="textarea"
          right-icon="edit"
          maxlength="50" placeholder="请输入解说词" show-word-limit
      />
      <van-button type="warning" round block @click="handleFinishEdit(item)">确认修改</van-button>
    </van-action-sheet>
  </div>
</template>

<script>

import {mapState, mapActions, mapGetters} from "vuex"
import {genSrcPreviewSrc} from "../../util/JoyeaUtil";
import api from "../../api";
import {getDocumentImage} from '../../util/JoyeaUtil';
import {createAlbum} from "../../api/"

export default {
  name: "OrderContainer",
  data() {
    return {
      newAlbumName: "",
      submitDialogVisible: false,
      orderItemEdit: {
        visible: false,
        item: {
          path: ""
        }
      }
    }
  },
  methods: {
    ...mapActions([
      'clearFunc',
      'removeFunc',
      'setOrderEditInfoFunc'
    ]),
    handleGetDocumentImage(mimeType) {
      return getDocumentImage(mimeType)
    },
    handleClickOrderItem(item) {
      this.orderItemEdit.item = item;
      this.orderItemEdit.visible = true;
    },
    handleDeleteOrderList(item) {
      this.removeFunc(item.neid);
    },
    handleSaveList() {
      if (this.orderList.length === 0) {
        this.$notify({type: 'warning', message: '清单列表为空！'});
        return;
      }
      this.newAlbumName = this.editMode ? this.getOrderEditInfo.name : '';
      this.submitDialogVisible = true;
    },
    handleExitEditMode() {
      this.$dialog.confirm({
        message: "确定退出编辑清单模式？"
      }).then(() => {
        this.setOrderEditInfoFunc({}).then(() => {
        });
      })
    },
    handleCancelSubmit() {
      this.newAlbumName = "";
    },
    handleFinishEdit(item) {
      this.orderItemEdit.visible = false;
    },
    handleSubmitNewAlbum() {
      if (this.newAlbumName.length === 0) {
        this.$notify({type: 'warning', message: '清单名称不能为空，请先设置！'});
        return;
      }
      if(!this.editMode ){  //创建新的清单
        createAlbum(this.newAlbumName,this.orderList).then(resp => {
          if(resp.code === 2000){
            this.$notify({type: 'success', message: '保存成功'});
            this.$store.dispatch("clearFunc");
            this.setOrderEditInfoFunc({})
          }else{
            this.$notify({type: 'warning', message: '保存失败'});
          }
        })
      }else{  //保存编辑之后的清单
        api({
          action: 'album',
          method: this.editMode ? 'reSave' : 'save',
          id: this.getOrderEditInfo.id,
          name: this.newAlbumName,
          src: this.orderList
        }).then(response => {
          if (response.result) {
            this.$notify({type: 'success', message: this.editMode ? '重新编辑成功' : '保存成功'});
            this.$store.dispatch("clearFunc");
            this.setOrderEditInfoFunc({}).then(() => {

            })
          } else {
            this.$notify({type: 'warning', message: this.editMode ? '重新编辑失败' : '保存失败'});
          }
        });
      }
    },
    genPreviewUrl(neid, hash, rev, mime_type) {
      let previewType = 'pic';    // if video is av
      if (mime_type.startsWith("doc")) {
        previewType = 'doc'
      } else if (mime_type.startsWith("video")) {
        previewType = 'av'
      }
      return genSrcPreviewSrc(neid, hash, rev, previewType, this.userInfo.session);
    }
  },
  computed: {
    ...mapState({
      orderList: state => state.orderList
    }),
    ...mapGetters([
      'userInfo', 'getOrderEditInfo'
    ]),
    editMode: function () {
      return this.getOrderEditInfo.name && this.getOrderEditInfo.name.length > 0;
    }
  },
  activated() {
  },
}
</script>

<style scoped>
.delete-button {
  height: 100%;
}
</style>