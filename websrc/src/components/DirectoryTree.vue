<template>
    <div class="directory-tree">
        <el-tree :data="treeData" :props="defaultProps" lazy indent="8"
                 :load="handleLoadChild"
                 @node-click="handleNodeClick">
            <template slot-scope="scope">
                <div v-if="scope.data.parentDirId">
                    <i class="iconfont-color icon-icon_folder"/>
                    {{ scope.data.dirName }}
                </div>
                <div v-else class="directory-item">
                    <b style="color: #303133">{{ scope.data.dirName }}</b>
                </div>
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
            treeData: [],
            defaultProps: {
                label: 'dirName',
                children: 'children',
                isLeaf: 'isLeaf'
            }
        }
    },
    methods: {
        handleLoadChild(node, resolve) {
            getChildDirectory(node.data.dirType, node.data.id).then(data => {
                if (data.code === "0") {
                    data.list.forEach(item => {
                        item.isLeaf = !item.hasChild;
                    })
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
                this.treeData = data.list;
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

.directory-tree{

}

</style>
