<template>
    <div>
        <div class="directory-head">细分市场素材库</div>
        <el-tree :data="selfData" :props="defaultProps" lazy
                 :load="handleLoadChild"
                 @node-click="handleNodeClick">
            <template slot-scope="scope">
                <div class="directory-item"><i class="iconfont-color icon-icon_folder"/>{{ scope.data.dirName }}</div>
            </template>
        </el-tree>
        <el-divider></el-divider>
        <div class="directory-head">基础素材库</div>
        <el-tree :data="lenovoData" :props="defaultProps" lazy
                 :load="handleLoadChild"
                 @node-click="handleNodeClick">
            <template slot-scope="scope">
                <div class="directory-item"><i class="iconfont-color icon-icon_folder"/>{{ scope.data.dirName }}</div>
            </template>
        </el-tree>
    </div>
</template>

<script>

import {getRootDirectory, getChildDirectory} from "@/api";

export default {
    name: "DirectoryTree",
    data() {
        return {
            selfData: [],
            lenovoData: [],
            defaultProps: {
                label: 'dirName',
                children: 'children',
            }
        }
    },
    methods: {
        handleLoadChild(node, resolve) {
            getChildDirectory(node.data.dirType, node.data.id).then(data => {
                if (data.code === "0") {
                    resolve(data.list);
                }
            }).finally(() => {

            })
        },
        handleNodeClick(data) {
            this.$emit("handleClickItem", data.dirType, data.path);
        }
    },
    mounted() {
        getRootDirectory().then(data => {
            if (data.code === "0") {
                this.selfData = data.obj.selfDirectory;
                this.lenovoData = data.obj.lenovoDirectory;
            }
        }).finally(() => {

        })
    }
}
</script>

<style scoped>

.directory-head {
    padding: 5px 0;
}

.directory-item {
    font-size: 14px;
}

</style>