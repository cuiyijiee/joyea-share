<template>
    <div>
        <el-dialog :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :title="'【' + search.keyword + '】的搜索结果'"
                   :visible.sync="visible.searchDialogVisible"
                   class="search-dialog"
                   @close="">
            <el-tabs v-model="search.resultShowType" class="search-result-type-switch"
                     tab-position="top" @tab-click="handleFilterSearchResult">
                <el-tab-pane key="0" label="全部"></el-tab-pane>
                <el-tab-pane key="1" label="文件夹"></el-tab-pane>
                <el-tab-pane key="2" label="图片"></el-tab-pane>
                <el-tab-pane key="3" label="视频"></el-tab-pane>
                <el-tab-pane key="4" label="文档"></el-tab-pane>
            </el-tabs>
            <!--            <div v-loading="loading.searchMore" class="search-result-content">-->
            <div class="search-result-content">
                <div class="function-block">
                    <el-button icon="iconfont el-icon-a-icon_addtolist" size="mini" type="primary"
                               :disabled="directoryType === ''"
                               @click="handleBatchAddToList">加入清单
                    </el-button>
                    <el-button icon="iconfont el-icon-a-icon_addtomarketsegments" :disabled="directoryType!=='SELF'"
                               size="mini" type="primary" @click="handleBatchAddToPrivate">加入细分市场
                    </el-button>
                    <span>
                        <el-popover
                            placement="bottom-end"
                            trigger="click">
                            <div class="sort-item" @click="handleSortTypeSelected(1)">
                                <i class="iconfont el-icon-icon_right sort-icon"
                                   :style="{visibility : sortType === 1 ? 'visible' : 'hidden'}"></i>文件名</div>
                            <div class="sort-item" @click="handleSortTypeSelected(2)">
                                <i class="iconfont el-icon-icon_right sort-icon"
                                   :style="{visibility : sortType === 2 ? 'visible' : 'hidden'}"></i>文件类型</div>
                            <el-divider></el-divider>
                            <div class="sort-item" @click="handleSortOrderSelected(1)">
                                <i class="iconfont el-icon-icon_right sort-icon"
                                   :style="{visibility : sortOrder === 1 ? 'visible' : 'hidden'}"></i>升序</div>
                            <div class="sort-item" @click="handleSortOrderSelected(2)">
                                <i class="iconfont el-icon-icon_right sort-icon"
                                   :style="{visibility : sortOrder === 2 ? 'visible' : 'hidden'}"></i>降序</div>
                            <el-button slot="reference" size="mini" class="function-block-sort"
                                       icon="iconfont el-icon-icon_sort">排序</el-button>
                        </el-popover>
                    </span>
                </div>
                <el-table v-loading="loading.search" :data="searchShowResult" @selection-change="handleSelectionChange"
                          empty-text="暂没有搜索数据" height="500" style="width: 100%" ref="multipleTable">
                    <!--多选框，屏蔽文件夹，使其不可选中-->
                    <el-table-column type="selection" width="55"
                                     :selectable="(row) => {return !row.is_dir}"></el-table-column>
                    <el-table-column label="文件名" show-overflow-tooltip>
                        <template slot-scope="scope">
                            <div>
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
                                    <span class="file-name" @click="handleGoToPreview(scope.row)">
                                        <b>{{ scope.row.path.substr(scope.row.path.lastIndexOf("/") + 1) }}</b>
                                    </span>
                                    <div v-if="scope.row.desc" class="file-desc">
                                        <el-tag v-for="tag in scope.row.desc.split(' ')" size="mini"
                                                style="margin-right: 2px"
                                                type="info">{{ tag.replace(markReg, "") }}
                                        </el-tag>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="预览" width="150">
                        <template slot-scope="scope">
                            <el-image v-if="scope.row.mime_type && scope.row.mime_type.startsWith('image')"
                                      :onerror="defaultImg"
                                      :preview-src-list="[].concat(genPreviewUrl(scope.row.neid))"
                                      :src="genPreviewUrl(scope.row.neid)"
                                      class="preview_img"
                                      style="height: 90px;width: 120px">
                            </el-image>
                        </template>
                    </el-table-column>
                    <el-table-column label="文件类型" width="220">
                        <template slot-scope="scope">
                            <div>
                            <span class="file-type-show">
                                {{ formatFileShowType(scope.row) }}
                            </span>
                                <span class="file-type-hide">
                                <el-tooltip v-if="scope.row.is_dir" :open-delay="defaultOpenDelay" placement="top">
                                    <div slot="content">跳转至文件所在位置</div>
                                    <el-icon
                                        class="iconfont el-icon-a-icon_jumptothelocation file-type-icon"
                                        @click.native="handleGotoDir(scope.row)"></el-icon>
                                </el-tooltip>
                                <el-tooltip
                                    v-if="scope.row.is_dir"
                                    :open-delay="defaultOpenDelay" placement="top">
                                    <div slot="content">获取短链</div>
                                    <el-icon class="iconfont el-icon-a-icon_getshortchain file-type-icon"
                                             @click.native="handleGetCurRedirectPath(scope.row)"></el-icon>
                                </el-tooltip>
                                <el-tooltip v-if="directoryType === 'LENOVO' && !scope.row.is_dir"
                                            :open-delay="defaultOpenDelay"
                                            placement="top">
                                    <div slot="content">加入清单</div>
                                    <el-icon class="iconfont el-icon-a-icon_addtolist file-type-icon"
                                             @click.native="handleAdd(scope.row)"></el-icon>
                                </el-tooltip>

                                <el-tooltip v-if="directoryType === 'SELF' && !scope.row.is_dir"
                                            :open-delay="defaultOpenDelay"
                                            placement="top">
                                    <div slot="content">加入细分市场</div>
                                    <el-icon class="iconfont el-icon-a-icon_addtomarketsegments file-type-icon"
                                             @click.native="handleAddSrcToPrivateDir(scope.row)"></el-icon>
                                </el-tooltip>
                            </span>
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
                <el-button type="primary" size="small" plain :disabled="!search.hasNext"
                           class="search-result-page-switch" @click="handleStartSearch">
                    <span v-show="loading.searchMore" class="rotate_bg">
                        <i class="iconfont el-icon-icon_loading rotateImages"></i></span>
                    <span style="font-size: 16px;line-height: 1">{{ loading.searchMore ? "加载中" : "加载更多" }}</span>
                </el-button>
            </div>
        </el-dialog>
        <el-image-viewer
            v-if="imageViewVisible"
            :on-close="()=>{imageViewVisible = false}"
            :url-list="imageViewUrl" :z-index=9999></el-image-viewer>
        <video-preview-dialog ref="VideoPreviewDialog"></video-preview-dialog>
    </div>
