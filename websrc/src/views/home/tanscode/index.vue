<template>
    <section>
        <el-table
            :data="data"
            style="width: 80%;margin: auto">
            <el-table-column
                type="index"
                width="50">
            </el-table-column>
            <el-table-column
                prop="fileName"
                label="素材名称">
            </el-table-column>
            <el-table-column
                label="分享链接"
                width="600">
                <template slot-scope="scope">http://{{ rootPath }}/apiv2/transcode/preview?id={{ scope.row.id }}
                </template>
            </el-table-column>
            <el-table-column
                width="100"
                label="状态">
                <template slot-scope="scope">
                    <el-tag>{{ scope.row.transVideoUrl !== undefined ? "启用" : "转码中" }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column
                fixed="right"
                label="操作"
                width="100">
                <template slot-scope="scope">
                    <el-popover
                        :ref="scope.row.id"
                        placement="left"
                        trigger="click">
                        <p>确定删除吗？</p>
                        <div style="text-align: center; margin: 0">
                            <el-button size="mini" type="text" @click="visibleDelCancel(scope.row.id)">取消</el-button>
                            <el-button type="primary" size="mini" @click="handleDelete(scope.row) ">确定</el-button>
                        </div>
                        <el-button slot="reference">删除</el-button>
                    </el-popover>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            background
            layout="prev, pager, next"
            style="width: 80%;margin: 20px auto;text-align: right"
            @current-change="handleGetList"
            :total="total">
        </el-pagination>
    </section>
</template>

<script>

import {listTranscodeVideo, deleteTranscodeVideo} from "@/api";

export default {
    name: "transcode",
    data() {
        return {
            rootPath: window.location.host,
            curPage: 1,
            pageSize: 10,
            total: 0,
            data: []
        }
    },
    methods: {
        visibleDelCancel(id){
            this.$refs[id].doClose();
        },
        handleGetList(curPage, pageSize) {
            listTranscodeVideo("", curPage, pageSize ? pageSize : this.pageSize).then(resp => {
                this.total = resp.total;
                this.curPage = resp.pageNum;
                this.pageSize = resp.pageSize;
                this.data = resp.list;
            })
        },
        handleDelete(row) {
            deleteTranscodeVideo(row.id).then(resp => {
                this.$message.success("删除成功！");
                this.handleGetList(this.curPage, this.pageSize);
            })
        }
    },
    computed: {},
    mounted() {
        this.handleGetList(this.curPage, this.pageSize);
    }
}
</script>

<style scoped>
</style>