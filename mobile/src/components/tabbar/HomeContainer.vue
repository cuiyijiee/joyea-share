<template>
    <div id="home" style="background-color: #f7f8fa;">
        <form action="/">
            <van-search v-model="searchKey" placeholder="请输入搜索要搜索的资源"
                        @search="onSearch" @cancel="onCancel"/>
        </form>
        <van-sticky :offset-top="46">
            <van-row style="background-color: #fff;">
                <van-col span="2">
                    <van-icon class="my_icon" name="arrow-left"
                              v-if="dir.currentPath.length !== 0"
                              @click="handleClickBackDir"/>
                </van-col>
                <van-col span="22">{{dir.currentPath.length === 0 ? "/" : dir.currentPath[dir.currentPath.length -1]}}
                </van-col>
            </van-row>
        </van-sticky>
        <van-tabs v-model="currentTypeActive">
            <van-tab title="全部">
                <van-empty v-if="dir.tableData.length === 0" description="当前路径没有文件"/>
                <van-list>
                    <van-cell v-for="item in dir.tableData" :key="item.path" @click="handleClickItem(item)">
                        <van-row>
                            <van-col span="2">
                                <van-icon v-if="item['is_dir']" class="my_icon" name="credit-pay"/>
                                <van-icon v-else-if="item.mime_type.startsWith('video')" class="my_icon"
                                          name="video-o"/>
                                <van-icon v-else-if="item.mime_type.startsWith('image')" class="my_icon"
                                          name="photo-o"/>
                                <van-icon v-else-if="item.mime_type.startsWith('doc')" class="my_icon" name="orders-o"/>
                                <van-icon v-else class="my_icon" name="info-o"/>
                            </van-col>
                            <van-col span="20">
                                {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                            </van-col>
                            <van-col span="2">
                                <van-icon v-if="!item['is_dir']" name="plus" @click="handleClickAddItem(item)"/>
                            </van-col>
                        </van-row>
                    </van-cell>
                </van-list>
            </van-tab>
            <van-tab title="图片">
                <van-list>
                    <van-cell v-for="item in dir.tableData" v-if="!item.is_dir && item.mime_type.startsWith('image')"
                              :key="item.path">
                        <van-row>
                            <van-col span="2">
                            <img class="my_icon my_icon_size" preview="buildList"
                                 :src="genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type)"/>
                        </van-col>
                            <van-col span="20">
                                {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                            </van-col>
                            <van-col span="2">
                                <van-icon v-if="!item['is_dir']" name="plus" @click="handleClickAddItem(item)"/>
                            </van-col>
                        </van-row>
                    </van-cell>
                </van-list>
            </van-tab>
            <van-tab title="视频">
                <van-list>
                    <van-cell v-for="item in dir.tableData" v-if="!item.is_dir && item.mime_type.startsWith('video')"
                              :key="item.path">
                        <van-icon class="my_icon" name="video-o"/>
                        {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                    </van-cell>
                </van-list>
            </van-tab>
            <van-tab title="文档">
                <van-list>
                    <van-cell v-for="item in dir.tableData" v-if="!item.is_dir && item.mime_type.startsWith('doc')"
                              :key="item.path">
                        <van-icon class="my_icon" name="orders-o"/>
                        {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                    </van-cell>
                </van-list>
            </van-tab>
        </van-tabs>
    </div>
</template>

<script>
    import api from "../../api/";
    import {genSrcPreviewSrc} from "../../util/JoyeaUtil"
    import {mapActions, mapState} from "vuex"

    export default {
        name: "HomeContainer",
        data() {
            return {
                globalPathType: "ent",
                searchKey: "",
                currentTypeActive: 0,
                dir: {
                    currentPath: [],
                    tableData: [],
                    loadingDir: false
                },
                userInfo: {},
            }
        },
        methods: {
            onSearch() {

            },
            onCancel() {

            },
            handleClickAddItem(item) {
                this.$store.dispatch("addFunc", item)
            },
            handleClickItem(item) {
                if (item['is_dir']) {
                    this.handleListLenovoDir(item.path, this.globalPathType)
                }
            },
            handleClickBackDir() {
                if (this.dir.currentPath.length > 0) {
                    let backPath = "/";
                    for (let i = 0; i < this.dir.currentPath.length - 1; i++) {
                        backPath = backPath + this.dir.currentPath[i] + "/"
                    }
                    this.handleListLenovoDir(backPath);
                }
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
                        console.log('文件夹列表获取失败' + response.msg)
                    }
                    this.dir.loadingDir = false;
                });
            },
        },
        mounted() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
            this.handleListLenovoDir("/营销素材展示", "ent");
        }
    }
</script>

<style scoped>
    .my_icon {
        padding-left: 10px;
        padding-right: 10px;
        padding-top: 5px;
    }

    .my_icon {
        padding-left: 10px;
        padding-right: 10px;
        padding-top: 5px;
    }


    .my_icon_size {
        width: 15px;
        height: 15px;
    }

    .my_icon_size_large {
        width: 50px;
        height: 50px;
    }
</style>