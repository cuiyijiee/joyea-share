<template>
    <el-dialog :close-on-click-modal="false"
               :close-on-press-escape="false"
               :title="'【' + search.key + '】的搜索结果'"
               :visible.sync="visible.searchDialogVisible"
               class="search-dialog"
               @close="">
        <el-tabs style="height: 40px;" tab-position="top">
            <el-tab-pane label="全部">全部</el-tab-pane>
            <el-tab-pane label="文件夹">文件夹</el-tab-pane>
            <el-tab-pane label="图片">图片</el-tab-pane>
            <el-tab-pane label="视频">视频</el-tab-pane>
            <el-tab-pane label="文档">文档</el-tab-pane>
        </el-tabs>
        <el-table v-loading="loading.search" :data="searchResult" empty-text="暂没有搜索数据" height="500"
                  stripe style="width: 100%"
                  @row-click="handleClickSearch">
            <el-table-column label="文件名" show-overflow-tooltip>
                <template slot-scope="scope">
                    <div @click="handleGoToPreview(scope.row)">
                        <h4 style="margin: 2px">
                            <i v-if="scope.row.is_dir" class="el-icon-folder-opened"></i>
                            <i v-else-if="scope.row.mime_type.startsWith('video')"
                               class="el-icon-video-camera"></i>
                            <img v-else-if="scope.row.mime_type.startsWith('image')" :onerror="defaultImg"
                                 :preview-text="scope.row.path" :src="genPreviewUrl(scope.row.neid)"
                                 class="preview_img" preview="search_image_list"
                                 style="height: 60px;width: 60px">
                            <i v-else-if="scope.row.mime_type.startsWith('doc')"
                               class="el-icon-tickets"></i>
                            <i v-else class="el-icon-question"></i>
                            {{ scope.row.path.substr(scope.row.path.lastIndexOf("/") + 1) }}</h4>
                        <div v-if="scope.row.desc">
                            <el-tag v-for="tag in scope.row.desc.split(' ')" size="mini" style="margin-right: 2px"
                                    type="info">{{ tag.replace(markReg, "") }}
                            </el-tag>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="文件类型" width="220">
                <template slot-scope="scope">
                    <div v-if="scope.row.is_dir">
                        <el-button circle icon="el-icon-folder-opened" type="primary"
                                   @click.stop="handleClickSearch(scope.row)"/>
                    </div>
                    <div v-else>
                        <el-button circle icon="el-icon-plus"
                                   type=""
                                   @click.stop="handleAdd(scope.$index, scope.row)"/>

                        <el-button v-if="directoryType === 'SELF'" @click="handleAddSrcToPrivateDir(scope.row)">
                            添加到细分市场
                        </el-button>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="引用次数" width="100">
                <template slot-scope="scope">
                    <span>{{ scope.row.is_dir ? '-' : scope.row.ref_num }}</span>
                </template>
            </el-table-column>
            <el-table-column label="下载次数" width="100">
                <template slot-scope="scope">
                    <span>{{ scope.row.is_dir ? '-' : scope.row.download_num }}</span>
                </template>
            </el-table-column>
        </el-table>
        <el-button :class="{no_display:!search.hasNext}" :loading="loading.searchMore"
                   class="load_more_bt"
                   style="margin-top: 30px"
                   @click.stop="handleLoadMore">加载更多
        </el-button>
    </el-dialog>
</template>

<script>

import {ftsSearch} from "@/api";

export default {
    name: "SearchResultDialog",
    data() {
        return {
            markReg: /<mark>|<\/mark>/g,
            directoryType: "SELF",
            search: {
                type: '',
                key: '新时代',
                hasNext: false
            },
            searchResult: [],
            visible: {
                searchDialogVisible: true
            },
            loading: {
                searchMore: false
            },
            loadMoreForm: {
                key: '新时代',
                nextOffset: 0
            },
            defaultImg: 'this.src="' + require('@assets/error.png') + '"', //默认图地址
        }
    },
    methods: {
        handleLoadMore() {

        },
        handleClickSearch() {

        },
        handleGoToPreview() {

        },
        genPreviewUrl() {

        },
        handleAdd() {

        },
        handleAddSrcToPrivateDir() {

        },
        handleSearch() {
            ftsSearch(this.loadMoreForm.key, this.loadMoreForm.nextOffset).then(response => {
                if (response.code === "0") {
                    this.search.hasNext = response.obj["has_more"];
                    if (this.search.hasNext) {
                        this.loadMoreForm.key = this.search.key;
                        this.loadMoreForm.nextOffset = response.obj["next_offset"];
                    }
                    this.searchResult = [];
                    response.obj.content.forEach(item => {
                        item.joyeaDesc = "";
                        item.isModify = false;
                        this.searchResult.push(item)
                    })
                } else {
                    this.$notify.error({
                        title: '搜索出错',
                        message: '搜索过程出现错误：' + response.msg
                    });
                    console.log(response.msg)
                }
            }).finally(() => {
                this.loading.searchMore = false
            })
        }
    },
    created() {
        this.handleSearch();
    }
}
</script>

<style lang="scss" scoped>

/deep/ .el-dialog {
    .el-dialog__header {
        background-color: #EAE8EB;
    }

    .el-dialog__body {
        padding: 0px 20px 10px 20px;
        text-align: center;
    }
}
</style>