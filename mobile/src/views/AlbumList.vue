<template>
    <div id="album">
        <div v-for="menu in albumMenuList">
            <van-swipe-cell :key="menu.id">
                <van-card style="margin: 2px 0 0 0" @click="handleListMenuAlbum(menu.id)"
                          :title="menu.name" :thumb="folderImg"/>
                <template #right>
                    <van-button square text="重命名" type="primary" class="delete-button"
                                @click="showRenameAlbumMenuDialog(menu)"/>
                    <van-button square text="删除" type="danger" class="delete-button"
                                @click="handleDeleteAlbumMenu(menu)"/>
                </template>
            </van-swipe-cell>
        </div>
        <div class="default-album">
            <van-swipe-cell v-for="item in defaultAlbumList" :key="item.path">
                <van-card style="margin: 2px 0 2px 0" :num="item.srcList.length" @click="handleShowAlbum(item)"
                          :title="item.albumName" :thumb="item.cover"/>
                <template #right>
                    <van-button square :text="item.shared ? '取消分享' : '分享'"
                                :type="item.shared ? 'warning' : 'info'" class="delete-button"
                                @click="handleShareAlbum(item)"/>
                    <van-button square text="编辑" type="primary" class="delete-button"
                                @click="handleEditAlbum(item)"/>
                    <van-button square text="移动" type="warning" class="delete-button"
                                @click="showMoveMenuDialog(item)"/>
                    <van-button square text="删除" type="danger" class="delete-button"
                                @click="handleDeleteAlbum(item)"/>
                </template>
            </van-swipe-cell>
        </div>
        <van-action-sheet v-model="albumMenuVisible" :title="currentSelectedMenuName">
            <div v-if="albumLoading">
                <van-loading style="text-align: center" type="spinner" size="24px"/>
            </div>
            <div v-else>
                <van-empty
                    v-if="albumList.length === 0"
                    class="custom-image"
                    image="https://img.yzcdn.cn/vant/custom-empty-image.png"
                    description="该文件尚未添加清单!"/>
                <van-swipe-cell v-for="item in albumList" :key="item.path">
                    <van-card style="margin: 2px 0 2px 0" :num="item.srcList.length" @click="handleShowAlbum(item)"
                              :title="item.albumName" :thumb="item.cover"/>
                    <template #right>
                        <van-button square :text="item.shared ? '取消分享' : '分享'"
                                    :type="item.shared ? 'warning' : 'info'" class="delete-button"
                                    @click="handleShareAlbum(item)"/>
                        <van-button square text="编辑" type="primary" class="delete-button"
                                    @click="handleEditAlbum(item)"/>
                        <van-button square text="移动" type="warning" class="delete-button"
                                    @click="showMoveMenuDialog(item)"/>
                        <van-button square text="删除" type="danger" class="delete-button"
                                    @click="handleDeleteAlbum(item)"/>
                    </template>
                </van-swipe-cell>
            </div>
        </van-action-sheet>
        <van-action-sheet v-model="albumVisible" :title="albumItem.albumName">
            <van-card v-for="item in albumItem.srcList" @click="handlePreview(item)">
                <template #thumb>
                    <van-image width="80" height="80" v-if="item.srcType.startsWith('image')"
                               :src="getPreviewUrl(item)"/>
                    <van-image v-else width="80" height="80" :src="getDocumentImage(item.srcType)"></van-image>
                </template>
                <template #tags>
                    <div>{{ item.srcPath.substr(item.srcPath.lastIndexOf('/') + 1) }}</div>
                    <div v-if="item.srcDesc && item.srcDesc.trim().length > 0"
                         style="color: #8c939d">
                        解说词:{{ item.srcDesc }}
                    </div>
                </template>
            </van-card>
        </van-action-sheet>
        <van-button v-if="!albumMenuVisible" @click="createMenuVisible = true" class="float-button" round type="info"
                    icon="plus"></van-button>
        <van-dialog v-model="createMenuVisible" title="新建目录" show-cancel-button @confirm="handleAddMenu">
            <van-field v-model="newMenuName" label="清单名:" placeholder="请输入新的目录名"/>
        </van-dialog>
        <van-dialog v-model="renameMenuVisible" title="重命名目录" show-cancel-button @confirm="handleRenameAlbumMenu">
            <van-field v-model="renameAlbumMenuName" label="清单名:" placeholder="请输入新的目录名"/>
        </van-dialog>
        <van-dialog v-model="moveAlbumVisible" title="移动清单至" show-cancel-button @confirm="handleMoveMenu">
            <van-picker
                ref="menuPicker"
                :columns="moveMenuNameOption"
            />
        </van-dialog>
    </div>
</template>

