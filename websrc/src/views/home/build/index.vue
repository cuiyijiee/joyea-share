<template>
    <section>
        <!--工具条-->
        <el-input placeholder="请输入关键字" v-model="search.key" class="input-with-select px10_divider"
                  @keyup.enter.native="handleSearch">
            <el-button slot="append" icon="el-icon-search" v-on:click="handleSearch"/>
        </el-input>
        <el-row :gutter="20" class="px10_divider">
            <el-col :span="13" class="bg-purple">
                <el-tabs v-model="searchTabName" type="card">
                    <el-tab-pane label="网盘目录" name="dir">
                        <!--文件路径显示-->
                        <el-row class="contentHead">
                            <el-col :span="18">
                                <span style=" color:#66b1ff;font-size: 15px;cursor:pointer;"
                                      @click="handleListLenovoDir('/','ent')">根目录</span>
                                <span style="display: inline" v-for="(item,index) in dir.currentPath">
                                    /
                                    <span style=" color:#66b1ff;font-size: 15px;cursor:pointer;"
                                          @click="handleClickDirPath(item,index)">{{item}}</span>
                                </span>
                            </el-col>
                            <el-col :span="3" :offset="3">
                                共{{dir.tableData.length}}个资源
                            </el-col>
                        </el-row>
                        <el-table ref="fileTable"
                                  v-loading="dir.loadingDir"
                                  :data="dir.tableData"
                                  tooltip-effect="dark"
                                  empty-text="文件夹为空"
                                  @row-click="handleClickDirItem"
                                  style="width: 100%">
                            <el-table-column
                                    show-overflow-tooltip
                                    label="文件名">
                                <template slot-scope="scope">
                                    <i v-if="scope.row.is_dir" class="el-icon-folder-opened"></i>
                                    <i v-else-if="scope.row.mime_type.startsWith('video')"
                                       class="el-icon-video-camera"></i>
                                    <i v-else-if="scope.row.mime_type.startsWith('image')"
                                       class="el-icon-picture-outline"></i>
                                    <i v-else-if="scope.row.mime_type.startsWith('doc')" class="el-icon-tickets"></i>
                                    <i v-else class="el-icon-question"></i>
                                    {{' ' + scope.row.path.substr(scope.row.path.lastIndexOf('/')+1)}}
                                    <div v-if="scope.row.tags">
                                        <el-tag style="margin-right: 2px" v-for="tag in scope.row.tags" type="success"
                                                size="mini">{{tag}}
                                        </el-tag>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column label="解说词" width="70">
                                <template slot-scope="scope">
                                    <span>{{scope.row.is_dir ? '-' :scope.row.desc.length}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    label="素材格式"
                                    width="100">
                                <template slot-scope="scope">
                                    {{scope.row.is_dir ? '-' : scope.row.mime_type}}
                                </template>
                            </el-table-column>
                            <el-table-column
                                    label="大小"
                                    width="100">
                                <template slot-scope="scope">
                                    {{scope.row.is_dir ? '-' : scope.row.size}}
                                </template>
                            </el-table-column>
                            <el-table-column
                                    label="操作"
                                    width="180">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.is_dir">-</span>
                                    <span v-else>
                                        <el-button circle type="primary" @click.stop="handleAdd(scope.$index, scope.row)"
                                                   icon="el-icon-plus"/>
<!--                                        <el-button circle type="" icon="el-icon-view"-->
<!--                                                   @click="handleGoToPreview(scope.row)"/>-->
                                        <el-button circle :type="scope.row.collect ? 'warning' : ''"
                                                   @click.stop="handleCollect(scope.$index, scope.row)"
                                                   icon="el-icon-star-off"/>
                                    </span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                    <el-tab-pane label="网盘搜索结果" name="pan">
                        <el-table stripe empty-text="暂没有搜索数据" :data="searchResult" style="width: 100%"
                                  @row-click="handleClickSearch"
                                  v-loading="loading.search">
                            <el-table-column label="素材名" show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <h4 style="margin: 2px">
                                        <i v-if="scope.row.is_dir" class="el-icon-folder-opened"></i>
                                        <i v-else-if="scope.row.mime_type.startsWith('video')"
                                           class="el-icon-video-camera"></i>
                                        <i v-else-if="scope.row.mime_type.startsWith('image')"
                                           class="el-icon-picture-outline"></i>
                                        <i v-else-if="scope.row.mime_type.startsWith('doc')"
                                           class="el-icon-tickets"></i>
                                        <i v-else class="el-icon-question"></i>
                                        {{scope.row.path.substr(scope.row.path.lastIndexOf("/")+1)}}</h4>
                                    {{scope.row.path}}
                                    <div v-if="scope.row.tags">
                                        <el-tag style="margin-right: 2px" v-for="tag in scope.row.tags" type="success"
                                                size="mini">{{tag}}
                                        </el-tag>
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column label="解说词" width="70">
                                <template slot-scope="scope">
                                    <span>{{scope.row.is_dir ? "-" : scope.row.desc.length}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="素材格式" width="100">
                                <template slot-scope="scope">
                                    {{scope.row.is_dir ? "-" : scope.row.mime_type}}
                                </template>
                            </el-table-column>
                            <el-table-column label="大小" width="100">
                                <template slot-scope="scope">
                                    {{scope.row.is_dir ? "-" : scope.row.size}}
                                </template>
                            </el-table-column>
                            <!--                    <el-table-column prop="updator" label="是否有解说词" /> -->
                            <el-table-column label="操作" width="180">
                                <template slot-scope="scope">
                                    <div v-if="scope.row.is_dir">
                                        <el-button circle type="primary" @click.stop="handleClickDirItem(scope.row)"
                                                   icon="el-icon-folder-opened"/>
                                    </div>
                                    <div v-else>
                                        <el-button circle type="primary" @click.stop="handleAdd(scope.$index, scope.row)"
                                                   icon="el-icon-plus"/>
<!--                                        <el-button circle type="" icon="el-icon-view"-->
<!--                                                   v-if="!scope.row.mime_type.startsWith('other')"-->
<!--                                                   @click="handleGoToPreview(scope.row)"/>-->
                                        <el-button circle :type="scope.row.collect ? 'warning' : ''"
                                                   @click.stop="handleCollect(scope.$index, scope.row)"
                                                   icon="el-icon-star-off"/>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                        <el-button class="load_more_bt" :class="{no_display:!search.hasNext}"
                                   :loading="loading.searchMore"
                                   @click.stop="handleLoadMore">加载更多
                        </el-button>
                    </el-tab-pane>
                    <el-tab-pane label="清单搜索结果" name="list">
                        <el-table stripe empty-text="暂没有搜索数据" :data="searchListResult" style="width: 100%"
                                  v-loading="loading.searchList">
                            <el-table-column prop="album_id" label="清单ID" width="100"/>
                            <el-table-column prop="album_name" show-overflow-tooltip label="清单名称"/>
                            <el-table-column prop="user_name" label="创建者" width="150"/>
                            <el-table-column prop="created_at" label="创建时间" width="150"/>
                            <el-table-column prop="refer_num" label="引用数" width="100"/>
                            <el-table-column label="操作" width="100">
                                <template slot-scope="scope">
                                    <el-button type="primary" size="mini" icon="el-icon-view"
                                               @click.stop="handleSeeListDetail(scope.row)"></el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                </el-tabs>
            </el-col>
            <el-col :span="11" class="bg-purple">
                <div class="content_div">
                    <div style="width: 100%" class="center_vertical">
                        <h1>清单编辑列表（{{toCreateAlbum.list.length}}）</h1>
                    </div>
                    <el-table stripe empty-text="清单内还没有内容"
                              row-key="neid" ref="table" id="toSortTable"
                              :data="toCreateAlbum.list" style="width: 100%">
                        <el-table-column label="预览">
                            <template slot-scope="scope">
                                <el-tooltip class="item" effect="dark" :content="scope.row.path" placement="top">
                                    <img v-if="scope.row.mime_type.startsWith('video')" src="play.png"
                                         @click="handleGoToPreview(scope.row)">
                                    <img v-if="scope.row.mime_type.startsWith('doc')" src="doc.png"
                                         @click="handleGoToPreview(scope.row)">
                                    <img v-else-if="scope.row.mime_type.startsWith('image')" class="preview_img"
                                         :onerror="defaultImg"
                                         preview="buildList" :preview-text="scope.row.path"
                                         :src="genPreviewUrl(scope.row.neid,scope.row.hash,scope.row.rev,scope.row.mime_type)">
                                    <img v-else src="unknown.png" @click="handleGoToPreview(scope.row)">
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
                                <el-button type="primary" @click.stop="handleModify(scope.$index, scope.row,true)"
                                           size="mini">
                                    {{scope.row.isModify?'保存':"修改"}}
                                </el-button>
                                <el-button v-if="!scope.row.isModify" @click.stop="handleDelete(scope.$index, scope.row)"
                                           size="mini" type="danger" plain>删除
                                </el-button>
                                <el-button v-else size="mini" @click.stop="handleModify(scope.$index,scope.row,false)"
                                           type="danger" plain>取消
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-row class="load_more_bt" :class="{no_display:toCreateAlbum.list.length === 0}" :gutter="5">
                        <el-col :span="8">
                            <el-button type="primary" @click.stop="handleSaveList" class="load_more_bt"
                                       icon="el-icon-folder-add" :loading="loading.saveList">保存清单
                            </el-button>
                        </el-col>
                        <el-col :span="8">
                            <el-button type="success" @click.stop="handleDownloadSrc(true)" class="load_more_bt"
                                       v-loading.fullscreen.lock="loading.fullscreenLoading"
                                       icon="el-icon-suitcase">下载准备
                            </el-button>
                        </el-col>
                        <el-col :span="8">
                            <el-button type="danger" @click.stop="handleClearList" class="load_more_bt"
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
                <el-button type="primary" @click.stop="handleCustomDesc"
                           style="margin-top: 10px" icon="el-icon-circle-plus">不借鉴</el-button>
            </span>
        </el-dialog>
        <el-dialog :title="'【' + selectListName +'】清单详情'" :visible.sync="visible.listDetailDialogVisible">
            <el-table :data="listDetail" @selection-change="handleSelectListItem" v-loading="loading.listDetailLoading">
                <el-table-column
                        type="selection"
                        width="55">
                </el-table-column>
                <el-table-column label="预览">
                    <template slot-scope="scope">
                        <el-tooltip class="item" effect="dark" :content="scope.row.path" placement="top">
                            <img class="preview_img"
                                 :onerror="defaultImg"
                                 :src="genPreviewUrl(scope.row.neid,scope.row.hash,scope.row.rev,scope.row.mime_type)">
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="解说词">
                    <template slot-scope="scope">
                        <span>{{scope.row.desc.length === 0 ? "暂未设置解说词" : scope.row.desc}}</span>
                    </template>
                </el-table-column>
            </el-table>
            <div slot="footer" class="dialog-footer">
                <el-button @click.stop="visible.listDetailDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click.stop="handleAddListDetailToBuild"
                           :disabled="selectListItem.length === 0">添 加
                </el-button>
            </div>
        </el-dialog>
        <el-dialog :title="toPlayVideo.title" :visible.sync="visible.videoDialogVisible" @close="handleCloseVideo"
                   @opened="playVideo()">
            <video id="myVideo" class="video-js vjs-big-play-centered vjs-fluid" oncontextmenu="return false">
                <p class="vjs-no-js">
                    To view this video please enable JavaScript, and consider upgrading to a
                    web browser that
                    <a href="https://videojs.com/html5-video-support/" target="_blank">
                        supports HTML5 video
                    </a>
                </p>
            </video>
        </el-dialog>
        <el-dialog :title="toPlayImage.title" :visible.sync="visible.imageDialogVisible" @close="handleCloseImage">
            <el-image :src="toPlayImage.url">
                <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                </div>
            </el-image>
        </el-dialog>
    </section>
</template>

<script>
    import api from "../../../api";
    import genSrcPreviewSrc from "../../../utils"
    import Sortable from 'sortablejs';
    import videojs from 'video.js'

    export default {
        name: "index",
        components: {
        },
        data() {
            return {
                dir: {
                    currentPath: [],
                    tableData: [],
                    loadingDir: false
                },
                searchTabName: "dir", // dir ,pan ,list
                search: {
                    type: '',
                    key: '',
                    hasNext: false
                },
                userInfo: {},
                loading: {
                    search: false,
                    searchMore: false,
                    searchList: false,
                    saveList: false,
                    fullscreenLoading: false,
                    listDetailLoading: false,
                },
                searchResult: [],
                searchListResult: [],
                selectListId: 0,
                selectListName: 0,
                selectListItem: [],
                listDetail: [],
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
                    key: '',
                    nextOffset: 0
                },
                visible: {
                    listDetailDialogVisible: false,
                    videoDialogVisible: false,
                    imageDialogVisible: false
                },
                defaultImg: 'this.src="' + require('@assets/error.png') + '"', //默认图地址
                player: null,
                toPlayVideo: {
                    title: '',
                    url: ''
                },
                toPlayImage: {
                    title: '',
                    url: ''
                }
            }
        },
        methods: {
            handlePlayVideo(url, title) {
                this.visible.videoDialogVisible = true;
                this.toPlayVideo.url = url;
                this.toPlayVideo.title = title;
            },
            handlePlayImage(url, title) {
                this.visible.imageDialogVisible = true;
                this.toPlayImage.url = url;
                this.toPlayImage.title = title;
            },
            playVideo() {
                let _this = this;
                if (this.player == null) {
                    videojs(document.getElementById('myVideo'), {
                        controls: true, // 是否显示控制条
                        preload: 'auto',
                        autoplay: false,
                        fluid: true, // 自适应宽高
                        language: 'zh-CN', // 设置语言
                        muted: false, // 是否静音
                        inactivityTimeout: false,
                        controlBar: { // 设置控制条组件
                            children: [
                                {name: 'playToggle'}, // 播放按钮
                                {name: 'currentTimeDisplay'}, // 当前已播放时间
                                {name: 'progressControl'}, // 播放进度条
                                {name: 'durationDisplay'}, // 总时间
                                {
                                    name: 'volumePanel', // 音量控制
                                    inline: false, // 不使用水平方式
                                },
                                {name: 'FullscreenToggle'} // 全屏
                            ]
                        },
                        sources: [ // 视频源
                            {
                                src: _this.toPlayVideo.url,
                                type: 'video/mp4',
                            }
                        ]
                    }, function () {
                        _this.player = this;
                    });
                } else {
                    const data = {
                        src: _this.toPlayVideo.url,
                        type: 'video/mp4'
                    };
                    this.player.pause();
                    this.player.src(data);
                    this.player.load(data);
                    this.player.play();
                }
            },
            handleCloseVideo() {
                if (this.player != null) {
                    this.player.pause();
                }
            },
            handleCloseImage() {
                this.toPlayImage.url = "";
            },
            handleSelectListItem(val) {
                this.selectListItem = val;
            },
            handleAddListDetailToBuild() {
                this.selectListItem.forEach(item => {
                    this.toCreateAlbum.list.push({
                        joyeaDesc: item.desc,
                        path: item.path,
                        neid: item.neid,
                        hash: item.hash,
                        rev: item.rev,
                        mime_type: item.mime_type,
                        filename: item.path.substr(item.path.lastIndexOf("/") + 1),
                        bytes: item.bytes,
                        isModify: false
                    })
                });
                api({
                    action: "referList",
                    albumId: this.selectListId
                }).then(response => {
                    if (response.result) {

                    } else {
                        console.log("引用计数失败：" + response.msg)
                    }
                });
                this.visible.listDetailDialogVisible = false;
            },
            handleSeeListDetail(row) {
                this.visible.listDetailDialogVisible = true;
                this.loading.listDetailLoading = true;
                this.listDetail = [];
                this.selectListItem = [];
                this.selectListId = row.album_id;
                this.selectListName = row.album_name;
                api({
                    action: "listDetail",
                    albumId: row.album_id,
                }).then(response => {
                    if (response.result) {
                        response.list.forEach(item => {
                            this.listDetail.push(item)
                        });
                    } else {
                        _this.$notify.error({
                            title: '查看出错',
                            message: '查看过程出现错误：' + response.msg
                        });
                        console.log(response.msg)
                    }
                    this.loading.listDetailLoading = false;
                })
            },
            handleSearch() {
                let _this = this;
                if (_this.search.key.trim().length === 0) {
                    _this.$message.warning("请输入搜索的关键字！")
                } else {
                    if (_this.searchTabName === 'list') {
                        _this.loading.searchList = true;
                        api({
                            action: "searchList",
                            searchKey: _this.search.key
                        }).then(response => {
                            _this.loading.searchList = false;
                            if (response.result) {
                                _this.searchListResult = [];
                                if(response.list.length === 0){
                                    _this.$message.error("没有搜索到与【" + _this.search.key +"】有关的清单！")
                                }else{
                                    response.list.forEach(list => {
                                        _this.searchListResult.push(list)
                                    })
                                }

                            } else {
                                _this.$notify.error({
                                    title: '搜索出错',
                                    message: '搜索过程出现错误：' + response.msg
                                });
                                console.log(response.msg)
                            }
                        });
                        _this.searchTabName = "list";
                    } else {
                        _this.loading.search = true;
                        api({
                            action: 'search',
                            searchKey: _this.search.key,
                            offset: 0
                        }).then(response => {
                            if (response.result) {
                                _this.search.hasNext = response["has_more"];
                                if (_this.search.hasNext) {
                                    _this.loadMoreForm.key = _this.search.key;
                                    _this.loadMoreForm.nextOffset = response["next_offset"];
                                }
                                _this.searchResult = [];
                                if(response.content.length === 0){
                                    _this.$message.error("没有搜索到与【" + _this.search.key +"】有关的文件或文件夹！")
                                }else{
                                    response.content.forEach(item => {
                                        item.joyeaDesc = "";
                                        item.isModify = false;
                                        _this.searchResult.push(item)
                                    })
                                }
                            } else {
                                _this.$notify.error({
                                    title: '搜索出错',
                                    message: '搜索过程出现错误：' + response.msg
                                });
                                console.log(response.msg)
                            }
                        }).finally(() => {
                            _this.loading.search = false
                        });
                        _this.searchTabName = "pan";
                    }
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
                let isIn = false;
                this.toCreateAlbum.list.forEach(item => {
                    if (item.neid === row.neid) {
                        isIn = true;
                    }
                });
                if (isIn) {
                    this.$confirm('编辑列表中已经存在该记录，是否继续添加?', '提示', {
                        confirmButtonText: '继续添加',
                        cancelButtonText: '取消添加',
                        type: 'warning'
                    }).then(() => {
                        this.toCreateAlbum.list.push(row);
                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '取消添加'
                        });
                    });
                } else {
                    this.toCreateAlbum.list.push(row);
                }
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
                            message: row.collect ? "收藏成功:" + row.path.substr(row.path.lastIndexOf('/') + 1) : "取消收藏成功:" + row.path.substr(row.path.lastIndexOf('/') + 1)
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
            genPreviewUrl(neid, hash, rev, mime_type) {
                let previewType = 'pic';    // if video is av
                if (mime_type.startsWith("doc")) {
                    previewType = 'doc'
                } else if (mime_type.startsWith("video")) {
                    previewType = 'av'
                }
                return genSrcPreviewSrc(neid, hash, rev, previewType, this.userInfo.session);
            },
            handleSaveList() {
                this.$prompt(this.toCreateAlbum.idEditMode ? '当前是编辑模式，重命名清单名称！' : '请输入要保存的清单的名称',
                    this.toCreateAlbum.idEditMode ? '编辑提示' : '保存提示',
                    {
                        confirmButtonText: this.toCreateAlbum.idEditMode ? '重新保存' : '保存清单',
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
                if (row.mime_type.startsWith("doc")) {
                    previewType = 'doc'
                } else if (row.mime_type.startsWith("video")) {
                    previewType = 'av'
                }
                let url = genSrcPreviewSrc(row.neid, row.hash, row.rev, previewType, this.userInfo.session);
                if (row.mime_type.startsWith("video")) {
                    this.handlePlayVideo(url, row.path.substr(row.path.lastIndexOf("/") + 1),);
                }else if(row.mime_type.startsWith("image")){
                    this.handlePlayImage(url, row.path.substr(row.path.lastIndexOf("/") + 1),);
                } else if(row.mime_type.startsWith("doc")) {
                    window.open(url);
                }else{
                    this.$message.error("暂不支持的预览类型！")
                }
            },
            handleDownloadSrc(isList, row) {
                let _this = this;
                let toDownloadList = [];
                let totalBytes = 0;
                if (isList) {
                    let index = 0;
                    this.toCreateAlbum.list.forEach(src => {
                        totalBytes += src.bytes;
                        toDownloadList.push({
                            index: index += 1,
                            filename: src.path.substr(src.path.lastIndexOf("/") + 1),
                            rev: src.rev,
                            neid: src.neid.toString(),
                            path: src.path.replace("+", "%2b"),
                            path_type: src.path_type
                        })
                    })
                } else {
                    toDownloadList.push({
                        filename: row.path.substr(row.path.lastIndexOf("/") + 1),
                        rev: row.rev,
                        neid: row.neid.toString(),
                        path_type: row.path_type
                    })
                }
                let totalKb = totalBytes / 1024;
                let totalMb = totalKb / 1024;
                let warnMb = 300;
                this.$confirm(
                    "您已选中【 " + toDownloadList.length + " 】个文件，" + (totalMb > warnMb
                    ? ("待准备文件列表大小为【 " + totalMb.toFixed(2) + "MB 】,文件较大，建议您分批次准备。")
                    : ("待准备文件列表大小为【 " + (totalMb > 1 ? totalMb.toFixed(2) + "MB" : totalKb.toFixed(2) + "KB") + " 】。")) + "准备完成后会在右上角提示您下载!",

                    '提示',
                    {
                        confirmButtonText: '准备',
                        cancelButtonText: '取消',
                        type: totalMb > warnMb ? "danger" : "primary"
                    }).then(() => {
                    //this.loading.fullscreenLoading = true;
                    api({
                        action: "downloadSrc",
                        src: toDownloadList
                    }).then(response => {
                        let taskId = response.id;
                        console.log("获取到下载ID：" + taskId);
                        this.$store.dispatch('downloadStatus/setVisible', true);
                        this.$notify.success({
                            title: "提示",
                            message: "成功创建下载任务"
                        });
                        let timer = 0;
                        timer = setInterval(function () {
                            api({
                                action: "queryDownload",
                                id: taskId
                            }).then(response => {
                                if (response.done) {
                                    _this.$notify.success({
                                        title: "任务下载提示",
                                        message: "您有一个任务【" + taskId + "】任务下载成功！"
                                    });
                                    clearInterval(timer);
                                    _this.$store.dispatch('downloadStatus/setVisible', false);
                                }
                            });
                        }, 2 * 1000);
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消下载'
                    });
                });
            },
            handleClickDirPath(item, index) {
                let toReachPath = "";
                for (let i = 0; i <= index; i++) {
                    toReachPath = toReachPath + "/" + this.dir.currentPath[i]
                }
                this.handleListLenovoDir(toReachPath, "ent")
            },
            handleListLenovoDir(path, pathType) {
                this.dir.loadingDir = true;
                api({
                    action: 'listLenovoDir',
                    path: path.replace("+", "%2B"),
                    path_type: pathType === undefined ? 'ent' : pathType
                }).then(response => {
                    if (response.result) {
                        this.dir.tableData = [];
                        if (response.data.content) {
                            response.data.content.forEach(item => {
                                item.joyeaDesc = "";
                                item.isModify = false;
                                this.dir.tableData.push(item)
                            });
                            this.dir.currentPath = [];
                            response.data.path.split('/').forEach(item => {
                                if (item.length !== 0) {
                                    this.dir.currentPath.push(item)
                                }
                            });
                        }
                    } else {
                        this.$notify.error({
                            title: '提示',
                            message: '文件夹列表获取失败'
                        });
                        console.log('文件夹列表获取失败' + response.msg)
                    }
                    this.dir.loadingDir = false;
                });
            },
            handleClickDirItem(row, column, event) {
                this.searchTabName = "dir";
                if (row.is_dir) {
                    this.handleListLenovoDir(row.path, 'ent')
                }else{
                    this.handleGoToPreview(row);
                }
            },
            handleClickSearch(row, column, event) {
                if (row.is_dir) {
                    this.handleClickDirItem(row);
                }
            },
        },
        mounted() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
            this.handleListLenovoDir("/", "ent");
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
            const table = document.querySelector('#toSortTable .el-table__body-wrapper tbody');
            const self = this;
            Sortable.create(table, {
                onEnd({newIndex, oldIndex}) {
                    const targetRow = self.toCreateAlbum.list.splice(oldIndex, 1)[0];
                    self.toCreateAlbum.list.splice(newIndex, 0, targetRow)
                }
            });
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
        margin-top: 5px;
        width: 100%;
    }

    .preview_img {
        max-height: 50px;
        height: auto;
        width: auto;
    }

    .contentHead {
        font-size: 12px;
        color: gray;
        margin: 10px auto;
    }

</style>
