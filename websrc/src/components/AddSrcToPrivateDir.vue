<template>
    <span style="text-align: left">
        <el-button class="search-button" size="small"
                   @click="visible.addSrcVisible = !visible.addSrcVisible">
                        从素材库添加文件
                    </el-button>
        <LenovoDirSelector :visible.sync="visible.addSrcVisible" @close="visible.addSrcVisible=false"
                           :is-dir="false" @selectSrc="handleSelectSrc">
        </LenovoDirSelector>
    </span>
</template>

<script>

import {newPrivateDirSrc} from "@/api";
import LenovoDirSelector from "@/components/LenovoDirSelector";

export default {
    name: "AddSrcToPrivateDir",
    components: {
        LenovoDirSelector
    },
    props: ['curDirNeid'],
    data() {
        return {
            visible: {
                addSrcVisible: false
            }
        }
    },
    methods: {
        handleSelectSrc(item) {
            this.visible.addSrcVisible = false;
            newPrivateDirSrc(this.curDirNeid, item.path).then(resp => {
                if (resp.code === '0') {
                    this.$message.success("添加文件成功！");
                    this.$emit("onAddSuccess");
                }else{
                    this.$message.error("添加文件失败: " + resp.msg);
                }
            })
        }
    }
}
</script>

<style scoped>

</style>