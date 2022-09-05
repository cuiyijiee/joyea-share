<template>
    <div>
        <el-dialog :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :title="'【' + search.key + '】的搜索结果'"
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
            <div v-loading="loading.searchMore" class="search-result-content">
                <div class="function-block">
                    <el-button icon="iconfont el-icon-a-icon_addtolist" size="mini" type="primary">加入清单</el-button>
                    <el-button icon="iconfont el-icon-a-icon_addtomarketsegments" size="mini" type="primary">加入细分市场
                    </el-button>
                </div>
                <el-table v-loading="loading.search" :data="searchShowResult"
                          empty-text="暂没有搜索数据" height="500" style="width: 100%">
                    <el-table-column type="selection" width="55"></el-table-column>
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
                                <el-tooltip v-if="directoryType === 'LENOVO'" :open-delay="defaultOpenDelay"
                                            placement="top">
                                    <div slot="content">加入清单</div>
                                    <el-icon class="iconfont el-icon-a-icon_addtolist file-type-icon"
                                             @click.native="handleAdd(scope.row)"></el-icon>
                                </el-tooltip>
                                <el-tooltip v-if="directoryType === 'PRIVATE'" :open-delay="defaultOpenDelay"
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
                <el-button type="primary" size="small" plain
                           class="search-result-page-switch">加载更多</el-button>
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
                key: '新时代',
                hasNext: false,
                currentOffset: 0,
                nextOffset: 0,

                currentIndex: 0,
                defaultLimit: 20,
                resultShowType: "0",
                currentIndexImageList: []
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
        handleStartSearch(isNext) { //isNext: 0-首页，-1-上一页，1-下一页
            this.loading.searchMore = true;
            //计算入参索引
            let nextLoadOffset = 0;
            if (isNext === -1) {
                nextLoadOffset = this.search.currentOffset - this.search.defaultLimit;
            } else if (isNext === 1) {
                nextLoadOffset = this.search.nextOffset;
            } else {
                this.search.currentIndex = 0;
            }
            ftsSearch(this.search.key, nextLoadOffset, this.search.defaultLimit).then(response => {
                if (response.code === "0") {
                    this.search.currentIndex += isNext;
                    this.search.hasNext = response.obj["has_more"];

                    //保存索引
                    this.search.currentOffset = this.search.currentIndex * this.search.defaultLimit;
                    this.search.nextOffset = (this.search.currentIndex + 1) * this.search.defaultLimit;

                    this.searchResult = [];
                    response.obj.content.forEach(item => {
                        item.joyeaDesc = "";
                        item.isModify = false;
                        this.searchResult.push(item);
                    })
                    //过滤当前所选标签文件
                    this.handleFilterSearchResult();

                    //生成图片预览url列表
                    this.search.currentIndexImageList = this.searchResult.filter(temp => {
                        return !temp.is_dir && temp.mime_type.startsWith("image")
                    }).map(temp => {
                        return genSrcPreviewSrc(temp.neid)
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
                    break
                case "2":
                    this.searchShowResult = this.searchResult.filter(temp => {
                        return !temp.is_dir && temp.mime_type.startsWith('image')
                    });
                    break
                case "3":
                    this.searchShowResult = this.searchResult.filter(temp => {
                        return !temp.is_dir && temp.mime_type.startsWith('video')
                    });
                    break
                case "4":
                    this.searchShowResult = this.searchResult.filter(temp => {
                        return !temp.is_dir && temp.mime_type.startsWith('doc')
                    });
                    break
            }
        },
        handleSearch(searchKey) {
            this.search.key = searchKey;
            this.visible.searchDialogVisible = true;
            this.handleStartSearch(0);
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

</style>