</template>

<script>

import {addRedirectPath, ftsSearch} from "@/api";
import genSrcPreviewSrc, {formatFileShowType} from "@/utils";
import VideoPreviewDialog from "@/components/VideoPreviewDialog";
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'

export default {
    name: "SearchResultDialog",
    components: {
        VideoPreviewDialog,
        ElImageViewer
    },
    props: {
        directoryType: {
            default: "",
            type: String
        }
    },
    data() {
        return {
            defaultOpenDelay: 1000,
            imageViewVisible: false,
            imageViewUrl: [],
            markReg: /<mark>|<\/mark>/g,
            search: {
                type: '',
                keyword: '',
                hasNext: false,
                nextOffset: 0,

                currentIndex: 0,
                defaultLimit: 30,
                resultShowType: "0"
            },
            searchResult: [],
            searchShowResult: [],
            visible: {
                searchDialogVisible: false
            },
            loading: {
                searchMore: false
            },
            defaultImg: 'this.src="' + require('@assets/error.png') + '"', //默认图地址
            multipleSelection: [],
            sortType: 1,
            sortOrder: 2
        }
    },
    methods: {
        formatFileShowType,
        handleGoToPreview(fileItem) {
            if (fileItem.is_dir) {
                this.handleGotoDir(fileItem);
            } else if (fileItem.mime_type && fileItem.mime_type.startsWith("video")) {
                this.$refs.VideoPreviewDialog.playVideo(fileItem)
            } else if (fileItem.mime_type && fileItem.mime_type.startsWith("image")) {
                this.imageViewUrl = [genSrcPreviewSrc(fileItem.neid)]
                this.imageViewVisible = true;
            } else if (fileItem.mime_type.startsWith("doc")) {
                window.open(genSrcPreviewSrc(fileItem.neid));
            } else {
                this.$message.error("暂不支持的预览类型！")
            }
        },
        handleGotoDir(fileItem) {
            this.visible.searchDialogVisible = false;
            this.$emit("goToDir", fileItem);
        },
        handleGetCurRedirectPath(fileItem) {
            let _this = this;
            let currentFullPath = fileItem.path;
            if (currentFullPath.length === 0 && this.directoryType === "SELF") {
                currentFullPath = "/"
            }
            addRedirectPath(currentFullPath, this.directoryType).then(resp => {
                let redirectPath = window.location.protocol + "//"
                    + window.location.host + "/api/redirectPath?id=" + resp.id;
                let input = document.createElement("input");
                input.value = redirectPath;
                document.body.appendChild(input);
                input.select();
                input.setSelectionRange(0, input.value.length);
                document.execCommand('Copy');
                document.body.removeChild(input);
                _this.$message.success("获取成功，已复制到剪贴板！");
            })
        },
        genPreviewUrl(neid) {
            return genSrcPreviewSrc(neid);
        },
        handleAdd(fileItem) {
            this.$emit("handleAdd", fileItem)
        },
        handleAddSrcToPrivateDir(fileItem) {
            this.$emit("addSrcToPrivateDir", fileItem);
        },
        handleStartSearch() {
            this.loading.searchMore = true;
            ftsSearch(this.search.keyword, this.search.nextOffset, this.search.defaultLimit).then(response => {
                if (response.code === "0") {
                    this.search.hasNext = response.obj["has_more"];
                    this.search.nextOffset = response.obj["next_offset"];
                    response.obj.content.forEach(item => {
                        item.joyeaDesc = "";
                        item.isModify = false;
                        this.searchResult.push(item);
                    })
                    //过滤当前所选标签文件
                    this.handleFilterSearchResult();
                    if (this.search.nextOffset <= this.search.defaultLimit) {
                        this.handleSortShowResult();
                    }
                } else {
                    this.$notify.error({
                        title: '搜索出错',
                        message: '搜索过程出现错误：' + response.msg
                    });
                }
            }).finally(() => {
                this.loading.searchMore = false;
            })
        },
        handleFilterSearchResult() {
            switch (this.search.resultShowType) {
                case "0" || "":
                    this.searchShowResult = this.searchResult;
                    break;
                case "1":
                    this.searchShowResult = this.searchResult.filter(temp => {
                        return temp.is_dir;
                    });
                    break;
                case "2":
                    this.searchShowResult = this.searchResult.filter(temp => {
                        return !temp.is_dir && temp.mime_type.startsWith('image');
                    });
                    break;
                case "3":
                    this.searchShowResult = this.searchResult.filter(temp => {
                        return !temp.is_dir && temp.mime_type.startsWith('video');
                    });
                    break;
                case "4":
                    this.searchShowResult = this.searchResult.filter(temp => {
                        return !temp.is_dir && temp.mime_type.startsWith('doc');
                    });
                    break;
            }
        },
        handleSearch(searchKey) {
            this.search.keyword = searchKey;
            this.search.nextOffset = 0;
            this.search.resultShowType = "0";
            this.searchResult = [];
            this.searchShowResult = [];
            this.visible.searchDialogVisible = true;
            this.handleStartSearch();
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        handleBatchAddToList() {
            this.$emit("handleBatchAdd", this.multipleSelection)
        },
        handleBatchAddToPrivate() {
            this.$emit("batchAddSrcToPrivateDir", this.multipleSelection);
        },
        handleSortTypeSelected(val) {
            this.sortType = val;
            this.handleSortShowResult();
        },
        handleSortOrderSelected(val) {
            this.sortOrder = val;
            this.handleSortShowResult();
        },
        handleSortShowResult() {

            let dirList = [];
            let picList = [];
            let videoList = [];
            let docList = [];
            let otherList = [];

            this.searchShowResult.forEach(item => {
                if (item.is_dir) {
                    dirList.push(item)
                } else if (item.mime_type && item.mime_type.startsWith("image")) {
                    picList.push(item)
                } else if (item.mime_type && item.mime_type.startsWith("video")) {
                    videoList.push(item)
                } else if (item.mime_type && item.mime_type.startsWith("doc")) {
                    docList.push(item)
                } else {
                    otherList.push(item)
                }
            });

            dirList = dirList.sort((file1, file2) => {
                let fileNameA = file1.path.substr(file1.path.lastIndexOf("/") + 1).substr(0, 1);
                let fileNameB = file2.path.substr(file2.path.lastIndexOf("/") + 1).substr(0, 1);
                return fileNameA.localeCompare(fileNameB);
            });
            picList = picList.sort((file1, file2) => {
                let fileNameA = file1.path.substr(file1.path.lastIndexOf("/") + 1).substr(0, 1);
                let fileNameB = file2.path.substr(file2.path.lastIndexOf("/") + 1).substr(0, 1);
                return fileNameA.localeCompare(fileNameB);
            });
            videoList = videoList.sort((file1, file2) => {
                let fileNameA = file1.path.substr(file1.path.lastIndexOf("/") + 1).substr(0, 1);
                let fileNameB = file2.path.substr(file2.path.lastIndexOf("/") + 1).substr(0, 1);
                return fileNameA.localeCompare(fileNameB);
            });
            docList = docList.sort((file1, file2) => {
                let fileNameA = file1.path.substr(file1.path.lastIndexOf("/") + 1).substr(0, 1);
                let fileNameB = file2.path.substr(file2.path.lastIndexOf("/") + 1).substr(0, 1);
                return fileNameA.localeCompare(fileNameB);
            });
            otherList = otherList.sort((file1, file2) => {
                let fileNameA = file1.path.substr(file1.path.lastIndexOf("/") + 1).substr(0, 1);
                let fileNameB = file2.path.substr(file2.path.lastIndexOf("/") + 1).substr(0, 1);
                return fileNameA.localeCompare(fileNameB);
            });

            this.searchShowResult = [];
            if (this.sortType === 1) {
                //降序
                if (this.sortOrder === 2) {
                    dirList = dirList.reverse();
                    picList = picList.reverse();
                    videoList = videoList.reverse();
                    docList = docList.reverse();
                    otherList = otherList.reverse();
                }
                this.searchShowResult = this.searchShowResult.concat(dirList)
                    .concat(picList)
                    .concat(videoList)
                    .concat(docList)
                    .concat(otherList);
            } else if (this.sortType === 2) {
                //降序
                if (this.sortOrder === 2) {
                    this.searchShowResult = this.searchShowResult.concat(dirList)
                        .concat(picList)
                        .concat(videoList)
                        .concat(docList)
                        .concat(otherList);
                } else if (this.sortOrder === 1) {
                    this.searchShowResult = this.searchShowResult.concat(otherList)
                        .concat(docList)
                        .concat(videoList)
                        .concat(picList)
                        .concat(dirList);
                }
            }
        }
    },
    watch: {
        "search.keyword": {
            immediate: true,
            handler(v1, v2) {
                if (v1 !== '' || v1 !== v2) {
                    this.$nextTick(() => {
                        this.searchResult = [];
                        this.searchShowResult = [];
                    });
                }
            }
        }
    }
}
</script>

<style lang="scss" scoped>

::v-deep .el-dialog {

    margin-top: 8vh !important;
    width: 75%;

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

.function-block-sort {
    position: absolute;
    right: 30px;
    margin-top: 8px;
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
    margin-top: 20px;
    width: 300px;
}

.file-icon {
    .iconfont-color {
        font-size: 30px;
    }
}

.file-name {
    margin-left: 30px;
    font-size: 15px;
}

.file-desc {
    margin-top: 5px;
    margin-left: 60px;
}

.file-type-icon {
    margin: 0 5px;
}

.file-type-icon:hover {
    color: #EB7708;
}

.file-type-hide {
    display: none;
}

.el-table__body tr:hover > td .file-type-hide {
    display: block;
}

.file-type-show {
    display: block;
}

.el-table__body tr:hover > td .file-type-show {
    display: none;
}

.el-table__body tr:hover > td .file-name {
    color: #EB7708;
}

.sort-icon {
    margin: 0 10px;
}

.sort-item {
    margin: 5px 0;
}

.rotate_bg {
}

.rotate_bg i.rotateImages {
    -webkit-animation: myRotate 1s linear infinite;
    animation: myRotate 1s linear infinite;
    padding: 0 15px;
}

@-webkit-keyframes myRotate {
    0% {
        -webkit-transform: rotate(0deg);
    }
    50% {
        -webkit-transform: rotate(180deg);
    }
    100% {
        -webkit-transform: rotate(360deg);
    }
}

@keyframes myRotate {
    0% {
        -webkit-transform: rotate(0deg);
    }
    50% {
        -webkit-transform: rotate(180deg);
    }
    100% {
        -webkit-transform: rotate(360deg);
    }
}
</style>
