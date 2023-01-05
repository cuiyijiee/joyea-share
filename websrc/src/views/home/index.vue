<template>
    <div>
        <el-row class="container">
            <el-col :span="24" class="header">
                <el-col :span="9">
                    <div class="main_content">
                        <img :src="joyeaLogo" class="logo" @click="jumpToBuild('')"/>
                        <span class="logo_text"><b>仅一素材库</b></span>
                    </div>
                </el-col>

                <el-col :span="15" class="userinfo">
                    <el-button :class="{'is-active':currentPath.startsWith('/build')}" class="interval"
                               icon="iconfont el-icon-icon_home_20_20px"
                               round size="mini"
                               @click="jumpToBuild('')"> 首页
                    </el-button>
                    <el-button v-if="!currentPath.startsWith('/build') || (currentPath.startsWith('/build') && directoryType.trim().length > 0)"
                               :class="{'is-active':currentPath.startsWith('/build')}" class="interval"
                               icon="iconfont el-icon-icon_xifenshichang_20_20px"
                               round size="mini"
                               @click="jumpToBuild('SELF')"> 细分市场
                    </el-button>
                    <el-button v-if="!currentPath.startsWith('/build') || (currentPath.startsWith('/build') && directoryType.trim().length > 0)"
                               :class="{'is-active':currentPath.startsWith('/build')}" class="interval"
                               icon="iconfont el-icon-icon_sucaiku_20_20px"
                               round size="mini"
                               @click="jumpToBuild('LENOVO')"> 基础素材库
                    </el-button>
<!--                    <el-button :class="{'is-active':currentPath.startsWith('/manage/list')}"-->
<!--                               icon="iconfont el-icon-icon_wodeqingdan_20_20px" round size="mini"-->
<!--                               style=""-->
<!--                               @click="jumpToList">我的清单-->
<!--                    </el-button>-->
                    <el-button :class="{'is-active':currentPath.startsWith('/upload/index')}"
                               icon="iconfont el-icon-icon_sucaishangchuan_20_20px" round
                               size="mini"
                               style="margin-right: 10px"
                               @click="jumpToUpload">素材上传
                    </el-button>
                    <el-popover
                        placement="bottom-start"
                        style="margin-right: 20px"
                        trigger="click"
                        width="600"
                        @hide="handleCloseDownload"
                        @show="handleOpenDownload">
                        <el-table :data="downloadTask" empty-text="今日暂无下载任务">
                            <!--                            <el-table-column width="300" property="id" label="任务ID"></el-table-column>-->
                            <el-table-column label="任务名称" show-overflow-tooltip
                                             width="300">
                                <template slot-scope="scope">
                                    <div v-if="scope.row.opened" style="color: #888888">
                                        {{ scope.row.firstSrcName + ".zip" }}
                                    </div>
                                    <div v-else="scope.row.opened" style="color: #000000">
                                        {{ scope.row.firstSrcName + ".zip" }}
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column label="下载时间" width="200">
                                <template slot-scope="scope">
                                    {{ scope.row.startTime | dateFormat }}
                                </template>
                            </el-table-column>
                            <el-table-column label="状态" width="100">
                                <template slot-scope="scope">
                                    <el-button :icon="scope.row.status?'el-icon-download':'el-icon-loading'"
                                               :type="scope.row.status?'success':'danger'"
                                               size="mini"
                                               @click="handleDownload(scope.row)"></el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <el-button slot="reference"
                                   :icon="visible ? 'el-icon-loading' :'iconfont el-icon-icon_xiazailiebiao_20_20px' "
                                   :type="visible?'danger':''" round
                                   size="mini">
                            下载列表
                        </el-button>
                    </el-popover>

                    <el-dropdown trigger="click">
                        <div>
                            <el-button circle icon="iconfont el-icon-icon_me_20_20px" size="mini"
                                       style="background-color: #eb7708 "
                                       type="warning"></el-button>
                            <span class="el-dropdown-link userinfo-inner">{{ userInfo.name }}<i
                                class="el-icon-arrow-down el-icon--right"/></span>
                        </div>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item @click.native="jumpToList">我的清单
                            </el-dropdown-item>
                            <el-dropdown-item v-if="userInfo.isAdmin" @click.native="jumpToUploadManage">素材审核
                            </el-dropdown-item>
                            <el-dropdown-item v-if="userInfo.isAdmin" @click.native="jumpToTranscode">转码素材管理
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="logout">注销登陆</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </el-col>
            </el-col>
            <el-col :span="24" class="main">
                <section class="content-container">
                    <div class="grid-content bg-purple-light">
                        <el-col :span="24" class="content-wrapper">
                            <transition mode="out-in" name="fade">
                                <router-view></router-view>
                            </transition>
                        </el-col>
                    </div>
                </section>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import {check, getTodayDownload, logout} from "../../api/index";
import getNowFormatDate from "../../utils/time"
import store from '@/store'
import {mapActions, mapGetters} from "vuex";

const localStorage = window.localStorage;
let timer = 0;

