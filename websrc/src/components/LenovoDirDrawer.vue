<template>
    <section>
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
    </section>
</template>

<script>
import api, {getFileMetadata} from "@/api";
import {genFileName, filterDirList} from "../utils/JoyeaUtil";

export default {
    name: "LenovoDirDrawer",
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
            getFileMetadata("LENOVO",path.replace("+", "%2B"),"").then(response => {
                if (response.code === "0") {
                    this.currentTypeActive = 0;
                    this.dir.tableData = [];
                    if (response.obj.content) {
                        response.obj.content.forEach(item => {
                            item.joyeaDesc = "";
                            item.isModify = false;
                            this.dir.tableData.push(item)
                        });
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