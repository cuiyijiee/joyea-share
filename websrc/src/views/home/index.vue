<template>
    <div>
        <el-row class="container">
            <el-col :span="24" class="header">
                <el-col :span="9">
                    <div class="main_content">
                        <img :src="joyeaLogo" @click="jumpToBuild('')"
                             style="height: 30px;vertical-align: middle;"/>
                        <span class="logo" style="font-size: 20px;margin-left: 10px;vertical-align:bottom"><b>仅一素材库</b></span>
                    </div>
                </el-col>

                <el-col :span="15" class="userinfo">
                    <el-button size="mini"  @click="jumpToBuild('')" round
                               icon="iconfont el-icon-icon_home_20_20px" class="interval"
                               :class="{'is-active':currentPath.startsWith('/build')}"> 首页
                    </el-button>
                    <el-button size="mini"  @click="jumpToBuild('SELF')" round
                               v-if="currentPath.startsWith('/build')"
                               icon="iconfont el-icon-icon_xifenshichang_20_20px" class="interval"
                               :class="{'is-active':currentPath.startsWith('/build')}"> 细分市场
                    </el-button>
                    <el-button size="mini"  @click="jumpToBuild('LENOVO')" round
                               v-if="currentPath.startsWith('/build')"
                               icon="iconfont el-icon-icon_sucaiku_20_20px" class="interval"
                               :class="{'is-active':currentPath.startsWith('/build')}"> 素材库
                    </el-button>
                    <el-button size="mini" icon="iconfont el-icon-icon_wodeqingdan_20_20px" @click="jumpToList" round
                               :class="{'is-active':currentPath.startsWith('/manage/list')}"
                               style="">我的清单
                    </el-button>
                    <el-button size="mini" icon="iconfont el-icon-icon_sucaishangchuan_20_20px" @click="jumpToUpload" round
                               :class="{'is-active':currentPath.startsWith('/upload/index')}"
                               style="margin-right: 10px">素材上传
                    </el-button>
                    <el-popover
                        style="margin-right: 20px"
                        placement="bottom-start"
                        width="600"
                        @show="handleOpenDownload"
                        @hide="handleCloseDownload"
                        trigger="click">
                        <el-table :data="downloadTask" empty-text="今日暂无下载任务">
                            <!--                            <el-table-column width="300" property="id" label="任务ID"></el-table-column>-->
                            <el-table-column width="300" label="任务名称"
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <div v-if="scope.row.opened" style="color: #888888">
                                        {{ scope.row.firstSrcName + ".zip" }}
                                    </div>
                                    <div v-else="scope.row.opened" style="color: #000000">
                                        {{ scope.row.firstSrcName + ".zip" }}
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column width="200" label="下载时间">
                                <template slot-scope="scope">
                                    {{ scope.row.startTime | dateFormat }}
                                </template>
                            </el-table-column>
                            <el-table-column width="100" label="状态">
                                <template slot-scope="scope">
                                    <el-button :type="scope.row.status?'success':'danger'" size="mini"
                                               @click="handleDownload(scope.row)"
                                               :icon="scope.row.status?'el-icon-download':'el-icon-loading'"></el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <el-button slot="reference" size="mini" :type="visible?'danger':''" round
                                   :icon="visible ? 'el-icon-loading' :'iconfont el-icon-icon_xiazailiebiao_20_20px' ">下载列表</el-button>
                    </el-popover>

                    <el-dropdown trigger="hover">
                        <div>
                            <el-button size="mini" type="warning" style="background-color: #eb7708 " circle icon="iconfont el-icon-icon_me_20_20px"></el-button>
                            <span class="el-dropdown-link userinfo-inner">{{ userInfo.name }}</span>
                        </div>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-if="userInfo.isAdmin" @click.native="jumpToUploadManage">素材审核</el-dropdown-item>
                            <el-dropdown-item v-if="userInfo.isAdmin" @click.native="jumpToTranscode">转码素材管理</el-dropdown-item>
                            <el-dropdown-item @click.native="logout">注销登陆</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </el-col>
            </el-col>
            <el-col :span="24" class="main">
                <section class="content-container">
                    <div class="grid-content bg-purple-light">
                        <el-col :span="24" class="content-wrapper">
                            <transition name="fade" mode="out-in">
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
                this.$router.push("/build?_=" + (new Date()).getTime());
            } else {
                this.$EventBus.$emit("switchSpace",path);
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
        }
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
    }
}
</script>

<style scoped lang="scss">
@import '../../style/vars';

.container {
    position: absolute;
    top: 0px;
    bottom: 0px;
    width: 100%;

    .header {
        height: 60px;
        line-height: 60px;
        background: #E9E9E9;
        color: #000;

        .userinfo {
            text-align: right;
            padding-right: 150px;
            float: right;

            .userinfo-inner {
                cursor: pointer;
                color: #000;

                img {
                    width: 40px;
                    height: 40px;
                    border-radius: 20px;
                    margin: 10px 0px 10px 10px;
                    float: right;
                }
            }
        }

        .main_content {
            margin: 0 150px;
        }

        .logo {
            height: 60px;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;

            .txt {
                color: #fff;
            }
        }


        .tools {
            padding: 0px 23px;
            width: 14px;
            height: 60px;
            line-height: 60px;
            cursor: pointer;
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
</style>
