<template>
  <div>
    <el-dialog :visible="visible" title="请选择目录" @close="handleClickClose" @opened="handleDialogOpened"  :modal-append-to-body="true">
      <div>
        <span>
          <span style=" color:#1f1731;font-size: 15px;cursor:pointer;"
                @click="handleListLenovoDir('/营销素材展示','ent')">首页</span>
          <span style="display: inline" v-for="(item,index) in dir.currentPath" v-if="item !== '营销素材展示'">/
            <span style=" color:#1f1731;font-size: 15px;cursor:pointer;"
                  @click="handleClickDirPath(item,index)">{{ item }}</span>
          </span>
        </span>
        <el-button @click="handleSelect" type="primary" style="margin: 0 20px">选择</el-button>
      </div>
      <div>
        <el-table :data="filterDirList(dir.tableData)" :show-header="false" empty-text="没有可选目录啦!"
                  @row-click="handleClickItem" v-loading="dir.loadingDir">
          <el-table-column label="路径">
            <template slot-scope="scope">
              {{ genFileName(scope.row.path) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import api from "@/api";
import {genFileName, filterDirList} from "../utils/JoyeaUtil";

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
    handleClickDirPath(item, index) {
      let toReachPath = "";
      for (let i = 0; i <= index; i++) {
        toReachPath = toReachPath + "/" + this.dir.currentPath[i]
      }
      this.handleListLenovoDir(toReachPath, "ent")
    },
    handleSelect() {
      this.$emit('selectPath', {
        neid: this.dir.currentPathNeid,
        path: this.dir.currentPath
      });
    },
    handleClickClose() {
      this.$emit('close');
    },
    handleClickItem(item) {
      this.handleListLenovoDir(item.path, "ent");
    },
    handleListLenovoDir(path, pathType) {
      this.dir.loadingDir = true;
      api({
        action: 'listLenovoDir',
        path: path.replace("+", "%2B"),
        path_type: pathType === undefined ? 'ent' : pathType
      }).then(response => {
        //toast.clear();
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
    handleDialogOpened(){
      this.handleListLenovoDir("/营销素材展示", "ent");
    }
  },
  created() {

  }
}
</script>

<style scoped>

</style>