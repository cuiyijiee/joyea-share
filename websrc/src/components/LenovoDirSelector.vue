<template>
  <span>
    <el-dialog :visible="visible" title="请选择目录" @close="handleClickClose" @opened="handleDialogOpened"
               :modal-append-to-body="true">
      <div>
        <span>
          <span style=" color:#1f1731;font-size: 15px;cursor:pointer;"
                @click="handleListLenovoDir('/营销素材展示','ent')">首页</span>
          <span style="display: inline" v-for="(item,index) in dir.currentPath" v-if="item !== '营销素材展示'">/
            <span style=" color:#1f1731;font-size: 15px;cursor:pointer;"
                  @click="handleClickDirPath(item,index)">{{ item }}</span>
          </span>
        </span>
        <el-button v-if="isDir" @click="handleSelectDir" type="primary" style="margin: 0 20px">选择</el-button>
      </div>
      <div>
        <el-table :data="dir.tableData" :show-header="false" empty-text="没有可选目录啦!"
                  @row-click="handleClickItem" v-loading="dir.loadingDir">
          <el-table-column label="路径" >
            <template slot-scope="scope">
                <i v-if="scope.row.is_dir" class="el-icon-folder-opened"></i>
                <i v-else-if="scope.row.mime_type.startsWith('video')" class="el-icon-video-camera"></i>
                <img v-else-if="scope.row.mime_type.startsWith('image')"
                     style="width: 60px;height: 60px;line-height: 0px"
                     :onerror="defaultImg"
                     :src="genSrcPreviewUrl(scope.row.neid)">
                <i v-else-if="scope.row.mime_type.startsWith('doc')" class="el-icon-tickets"></i>
                <i v-else-if="scope.row.mime_type.startsWith('word')" class="el-icon-link"></i>
                <i v-else class="el-icon-question"></i>
                <span style="line-height: 30px">{{ genFileName(scope.row.path) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80" align="center">
            <template slot-scope="scope">
                <div v-if="scope.row.is_dir || scope.row.mime_type.startsWith('word')">-</div>
                <el-button v-else @click.stop="handleSelectSrc(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </span>
</template>

<script>
import {getFileMetadata} from "@/api";
import {genFileName, filterDirList} from "@/utils/JoyeaUtil";
import genSrcPreviewUrl from "@/utils";

export default {
    name: "LenovoDirSelector",
    data() {
        return {
            defaultImg: 'this.src="' + require('@assets/error.png') + '"', //默认图地址
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
        },
        isDir: {
            type: Boolean,
            default: true
        }
    },
    methods: {
        genFileName,
        filterDirList,
        genSrcPreviewUrl,
        handleClickDirPath(item, index) {
            let toReachPath = "";
            for (let i = 0; i <= index; i++) {
                toReachPath = toReachPath + "/" + this.dir.currentPath[i]
            }
            this.handleListLenovoDir(toReachPath, "ent")
        },
        handleSelectDir() {
            this.$emit('selectDirPath', {
                neid: this.dir.currentPathNeid,
                path: this.dir.currentPath
            });
        },
        handleSelectSrc(item) {
            this.$emit('selectSrc', {
                neid: item.neid,
                path: item.path
            });
        },
        handleClickClose() {
            this.$emit('close');
            this.dir.tableData = [];
        },
        handleClickItem(item) {
            if(item.is_dir){
                this.handleListLenovoDir(item.path, "ent");
            }
        },
        handleListLenovoDir(path) {
            this.dir.loadingDir = true;
            getFileMetadata("LENOVO", path, "").then(response => {
                if (response.code === "0") {
                    this.currentTypeActive = 0;
                    this.dir.tableData = [];
                    if (response.obj.content) {
                        response.obj.content.forEach(item => {
                            this.dir.tableData.push(item)
                        });
                        if (this.isDir) {
                            this.dir.tableData = filterDirList(this.dir.tableData);
                        }
                        this.dir.currentPath = [];
                        this.dir.currentPathNeid = response.obj.neid;
                        response.obj.path.split('/').forEach(item => {
                            if (item.length !== 0) {
                                this.dir.currentPath.push(item);
                            }
                        });
                    }
                } else {
                    console.log('文件夹列表获取失败' + response.msg)
                }
                this.dir.loadingDir = false;
            })
        },
        handleDialogOpened() {
            this.handleListLenovoDir("/营销素材展示", "ent");
        }
    },
    created() {

    }
}
</script>

<style scoped>

</style>
