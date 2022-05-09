<template>
<span>
    <div class="block">
      <el-cascader v-model="value" placeholder="请搜索添加文件管理员" size="small"
                   :options="options" :disabled="!editable" @click.stop="console.log('123')"
                   :props="{ multiple: true }" filterable></el-cascader>
        <el-button :disabled="!enabled" @click.stop="handleSave" :icon="editable ? 'el-icon-check':'el-icon-edit'" circle
                   type="primary" size="small"></el-button>
        <el-button @click.stop="handleReset" v-show="editable" icon="el-icon-close" circle type="danger"
                   size="small"></el-button>
    </div>
</span>
</template>

<script>

import {listJoyeaUser, updatePrivateDirectoryAdmin} from "@/api";

export default {
    name: "PrivateDirectoryAdminManager",
    props: ['fileItem','enabled'],
    data() {
        return {
            value: [],
            options: [],
            editable: false
        }
    },
    methods: {
        handleSave() {
            if (!this.editable) {
                this.editable = true;
            } else {
                let adminArr = []
                this.value.forEach(item => {
                    adminArr.push(item[1])
                })
                updatePrivateDirectoryAdmin(this.fileItem.neid, adminArr).then(resp => {
                    if (resp.code === '0') {
                        this.$message.success("保存成功！");
                        this.editable = false;
                    } else {
                        this.$message.error("保存失败：" + resp.msg);
                    }
                })
            }
        },
        handleReset() {
            this.editable = false;
        },
        handleGetAllAdmin() {
            if(!this.fileItem.adminUser) {
                this.fileItem.adminUser = [];
            }
            this.fileItem.adminUser.forEach(item => {
                let tmpResult = [];
                tmpResult.push(item.department);
                tmpResult.push(item.id);
                this.value.push(tmpResult);
            })
        },
        handleListAllUser() {
            let department = {}
            let _this = this;
            listJoyeaUser().then(resp => {
                resp.list.forEach(item => {
                    if (!department[item.department]) {
                        department[item.department] = []
                    }
                    department[item.department].push({
                        value: item.id,
                        label: item.joyeaName,
                    })
                })
                Object.keys(department).forEach(function (key) {
                    _this.options.push({
                        value: key,
                        label: key,
                        children: department[key]
                    })
                })
            })
        },
    },
    created() {
        this.handleGetAllAdmin();
        this.handleListAllUser();
    }
}
</script>

<style scoped>

</style>