<template>
    <section id="build">
        <!--工具条-->
        <div class="main-tools">
            <el-input v-model="search.key" class="my-input" placeholder="请输入关键字"
                      @keyup.enter.native="handleSearch">
                <el-button slot="append" class="search-button"
                           icon="el-icon-search" style=""
                           v-on:click="handleSearch"/>
            </el-input>
            <div style="padding:15px 0;color: #808080">热门搜索:
                <span v-for="key in topSearchKey" style="padding: 15px;cursor: pointer"
                      @click="handleClickTopSearchKey(key)"><u><b>{{ key }}</b></u></span>
            </div>
        </div>
        <el-drawer ref="drawer" :visible.sync="menuDrawerVisible" custom-class="demo-drawer"
                   direction="ltr" title="目录菜单">
            <div class="demo-drawer__content">
                <lenovo-dir-drawer/>
            </div>
        </el-drawer>
        <div v-if="directoryType.length === 0">
            <space-selector @onDirectoryTypeSelected="handleDirectoryTypeSelected"/>
        </div>
        <div v-else-if="curDirNeid === '541796009' && toCreateAlbum.list.length === 0"
             style="height:1080px;padding: 0 40px;background: #d1d1d1;">
            <div v-loading="dir.loadingDir || loading.search"
                 element-loading-background="rgba(209, 209, 209)"
                 style="padding: 10px 10px 0 10px;height: 100%; ">
                <el-row :gutter="10" align="middle" justify="center">
                    <el-col v-for="(menu,index) in menuPath" :key="index" class="card-panel-col"
                            :lg="{span: '4-8'}" :md="{span: '4-8'}" :sm="12" :xl="{span: '4-8'}" :xs="12">
                        <div class="menu-content" style="border: #000000 3px" @click="handleListLenovoDir(menu.path)">
                            <img :src="menu.icon" style=""/>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </div>
        <el-row v-else :gutter="20" style="margin:0 40px;padding: 10px 0 0 0;height:1080px;">
            <el-col :span="4" class="bg-purple">
                <directory-tree @handleClickItem="handleOpenDir"></directory-tree>
            </el-col>
            <el-col :span="16" class="bg-purple">
                <div class="directory-tools">
                    <el-button icon="iconfont el-icon-a-icon_getshortchain" size="mini" type="primary"
                               @click="handleGetCurRedirectPath">获取短链
                    </el-button>
                    <el-button v-if="hasBtnShowPermission(null,'WORD_MANAGER')"
                               size="mini" type="primary"
                               @click="visible.addWordDialogVisible = !visible.addWordDialogVisible">
                        管理小白板
                    </el-button>
                    <add-src-to-private-dir v-if="hasBtnShowPermission(null,'NEW_PRIVATE_DIR')" :curDirNeid="curDirNeid"
                                            @onAddSuccess="handleRefreshDir" @preview="handleClickDirItem"/>
                    <add-private-directory v-if="hasBtnShowPermission(null,'NEW_PRIVATE_DIR')"
                                           :curDirNeid="curDirNeid" @onAddSuccess="handleRefreshDir"/>
                </div>
                <!--文件路径显示-->
                <el-row class="adminContentHead">
                    <el-col :span="15" style="color:#000000;font-size: 15px;">
                        <span style="cursor:pointer;"
                              @click="handleBackMenuPath"><b>返回</b></span>
                        <span
                            style="cursor:pointer;margin: 0 8px;position: relative;top: -0.1em;"
                            @click="handleGoRootPath">|</span>
                        <span style="cursor:pointer;"
                              @click="handleGoRootPath">{{
                                directoryType === 'SELF' ? '细分市场素材库' : '基础素材库'
                            }}</span>
                        <span v-for="(item,index) in dir.currentPath" v-if="item !== '营销素材展示'"
                              style="display: inline">
                            <span style="position: relative;top: -0.025em;margin: 0 4px;font-size: 15px;">/</span>
                            <span style="cursor:pointer;"
                                  @click="handleClickDirPath(item,index)">{{ item }}</span>
                        </span>
                    </el-col>
                    <el-col :span="9" style="text-align: right">
                        <span>
                            共{{ dir.tableData.length }}个资源
                        </span>
                        <span>
                            <el-button size="small" type="primary"
                                       @click="handleGetCurRedirectPath">获取短链</el-button>
                        </span>
                    </el-col>
                </el-row>
                <div class="adminContentHead"
                     style="text-align: right">

                </div>
                <el-table ref="fileTable" v-loading="dir.loadingDir || loading.search" :data="dir.tableData"
                          empty-text="文件夹为空" style="width: 100%;" tooltip-effect="dark"
                          @row-click="handleClickDirItem">
                    <el-table-column label="文件名" show-overflow-tooltip>
                        <template slot-scope="scope">
                            <div style="">
                                <i v-if="scope.row.is_dir" class="el-icon-folder-opened"></i>
                                <i v-else-if="scope.row.mime_type.startsWith('video')" class="el-icon-video-camera"></i>
                                <img v-else-if="scope.row.mime_type.startsWith('image')"
                                     :onerror="defaultImg"
                                     :preview-text="scope.row.path"
                                     :src="genPreviewUrl(scope.row.neid)"
                                     fit="contain" preview="dir_image_list"
                                     style="width: 120px; height: 90px; line-height: 30px; background-color:#DCDCDC"/>
                                <i v-else-if="scope.row.mime_type.startsWith('doc')" class="el-icon-tickets"></i>
                                <i v-else-if="scope.row.mime_type.startsWith('word')" class="el-icon-link"></i>
                                <i v-else class="el-icon-question"></i>
                                <span v-if="scope.row.mime_type && scope.row.mime_type.startsWith('word')"
                                      style="vertical-align:center;color: #333333">
                                    {{ ' ' + scope.row.path }}</span>
                                <span v-else style="vertical-align:center;color: #333333">
                                    {{ ' ' + scope.row.file_name }}</span>
                                <div v-if="scope.row.desc">
                                    <el-tag v-for="(tag,index) in scope.row.desc.split(' ')" size="mini"
                                            style="margin-right: 2px" :key="index"
                                            type="info">{{ tag.replace(markReg, "") }}
                                    </el-tag>
                                </div>
                                <div v-else-if="scope.row.tags">
                                    <el-tag v-for="(tag,index) in scope.row.tags" size="mini"
                                            style="margin-right: 2px" :key="index"
                                            type="info">{{ tag.name.replace(markReg, "") }}
                                    </el-tag>
                                </div>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column v-if="directoryType === 'SELF'" align="center" label="管理员">
                        <template slot-scope="scope">
                            <PrivateDirectoryAdminManager
                                v-if="scope.row.is_dir && curDirNeid === '0'"
                                :enabled="userInfo.isAdmin && directoryType === 'SELF'"
                                :file-item="scope.row"/>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="引用次数" width="80">
                        <template slot-scope="scope">
                            <span>{{ scope.row.is_dir ? '-' : scope.row.ref_num }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="下载次数" width="80">
                        <template slot-scope="scope">
                            <span>{{ scope.row.is_dir ? '-' : scope.row.download_num }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column align="center" label="操作" width="160">
                        <template slot-scope="scope">
                            <span v-if="!scope.row.is_dir && !scope.row.mime_type.startsWith('word')">
                                <el-button circle icon="el-icon-plus" type=""
                                           @click.stop="handleAdd(scope.row)"/>
                            </span>
                            <span v-if="hasBtnShowPermission(scope.row,'TRANSCODE')">
                                <el-button circle icon="el-icon-link"
                                           @click.stop="handleAddTranscodeVideo(scope.$index, scope.row)"/>
                            </span>
                            <RenamePrivateDirectory v-if="hasBtnShowPermission(scope.row,'RENAME')"
                                                    :file-item="scope.row"
                                                    @onModifySuccess="handleRefreshDir"/>
                            <span v-if="hasBtnShowPermission(scope.row,'RENAME_SRC')">
                                <el-button circle icon="el-icon-edit"
                                           @click.stop="handleUpdateAlias(scope.$index, scope.row)">
                                </el-button>
                            </span>
                            <el-button
                                v-if="hasBtnShowPermission(scope.row,'PRIVATE_DIR_REMOVE_SRC')"
                                circle
                                icon="el-icon-delete" @click.stop="handleRemoveSrc(scope.$index, scope.row)">
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </el-col>
            <!--右边清单操作-->
            <el-col :span="4" class="bg-purple">
                <div class="content_div">
                    <div class="center_vertical" style="width: 100%">
                        <h1>清单编辑列表（{{ toCreateAlbum.list.length }}）</h1>
                    </div>
                    <el-table id="toSortTable" ref="table"
                              :data="toCreateAlbum.list" empty-text="清单内还没有内容" row-key="neid"
                              stripe style="width: 100%">
                        <el-table-column label="预览">
                            <template slot-scope="scope">
                                <el-tooltip :content="scope.row.path.substr(scope.row.path.lastIndexOf('/')+1)"
                                            class="item"
                                            effect="dark"
                                            placement="top">
                                    <img v-if="scope.row.mime_type.startsWith('video')" class="preview_img"
                                         src="video.png" @click="handleGoToPreview(scope.row)">
                                    <img v-else-if="scope.row.mime_type.startsWith('doc')"
                                         :src="handleGetDocumentImage(scope.row.mime_type)"
                                         class="preview_img" @click="handleGoToPreview(scope.row)">
                                    <img v-else-if="scope.row.mime_type.startsWith('image')" :onerror="defaultImg"
                                         :preview-text="scope.row.path"
                                         :src="genPreviewUrl(scope.row.neid)" class="preview_img"
                                         preview="build_image_list">
                                    <img v-else class="preview_img" src="unknown.png"
                                         @click="handleGoToPreview(scope.row)">
                                </el-tooltip>
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" width="150">
                            <template slot-scope="scope">
                                <el-button v-if="!scope.row.isModify"
                                           plain
                                           size="mini" type="" @click.stop="handleDelete(scope.$index, scope.row)">删除
                                </el-button>
                                <el-button v-else plain size="mini"
                                           type="" @click.stop="handleModify(scope.$index,scope.row,false)">取消
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-row :class="{no_display:toCreateAlbum.list.length === 0}" :gutter="5" class="load_more_bt">
                        <el-col :span="8">
                            <el-button :loading="loading.saveList" class="load_more_bt" icon="el-icon-folder-add"
                                       size="mini"
                                       type="info" @click.stop="handleSaveList">保存清单
                            </el-button>
                        </el-col>
                        <el-col :span="8">
                            <el-button v-loading="loading.downloadLoading" class="load_more_bt" icon="el-icon-suitcase"
                                       size="mini" type="info"
                                       @click.stop="handleDownloadSrc(true)">下载准备
                            </el-button>
                        </el-col>
                        <el-col :span="8">
                            <el-button class="load_more_bt" icon="el-icon-delete" size="mini" type="info"
                                       @click.stop="handleClearList">清空
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
            </el-col>
        </el-row>
        <el-dialog ref="addDialog" :visible.sync="toCreateAlbum.descSelectDialogVisible" center title="选择已有解说词">
            <span class="dialog-footer">
                <img :src="toCreateAlbum.previewUrl" class="preview_img" style="margin: 0 auto">
            </span>
            <el-table :data="toCreateAlbum.toSelectDesc" empty-text="暂无可用解说词" style="width: 70%"
                      @row-click="handleSelectDesc">
                <el-table-column prop="desc"></el-table-column>
            </el-table>
            <span slot="footer" class="dialog-footer">
                <el-button icon="el-icon-circle-plus" style="margin-top: 10px"
                           type="primary" @click.stop="handleCustomDesc">不借鉴</el-button>
            </span>
        </el-dialog>
        <el-dialog :title="'【' + selectListName +'】清单详情'" :visible.sync="visible.listDetailDialogVisible">
            <el-table v-loading="loading.listDetailLoading" :data="listDetail" @selection-change="handleSelectListItem">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column label="预览">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.path" class="item" effect="dark" placement="top">
                            <img :onerror="defaultImg"
                                 :src="genPreviewUrl(scope.row.neid)"
                                 class="preview_img">
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>
            <div slot="footer" class="dialog-footer">
                <el-button @click.stop="visible.listDetailDialogVisible = false">取 消</el-button>
                <el-button :disabled="selectListItem.length === 0" type="primary"
                           @click.stop="handleAddListDetailToBuild">添 加
                </el-button>
            </div>
        </el-dialog>
        <el-dialog :title="toPlayVideo.title" :visible.sync="visible.videoDialogVisible" @close="handleCloseVideo"
                   @opened="initVideoPlayer()">
            <video id="myVideo" class="video-js vjs-big-play-centered vjs-16-9"
                   oncontextmenu="return false">
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
        <search-result-dialog
            ref="searchDialog"
            :directoryType="directoryType"
            @addSrcToPrivateDir="handleAddSrcToPrivateDir"
            @batchAddSrcToPrivateDir="batchAddSrcToPrivateDir"
            @goToDir="handleClickSearch"
            @handleAdd="handleAdd"
            @handleBatchAdd="handleBatchAdd"
        >
        </search-result-dialog>
        <el-dialog :close-on-click-modal="false" :visible.sync="visible.addWordDialogVisible" title="小白板管理"
                   @open="handleFilterCurDirWordList">
            <div style="text-align: right">
                <el-button class="search-button" size="small" @click="handleSaveWordToDir">&nbsp;&nbsp;&nbsp;保 存&nbsp;&nbsp;&nbsp;</el-button>
            </div>
            <el-row>
                <el-col :span="12">
                    <el-input v-model="wordListSearchText" placeholder="请输入关键字" style="width: auto">
                    </el-input>
                    <el-button icon="el-icon-search" @click="handleGetMyWordList"></el-button>
                    <el-table v-loading="wordListLoading" :data="wordListOption" style="width: 100%">
                        <el-table-column label="待选小白板" prop="title">
                        </el-table-column>
                        <el-table-column fixed="right" label="操作" width="100">
                            <template slot-scope="scope">
                                <el-button size="small" type="text" @click="handleAddWordListRow(scope.row)">添加
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <el-pagination :current-page.sync="wordListPageNum" :page-size="wordListPageSize"
                                   :total=wordListTotal
                                   background layout="prev, pager, next"
                                   @current-change="handleGetMyWordList">
                    </el-pagination>
                </el-col>
                <el-col :span="12">
                    <el-table :data="wordListSelected" style="width: 100%">
                        <el-table-column label="已选小白板" prop="title">
                        </el-table-column>
                        <el-table-column fixed="right" label="操作" width="100">
                            <template slot-scope="scope">
                                <el-button size="small" type="text" @click="handleDeleteWordListRow(scope.row)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
        </el-dialog>
    </section>
</template>

<script>
import api, {
    addRedirectPath,
    addTranscodeVideo,
    addWordToDir,
    batchNewPrivateDirSrc,
    ftsSearch,
    getFileMetadata,
    getMyWordList,
    getTopSearchKey,
    newPrivateDirSrc,
    prepareDownloadFile,
    queryDownload,
    removePrivateDir,
    removePrivateDirSrc,
    updateSrcAlias
} from "../../../api";
import genSrcPreviewSrc, {getVideoPreviewUrl} from "../../../utils"
import Sortable from 'sortablejs';
import videojs from 'video.js'
import {getDocumentImage, getFileNameWithoutExtension, getQueryParam, joyeaMenuPath} from "@/utils/JoyeaUtil";
import {mapGetters} from "vuex";
import LenovoDirSelector from "@/components/LenovoDirSelector";
import LenovoDirDrawer from "@/components/LenovoDirDrawer";
import AddPrivateDirectory from "@/components/AddPrivateDirectory";
import AddSrcToPrivateDir from "@/components/AddSrcToPrivateDir";
import RenamePrivateDirectory from "@/components/RenamePrivateDirectory";
import PrivateDirectoryAdminManager from "@/components/PrivateDirectoryAdminManager";
import SpaceSelector from "@/components/SpaceSelector";
import SearchResultDialog from "@/components/SearchResultDialog"
import DirectoryTree from "@/components/DirectoryTree.vue";

let videoLoading = require("@/assets/video-loading.jpg")

export default {
    name: "index",
    components: {
        LenovoDirSelector,
        LenovoDirDrawer,
        AddPrivateDirectory,
        AddSrcToPrivateDir,
        RenamePrivateDirectory,
        PrivateDirectoryAdminManager,
        SpaceSelector,
        SearchResultDialog,
        DirectoryTree
    },
    data() {
        return {
            directoryType: "",
            newPrivateDirName: "",
            parentDirId: "",
            menuDrawerVisible: false,
            dir: {
                currentPath: [],
                tableData: [],
                loadingDir: false
            },
            markReg: /<mark>|<\/mark>/g,
            search: {
                type: '',
                key: '',
                hasNext: false
            },
            loading: {
                search: false,
                searchMore: false,
                searchList: false,
                saveList: false,
                downloadLoading: false,
                listDetailLoading: false,
            },
            menuPath: joyeaMenuPath,
            searchResult: [],
            searchListResult: [],
            selectListId: 0,
            selectListName: 0,
            selectListItem: [],
            listDetail: [],
            topSearchKey: [],
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
                videoPrepared: false,
                imageDialogVisible: false,
                searchDialogVisible: false,
                addWordDialogVisible: false,
                addPrivateDirVisible: false
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
            },
            wordListLoading: false,
            wordListOption: [],
            wordListTotal: 0,
            wordListPageSize: 10,
            wordListPageNum: 1,
            wordListSearchText: "",
            wordListSelected: [],
            curDirNeid: "",
            curDirAdminUser: [],
            videoPreviewPromise: Object,
            videoPreviewAbort: Object
        }
    },
    computed: {
        ...mapGetters({
            'userInfo': 'userInfo/userInfo'
        })
    },
    watch: {
        directoryType() {
            this.$EventBus.$emit("directoryTypeChanged", this.directoryType);
        }
    },
    methods: {
        hasBtnShowPermission(fileItem, mode) {
            let hasPermission = false;
            try {
                switch (mode) {
                    case "PRIVATE_DIR_REMOVE_SRC":
                        hasPermission = this.directoryType === "SELF"
                            && (this.userInfo.isAdmin || this.curDirAdminUser.filter(item => this.userInfo.email === item.joyeaId).length > 0)
                            && !fileItem.mime_type.startsWith("word");
                        break;
                    // case "PRIVATE_DIR_DOWNLOAD":
                    //     hasPermission = this.directoryType === "SELF" && fileItem.is_dir
                    //     break;
                    case "NEW_PRIVATE_DIR":
                        hasPermission = this.directoryType === "SELF"
                            && (this.userInfo.isAdmin || this.curDirAdminUser.filter(item => this.userInfo.email === item.joyeaId).length > 0);
                        break;
                    case "WORD_MANAGER":
                        hasPermission = (this.userInfo.isAdmin || this.curDirAdminUser.filter(item => this.userInfo.email === item.joyeaId).length > 0);
                        break;
                    case "MANAGER":
                        hasPermission = this.directoryType === "SELF" && fileItem.path === '/' + fileItem.file_name
                            && fileItem.is_dir
                            && (this.userInfo.isAdmin || fileItem.adminUser.filter(item => this.userInfo.email === item.joyeaId).length > 0);
                        break;
                    case "RENAME":
                        hasPermission =
                            this.directoryType === "SELF" && fileItem.is_dir &&
                            (this.userInfo.isAdmin || this.curDirAdminUser.filter(item => this.userInfo.email === item.joyeaId).length > 0)
                        break;
                    case "RENAME_SRC":
                        hasPermission =
                            this.directoryType === "SELF" && !fileItem.is_dir && fileItem.mime_type !== 'word' &&
                            (this.userInfo.isAdmin || this.curDirAdminUser.filter(item => this.userInfo.email === item.joyeaId).length > 0)
                        break;
                    case "TRANSCODE":
                        hasPermission = this.userInfo.isAdmin
                            && !fileItem.is_dir
                            && (fileItem.mime_type && fileItem.mime_type.startsWith('video'))
                        break;
                    default:
                        break
                }
            } catch (e) {
                hasPermission = false;
            }
            //console.log(mode + " - " + hasPermission + " - " + JSON.stringify(fileItem));
            return hasPermission;
        },
        handleGetCurRedirectPath() {
            let _this = this;
            let currentFullPath = "";
            this.dir.currentPath.forEach(tmp => {
                currentFullPath = currentFullPath + "/" + tmp
            })
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
        handleGoToPreview(row) {
            if (row.mime_type.startsWith("video")) {
                this.handlePlayVideo(row);
                return
            } else if (row.mime_type.startsWith("word")) {
                let nextPlusToken = localStorage.getItem("nextx_token");
                let url = "https://m.nextxx.cn/fullscreen/#/main/pc/boardDetail?access_token=" + nextPlusToken + "&id=" + row.neid;
                window.open(url);
                return;
            }
            let url = genSrcPreviewSrc(row.neid);
            if (row.mime_type.startsWith("video")) {
            } else if (row.mime_type.startsWith("image")) {
            } else if (row.mime_type.startsWith("doc")) {
                window.open(url);
            } else {
                this.$message.error("暂不支持的预览类型！")
            }
        },
        handlePlayVideo(row) {
            this.visible.videoDialogVisible = true;
            this.visible.videoPrepared = false;
            let title = row.path.substr(row.path.lastIndexOf("/") + 1);
            const {promise, abort} = getVideoPreviewUrl(row.neid, 30)
            this.videoPreviewPromise = promise;
            this.videoPreviewAbort = abort;
            let _this = this;
            this.videoPreviewPromise.then(videoUrl => {
                _this.visible.videoPrepared = true;
                _this.toPlayVideo.url = videoUrl;
                _this.toPlayVideo.title = title;
                if (_this.visible.videoDialogVisible) {
                    _this.playVideo(videoUrl);
                }
            })
        },
        handleClickTopSearchKey(key) {
            this.handleSearch(key);
        },
        handleGetDocumentImage(mimeType) {
            return getDocumentImage(mimeType)
        },
        initVideoPlayer() {
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
                    poster: videoLoading,
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
                    sources: []
                }, function () {
                    _this.player = this;
                });
            } else {
                _this.player.poster(videoLoading)
            }
        },
        playVideo(videoUrl) {
            const data = {
                src: videoUrl,
                type: 'video/mp4'
            };
            this.player.pause();
            this.player.src(data);
            this.player.load(data);
            this.player.play();
        },
        handleCloseVideo() {
            if (this.videoPreviewAbort) {
                this.videoPreviewAbort();
            }
            if (this.player != null) {
                this.player.reset();
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
            let _this = this;
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
        handleSearch(searchKey) {
            if (searchKey !== undefined && typeof (searchKey) == 'string') {
                this.search.key = searchKey;
            }
            if (this.search.key.trim().length === 0) {
                this.$message.warning("请输入搜索的关键字！");
                return
            }
            this.$refs.searchDialog.handleSearch(this.search.key);
        },
        handleLoadMore() {
            let _this = this;
            if (_this.loadMoreForm.key.trim().length !== 0) {
                _this.loading.searchMore = true;
                ftsSearch(_this.loadMoreForm.key, this.loadMoreForm.nextOffset).then(response => {
                    if (response.code === "0") {
                        _this.search.hasNext = response.obj["has_more"];
                        if (_this.search.hasNext) {
                            _this.loadMoreForm.key = _this.search.key;
                            _this.loadMoreForm.nextOffset = response.obj["next_offset"];
                        }
                        //_this.searchResult = [];
                        response.obj.content.forEach(item => {
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
        handleAddTranscodeVideo(index, row) {
            addTranscodeVideo(row.neid).then(resp => {
                if (resp.code === "0") {
                    this.$message.success("添加成功！")
                } else {
                    this.$message.error("添加失败：" + resp.msg);
                }
            })
        },
        handleDownloadPrivateDir(index, row) {
            getFileMetadata(this.directoryType, row.path, "").then(response => {
                if (!response.obj.content) {
                    this.$message.error("当前文件夹可供下载文件为空！");
                    return;
                }
                response.obj.content = response.obj.content.filter(item => !item.is_dir && item.mime_type !== 'word')
                if (response.obj.content.length > 0) {
                    let _this = this;
                    let toDownloadList = [];
                    let totalBytes = 0;
                    let index = 0;
                    response.obj.content.forEach(src => {
                        totalBytes += src.bytes;
                        toDownloadList.push({
                            index: index += 1,
                            filename: src.file_name,
                            neid: src.neid,
                            path_type: "ent",
                            rev: "",
                            path: "",
                        })
                    })
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
                        this.loading.downloadLoading = true;
                        prepareDownloadFile(toDownloadList).then(resp => {
                            let taskId = resp.data;
                            this.$store.dispatch('downloadStatus/setVisible', true);
                            this.$notify.success({
                                title: "提示",
                                message: "成功创建下载任务"
                            });
                            let timer = 0;
                            timer = setInterval(function () {
                                queryDownload(taskId).then(resp => {
                                    if (resp.data && resp.data.status) {
                                        _this.$notify.success({
                                            title: "任务下载提示",
                                            message: "您有一个下载任务【" + getFileNameWithoutExtension(resp.data.firstSrcName) + ".zip】已准备好！"
                                        });
                                        clearInterval(timer);
                                        _this.$store.dispatch('downloadStatus/setVisible', false);
                                    }
                                })
                            }, 2 * 1000);
                        }).catch(() => {
                            this.$message({
                                type: 'info',
                                message: '已取消下载'
                            });
                        }).finally(() => {
                            this.loading.downloadLoading = false;
                        })
                    })
                } else {
                    this.$message.error("当前文件夹可供下载文件为空！");
                    return;
                }
            });
        },
        handleRemoveSrc(index, row) {
            if (row.is_dir) {
                this.$confirm('即将删除文件夹【' + row.file_name + '】', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    removePrivateDir(row.neid).then(resp => {
                        if (resp.code === '0') {
                            this.$message.success("删除成功！");
                            this.handleRefreshDir();
                        } else {
                            this.$message.error("删除失败：" + resp.msg);
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            } else {
                this.$confirm('即将删除文件【' + row.file_name + '】', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    removePrivateDirSrc(this.curDirNeid, row.neid).then(resp => {
                        if (resp.code === '0') {
                            this.$message.success("删除成功！");
                            this.handleRefreshDir();
                        } else {
                            this.$message.error("删除失败：" + resp.msg);
                        }
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            }
        },
        handleBatchAdd(multiRow) {
            if (this.directoryType === '') {
                this.$refs.searchDialog.close();
                this.handleDirectoryTypeSelected("LENOVO");
            }
            if (multiRow instanceof Array && multiRow.length > 0) {
                multiRow.forEach(item => {
                    this.handleAdd(item, true);
                });
                this.$message.success("成功添加【" + multiRow.length + "】条数据到清单！")
            }
        },
        handleAdd(row, needFilter) {
            let isIn = false;
            this.toCreateAlbum.list.forEach(item => {
                if (item.neid === row.neid) {
                    isIn = true;
                }
            });
            if (isIn && needFilter) {
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
            this.toCreateAlbum.descSelectDialogVisible = false;
        },
        handleCollect(index, row) {
            api({
                action: "srcCollect",
                method: row.collect ? 'unCollect' : 'collect',
                neid: row.neid,
                path: row.path,
                type: row.mime_type,
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
        genPreviewUrl(neid) {
            return genSrcPreviewSrc(neid);
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
        handleOpenDir(directoryType, path) {
            this.directoryType = directoryType;
            this.handleListLenovoDir(path);
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
                        neid: src.neid,
                        path_type: src.path_type,
                        rev: "",
                        path: "",
                    })
                })
            } else {
                toDownloadList.push({
                    filename: row.path.substr(row.path.lastIndexOf("/") + 1),
                    rev: row.rev,
                    neid: row.neid.toString(),
                    path_type: row.path_type,
                    path: ""
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
                this.loading.downloadLoading = true;
                prepareDownloadFile(toDownloadList).then(resp => {
                    let taskId = resp.data;
                    this.$store.dispatch('downloadStatus/setVisible', true);
                    this.$notify.success({
                        title: "提示",
                        message: "成功创建下载任务"
                    });
                    let timer = 0;
                    timer = setInterval(function () {
                        queryDownload(taskId).then(resp => {
                            if (resp.data && resp.data.status) {
                                _this.$notify.success({
                                    title: "任务下载提示",
                                    message: "您有一个下载任务【" + getFileNameWithoutExtension(resp.data.firstSrcName) + ".zip】已准备好！"
                                });
                                clearInterval(timer);
                                _this.$store.dispatch('downloadStatus/setVisible', false);
                            }
                        })
                    }, 2 * 1000);
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消下载'
                    });
                }).finally(() => {
                    this.loading.downloadLoading = false;
                })
            })
        },
        handleClickDirPath(item, index) {
            let toReachPath = "";
            for (let i = 0; i <= index; i++) {
                toReachPath = toReachPath + "/" + this.dir.currentPath[i]
            }
            this.handleListLenovoDir(toReachPath === '' ? "/" : toReachPath);
        },
        handleDirectoryTypeSelected(type) {
            this.directoryType = type;
            this.handleGoRootPath();
            const table = document.querySelector('#toSortTable .el-table__body-wrapper tbody');
            const self = this;
            if (table) {
                Sortable.create(table, {
                    onEnd({newIndex, oldIndex}) {
                        const targetRow = self.toCreateAlbum.list.splice(oldIndex, 1)[0];
                        self.toCreateAlbum.list.splice(newIndex, 0, targetRow);
                    }
                });
            }
        },
        handleListLenovoDir(path) {
            this.dir.loadingDir = true;
            if (this.directoryType.length === 0) {
                return;
            }
            getFileMetadata(this.directoryType, path, "").then(response => {
                if (response.code === "0") {
                    this.curDirNeid = response.obj.neid;
                    this.curDirAdminUser = response.obj.adminUser;
                    console.log("current dir neid:" + this.curDirNeid);
                    this.dir.tableData = [];
                    this.wordListSelected = [];
                    this.parentDirId = response.obj.neid;
                    if (response.obj.content) {
                        response.obj.content.forEach(item => {
                            item.joyeaDesc = "";
                            item.isModify = false;
                            if (item.mime_type && item.mime_type === 'word' && !this.userInfo.isAdmin) {
                            } else {
                                this.dir.tableData.push(item)
                            }
                        });
                        this.dir.currentPath = [];
                        response.obj.path.split('/').forEach(item => {
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
            }).finally(() => {
                //2021.11.20-侍-要求点开文件夹回到顶部！
                document.getElementById("build").scrollTop = 0;
            });
        },
        handleClickDirItem(row) {
            this.visible.searchDialogVisible = false;
            if (row.is_dir) {
                this.handleListLenovoDir(row.path)
            } else {
                this.handleGoToPreview(row);
            }
        },
        handleClickSearch(row, column, event) {
            if (row.is_dir) {
                this.directoryType = "LENOVO";
                this.handleClickDirItem(row);
            }
        },
        handleGetTopSearchKey() {
            getTopSearchKey().then(resp => {
                this.topSearchKey = resp.data;
            })
        },
        handleFilterCurDirWordList() {
            this.handleGetMyWordList();
            this.dir.tableData.forEach(item => {
                if (item.mime_type && item.mime_type.startsWith("word")) {
                    this.wordListSelected.push({
                        id: item.neid,
                        title: item.path
                    });
                }
            })
        },
        handleAddWordListRow(row) {
            let existedList = this.wordListSelected.filter(function (item) {
                return item.id === row.id;
            })
            if (existedList.length > 0) {
                this.$notify.error({
                    title: '提示',
                    message: '请勿重复添加！'
                });
            } else {
                this.wordListSelected.push(row);
            }
        },
        handleDeleteWordListRow(row) {
            this.wordListSelected = this.wordListSelected.filter(function (item) {
                return item.id !== row.id;
            })
        },
        handleSaveWordToDir() {
            addWordToDir(this.curDirNeid, this.wordListSelected).then(resp => {
                if (resp.code === 2000) {
                    this.visible.addWordDialogVisible = false;
                    this.handleRefreshDir();
                } else {
                    this.$notify.error({
                        title: '提示',
                        message: '保存小白板失败！'
                    });
                }
            })
        },
        handleRefreshDir() {
            this.handleClickDirPath(undefined, this.dir.currentPath.length - 1);
        },
        handleGetMyWordList() {
            this.wordListLoading = true;
            getMyWordList(this.wordListSearchText, this.wordListPageSize, this.wordListPageNum - 1).then(resp => {
                this.wordListOption = resp.data.data;
                this.wordListTotal = resp.data.total;
                //this.wordListPageSize = resp.data.pageSize;
                this.wordListPageNum = resp.data.pageNum + 1;
            }).finally(() => {
                this.wordListLoading = false;
            })
        },
        handleGoRootPath() {
            if (this.directoryType === "SELF") {
                this.handleListLenovoDir("/");
            } else {
                this.handleListLenovoDir("/营销素材展示");
            }
        },
        handleBackMenuPath() {
            if (this.dir.currentPath.length === 0) {
                this.directoryType = ""
            } else if (this.dir.currentPath.length === 1) {
                this.handleGoRootPath();
            } else {
                this.handleClickDirPath(undefined, this.dir.currentPath.length - 2)
            }
        },
        handleAddSrcToPrivateDir(item) {
            newPrivateDirSrc(this.curDirNeid, item.path).then(resp => {
                if (resp.code === '0') {
                    this.visible.addSrcVisible = false;
                    this.$message.success("添加文件成功！");
                    this.visible.searchDialogVisible = false;
                    this.handleRefreshDir();
                } else {
                    this.$message.error("添加文件失败：" + resp.msg);
                }
            })
        },
        batchAddSrcToPrivateDir(multiRow) {
            batchNewPrivateDirSrc(this.curDirNeid, multiRow.map(item => item.path)).then(resp => {
                if (resp.code === '0') {
                    this.visible.addSrcVisible = false;
                    this.$message.success("批量添加文件成功！");
                    this.handleRefreshDir();
                } else {
                    this.$message.error("添加文件失败: " + resp.msg);
                }
            })
        },
        handleUpdateAlias(index, row) {
            this.$prompt('请输入要修改的文件名', '修改提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                inputValue: row.file_name
            }).then(({value}) => {
                if (!value || value.length <= 0) {
                    this.$message.error("名字不能未空！");
                    return;
                }
                if (value.indexOf("/") > 0) {
                    this.$message.error("名字不能含有字符【/】！");
                    return;
                }
                updateSrcAlias(this.curDirNeid, row.neid, value).then(resp => {
                    if (resp.code === '0') {
                        this.$message.success("修改成功！");
                        row.file_name = value;
                    }
                })
            }).catch(() => {
            });
        }
    },
    mounted() {
        let redirectPath = window.localStorage.getItem("redirectPath")
        if (redirectPath) {
            let redirectObj = JSON.parse(redirectPath);
            this.directoryType = redirectObj['type'];
            this.handleListLenovoDir(redirectObj['path']);
            setTimeout(() => {
                window.localStorage.removeItem("redirectPath")
            }, 5000)
        } else {
            //this.handleGoRootPath();
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
        this.handleGetTopSearchKey();
        let queryParams = this.$route.query;
        if (queryParams && queryParams.directoryType && queryParams.directoryType.trim().length > 0) {
            this.directoryType = queryParams.directoryType;
            this.handleGoRootPath();
        }
        let _this = this;
        this.$EventBus.$on('switchSpace', function (data) {
            _this.directoryType = data;
            if (data && data.trim().length > 0) {
                _this.handleGoRootPath();
            }
        });
        let nextPlusToken = getQueryParam("access_token");
        if (nextPlusToken) {
            localStorage.setItem("nextx_token", nextPlusToken);
        }
    },
    destroyed() {
    }
}
</script>

<style lang="scss" scoped>
@import "../../../style/vars";

.px10_divider {
    padding: 10px;
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

.adminContentHead {
    font-size: 12px;
    color: gray;
    margin: 10px auto;
    line-height: 30px;
}

.box-card {
    width: 220px;
    height: 220px;
}

.menu-content {
    margin: 20px auto;
    height: 225px;
    width: 225px;
    background-image: linear-gradient(to bottom right, #ffffff, #c7c7c7);
    text-align: center;
    box-shadow: 4px 4px 2px #888888;
}

.el-icon-folder-opened {
    color: #fec04a;
}

.el-col-lg-4-8 {
    width: 20%;
}

.main-tools {
    padding: 10px $--main-padding 0 $--main-padding;
    background: #E9E9E9;
}
</style>
