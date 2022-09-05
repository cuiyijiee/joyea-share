<template>
    <el-dialog :close-on-click-modal="false"
               :close-on-press-escape="false"
               :title="'【' + search.key + '】的搜索结果'"
               :visible.sync="visible.searchDialogVisible"
               class="search-dialog"
               @close="">
        <el-tabs class="search-result-type-switch" tab-position="top">
            <el-tab-pane label="全部"></el-tab-pane>
            <el-tab-pane label="文件夹"></el-tab-pane>
            <el-tab-pane label="图片"></el-tab-pane>
            <el-tab-pane label="视频"></el-tab-pane>
            <el-tab-pane label="文档"></el-tab-pane>
        </el-tabs>
        <div class="search-result-content" v-loading="loading.searchMore">
            <div class="function-block">
                <el-button size="mini" type="primary" icon="iconfont el-icon-a-icon_addtolist">加入清单</el-button>
                <el-button size="mini" type="primary" icon="iconfont el-icon-a-icon_addtomarketsegments">加入细分市场
                </el-button>
            </div>
            <el-table v-loading="loading.search" :data="searchResult" empty-text="暂没有搜索数据" height="500"
                      style="width: 100%"
                      @row-click="handleClickSearch">
                <el-table-column type="selection" width="55"></el-table-column>
                <el-table-column label="文件名" show-overflow-tooltip>
                    <template slot-scope="scope">
                        <div @click="handleGoToPreview(scope.row)">
                            <div class="file-name-block">
                                <span class="file-icon">
                                    <i v-if="scope.row.is_dir" class="iconfont-color icon-icon_folder"></i>
                                    <i v-else-if="scope.row.mime_type.startsWith('doc')"
                                       class="iconfont-color icon-icon_file"></i>
                                    <i v-else-if="scope.row.mime_type.startsWith('image')"
                                       class="iconfont-color icon-icon_pic"></i>
                                    <i v-else-if="scope.row.mime_type.startsWith('video')"
                                       class="iconfont-color icon-icon_vedio"></i>
                                    <i v-else class="el-icon-question"></i>
                                </span>
                                <span class="file-name">
                                    <b>{{ scope.row.path.substr(scope.row.path.lastIndexOf("/") + 1) }}</b>
                                </span>
                            </div>

                            <div v-if="scope.row.desc">
                                <el-tag v-for="tag in scope.row.desc.split(' ')" size="mini" style="margin-right: 2px"
                                        type="info">{{ tag.replace(markReg, "") }}
                                </el-tag>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column label="预览" width="150">
                    <template slot-scope="scope">
                        <img v-if="scope.row.mime_type && scope.row.mime_type.startsWith('image')" :onerror="defaultImg"
                             :src="genPreviewUrl(scope.row.neid)"
                             class="preview_img"
                             preview="search_image_list" :preview-text="scope.row.path"
                             style="height: 90px;width: 120px">
                    </template>
                </el-table-column>
                <el-table-column label="文件类型" width="220">
                    <template slot-scope="scope">
                        {{ formatFileShowType(scope.row) }}
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
            <el-button-group class="search-result-page-switch">
                <el-button type="primary"
                           icon="el-icon-arrow-left"
                           @click="handleSearch(-1)"
                           :disabled="search.currentOffset === 0">上一页
                </el-button>
                <el-button type="primary"
                           :disabled="!search.hasNext"
                           @click="handleSearch(1)">
                    下一页
                    <i class="el-icon-arrow-right el-icon--right"></i>
                </el-button>
            </el-button-group>
        </div>
    </el-dialog>
</template>

<script>

import {ftsSearch} from "@/api";
import genSrcPreviewSrc from "@/utils";

import {formatFileShowType} from "@/utils";

export default {
    name: "SearchResultDialog",
    data() {
        return {
            markReg: /<mark>|<\/mark>/g,
            directoryType: "SELF",
            search: {
                type: '',
                key: '新时代',
                hasNext: false,
                currentOffset: 0,
                nextOffset: 0,

                currentIndex: 0,
                defaultLimit: 20
            },
            searchResult: [],
            visible: {
                searchDialogVisible: true
            },
            loading: {
                searchMore: false
            },
            defaultImg: 'this.src="' + require('@assets/error.png') + '"', //默认图地址
        }
    },
    methods: {
        formatFileShowType,
        handleLoadMore() {

        },
        handleClickSearch() {

        },
        handleGoToPreview() {

        },
        genPreviewUrl(neid) {
            return genSrcPreviewSrc(neid);
        },
        handleAdd() {

        },
        handleAddSrcToPrivateDir() {

        },
        handleSearch(isNext) { //isNext: 0-首页，-1-上一页，1-下一页
            this.loading.searchMore = true;
            let nextLoadOffset = 0;
            if (isNext === -1) {
                nextLoadOffset = this.search.currentOffset - this.search.defaultLimit;
            } else if (isNext === 1) {
                nextLoadOffset = this.search.nextOffset;
            }
            ftsSearch(this.search.key, nextLoadOffset, this.search.defaultLimit).then(response => {
                if (response.code === "0") {
                    this.search.currentIndex += isNext;
                    this.search.hasNext = response.obj["has_more"];

                    this.search.currentOffset = this.search.currentIndex * this.search.defaultLimit;
                    this.search.nextOffset = (this.search.currentIndex + 1) * this.search.defaultLimit;

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
                }
            }).finally(() => {
                this.loading.searchMore = false;
            })
        }
    },
    created() {
        this.handleSearch(0);
    }
}
</script>

<style lang="scss" scoped>

 ::v-deep .el-dialog {

    margin-top: 8vh !important;
    width: 80%;

    .el-dialog__header {
        background-color: #EAE8EB;
    }

    .el-dialog__body {
        padding: 0 0 10px 0;
        text-align: center;
    }
}

 ::v-deep .el-tabs {
    background-color: #EAE8EB;
    padding: 0 20px 0px 20px;
}

.function-block {
    text-align: left;
    margin: 10px 0;

    .el-button {
        border-radius: 0px;
        font-size: 14px;
    }
}

.search-result-content {
    margin: 0 20px 0 20px;
}


.search-result-type-switch {
    height: 40px;
}

 ::v-deep .el-table-column--selection {
    .cell {
        padding: 0 14px !important;
    }
}

.search-result-page-switch {
    margin-top: 40px;

    button {
        width: 200px;
    }
}

.file-icon {
    .iconfont-color {
        font-size: 30px;
    }
}

.file-name {
    margin-left: 30px;
}

</style>
