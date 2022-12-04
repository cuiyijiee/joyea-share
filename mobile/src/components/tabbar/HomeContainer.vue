<template>
    <div id="home" style="background-color: #f7f8fa;height: 100%">
        <vm-back-top :bottom="100" :duration="1000" :timing="'ease'"></vm-back-top>
        <van-search v-model="searchKey" :show-action="canGoBackSearch"
                    background="#1f1731" placeholder="请输入搜索要搜索的资源"
                    shape="round" @search="onSearch">
            <template #action>
                <van-icon name="exchange" size="20" style="margin-top: 3px" @click="goBackSearch"/>
            </template>
        </van-search>
        <div v-if="directoryType.length === 0">
            <!--            <van-image :src="background" style="position: fixed"/>-->
            <div id="space-selector">
                <div style="text-align: center; background-color: #ffffff">
                    <van-image :src="lenovoSpaceBg"
                               style="padding: 10px 10px 0 10px"
                               @click="handleDirectoryTypeSelected('LENOVO')"/>
                    <van-image id="space_img" :src="selfSpaceBg"
                               style="padding: 10px 10px 0 10px"
                               @click="handleDirectoryTypeSelected('SELF')"/>
                </div>
            </div>
        </div>
        <div v-else>
            <div>
                <span style="font-size: 10px;color: #444444">热门搜索:</span>
                <van-tag v-for="item in topSearchKey" style="margin: 5px 2px" @click="handleClickTopKey(item)">{{
                        item
                    }}
                </van-tag>
            </div>
            <div v-if="dir.currentPath.length === 1 && dir.currentPath[0] === '营销素材展示'" style="margin-bottom: 50px">
                <van-grid :column-num="2">
                    <van-grid-item v-for="menu in menuPath" class="my_van-grid-item"
                                   @click="handleClickRootMenu(menu.path)">
                        <div class="menu-content">
                            <van-image :src="menu.icon" fit="contain"
                                       style="width: 50px;height: 50px;padding: 30px 20px 10px 20px"/>
                            <div style="padding: 20px;height: 30px;font-size: 12px">{{ menu.name }}</div>
                        </div>
                    </van-grid-item>
                </van-grid>
            </div>
            <div v-else>
                <van-sticky :offset-top="46">
                    <van-row style="background-color: #fff;padding: 8px 0">
                        <van-col span="3">
                            <van-icon v-if="dir.currentPath.length !== 0" class="my_icon" name="arrow-left" size="20"
                                      style="padding: 5px 10px 5px 10px" @click="handleClickBackDir"/>
                        </van-col>
                        <van-col span="18">
                            <v-touch v-on:press="handleAddRedirectPath">
                                <div style="margin-top: 2px">
                                    {{
                                        dir.currentPath.length === 0 ? "/" : dir.currentPath[dir.currentPath.length - 1]
                                    }}
                                </div>
                            </v-touch>
                        </van-col>
                        <van-col span="3">
                            <van-icon class="my_icon" name="wap-home-o" size="20"
                                      style="padding: 5px 10px 5px 10px" @click="handleClickRootDir"/>
                        </van-col>
                    </van-row>
                </van-sticky>
                <van-tabs v-model="currentTypeActive" sticky style="margin-bottom: 50px">
                    <van-tab title="全部">
                        <van-empty v-if="dir.tableData.length === 0" description="当前路径没有文件"/>
                        <van-list>
                            <van-cell v-for="item in dir.tableData" :key="item.path" @click="handleClickItem(item)">
                                <van-row style="display:flex; justify-content:center;align-items:center;">
                                    <van-col span="4">
                                        <van-icon v-if="item['is_dir']" class="my_icon" name="credit-pay" size="30"/>
                                        <van-icon v-else-if="item.mime_type.startsWith('video')" class="my_icon"
                                                  name="video-o"
                                                  size="30"/>
                                        <img v-else-if="item.mime_type.startsWith('image')"
                                             :src="genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type)"
                                             class="my_icon my_icon_size"/>
                                        <van-image v-else-if="item.mime_type.startsWith('doc')"
                                                   :src="handleGetDocumentImage(item.mime_type)"
                                                   style="width: 40px"/>
                                        <van-image v-else-if="item.mime_type.startsWith('word')"
                                                   :src="handleGetDocumentImage(item.mime_type)"
                                                   style="width: 27px"/>
                                        <van-icon v-else class="my_icon" name="info-o" size="30"/>
                                    </van-col>
                                    <van-col span="16">
                                        {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                                        <span v-if="item.desc">
                                            <van-tag v-for="tag in item.desc.split(' ')" mark style="margin-right: 2px">
                                            {{ tag.replace(markReg, "") }}
                                            </van-tag>
                                        </span>
                                        <span v-else-if="item.tags">
                                            <van-tag v-for="tag in item.tags" mark style="margin-right: 2px">
                                            {{ tag.name.replace(markReg, "") }}
                                            </van-tag>
                                        </span>
                                    </van-col>
                                    <van-col span="4">
                                        <van-button v-if="!item['is_dir'] " icon="plus" plain size="small"
                                                    style="border: #ffffff"
                                                    type="danger"
                                                    @click="handleClickAddItem(item)"/>
                                    </van-col>
                                </van-row>
                            </van-cell>
                        </van-list>
                    </van-tab>
                    <van-tab :badge="dir.tableData.filter(item => item.is_dir).length" title="文件夹">
                        <van-list>
                            <van-cell v-for="item in dir.tableData" v-if="item.is_dir"
                                      :key="item.path" @click="handleClickItem(item)">
                                <van-col span="4">
                                    <van-icon class="my_icon" name="credit-pay" size="30"/>
                                </van-col>
                                <van-col span="20">
                                    {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                                    <span v-if="item.desc">
                                    <van-tag v-for="tag in item.desc.split(' ')" mark style="margin-right: 2px">
                                    {{ tag.replace(markReg, "") }}
                                </van-tag>
                                </span>
                                    <span v-else-if="item.tags">
                                            <van-tag v-for="tag in item.tags" mark style="margin-right: 2px">
                                            {{ tag.name.replace(markReg, "") }}
                                            </van-tag>
                                        </span>
                                </van-col>
                            </van-cell>
                        </van-list>
                    </van-tab>
                    <van-tab
                        :badge="dir.tableData.filter(item => !item.is_dir && item.mime_type.startsWith('image')).length"
                        title="图片">
                        <van-grid :border="false" :column-num="3">
                            <van-grid-item
                                v-for="item in dir.tableData.filter(item => !item.is_dir && item.mime_type.startsWith('image'))"
                                @click="handleClickItem(item)">
                                <div>
                                    <img :src="genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type)"
                                         class="my_icon my_icon_size_large"/>
                                    <van-button hairline icon="plus" plain round size="small"
                                                style="position: absolute; top: 0px; left: 0px; border:#ffffff"
                                                type="danger"
                                                @click="handleClickAddItem(item)"/>
                                </div>
                                <div>
                                    <div style="font-size:10px;-webkit-text-size-adjust: none;">
                                        {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                                    </div>
                                    <span v-if="item.desc">
                                    <van-tag v-for="tag in item.desc.split(' ')" mark
                                             style="margin-right: 2px">
                                    {{ tag.replace(markReg, "") }}
                                </van-tag>
                                </span>
                                    <span v-else-if="item.tags">
                                            <van-tag v-for="tag in item.tags" mark style="margin-right: 2px">
                                            {{ tag.name.replace(markReg, "") }}
                                            </van-tag>
                                        </span>
                                </div>
                            </van-grid-item>
                        </van-grid>
                    </van-tab>
                    <van-tab
                        :badge="dir.tableData.filter(item => !item.is_dir && item.mime_type.startsWith('video')).length"
                        title="视频">
                        <van-list>
                            <van-cell v-for="item in dir.tableData"
                                      v-if="!item.is_dir && item.mime_type.startsWith('video')"
                                      :key="item.path" @click="handleClickItem(item)">
                                <van-row>
                                    <van-col span="4">
                                        <van-icon class="my_icon" name="video-o" size="30"/>
                                    </van-col>
                                    <van-col span="16">
                                        {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                                        <span v-if="item.desc">
                                        <van-tag v-for="tag in item.desc.split(' ')" mark style="margin-right: 2px">
                                        {{ tag.replace(markReg, "") }}
                                    </van-tag>
                                    </span>
                                        <span v-else-if="item.tags">
                                            <van-tag v-for="tag in item.tags" mark style="margin-right: 2px">
                                            {{ tag.name.replace(markReg, "") }}
                                            </van-tag>
                                        </span>
                                    </van-col>
                                    <van-col span="4">
                                        <van-button v-if="!item['is_dir']" icon="plus" plain size="small"
                                                    style="border: #ffffff"
                                                    type="danger"
                                                    @click="handleClickAddItem(item)"/>
                                    </van-col>
                                </van-row>
                            </van-cell>
                        </van-list>
                    </van-tab>
                    <van-tab
                        :badge="dir.tableData.filter(item => !item.is_dir && item.mime_type.startsWith('doc')).length"
                        title="文档">
                        <van-list>
                            <van-cell v-for="item in dir.tableData"
                                      v-if="!item.is_dir && item.mime_type.startsWith('doc')"
                                      :key="item.path" @click="handleClickItem(item)">
                                <van-row>
                                    <van-col span="4">
                                        <van-image :src="handleGetDocumentImage(item.mime_type)" style="width: 40px"/>
                                    </van-col>
                                    <van-col span="16">
                                        {{ item.path.substr(item.path.lastIndexOf('/') + 1) }}
                                        <span v-if="item.desc">
                                        <van-tag v-for="tag in item.desc.split(' ')" mark style="margin-right: 2px">
                                        {{ tag.replace(markReg, "") }}
                                    </van-tag>
                                    </span>
                                        <span v-else-if="item.tags">
                                            <van-tag v-for="tag in item.tags" mark style="margin-right: 2px">
                                            {{ tag.name.replace(markReg, "") }}
                                            </van-tag>
                                        </span>
                                    </van-col>
                                    <van-col span="4">
                                        <van-button v-if="!item['is_dir']" icon="plus" plain size="small"
                                                    style="border: #ffffff"
                                                    type="danger"
                                                    @click="handleClickAddItem(item)"/>
                                    </van-col>
                                </van-row>
                            </van-cell>
                        </van-list>
                    </van-tab>
                </van-tabs>
            </div>
        </div>
        <van-dialog v-model:show="redirectPathVisible" :show-cancel-button="false" :show-confirm-button="false"
                    title="获取短链成功！">
            <div style="margin: 0 auto;padding: 1rem">
                <textarea id="copyVal" rows="3"
                          style="width: 100%;color: #8c939d;border: solid 0px">{{redirectPath}}</textarea>
                <van-button round style="width: 100%;margin-top: 10px" type="primary" @click="copyRedirectText">复制到剪贴板
                </van-button>
            </div>
        </van-dialog>
    </div>
</template>
<script>
import {addRedirectPath, getDefaultJoyeaDesc, getFileMetadata, getTopSearchKey} from "@/api";
import {genSrcPreviewSrc, getDocumentImage, handleGoToPreview} from "@/util/JoyeaUtil"
import {mapGetters} from "vuex"
import eventBus from "../../service/eventbus";
import VmBackTop from 'vue-multiple-back-top'
import {Toast} from 'vant';

export default {
    name: "HomeContainer",
    components: {
        VmBackTop
    },
    computed: {
        ...mapGetters([
            'userInfo'
        ])
    },
    data() {
        return {

            background: require("@assets/background.png"),
            selfSpaceBg: require("@assets/self-space-bg.png"),
            lenovoSpaceBg: require("@assets/lenovo-space-bg.png"),

            directoryType: "",
            topSearchKey: [],
            canGoBackSearch: false,
            markReg: /<mark>|<\/mark>/g,
            globalPathType: "ent",
            searchKey: "",
            currentTypeActive: 0,
            dir: {
                currentPath: [],
                tableData: [],
                loadingDir: false
            },
            isFirst: true,
            menuPath: [
                {
                    name: "STICK线", path: "STICK线", icon: "menu-icon/1.png"
                },
                {
                    name: "听装线", path: "听装线", icon: "menu-icon/2.png"
                },
                {
                    name: "泡罩线", path: "泡罩线", icon: "menu-icon/5.png"
                },
                // {
                //   name: "软双铝线", path: "软双铝线", icon: "menu-icon/3.png"
                // },
                {
                    name: "制粒线", path: "制粒线", icon: "menu-icon/4.png"
                },
                {
                    name: "智能物流系统", path: "智能物流系统", icon: "menu-icon/9.png"
                },
                {
                    name: "其他生产线", path: "其他生产线", icon: "menu-icon/8.png"
                },
                {
                    name: "公司介绍", path: "公司介绍", icon: "menu-icon/6.png"
                },
                {
                    name: "友商信息", path: "友商信息", icon: "menu-icon/7.png"
                }
            ],
            redirectPath: "",
            redirectPathVisible: false
        }
    },
    methods: {
        handleDirectoryTypeSelected(type) {
            this.directoryType = type;
            this.handleGoRootPath();
        },
        handleGoRootPath() {
            if (this.directoryType === "SELF") {
                this.handleListLenovoDir("/");
            } else {
                this.handleListLenovoDir("/营销素材展示");
            }
        },
        handleAddRedirectPath() {
            let _this = this;
            let currentFullPath = "";
            this.dir.currentPath.forEach(tmp => {
                currentFullPath = currentFullPath + "/" + tmp
            })
            addRedirectPath(currentFullPath).then(resp => {
                _this.redirectPath = window.location.protocol + "//"
                    + window.location.host + "/api/redirectPath?id=" + resp.id;
                _this.redirectPathVisible = true;
            })
        },
        copyRedirectText() {
            window.getSelection().removeAllRanges();
            const dom = document.getElementById('copyVal')
            dom.select()
            const copyResult = document.execCommand('copy');
            this.$notify({
                message: copyResult ? '复制成功' : '复制失败',
                type: "success"
            });
            window.getSelection().removeAllRanges();
            this.redirectPathVisible = false;
        },
        handleGetDocumentImage(mimeType) {
            return getDocumentImage(mimeType)
        },
        goBackSearch() {
            this.$router.push("/search");
        },
        handleClickTopKey(key) {
            this.canGoBackSearch = true;
            this.$router.push({path: "/search", query: {searchKey: key}});
        },
        onSearch() {
            if (this.searchKey.trim().length === 0) {
                this.$dialog({message: "要搜索的关键字不能为空！"});
                return;
            }
            this.canGoBackSearch = true;
            this.$router.push({path: "/search", query: {searchKey: this.searchKey}});
        },
        handleClickAddItem(item) {
            event.stopPropagation();
            if (!this.$store.getters.exist(item['neid'])) {
                getDefaultJoyeaDesc(item['neid']).then(resp => {
                    item.joyeaDesc = resp.data;
                    this.$store.dispatch("addFunc", item).then(() => {
                        this.$notify({type: 'success', message: '加入素材车成功！'});
                    }).catch(exception => {
                        this.$dialog.alert({
                            message: '加入素材车失败：' + exception
                        })
                    })
                })
            } else {
                this.$dialog.alert({
                    message: '该素材已经在素材车中'
                })
            }
        },
        handleClickItem(item) {
            if (item['is_dir']) {
                this.handleListLenovoDir(item.path, this.globalPathType)
            } else {
                handleGoToPreview(this, item, this.userInfo.session, this.dir.tableData);
            }
        },
        handleClickRootDir() {
            this.directoryType = "";
            this.currentTypeActive = 0;
        },
        handleClickRootMenu(path) {
            this.handleListLenovoDir("/营销素材展示/" + path);
        },
        handleClickBackDir() {
            if (this.dir.currentPath.length > 0) {
                let backPath = "";
                for (let i = 0; i < this.dir.currentPath.length - 1; i++) {
                    backPath = backPath + "/" + this.dir.currentPath[i]
                }
                if (backPath.length === 0) backPath = "/";
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
        handleGetTopSearchKey() {
            getTopSearchKey().then(resp => {
                this.topSearchKey = resp['data'];
            })
        },
        handleListLenovoDir(path) {
            this.dir.loadingDir = true;
            const toast = Toast.loading({
                duration: 0, // 持续展示 toast
                forbidClick: true,
                message: '拼命加载中...',
            });

            getFileMetadata(this.directoryType, path, "").then(response => {
                toast.clear();
                if (response.code === "0") {
                    this.currentTypeActive = 0;
                    this.dir.tableData = [];
                    if (response.obj.content) {
                        response.obj.content.forEach(item => {
                            item.joyeaDesc = "";
                            item.isModify = false;
                            this.dir.tableData.push(item)
                        });
                        this.dir.currentPath = [];
                        response.obj.path.split('/').forEach(item => {
                            if (item.length !== 0) {
                                this.dir.currentPath.push(item)
                            }
                        });
                    }
                } else {
                    console.log('文件夹列表获取失败' + response.msg)
                }
                this.dir.loadingDir = false;
            })
        },
        // initStyle() {
        //     this.$nextTick(() => {
        //         let screenHeight = window.screen.height;
        //         let bodyHeight = screenHeight - 200;
        //         let spaceSelectorDiv = document.getElementById("space-selector");
        //         spaceSelectorDiv.style.paddingTop = (bodyHeight - spaceSelectorDiv.clientHeight) / 3 + "px";
        //         document.getElementById("space_img").style.marginTop = (bodyHeight - spaceSelectorDiv.clientHeight) / 3 + "px";
        //     })
        // }
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
            //this.handleListLenovoDir("/营销素材展示", "ent");
        }
    },
    activated() {
        eventBus.$on('showDir', function (data) {
            this.directoryType = "LENOVO"
            this.handleListLenovoDir(data.path, "ent");
        }.bind(this));
        eventBus.$on('goRoot', function (data) {
            this.directoryType = '';
        }.bind(this));
        this.handleGetTopSearchKey();
    }
}
</script>

<style lang="scss" scoped>
.top {
    padding: 10px;
    background: #ee0a24;
    color: #fff;
    text-align: center;
    border-radius: 2px;
}
</style>