export default {
    data() {
        return {
            currentPath: "",
            downloadTask: [],
            joyeaLogo: require("@assets/joyea.png"),
            directoryType:""
        }
    },
    computed: {
        visible() {
            return store.getters.barVisible;
        },
        ...mapGetters({
            'userInfo': 'userInfo/userInfo'
        })
    },
    methods: {
        ...mapActions('userInfo', [
            'clearUserSessionFunc'
        ]),
        jumpToBuild(path) {
            if (!this.currentPath.startsWith('/build')) {
                this.$router.push("/build?directoryType=" + path);
                this.directoryType = "";
            }else{
                this.$EventBus.$emit('switchSpace',path);
            }
        },
        jumpToUpload() {
            this.$router.replace("/upload/index?_=" + (new Date()).getTime());
        },
        jumpToList() {
            this.$router.replace("/manage/list?_=" + (new Date()).getTime());
        },
        jumpToUploadManage() {
            this.$router.replace("/upload/manage?_=" + (new Date()).getTime());
        },
        jumpToTranscode() {
            this.$router.replace("/transcode/index?_=" + (new Date()).getTime());
        },
        handleOpen() {
        },
        handleClose() {
        },
        handleSelect: function (a, b) {
        },
        logout: function () {
            let _this = this;
            this.$confirm('确认退出吗?', '提示', {}).then(() => {
                logout().then(resp => {
                    _this.clearUserSessionFunc().then(() => {
                        _this.$router.push('/login');
                    })
                })
            }).catch(() => {
            });
        },
        handleOpenDownload() {
            let _this = this;
            getTodayDownload().then(response => {
                _this.downloadTask = [];
                response.data.forEach(task => {
                    _this.downloadTask.push({
                        id: task.id,
                        startTime: task.startTime,
                        firstSrcName: task.firstSrcName.substr(0, task.firstSrcName.lastIndexOf(".")),
                        status: task.status,
                        opened: _this.handleQueryRecord(task.id)
                    });
                });
                _this.downloadTask.reverse();
            })
            timer = setInterval(function () {
                getTodayDownload().then(response => {
                    _this.downloadTask = [];
                    response.data.forEach(task => {
                        _this.downloadTask.push({
                            id: task.id,
                            startTime: task.startTime,
                            firstSrcName: task.firstSrcName.substr(0, task.firstSrcName.lastIndexOf(".")),
                            status: task.status,
                            opened: _this.handleQueryRecord(task.id)
                        });
                    });
                    _this.downloadTask.reverse();
                });
            }, 2 * 1000);
        },
        handleQueryRecord(taskId) {
            let todayOpenTask = localStorage.getItem(this.genTodayDownloadTaskKey());
            return !(todayOpenTask == null || JSON.parse(todayOpenTask).indexOf(taskId) === -1);
        },
        handleAddRecord(taskId) {
            let todayOpenTask = localStorage.getItem(this.genTodayDownloadTaskKey());
            if (todayOpenTask == null) {
                let record = [];
                record[0] = taskId;
                localStorage.setItem(this.genTodayDownloadTaskKey(), JSON.stringify(record));
            } else {
                let record = JSON.parse(todayOpenTask);
                record[record.length] = taskId;
                localStorage.setItem(this.genTodayDownloadTaskKey(), JSON.stringify(record));
            }
        },
        handleCloseDownload() {
            clearInterval(timer);
        },
        handleDownload(row) {
            if (row.status) {
                this.handleAddRecord(row.id);
                window.open(window.location.protocol + "//" + window.location.host + "/download/" + row.id + "/" + row.firstSrcName);
            }
        },
        genTodayDownloadTaskKey() {
            return "down_open_" + getNowFormatDate();
        }
    },
    watch: {
        $route(to, from) {
            this.currentPath = this.$router.currentRoute.fullPath;
        },
        // currentPath(){
        //     if(!this.currentPath.startsWith('/build')) {
        //         this.directoryType = '';
        //     }
        // }
    },
    mounted() {
        this.currentPath = this.$router.currentRoute.fullPath;
        if (!this.$route.params.checked) {
            check().then(resp => {
                if (resp.code === 4001) {
                    this.$router.push("/login");
                    this.$message.error("登录信息已过期，请重新登陆！")
                } else {

                }
            })
        }
        document.oncontextmenu = function (event) {
            event.preventDefault();
        };
        this.$EventBus.$on("directoryTypeChanged",directoryType => {
            this.directoryType = directoryType;
        })
    }
}
</script>

<style lang="scss" scoped>
@import '../../style/vars';

.container {
    position: absolute;
    top: 0px;
    bottom: 0px;
    width: 100%;

    .header {
        height: 60px;
        background: #E9E9E9;
        padding-top: 20px;

        .main_content {

            padding-left: $--main-padding;

            .logo {
                cursor: pointer;
                vertical-align: middle;
                height: 30px;
            }

            .logo_text {
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;
                margin-left: 10px;

                font-size: 20px;
                vertical-align: bottom;
            }
        }

        .userinfo {
            text-align: right;
            padding-right: $--main-padding;
            float: right;

            .el-button--mini {
                font-size: 16px;
            }

            .userinfo-inner {
                cursor: pointer;
                color: #000;
                padding-left: 10px;

                img {
                    width: 40px;
                    height: 40px;
                    border-radius: 20px;
                    margin: 10px 0px 10px 10px;
                    float: right;
                }
            }
        }
    }

    .el-menu-item {
        color: #8c939d;
    }

    .is-active {
        border: 0 !important;
        background: #eb7708;
        color: #ffffff;
    }

    .main {
        display: flex;
        // background: #324057;
        position: absolute;
        top: 60px;
        bottom: 0px;
        overflow: hidden;

        aside {
            flex: 0 0 150px;
            width: 150px;
            // position: absolute;
            // top: 0px;
            // bottom: 0px;
            .el-menu {
                height: 100%;
            }
        }

        .menu-expanded {
            flex: 0 0 150px;
            width: 150px;
        }

        .content-container {
            flex: 1;
            overflow-y: scroll;

            .breadcrumb-container {
                //margin-bottom: 15px;
                .title {
                    width: 150px;
                    float: left;
                    color: #475669;
                }

                .breadcrumb-inner {
                    float: right;
                }
            }

            .content-wrapper {
                background-color: #fff;
                box-sizing: border-box;
            }
        }
    }
}

::-webkit-scrollbar{
    display:none !important;
}
</style>
