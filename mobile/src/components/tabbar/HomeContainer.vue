<template>
  <div id="home" style="background-color: #f7f8fa;">
    <vm-back-top :bottom="100" :duration="1000" :timing="'ease'"></vm-back-top>
    <van-search v-model="searchKey" placeholder="请输入搜索要搜索的资源"
                background="#1f1731" shape="round"
                @search="onSearch" :show-action="canGoBackSearch">
      <template #action>
        <van-icon name="exchange" @click="goBackSearch" style="margin-top: 3px" size="20"/>
      </template>
    </van-search>
    <div>
      <span style="font-size: 10px;color: #444444">热门搜索:</span>
      <van-tag v-for="item in topSearchKey" style="margin: 5px 2px" @click="handleClickTopKey(item)">{{ item }}
      </van-tag>
    </div>
    <div v-if="dir.currentPath.length === 1 && dir.currentPath[0] === '装调素材'" style="margin-bottom: 50px">
      <van-grid :column-num="2">
        <van-grid-item class="my_van-grid-item" v-for="menu in menuPath"
                       @click="handleClickRootMenu(menu.path)">
          <div class="menu-content">
            <van-image :src="menu.icon" fit="contain"
                       style="width: 50px;height: 50px;padding: 30px 20px 10px 20px"/>
            <div style="padding: 20px;height: 30px;font-size: 12px">{{ menu.name }}</div>
          </div>
        </van-grid-item>
      </van-grid>
    </div>
    <div v-else>
      <van-sticky :offset-top="46">
        <van-row style="background-color: #fff;padding: 8px 0">
          <van-col span="3">
            <van-icon class="my_icon" name="arrow-left"
                      style="padding: 5px 10px 5px 10px"
                      v-if="dir.currentPath.length !== 0"
                      @click="handleClickBackDir" size="20"/>
          </van-col>
          <van-col span="18">
            <div style="margin-top: 2px">
              {{ dir.currentPath.length === 0 ? "/" : dir.currentPath[dir.currentPath.length - 1] }}
            </div>
          </van-col>
          <van-col span="3">
            <van-icon class="my_icon" name="wap-home-o" @click="handleClickRootDir"
                      style="padding: 5px 10px 5px 10px"
                      v-if="dir.currentPath.length !== 0" size="20"/>
          </van-col>
        </van-row>
      </van-sticky>
      <van-tabs v-model="currentTypeActive" sticky style="margin-bottom: 50px">
        <van-tab title="全部">
          <van-empty v-if="dir.tableData.length === 0" description="当前路径没有文件"/>
          <van-list>
            <van-cell v-for="item in dir.tableData" @click="handleClickItem(item)" :key="item.path">
              <van-row>
                <van-col span="4">
                  <van-icon size="30" v-if="item['is_dir']" class="my_icon" name="credit-pay"/>
                  <van-icon size="30" v-else-if="item.mime_type.startsWith('video')" class="my_icon"
                            name="video-o"/>
                  <img v-else-if="item.mime_type.startsWith('image')" class="my_icon my_icon_size"
                       :src="genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type)"/>
                  <van-image style="width: 40px" v-else-if="item.mime_type.startsWith('doc')"
                             :src="handleGetDocumentImage(item.mime_type)"/>
                  <van-icon size="30" v-else class="my_icon" name="info-o"/>
                </van-col>
                <van-col span="16">
                  {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                  <van-tag style="margin-right: 2px" mark v-for="tag in item.tags">
                    {{ tag.replace(markReg, "") }}
                  </van-tag>
                </van-col>
                <van-col span="4">
                  <van-button v-if="!item['is_dir']" icon="plus" size="small" type="danger" plain
                              style="border: #ffffff"
                              @click="handleClickAddItem(item)"/>
                </van-col>
              </van-row>
            </van-cell>
          </van-list>
        </van-tab>
        <van-tab title="文件夹" :badge="dir.tableData.filter(item => item.is_dir).length">
          <van-list>
            <van-cell v-for="item in dir.tableData" v-if="item.is_dir"
                      :key="item.path" @click="handleClickItem(item)">
              <van-col span="4">
                <van-icon size="30" class="my_icon" name="credit-pay"/>
              </van-col>
              <van-col span="20">
                {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                <van-tag style="margin-right: 2px" mark v-for="tag in item.tags">
                  {{ tag.replace(markReg, "") }}
                </van-tag>
              </van-col>
            </van-cell>
          </van-list>
        </van-tab>
        <van-tab title="图片"
                 :badge="dir.tableData.filter(item => !item.is_dir && item.mime_type.startsWith('image')).length">
          <van-grid :border="false" :column-num="3">
            <van-grid-item
                @click="handleClickItem(item)"
                v-for="item in dir.tableData.filter(item => !item.is_dir && item.mime_type.startsWith('image'))">
              <div>
                <img class="my_icon my_icon_size_large"
                     :src="genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type)"/>
                <van-button @click="handleClickAddItem(item)" round plain hairline type="danger"
                            style="position: absolute; top: 0px; left: 0px; border:#ffffff" icon="plus"
                            size="small"/>
              </div>
              <div>
                <div style="font-size:10px;-webkit-text-size-adjust: none;">
                  {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                </div>
                <van-tag style="margin-right: 2px" mark v-for="tag in item.tags">
                  {{ tag.replace(markReg, "") }}
                </van-tag>
              </div>
            </van-grid-item>
          </van-grid>
        </van-tab>
        <van-tab title="视频"
                 :badge="dir.tableData.filter(item => !item.is_dir && item.mime_type.startsWith('video')).length">
          <van-list>
            <van-cell v-for="item in dir.tableData"
                      v-if="!item.is_dir && item.mime_type.startsWith('video')"
                      :key="item.path" @click="handleClickItem(item)">
              <van-row>
                <van-col span="4">
                  <van-icon size="30" class="my_icon" name="video-o"/>
                </van-col>
                <van-col span="16">
                  {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                  <van-tag style="margin-right: 2px" mark v-for="tag in item.tags">
                    {{ tag.replace(markReg, "") }}
                  </van-tag>
                </van-col>
                <van-col span="4">
                  <van-button v-if="!item['is_dir']" icon="plus" size="small" type="danger" plain
                              style="border: #ffffff"
                              @click="handleClickAddItem(item)"/>
                </van-col>
              </van-row>
            </van-cell>
          </van-list>
        </van-tab>
        <van-tab title="文档"
                 :badge="dir.tableData.filter(item => !item.is_dir && item.mime_type.startsWith('doc')).length">
          <van-list>
            <van-cell v-for="item in dir.tableData" v-if="!item.is_dir && item.mime_type.startsWith('doc')"
                      :key="item.path" @click="handleClickItem(item)">
              <van-row>
                <van-col span="4">
                  <van-image style="width: 40px" :src="handleGetDocumentImage(item.mime_type)"/>
                </van-col>
                <van-col span="16">
                  {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                  <van-tag style="margin-right: 2px" mark v-for="tag in item.tags">
                    {{ tag.replace(markReg, "") }}
                  </van-tag>
                </van-col>
                <van-col span="4">
                  <van-button v-if="!item['is_dir']" icon="plus" size="small" type="danger" plain
                              style="border: #ffffff"
                              @click="handleClickAddItem(item)"/>
                </van-col>
              </van-row>
            </van-cell>
          </van-list>
        </van-tab>
      </van-tabs>
    </div>
  </div>
</template>
<script>
import api, {getDefaultJoyeaDesc, getTopSearchKey} from "../../api/";
import {genSrcPreviewSrc, getDocumentImage, handleGoToPreview} from "../../util/JoyeaUtil"
import {mapGetters} from "vuex"
import eventBus from "../../service/eventbus";
import VmBackTop from 'vue-multiple-back-top'
import {Toast} from 'vant';

export default {
  name: "HomeContainer",
  components: {
    VmBackTop
  },
  computed: {
    ...mapGetters([
      'userInfo'
    ])
  },
  data() {
    return {
      topSearchKey: [],
      canGoBackSearch: false,
      markReg: /<mark>|<\/mark>/g,
      globalPathType: "ent",
      searchKey: "",
      currentTypeActive: 0,
      dir: {
        currentPath: [],
        tableData: [],
        loadingDir: false
      },
      isFirst: true,
      menuPath: [
        {
          name: "装配作业指导类文件", path: "装配作业指导类文件", icon: "menu-icon/1-1.png"
        },
        {
          name: "调试作业指导文件", path: "调试作业指导文件", icon: "menu-icon/1-2.png"
        },
        {
          name: "我司设备培训素材", path: "我司设备培训素材", icon: "menu-icon/1-3.png"
        },
        // {
        //   name: "软双铝线", path: "软双铝线", icon: "menu-icon/3.png"
        // },
        {
          name: "外购大件类培训文件", path: "外购大件类培训文件", icon: "menu-icon/1-4.png"
        },
        {
          name: "通用元器件使用说明", path: "通用元器件使用说明", icon: "menu-icon/1-5.png"
        },
        {
          name: "新工具使用说明", path: "新工具使用说明", icon: "menu-icon/1-6.png"
        },
        {
          name: "实验室", path: "实验室", icon: "menu-icon/1-7.png"
        }
      ]
    }
  },
  methods: {
    handleGetDocumentImage(mimeType) {
      return getDocumentImage(mimeType)
    },
    goBackSearch() {
      this.$router.push("/search");
    },
    handleClickTopKey(key) {
      this.canGoBackSearch = true;
      this.$router.push({path: "/search", query: {searchKey: key}});
    },
    onSearch() {
      if (this.searchKey.trim().length === 0) {
        this.$dialog({message: "要搜索的关键字不能为空！"});
        return;
      }
      this.canGoBackSearch = true;
      this.$router.push({path: "/search", query: {searchKey: this.searchKey}});
    },
    handleClickAddItem(item) {
      event.stopPropagation();
      if (!this.$store.getters.exist(item['neid'])) {
        getDefaultJoyeaDesc(item['neid']).then(resp => {
          item.joyeaDesc = resp.data;
          this.$store.dispatch("addFunc", item).then(() => {
            this.$notify({type: 'success', message: '加入素材车成功！'});
          }).catch(exception => {
            this.$dialog.alert({
              message: '加入素材车失败：' + exception
            })
          })
        })
      } else {
        this.$dialog.alert({
          message: '该素材已经在素材车中'
        })
      }
    },
    handleClickItem(item) {
      if (item['is_dir']) {
        this.handleListLenovoDir(item.path, this.globalPathType)
      } else {
        handleGoToPreview(this, item, this.userInfo.session, this.dir.tableData);
      }
    },
    handleClickRootDir() {
      this.handleListLenovoDir("/装调素材");
      this.currentTypeActive = 0;
    },
    handleClickRootMenu(path) {
      this.handleListLenovoDir("/装调素材/" + path);
    },
    handleClickBackDir() {
      if (this.dir.currentPath.length > 0) {
        let backPath = "/";
        for (let i = 0; i < this.dir.currentPath.length - 1; i++) {
          backPath = backPath + this.dir.currentPath[i] + "/"
        }
        this.handleListLenovoDir(backPath);
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
    },
    handleGetTopSearchKey() {
      getTopSearchKey().then(resp => {
        this.topSearchKey = resp['data'];
      })
    },
    handleListLenovoDir(path, pathType) {
      this.dir.loadingDir = true;
      const toast = Toast.loading({
        duration: 0, // 持续展示 toast
        forbidClick: true,
        message: '拼命加载中...',
      });
      api({
        action: 'listLenovoDir',
        path: path.replace("+", "%2B"),
        path_type: pathType === undefined ? 'ent' : pathType
      }).then(response => {
        toast.clear();
        if (response.result) {
          this.currentTypeActive = 0;
          this.dir.tableData = [];
          if (response.data.content) {
            response.data.content.forEach(item => {
              item.joyeaDesc = "";
              item.isModify = false;
              this.dir.tableData.push(item)
            });
            this.dir.currentPath = [];
            response.data.path.split('/').forEach(item => {
              if (item.length !== 0) {
                this.dir.currentPath.push(item)
              }
            });
          }
        } else {
          console.log('文件夹列表获取失败' + response.msg)
        }
        this.dir.loadingDir = false;
      });
    },
  },
  mounted() {
    this.handleListLenovoDir("/装调素材", "ent");
  },
  activated() {
    eventBus.$on('showDir', function (data) {
      this.handleListLenovoDir(data.path, "ent");
    }.bind(this));
    this.handleGetTopSearchKey();
  }
}
</script>

<style scoped>
.top {
  padding: 10px;
  background: #ee0a24;
  color: #fff;
  text-align: center;
  border-radius: 2px;
}
</style>