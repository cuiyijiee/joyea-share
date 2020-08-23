<template>
  <van-action-sheet v-model="visible" title="选择目录" @close="onClose" :close-on-click-overlay="false" close-icon="">
    <div>
        <span>
          <span style=" color:#b3d8ff;font-size: 15px;cursor:pointer;"
                @click="handleListLenovoDir('/装调素材','ent')">首页</span>
          <span style="display: inline" v-for="(item,index) in dir.currentPath" v-if="item !== '装调素材'">/
            <span style=" color:#b3d8ff;font-size: 15px;cursor:pointer;"
                  @click="handleClickDirPath(item,index)">{{ item }}</span>
          </span>
        </span>
      <van-button style="margin: 0 20px" type="primary" @click="handleSelect">选择</van-button>
    </div>
    <div>
      <van-list>
        <van-cell v-for="item in filterDirList(dir.tableData)"
                  @click="handleClickItem(item)"
                  v-if="item.path !== '/装调素材/素材库上传临时文件夹'"
                  :key="item.path" :title="genFileName(item.path)"/>
        <van-empty image="error" v-if="filterDirList(dir.tableData).length === 0" description="没有可选目录啦!"/>
      </van-list>
    </div>
  </van-action-sheet>
</template>

<script>
import {Toast} from "vant";
import api from "@/api";
import {genFileName, filterDirList} from "@/util/JoyeaUtil";

export default {
  name: "LenovoDirSelector",
  data() {
    return {
      dir: {
        currentPathNeid: "",
        currentPath: [],
        tableData: [],
        loadingDir: false
      },
    }
  },
  props: {
    visible: {
      type: Boolean,
      default: true
    }
  },
  methods: {
    genFileName,
    filterDirList,
    onClose() {
      this.handleSelect();
    },
    handleSelect() {
      this.$emit('selectPath', {
        neid: this.dir.currentPathNeid,
        path: this.dir.currentPath
      });
    },
    handleClickItem(item) {
      this.handleListLenovoDir(item.path, "ent");
    },
    handleClickDirPath(item, index) {
      let toReachPath = "";
      for (let i = 0; i <= index; i++) {
        toReachPath = toReachPath + "/" + this.dir.currentPath[i]
      }
      this.handleListLenovoDir(toReachPath, "ent")
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
            this.dir.currentPathNeid = response.data.neid;
            response.data.path.split('/').forEach(item => {
              if (item.length !== 0) {
                this.dir.currentPath.push(item);
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
  created() {
    this.handleListLenovoDir("/装调素材", "ent");
  }
}
</script>

<style scoped>

</style>