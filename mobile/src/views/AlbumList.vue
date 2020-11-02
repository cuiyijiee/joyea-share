<template>
    <div id="album">
        <van-swipe-cell v-for="item in albumList" :key="item.path">
            <van-card
                style="margin: 2px 0 2px 0"
                :num="item.srcList.length"
                @click="handleShowAlbum(item)"
                :title="item.albumName"
                :thumb="item.cover"/>
            <template #right>
                <van-button square :text="item.shared ? '取消分享' : '分享'"
                            :type="item.shared ? 'warning' : 'info'" class="delete-button"
                            @click="handleShareAlbum(item)"/>
                <van-button square text="编辑" type="primary" class="delete-button" @click="handleEditAlbum(item)"/>
                <van-button square text="删除" type="danger" class="delete-button" @click="handleDeleteAlbum(item)"/>
            </template>
        </van-swipe-cell>
        <van-action-sheet v-model="albumVisible" :title="albumItem.albumName">
            <van-card
                v-for="item in albumItem.srcList"
                @click="handlePreview(item)">
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
    </div>
</template>

<script>
import api, {listMineAlbum} from "../api";
import {genSrcPreviewSrc, getDocumentImage, handleGoToPreview} from "../util/JoyeaUtil"
import {mapGetters, mapActions} from "vuex";
import {convertItem} from "../util/ImageViewUtil";
import {switchShare} from "../api";

export default {

    name: "AlbumList",
    data() {
        return {
            albumVisible: false,
            active: 1,
            albumItem: {},
            albumList: [],
        }
    },
    methods: {
        getDocumentImage,
        handleGetDocumentImage(mimeType) {
            return getDocumentImage(mimeType)
        },
        ...mapActions([
            'clearFunc', 'addFunc', 'setOrderEditInfoFunc'
        ]),
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
        genPreviewUrl(srcNeid, srcHash, srcRev, srcType) {
            let previewType = 'pic';    // if video is av
            if (srcType.startsWith("doc")) {
                previewType = 'doc'
            } else if (srcType.startsWith("video")) {
                previewType = 'av'
            }
            return genSrcPreviewSrc(srcNeid, srcHash, srcRev, previewType, this.userInfo.session);
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
        listAlbum() {
            let previewType = 'pic';    // if video is av
            listMineAlbum().then(response => {
                this.albumList = [];
                response.data.forEach(album => {
                    album.srcList.forEach(src => {
                        src.url = genSrcPreviewSrc(src.srcNeid, src.srcHash, src.srcRev, previewType, this.userInfo.session)
                    })
                    let images = album.srcList.filter(item => {
                        return item.srcType.startsWith("image")
                    })
                    album.cover = images.length > 0 ? images[0].url : 'cover.png';
                });
                this.albumList = response.data;
            });
        }
    },
    computed: {
        ...mapGetters([
            'userInfo'
        ])
    },
    created() {
        this.listAlbum();
    }
}
</script>

<style scoped>
</style>