<script>
import api, {
    createAlbumMenu,
    deleteAlbumMenu,
    listMineAlbum,
    listMyAlbumMenu,
    moveAlbumMenu,
    renameAlbumMenu
} from "../api";
import {genSrcPreviewSrc, getDocumentImage, handleGoToPreview} from "../util/JoyeaUtil"
import {mapGetters, mapActions} from "vuex";
import {convertItem} from "../util/ImageViewUtil";
import {switchShare} from "../api";

export default {
    name: "AlbumList",
    data() {
        return {
            defaultMenuName: "默认清单集合",
            folderImg: require("../assets/folder.png"),
            albumMenuVisible: false,
            currentSelectedMenuName: "默认清单合集",
            albumVisible: false,
            albumLoading: false,
            createMenuVisible: false,
            renameMenuVisible: false,
            moveAlbumVisible: false,
            active: 1,
            albumItem: {},
            albumList: [],
            defaultAlbumList: [],
            albumMenuList: [],

            newMenuName: "",
            toRenameMenuId: -1,
            renameAlbumMenuName: "",

            moveMenuNameOption: [],
            selectedToMoveAlbumId: -1
        }
    },
    methods: {
        getDocumentImage,
        showMoveMenuDialog(album) {
            this.moveMenuNameOption = this.albumMenuList.map(menu => menu.name);
            if (album.menu) {
                this.moveMenuNameOption.push(this.defaultMenuName);
                this.moveMenuNameOption = this.moveMenuNameOption.filter(menuName => {
                    return menuName !== album.menu.name;
                });
            }
            this.selectedToMoveAlbumId = album.albumId;
            if (this.moveMenuNameOption.length > 0) {
                this.moveAlbumVisible = true;
            } else {
                this.$notify({
                    type: "danger",
                    message: "暂时没有可移动的文件夹,请先创建!"
                });
            }
        },
        showRenameAlbumMenuDialog(menu) {
            this.toRenameMenuId = menu.id;
            this.renameAlbumMenuName = menu.name;
            this.renameMenuVisible = true;
        },
        handleMoveMenu() {
            let menuPickerSelected = this.$refs.menuPicker.getValues();
            if (menuPickerSelected.length > 0) {
                let toMoveMenu = this.albumMenuList.find(item => item.name === menuPickerSelected[0]);
                let toMoveAlbum = this.albumList.concat(this.defaultAlbumList).find(item => item.albumId = this.selectedToMoveAlbumId);
                if (toMoveMenu && toMoveAlbum.menu && toMoveAlbum.menu.id === toMoveMenu.id) {
                    this.$notify({
                        type: "danger",
                        message: "目标文件夹与当前文件夹相同，无法移动!"
                    });
                    return;
                }
                moveAlbumMenu(toMoveAlbum.albumId, toMoveMenu ? toMoveMenu.id : -1).then(resp => {
                    if (resp.code === 2000) {
                        if (toMoveAlbum.menu) {
                            this.albumList = this.albumList.filter(item => item.albumId !== toMoveAlbum.albumId);
                        } else {
                            this.defaultAlbumList = this.defaultAlbumList.filter(item => item.albumId !== toMoveAlbum.albumId);
                        }
                        this.handleListDefaultMenuAlbum();
                        this.$notify({
                            type: "success",
                            message: `成功移至文件夹【${toMoveMenu ? toMoveMenu.name : this.defaultMenuName}】!`
                        });
                    } else if (resp.code === 4204) {
                        this.$notify({
                            type: "danger",
                            message: `目标文件夹【${toMoveMenu ? toMoveMenu.name : this.defaultMenuName}】不存在，无法移动!`
                        });
                    }
                })
            } else {
                this.$notify({
                    type: "danger",
                    message: "暂时没有可移动的文件夹,请先创建!"
                });
            }
        },
        handleRenameAlbumMenu() {
            if (this.renameAlbumMenuName.trim().length > 0 && this.renameAlbumMenuName.trim() !== this.defaultMenuName) {
                renameAlbumMenu(this.toRenameMenuId, this.renameAlbumMenuName).then(resp => {
                    if (resp.code === 2000) {
                        this.albumMenuList.find(item => item.id === this.toRenameMenuId).name = this.renameAlbumMenuName;
                        this.$notify({
                            type: "success",
                            message: `目录重命名成功【${this.renameAlbumMenuName}】!`
                        });
                    } else if (resp.code === 4202) {
                        this.$notify({
                            type: "danger",
                            message: `你已创建该目录【${this.renameAlbumMenuName}】!`
                        });
                    }
                });
            }
        },
        handleDeleteAlbumMenu(menu) {
            this.$dialog.confirm({
                title: '删除提示',
                message: `你即将删除目录【${menu.name}】!`,
            }).then(() => {
                deleteAlbumMenu(menu.id).then(resp => {
                    if (resp.code === 2000) {
                        this.albumMenuList =
                            this.albumMenuList.filter(item => item.id !== menu.id);
                        this.$notify({
                            type: "success",
                            message: `目录删除成功【${menu.name}】!`
                        });
                    } else if (resp.code === 4203) {
                        this.$notify({
                            type: "danger",
                            message: `目录【${menu.name}】含有清单，请移除后删除该目录!`
                        });
                    }
                })
            })
        },
        handleGetDocumentImage(mimeType) {
            return getDocumentImage(mimeType)
        },
        ...mapActions([
            'clearFunc', 'addFunc', 'setOrderEditInfoFunc'
        ]),
        handleListDefaultMenuAlbum() {
            this.defaultAlbumList = [];
            listMineAlbum(-1).then(response => {
                response.data.forEach(album => {
                    album.srcList.forEach(src => {
                        src.url = genSrcPreviewSrc(src.srcNeid, src.srcHash, src.srcRev, 'pic', this.userInfo.session)
                    })
                    let images = album.srcList.filter(item => {
                        return item.srcType.startsWith("image")
                    })
                    album.cover = images.length > 0 ? images[0].url : 'cover.png';
                });
                this.defaultAlbumList = response.data;
            })
        },
        handleListMenuAlbum(menuId) {
            this.currentSelectedMenuName = this.albumMenuList.find(item => item.id === menuId).name;
            this.albumList = [];
            this.albumMenuVisible = true;
            this.albumLoading = true;
            listMineAlbum(menuId).then(response => {
                response.data.forEach(album => {
                    album.srcList.forEach(src => {
                        src.url = genSrcPreviewSrc(src.srcNeid, src.srcHash, src.srcRev, 'pic', this.userInfo.session)
                    })
                    let images = album.srcList.filter(item => {
                        return item.srcType.startsWith("image")
                    })
                    album.cover = images.length > 0 ? images[0].url : 'cover.png';
                });
                this.albumList = response.data;
            }).finally(() => {
                this.albumLoading = false;
            })
        },
        handleShareAlbum(album) {
            if (album.shared) {
                switchShare(album.albumId, false).then(resp => {
                    if (resp.data) {
                        this.$notify({
                            type: "success",
                            message: "取消分享成功！"
                        })
                        album.shared = false;
                    }
                })
            } else {
                let route = {
                    name: '编辑分享',
                    params: {albumId: album.albumId}
                };
                this.$router.replace(route);
            }
        },
        handleEditAlbum(item) {
            this.$dialog.confirm({
                title: "重新编辑提醒",
                message: "即将重新编辑清单【" + item.albumName + "】?"
            }).then(() => {
                this.clearFunc().then(() => {
                    item.srcList.forEach(src => {
                        this.addFunc(convertItem(src))
                    });
                    this.setOrderEditInfoFunc({
                        name: item.albumName,
                        id: item.albumId
                    }).then(() => {
                        this.$router.push("/order");
                    })
                })
            })
        },
        handleDeleteAlbum(item) {
            this.$dialog.confirm({
                title: "删除提醒",
                message: "即将删除清单【" + item.albumName + "】?"
            }).then(() => {
                api({
                    action: "album",
                    method: "delete",
                    album_id: item.albumId
                }).then(response => {
                    if (response.result) {
                        this.albumList = this.albumList.filter(album => {
                            return album.albumId !== item.albumId;
                        });
                        this.$notify({
                            type: "success",
                            message: "你的清单【" + item.albumName + "】已成功删除！"
                        });
                    }
                })
            })
        },
        handlePreview(clickItem) {
            clickItem = convertItem(clickItem);
            handleGoToPreview(this, clickItem, this.userInfo.session, this.albumItem.srcList);
        },
        getPreviewUrl(item) {
            let previewType = 'pic';    // if video is av
            if (item.srcType.startsWith("doc")) {
                previewType = 'doc'
            } else if (item.srcType.startsWith("video")) {
                previewType = 'av'
            }
            return genSrcPreviewSrc(item.srcNeid, item.srcHash, item.srcRev, previewType, this.userInfo.session);
        },
        handleShowAlbum(item) {
            this.albumItem = item;
            this.albumVisible = true;
        },
        listMyAlbumMenu() {
            listMyAlbumMenu().then(resp => {
                this.albumMenuList = resp.data;
            });
        },
        handleAddMenu() {
            createAlbumMenu(this.newMenuName).then(resp => {
                if (resp.code === 2000) {
                    this.albumMenuList.push({
                        id: resp.data,
                        name: this.newMenuName,
                        userId: this.userInfo.email
                    })
                } else if (resp.code === 4202) {
                    this.$notify({
                        type: "danger",
                        message: `你已创建该目录【${this.newMenuName}】!`
                    });
                }
            })
        }
    },
    computed: {
        ...mapGetters([
            'userInfo'
        ])
    },
    created() {
        this.listMyAlbumMenu();
        this.handleListDefaultMenuAlbum();
    }
}
</script>

<style scoped>
</style>