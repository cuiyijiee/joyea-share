<template>
    <section>
        <!--工具条-->
        <el-input placeholder="请输入关键字" v-model="search.key" class="input-with-select px10_divider"
                  @keyup.enter.native="handleSearch">
            <el-select v-model="search.type" slot="prepend" placeholder="请选择类型" style="width: 80px"
                       :value="search.type">
                <el-option v-for="item in options.search" :label="item.label" :value="item.value" :key="item.value"
                           :disabled="item.disabled"/>
            </el-select>
            <el-button slot="append" icon="el-icon-search" v-on:click="handleSearch"></el-button>
        </el-input>
        <el-row :gutter="20" class="px10_divider">
            <el-col :span="12" class="bg-purple">
                <div>
                    <div style="width: 100%" class="center_vertical">
                        <h1>搜索结果</h1>
                    </div>
                    <el-table stripe empty-text="暂没有搜索数据" :data="searchResult" style="width: 100%"
                              v-loading="loading.search">
                        <el-table-column prop="path" label="素材名"/>
                        <el-table-column label="解说词" width="80">
                            <template slot-scope="scope">
                                <span>{{scope.row.desc.length}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="mime_type" label="素材格式" width="100"/>
                        <!--                    <el-table-column prop="updator" label="是否有解说词" /> -->
                        <el-table-column label="操作" width="180">
                            <template slot-scope="scope">
                                <el-button circle type="primary" @click="handleAdd(scope.$index, scope.row)"
                                           icon="el-icon-plus"/>
                                <el-button circle type="" icon="el-icon-search"
                                           @click="handleGoToPreview(scope.row)"/>
                                <el-button circle :type="scope.row.collect ? 'warning' : ''"
                                           @click="handleCollect(scope.$index, scope.row)"
                                           icon="el-icon-star-off"/>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-button class="load_more_bt" :class="{no_display:!search.hasNext}"
                               :loading="loading.searchMore"
                               @click="handleLoadMore">加载更多
                    </el-button>
                </div>
            </el-col>
            <el-col :span="12" class="bg-purple">
                <div class="content_div">
                    <div style="width: 100%" class="center_vertical">
                        <h1>清单编辑列表（{{toCreateAlbum.list.length}}）</h1>
                    </div>
                    <el-table stripe empty-text="清单内还没有内容" :data="toCreateAlbum.list" style="width: 100%">
                        <el-table-column label="预览">
                            <template slot-scope="scope">
                                <el-tooltip class="item" effect="dark" :content="scope.row.path" placement="top">
                                    <img class="preview_img"
                                         :preview="scope.row.path" :preview-text="scope.row.path"
                                         :src="genPreviewUrl(scope.row.neid,scope.row.hash,scope.row.rev)">
                                </el-tooltip>
                            </template>
                        </el-table-column>
                        <el-table-column label="解说词">
                            <template slot-scope="scope">
                                <span v-if="scope.row.isModify">
                                    <el-input type="textarea" placeholder="请输入内容"
                                              v-model="toCreateAlbum.modifyRow.joyeaDesc"/>
                                </span>
                                <span v-else>{{scope.row.joyeaDesc}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="150">
                            <template slot-scope="scope">
                                <el-button type="primary" @click="handleModify(scope.$index, scope.row,true)"
                                           size="mini">
                                    {{scope.row.isModify?'保存':"修改"}}
                                </el-button>
                                <el-button v-if="!scope.row.isModify" @click="handleDelete(scope.$index, scope.row)"
                                           size="mini" type="danger" plain>删除
                                </el-button>
                                <el-button v-else size="mini" @click="handleModify(scope.$index,scope.row,false)"
                                           type="danger" plain>取消
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-row class="load_more_bt" :class="{no_display:toCreateAlbum.list.length === 0}">
                        <el-col :span="8">
                            <el-button type="primary" @click="handleSaveList" class="load_more_bt"
                                       icon="el-icon-folder-add" :loading="loading.saveList">保存
                            </el-button>
                        </el-col>
                        <el-col :span="8">
                            <el-button type="success" @click="handleDownloadSrc(true)" class="load_more_bt"
                                       v-loading.fullscreen.lock="loading.fullscreenLoading"
                                       icon="el-icon-download">下载
                            </el-button>
                        </el-col>
                        <el-col :span="8">
                            <el-button type="danger" @click="handleClearList" class="load_more_bt"
                                       icon="el-icon-delete">清空
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
            </el-col>
        </el-row>
        <el-dialog ref="addDialog" title="选择已有解说词" :visible.sync="toCreateAlbum.descSelectDialogVisible" center>
            <span class="dialog-footer">
                <img class="preview_img" style="margin: 0 auto" :src="toCreateAlbum.previewUrl">
            </span>
            <el-table style="width: 70%" :data="toCreateAlbum.toSelectDesc" @row-click="handleSelectDesc"
                      empty-text="暂无可用解说词">
                <el-table-column prop="desc"></el-table-column>
            </el-table>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="handleCustomDesc"
                           style="margin-top: 10px" icon="el-icon-circle-plus">不借鉴</el-button>
            </span>
        </el-dialog>
    </section>
</template>

<script>
    import api from "../../../api";
    import genSrcPreviewSrc from "../../../utils/index"
    import VueLoadImage from 'vue-load-image'

    export default {
        name: "index",
        components: {
            'vue-load-image': VueLoadImage
        },
        data() {
            return {
                search: {
                    type: '',
                    key: '',
                    hasNext: false
                },
                userInfo: {},
                options: {
                    search: [
                        {value: '', label: '全部', disabled: false},
                        {value: 'pic', label: '图片'},
                        {value: 'video', label: '视频', disabled: true},
                    ]
                },
                loading: {
                    search: false,
                    searchMore: false,
                    saveList: false,
                    fullscreenLoading: false
                },
                searchResult: [],
                toCreateAlbum: {
                    idEditMode: false, //是否是编辑模式
                    editListId: -1,
                    name: '',       //清单名称
                    list: [],       //资源列表
                    modifyRow: '',
                    toAddRow: {},
                    previewUrl: '',
                    descSelectDialogVisible: false,
                    toSelectDesc: []
                },
                loadMoreForm: {
                    type: '',
                    key: '',
                    nextOffset: 0
                },
            }
        },
        methods: {
            handleSearch() {
                let _this = this;
                if (_this.search.key.trim().length === 0) {
                    _this.$message.warning("请输入搜索的关键字！")
                } else {
                    _this.loading.search = true;
                    api({
                        action: 'search',
                        searchKey: _this.search.key,
                        searchType: _this.search.type,
                        offset: 0
                    }).then(response => {
                        if (response.result) {
                            _this.search.hasNext = response["has_more"];
                            if (_this.search.hasNext) {
                                _this.loadMoreForm.type = _this.search.type;
                                _this.loadMoreForm.key = _this.search.key;
                                _this.loadMoreForm.nextOffset = response["next_offset"];
                            }
                            _this.searchResult = [];
                            response.content.forEach(item => {
                                item.joyeaDesc = "";
                                item.isModify = false;
                                _this.searchResult.push(item)
                            })
                        } else {
                            _this.$notify.error({
                                title: '搜索出错',
                                message: '搜索过程出现错误：' + response.msg
                            });
                            console.log(response.msg)
                        }
                    }).finally(() => {
                        _this.loading.search = false
                    })
                }
            },
            handleLoadMore() {
                let _this = this;
                if (_this.loadMoreForm.key.trim().length !== 0) {
                    _this.loading.searchMore = true;
                    api({
                        action: 'search',
                        searchKey: _this.loadMoreForm.key,
                        searchType: _this.loadMoreForm.type,
                        offset: _this.loadMoreForm.nextOffset
                    }).then(response => {
                        if (response.result) {
                            _this.search.hasNext = response["has_more"];
                            if (_this.search.hasNext) {
                                _this.loadMoreForm.type = _this.search.type;
                                _this.loadMoreForm.key = _this.search.key;
                                _this.loadMoreForm.nextOffset = response["next_offset"];
                            }
                            //_this.searchResult = [];
                            response.content.forEach(item => {
                                item.joyeaDesc = "";
                                item.isModify = false;
                                _this.searchResult.push(item)
                            })
                        } else {
                            _this.$notify.error({
                                title: '搜索出错',
                                message: '搜索过程出现错误：' + response.msg
                            });
                            console.log(response.msg)
                        }
                    }).finally(() => {
                        _this.loading.searchMore = false
                    })
                }
            },
            handleAdd(index, row) {
                // this.toCreateAlbum.toAddRow = row;
                // this.toCreateAlbum.toSelectDesc = row.desc;
                // this.toCreateAlbum.previewUrl = this.genPreviewUrl(row.neid, row.hash, row.rev);
                // this.toCreateAlbum.descSelectDialogVisible = true;
                this.toCreateAlbum.list.push(row);
            },
            handleSelectDesc(row, column, event) {
                this.toCreateAlbum.toAddRow.joyeaDesc = row.desc;
                this.toCreateAlbum.list.push(this.toCreateAlbum.toAddRow);
                this.toCreateAlbum.toAddRow = {};
                this.toCreateAlbum.toSelectDesc = [];
                this.toCreateAlbum.descSelectDialogVisible = false;
            },
            handleCustomDesc() {
                this.toCreateAlbum.toAddRow.joyeaDesc = "";
                this.toCreateAlbum.list.push(this.toCreateAlbum.toAddRow);
                this.toCreateAlbum.toAddRow = {};
                this.toCreateAlbum.toSelectDesc = [];
                //this.$set(this.toCreateAlbum,"descSelectDialogVisible",false);
                this.toCreateAlbum.descSelectDialogVisible = false;
            },
            handleCollect(index, row) {
                api({
                    action: "srcCollect",
                    method: row.collect ? 'unCollect' : 'collect',
                    neid: row.neid,
                    path: row.path,
                    type: row.mime_type,
                    hash: row.hash,
                    rev: row.rev,
                    size: row.size
                }).then(response => {
                    if (response.result) {
                        row.collect = !row.collect;
                        this.$notify.success({
                            type: "success",
                            title: "提示",
                            message: row.collect ? "收藏成功:" + row.filename : "取消收藏成功:" + row.filename
                        })
                    } else {
                        console.log(response.msg)
                    }
                })
            },
            handleModify(index, row, cg) {
                //点击修改 判断是否已经保存所有操作
                for (let i of this.toCreateAlbum.list) {
                    if (i.isModify && i.path !== row.path) {
                        this.$message.warning("请先保存当前编辑项");
                        return false;
                    }
                }
                //是否是取消操作
                if (!cg) {
                    if (!this.toCreateAlbum.modifyRow.path) {
                    }
                    return row.isModify = !row.isModify;
                }
                //提交数据
                if (row.isModify) {
                    this.toCreateAlbum.list[index].joyeaDesc = this.toCreateAlbum.modifyRow.joyeaDesc;
                    row.isModify = false;
                } else {
                    this.toCreateAlbum.modifyRow = JSON.parse(JSON.stringify(row));
                    row.isModify = true;
                }
                return false;
            },
            handleDelete(index, row) {
                this.toCreateAlbum.list.splice(index, 1);
            },
            genPreviewUrl(neid, hash, rev) {
                let previewType = 'pic';    // if video is av
                return genSrcPreviewSrc(neid, hash, rev, previewType, this.userInfo.session);
                // return 'https://console.box.lenovo.com/v2/preview_router?type=' + previewType + '&root=databox&path=&path_type=ent&from=&neid='
                //     + neid + '&hash=' + hash + '&rev=' + rev + "&X-LENOVO-SESS-ID=" + this.userInfo.session;
                //+ '&date=' + new Date().getTime();
            },
            handleSaveList() {
                this.$prompt(this.toCreateAlbum.idEditMode ? '当前是编辑模式，重命名清单名称！' : '请输入要保存的清单的名称',
                    this.toCreateAlbum.idEditMode ? '编辑提示' : '保存提示',
                    {
                        confirmButtonText: this.toCreateAlbum.idEditMode ? '重新保存' : '保存',
                        cancelButtonText: '取消',
                        inputValue: this.toCreateAlbum.name
                    }
                ).then(action => {
                    let listName = action.value;
                    this.loading.saveList = true;
                    api({
                        action: 'album',
                        method: this.toCreateAlbum.idEditMode ? 'reSave' : 'save',
                        id: this.toCreateAlbum.editListId,
                        name: listName,
                        src: this.toCreateAlbum.list
                    }).then(response => {
                        if (response.result) {
                            this.$notify.success({
                                title: this.toCreateAlbum.idEditMode ? "编辑结果" : "保存结果",
                                message: this.toCreateAlbum.idEditMode ? "重新保存成功" : '保存成功'
                            })
                        } else {
                            console.log(response.msg);
                            this.$notify.error({
                                title: "保存结果",
                                message: this.toCreateAlbum.idEditMode ? "重新保存失败" : '保存失败'
                            })
                        }
                        this.loading.saveList = false;
                        this.$router.push("/manage/list");
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消保存'
                    });
                });
            },
            handleClearList() {
                this.$alert('当前列表还没有保存，确定清空吗？', '清空提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                        this.toCreateAlbum.list = [];
                    }
                });
            },
            handleGoToPreview(row) {
                let previewType = 'pic';    // if video is av
                let url = genSrcPreviewSrc(row.neid, row.hash, row.rev, previewType, this.userInfo.session);
                window.open(url);
            },
            handleDownloadSrc(isList, row) {
                let _this = this;
                this.loading.fullscreenLoading = true;
                let toDownloadList = [];
                if (isList) {
                    this.toCreateAlbum.list.forEach(src => {
                        toDownloadList.push({
                            filename: src.filename,
                            rev: src.rev,
                            neid: src.neid.toString(),
                            path: src.path,
                            path_type: src.path_type
                        })
                    })
                } else {
                    toDownloadList.push({
                        filename: row.filename,
                        rev: row.rev,
                        neid: row.neid.toString(),
                        path_type: row.path_type
                    })
                }
                api({
                    action: "downloadSrc",
                    src: toDownloadList
                }).then(response => {
                    let taskId = response.id;
                    console.log("获取到下载ID：" + taskId);
                    let retryTime = 0;
                    let timer = 0;
                    timer = setInterval(function () {
                        api({
                            action: "queryDownload",
                            id: taskId
                        }).then(response => {
                            if (response.done) {
                                window.open(window.location.protocol + "//" + window.location.host + "/download/" + taskId);
                                clearInterval(timer);
                                _this.loading.fullscreenLoading = false;
                            } else {
                                retryTime += 1;
                                if (retryTime > 30) {
                                    _this.$notify.info({
                                        title: "下载提示",
                                        message: "当前网速慢，请耐心等待！"
                                    })
                                }else if (retryTime > 30 * 5) {
                                    _this.$notify.error({
                                        title: "下载错误",
                                        message: "下载超时，请稍后再试！"
                                    });
                                    clearInterval(timer);
                                    _this.loading.fullscreenLoading = false;
                                }
                            }
                        });
                    }, 2 * 1000);
                })
            }
        },
        mounted() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
            let toEditList = this.$route.params.toEditList;
            if (toEditList) {
                this.toCreateAlbum.idEditMode = true;
                this.toCreateAlbum.name = toEditList.name;
                this.toCreateAlbum.editListId = toEditList.id;
                toEditList.list.forEach(src => {
                    //注意这里直接使用.，视图会不更新，一定要使用this.$set
                    this.$set(src, "joyeaDesc", src.desc);
                    this.$set(src, "isModify", false);
                    this.toCreateAlbum.list.push(src);
                });
            }
            let _this = this;
            document.onkeydown = function (e) {
                let key = window.event.keyCode;
                if (key === 13) {
                    if (_this.toCreateAlbum.descSelectDialogVisible) {
                        //_this.handleCustomDesc();
                        //_this.$refs.addDialog.close();
                    }
                }
            };
        }
    }
</script>

<style lang="scss" scoped>

    .px10_divider {
        margin-top: 10px;
    }

    .input-with-select .el-input-group__prepend {
        background-color: #fff;
    }

    .el-row {
        margin-bottom: 20px;

        &:last-child {
            margin-bottom: 0;
        }
    }

    .el-col {
        border-radius: 4px;
    }

    .bg-purple-dark {
        background: #99a9bf;
    }

    .bg-purple {
        padding: 5px;

    }

    .grid-content {
        border-radius: 4px;
        min-height: 36px;
    }

    .row-bg {
        padding: 10px 0;
        background-color: #f9fafc;
    }

    .no_display {
        display: none;
    }

    .center_vertical {
        padding: 1px;
        background: #d3dce6;
        text-align: center;
    }

    .load_more_bt {
        width: 100%;
    }

    .preview_img {
        max-height: 50px;
        height: auto;
        width: auto;
    }

</style>
