<template>
    <div>
        <el-row class="container">
            <el-col :span="24" class="header">
                <el-col :span="10" class="logo logo-width">
                    {{sysName}}
                </el-col>
                <el-col :span="4" class="userinfo">
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
                                   <div v-if="scope.row.opened" style="color: #888888"> {{scope.row.firstSrcName}}</div>
                                   <div v-else="scope.row.opened" style="color: #000000"> {{scope.row.firstSrcName}}</div>
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
                        <!--                    <span class="el-dropdown-link userinfo-inner"><img-->
                        <!--                            :src="this.sysUserAvatar"/> {{sysUserName}}</span>-->
                        <span class="el-dropdown-link userinfo-inner">{{userInfo.name}}</span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item @click.native="logout">注销登陆</el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </el-col>
            </el-col>
            <el-col :span="24" class="main">
                <aside class="menu-expanded">
                    <!--导航菜单-->
                    <el-menu :default-active="$route.path" class="el-menu-vertical-demo" @open="handleOpen"
                             @close="handleClose" @select="handleSelect"
                             unique-opened router>
                        <template v-for="(item,index) in $router.options.routes" v-if="!item.hidden">
                            <el-submenu :index="index+''" v-if="!item.noChild">
                                <template slot="title"><i :class="item.icon"></i>{{item.name}}</template>
                                <el-menu-item v-for="child in item.children" :index="child.path" :key="child.path"
                                              v-if="!child.hidden">{{child.name}}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-if="item.noChild && item.children && item.children.length > 0"
                                          :index="item.children[0].path">
                                <i :class="item.icon"></i>{{item.name}}
                            </el-menu-item>
                        </template>
                    </el-menu>
                </aside>
                <section class="content-container">
                    <div class="grid-content bg-purple-light">
                        <el-col :span="24" class="breadcrumb-container">
                            <strong class="title">{{$route.desc}}</strong>
                            <el-breadcrumb separator="/" class="breadcrumb-inner">
                                <el-breadcrumb-item v-for="item in $route.matched" :key="item.path">
                                    {{ item.name }}
                                </el-breadcrumb-item>
                            </el-breadcrumb>
                        </el-col>
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
    import api from "../../api/index";
    import getNowFormatDate from "../../utils/time"

    const localStorage = window.localStorage;
    let timer = 0;
    import store from '@/store'

    export default {
        data() {
            return {
                sysName: '仅一素材库系统',
                userInfo: {
                    name: '',
                    email: ''
                },
                downloadTask: []
            }
        },
        computed: {
            visible() {
                return store.getters.barVisible;
            }
        },
        methods: {
            handleOpen() {
            },
            handleClose() {
            },
            handleSelect: function (a, b) {
            },
            logout: function () {
                let _this = this;
                this.$confirm('确认退出吗?', '提示', {}).then(() => {
                    _this.$router.push('/login');
                    api({
                        action: 'user',
                        method: 'logout',
                        user: _this.userInfo.email
                    }).then(resp => {

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
                            opened:_this.handleQueryRecord(task.id)
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
                                opened:_this.handleQueryRecord(task.id)
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
                    localStorage.setItem(this.genTodayDownloadTaskKey(),JSON.stringify(record));
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
        mounted() {
            if (!this.$route.params.checked) {
                api({
                    action: "user",
                    method: "check"
                }).then(response => {
                    if (response.result) {

                    } else {
                        this.$router.push("/login");
                        this.$message.error(response.msg)
                    }
                })
            }
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            } else {
                this.$router.push("/login")
            }
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
            background: $color-primary;
            color: #fff;

            .userinfo {
                text-align: right;
                padding-right: 35px;
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

            .logo {
                //width:150px;
                height: 60px;
                font-size: 22px;
                padding-left: 20px;
                padding-right: 20px;
                -webkit-user-select: none;
                -moz-user-select: none;
                -ms-user-select: none;
                user-select: none;

                img {
                    width: 40px;
                    float: left;
                    margin: 10px 10px 10px 18px;
                }

                .txt {
                    color: #fff;
                }
            }

            .logo-width {
                width: 150px;
            }

            .tools {
                padding: 0px 23px;
                width: 14px;
                height: 60px;
                line-height: 60px;
                cursor: pointer;
            }
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
                // background: #f1f2f7;
                flex: 1;
                // position: absolute;
                // right: 0px;
                // top: 0px;
                // bottom: 0px;
                // left: 150px;
                overflow-y: scroll;
                padding: 20px;

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
