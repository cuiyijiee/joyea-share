<template>
    <span>
        <el-popover placement="top" width="320" v-model="visible">
            <div>重命名文件夹【 {{ fileItem.file_name }} 】</div>
            <el-input v-model="newPrivateDirName"></el-input>
              <div style="text-align: right; margin: 0">
                <el-button size="mini" type="text" @click="visible = false">取消</el-button>
                <el-button type="primary" size="mini" @click.stop="handleRenamePrivateDir">确定</el-button>
              </div>
            <el-button slot="reference"
                       @click.stop=""
                       icon="el-icon-edit-outline" circle/>
        </el-popover>
    </span>
</template>

<script>

import {renamePrivateDir} from "@/api";

export default {
    name: "RenamePrivateDirectory",
    props: ['fileItem'],
    data() {
        return {
            visible: false,
            newPrivateDirName: "",
        }
    },
    methods: {
        handleRenamePrivateDir() {
            if (this.newPrivateDirName === '' || this.newPrivateDirName.trim().length === 0) {
                this.$message.error("文件夹不能为空！");
                return;
            }
            if(this.newPrivateDirName.trim() === this.fileItem.file_name) {
                this.$message.error("请修改后保存！");
                return;
            }
            if(this.newPrivateDirName.indexOf("/") > 0) {
                this.$message.error("文件夹名称不能包括/");
                return;
            }
            renamePrivateDir(this.fileItem.neid, this.newPrivateDirName).then(resp => {
                if (resp.code === '0') {
                    this.$message.success("修改文件夹成功!");
                    this.visible = false;
                    this.$emit("onModifySuccess")
                } else {
                    this.$message.error("修改文件夹名称失败: " + resp.msg);
                }
            })
        }
    },
    created() {
        this.newPrivateDirName = this.fileItem.file_name;
    }
}
</script>

<style scoped>

</style>