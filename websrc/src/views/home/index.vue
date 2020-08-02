<template>
    <div>
        <el-row class="container">
            <el-col :span="24" class="header">
                <el-col :span="12">
                    <div class="main_content" @click="jumpToBuild">
                        <img src="logo.gif"
                             style="height: 30px;vertical-align: middle"/>
                        <span class="logo" style="font-size: 20px;margin-left: 10px;vertical-align:bottom">仅一素材库</span>
                    </div>
                </el-col>

                <el-col :span="12" class="userinfo">
                    <el-button size="small" icon="el-icon-edit" @click="jumpToBuild"
                               :class="{'is-active':currentPath==='/build'}"> 工作台
                    </el-button>
                    <el-button size="small" icon="el-icon-tickets" @click="jumpToList"
                               :class="{'is-active':currentPath==='/manage/list'}"
                               style="margin-right: 10px">我的清单
                    </el-button>
                    <el-popover
                            style="margin-right: 20px"
                            placement="bottom-start"
                            width="550"
                            @show="handleOpenDownload"
                            @hide="handleCloseDownload"
                            trigger="click">
                        <el-table :data="downloadTask" empty-text="今日暂无下载任务">
                            <!--                            <el-table-column width="300" property="id" label="任务ID"></el-table-column>-->
                            <el-table-column width="300" label="任务名称"
                                             show-overflow-tooltip>
                                <template slot-scope="scope">
                                    <div v-if="scope.row.opened" style="color: #888888"> {{scope.row.firstSrcName}}
                                    </div>
                                    <div v-else="scope.row.opened" style="color: #000000"> {{scope.row.firstSrcName}}
                                    </div>
                                </template>
                            </el-table-column>
                            <el-table-column width="150" property="startTime" label="下载时间"></el-table-column>
                            <el-table-column width="100" label="状态">
                                <template slot-scope="scope">
                                    <el-button :type="scope.row.status?'success':'danger'" size="mini"
                                               @click="handleDownload(scope.row)"
                                               :icon="scope.row.status?'el-icon-download':'el-icon-loading'"></el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <el-button slot="reference" size="small" circle :type="visible?'danger':''"
                                   :icon="visible ? 'el-icon-loading' :'el-icon-download' "></el-button>
                    </el-popover>
                    <el-dropdown trigger="hover">
                        <span class="el-dropdown-link userinfo-inner">{{userInfo.name}}</span>
                        <el-dropdown-menu slot="dropdown">
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
    import api, {check, logout} from "../../api/index";
    import getNowFormatDate from "../../utils/time"

    const localStorage = window.localStorage;
    let timer = 0;
    import store from '@/store'
    import {mapActions, mapGetters} from "vuex";

    export default {
        data() {
            return {
                currentPath: "",
                downloadTask: []
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
            jumpToBuild() {
                if (this.currentPath !== '/build') {
                    this.$router.replace("/build");
                }
            },
            jumpToList() {
                this.$router.replace("/manage/list");
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
                api({
                    action: "todayDownloadList"
                }).then(response => {
                    _this.downloadTask = [];
                    response.task.forEach(task => {
                        _this.downloadTask.push({
                            id: task.id,
                            startTime: task.startTime,
                            firstSrcName: task.firstSrcName.substr(0, task.firstSrcName.lastIndexOf(".")),
                            status: task.finishTime.length !== 0,
                            opened: _this.handleQueryRecord(task.id)
                        });
                    });
                    _this.downloadTask.reverse();
                });
                timer = setInterval(function () {
                    api({
                        action: "todayDownloadList"
                    }).then(response => {
                        _this.downloadTask = [];
                        response.task.forEach(task => {
                            _this.downloadTask.push({
                                id: task.id,
                                startTime: task.startTime,
                                firstSrcName: task.firstSrcName.substr(0, task.firstSrcName.lastIndexOf(".")),
                                status: task.finishTime.length !== 0,
                                opened: _this.handleQueryRecord(task.id)
                            });
                        });
                        _this.downloadTask.reverse();
                        console.log(_this.downloadTask)
                    })
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
            },
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
            background: #1e162f;
            color: #fff;

            .userinfo {
                text-align: right;
                padding-right: 150px;
                float: right;

                .userinfo-inner {
                    cursor: pointer;
                    color: #fff;

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
