<template>
    <section>
        <!--工具条-->
        <el-input placeholder="输入你想搜索的任何内容" v-model="search.key" class="input-with-select px10_divider">
            <el-select v-model="search.type" slot="prepend" placeholder="请选择类型" style="width: 80px"
                       :value="search.type">
                <el-option v-for="item in options.search" :label="item.label" :value="item.value" :key="item.value" :disabled="item.disabled"/>
            </el-select>
            <el-button slot="append" icon="el-icon-search" v-on:click="handleSearch"></el-button>
        </el-input>
        <el-row :gutter="20" class="px10_divider">
            <el-col :span="12" class="bg-purple">
                <div>
                    <div style="width: 100%" class="center_vertical">
                        <h1>搜索结果</h1>
                    </div>
                    <el-table stripe empty-text="暂没有搜索数据" :data="searchResult" style="width: 100%" v-loading="loading.search">
                        <el-table-column prop="path" label="素材名"/>
                        <el-table-column prop="mime_type" label="素材格式" width="150"/>
                        <!--                    <el-table-column prop="updator" label="是否有解说词" /> -->
                        <el-table-column label="操作" width="150">
                            <template slot-scope="scope">
                                <el-button circle type="primary" @click="handleAdd(scope.$index, scope.row)"
                                           icon="el-icon-plus"/>
                                <el-button circle type="warning" @click="handleCollect(scope.$index, scope.row)"
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
                        <h1>清单列表</h1>
                    </div>
                    <el-table stripe empty-text="清单内还没有内容" :data="toCreateAlbum.list" style="width: 100%">
                        <el-table-column label="预览">
                            <template slot-scope="scope">
                                <el-tooltip class="item" effect="dark" :content="scope.row.path" placement="top">
                                    <img :preview="scope.row.path" :preview-text="scope.row.path" class="preview_img"
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
                    <el-row  class="load_more_bt" :class="{no_display:toCreateAlbum.list.length === 0}">
                        <el-col  :span="12">
                            <el-button type="primary" @click="handleSaveList" class="load_more_bt">{{'保存当前清单(' + toCreateAlbum.list.length + ')'}}
                            </el-button>
                        </el-col>
                        <el-col  :span="12">
                            <el-button type="danger" @click="handleClearList" class="load_more_bt" >{{'清空当前清单(' + toCreateAlbum.list.length + ')'}}
                            </el-button>
                        </el-col>
                    </el-row >
                </div>
            </el-col>
        </el-row>
    </section>
</template>

<script>
    import api from "../../../api";

    export default {
        name: "index",
        data() {
            return {
                search: {
                    type: 'pic',
                    key: '仅一',
                    hasNext: false
                },
                userInfo:{

                },
                options: {
                    search: [
                        {value: 'pic', label: '图片'},
                        {value: 'video', label: '视频',disabled: false},
                        {value: '', label: '全部',disabled: false}
                    ]
                },
                loading: {
                    search: false,
                    searchMore: false
                },
                searchResult: [],
                toCreateAlbum: {
                    name: '',
                    list: [],
                    modifyRow: ''
                },
                loadMoreForm: {
                    type: '',
                    key: '',
                    nextOffset: 0
                }
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
                this.toCreateAlbum.list.push(row);
            },
            handleCollect(index, row) {

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
                return 'https://console.box.lenovo.com/v2/preview_router?type=' + previewType +'&root=databox&path=&path_type=ent&from=&neid='
                    + neid + '&hash=' + hash + '&rev=' + rev + "&X-LENOVO-SESS-ID=" + this.userInfo.session;
                //+ '&date=' + new Date().getTime();
            },
            handleSaveList() {
                this.$prompt('请输入清单的名称', '保存提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(({ value }) => {
                    this.$notify.success({
                        title:"保存结果",
                        message:'保存成功'
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消保存'
                    });
                });
            },
            handleClearList(){
                this.$alert('当前列表还没有保存，确定清空吗？', '清空提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                        this.toCreateAlbum.list = [];
                    }
                });
            }
        },
        mounted() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
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
        height: auto;
        width: 100%;
    }

</style>
