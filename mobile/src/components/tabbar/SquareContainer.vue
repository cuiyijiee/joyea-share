<template>
    <div style="margin-bottom: 50px">
        <van-dropdown-menu active-color="#ee0a24">
            <van-dropdown-item v-model="selectedHangyeTag" :options="hangyeOption" @change="handleTagChange"/>
            <van-dropdown-item v-model="selectedXianbieTag" :options="xianbieOption" @change="handleTagChange"/>
            <van-dropdown-item v-model="selectedJixingTag" :options="jixingOption" @change="handleTagChange"/>
        </van-dropdown-menu>
        <van-dropdown-menu active-color="#ee0a24">
            <van-dropdown-item v-model="selectedJieduanTag" :options="jieduanOption" @change="handleTagChange"/>
            <van-dropdown-item v-model="selectedShichangTag" :options="shichangOption" @change="handleTagChange"/>
        </van-dropdown-menu>
        <van-tabs @click="handleClickTab">
            <van-tab title="收藏数排序">
            </van-tab>
            <van-tab title="时间排序">
            </van-tab>
        </van-tabs>
        <van-pull-refresh v-model="refreshing" @refresh="handleRefreshAlbum">
            <van-list
                    v-model="loading"
                    :finished="finished"
                    finished-text="没有更多了"
                    @load="handleListAlbum">
                <van-swipe-cell v-for="item in albumList" :key="item.path">
                    <van-card
                            style="margin: 2px 0 2px 0"
                            @click="handleShowAlbum(item)"
                            :title="item.albumName"
                            :thumb="item.cover">
                        <template #tags>
                            <div style="word-break: break-all;white-space: normal;">{{item.shareDesc ?
                                item.shareDesc : '暂未设置简介'}}
                            </div>
                            <div style="color: #8c939d;line-height: 20px">上传者: {{item.userName}}</div>
                            <div style="color: #8c939d;line-height: 20px">创建于: {{item.createdAt}}</div>
                        </template>
                        <template #footer>
                            <div style="margin-top: 5px">
                                <span style="position: absolute;left: 20px">
                                <van-tag mark type="warning" style="margin-right: 2px;" v-if="item.hangyeTagId !== -1"
                                         color="#cccccc">
                                    {{hangyeOption.find(option => {return option.value === item.hangyeTagId}).text}}
                                </van-tag>
                                <van-tag mark type="warning" style="margin-right: 2px" v-if="item.xianbieTagId !== -1"
                                         color="#cccccc">
                                    {{xianbieOption.find(option => {return option.value === item.xianbieTagId}).text}}
                                </van-tag>
                                <van-tag mark type="warning" style="margin-right: 2px" v-if="item.jixingTagId !== -1"
                                         color="#cccccc">
                                    {{jixingOption.find(option => {return option.value === item.jixingTagId}).text}}
                                </van-tag>
                                <van-tag mark type="warning" style="margin-right: 2px" v-if="item.jieduanTagId !== -1"
                                         color="#cccccc">
                                    {{jieduanOption.find(option => {return option.value === item.jieduanTagId}).text}}
                                </van-tag>
                                <van-tag mark type="warning" style="margin-right: 2px" v-if="item.shichangTagId !== -1"
                                         color="#cccccc">
                                    {{shichangOption.find(option => {return option.value === item.shichangTagId}).text}}
                                </van-tag>
                            </span>
                                <van-button size="mini" @click="handleCollect(item)">收藏 {{item.likeNum}}</van-button>
                            </div>
                        </template>
                    </van-card>
                    <template #right>
                    </template>
                </van-swipe-cell>
            </van-list>
        </van-pull-refresh>
        <van-action-sheet v-model="albumVisible" :title="albumItem.albumName">
            <van-grid border :column-num="3" :gutter="5">
                <van-grid-item
                        v-for="item in albumItem.srcList" @click="handleGotoPreview(item)">
                    <van-image v-if="item.srcType.startsWith('image')" class="my_icon my_preview_size"
                               @click="handlePreview(item)"
                               :src="genPreviewUrl(item.srcNeid,item.srcHash,item.srcRev,item.srcType)"/>
                    <van-image v-else-if="item.srcType.startsWith('video')" class="my_icon my_preview_size"
                               @click="handlePreviewVideo(item)"
                               src="video.png"/>
                    <van-image v-else-if="item.srcType.startsWith('doc')" class="my_icon my_preview_size"
                               @click="handlePreviewDoc(item)"
                               :src="handleGetDocumentImage(item.srcType)"/>
                    <van-image v-else class="my_icon my_preview_size" @click="handlePreview(item)"
                               src="unknown.png"/>
                    <div style="width: 98%">
                        <div style="font-size:10px;-webkit-text-size-adjust: none;word-break:break-all;">
                            {{item.srcPath.substr(item.srcPath.lastIndexOf('/')+1)}}
                        </div>
                        <van-tag round type="success" v-for="tag in item.tags">{{tag.replace(markReg,"")}}</van-tag>
                    </div>
                </van-grid-item>
            </van-grid>
        </van-action-sheet>
        <van-button @click="handleClickShareAlbum" class="float-button" round type="info" icon="plus"></van-button>
    </div>
</template>

<script>
    import {pageListAlbum, copyAlbum} from "../../api";
    import {genSrcPreviewSrc, getDocumentImage, handleGoToPreview} from "../../util/JoyeaUtil";
    import {mapGetters} from "vuex";
    import {hangyeOption, jixingOption, xianbieOption, jieduanOption, shichangOption} from "../../util/JoyeaTagUtil";
    import {albumCoverOption} from "../../util/JoyeaTagUtil";
    import {GenImageListView} from "../../util/ImageViewUtil";

    export default {
        name: "SquareContainer",
        data() {
            return {
                hangyeOption: hangyeOption,
                xianbieOption: xianbieOption,
                jixingOption: jixingOption,
                jieduanOption: jieduanOption,
                shichangOption: shichangOption,

                albumCoverOption: albumCoverOption,

                selectedHangyeTag: -1,
                selectedXianbieTag: -1,
                selectedJixingTag: -1,
                selectedJieduanTag: -1,
                selectedShichangTag: -1,
                curPage: 1,
                sortType: 0,
                pageSize: 10,
                loading: false,
                refreshing: false,
                finished: false,
                albumList: [],
                albumVisible: false,
                albumItem: {},
            }
        },
        methods: {
            handleTagChange(value) {
                this.handleRefreshAlbum();
            },
            handleClickTab(index, title) {
                this.sortType = index;
                this.handleRefreshAlbum();
            },
            handleCollect(item) {
                event.stopPropagation();
                if (item.userId === this.userInfo.email) {
                    this.$notify({
                        type: "danger",
                        message: "不能收藏自己的清单!"
                    })
                } else {
                    copyAlbum(item.albumId).then(resp => {
                        this.$notify({
                            type: "success",
                            message: "收藏成功!"
                        });
                        item.likeNum++;
                    })
                }
            },
            handleRefreshAlbum() {
                this.finished = false;
                this.refreshing = true;
                this.curPage = 1;
                this.albumList = [];
                this.handleListAlbum();
            },
            handleShowAlbum(item) {
                this.albumItem = item;
                this.albumVisible = true;
            },
            handleGetDocumentImage(mimeType) {
                return getDocumentImage(mimeType)
            },
            handleGotoPreview(item) {
                if (item.mime_type.startsWith("image")) {
                } else {
                    this.handlePreview(item)
                }
            },
            handlePreviewDoc(clickItem) {
                handleGoToPreview(this, clickItem, this.userInfo.session);
                event.stopPropagation();
            },
            handlePreviewVideo(clickItem) {
                handleGoToPreview(this, clickItem, this.userInfo.session);
                event.stopPropagation();
            },
            handlePreview(clickItem) {
                GenImageListView(this, this.albumItem.srcList, this.userInfo.session, clickItem);
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
            handleListAlbum() {
                this.loading = true;
                let previewType = 'pic';    // if video is av
                pageListAlbum(this.curPage, this.pageSize, true, this.sortType,
                    this.selectedHangyeTag,
                    this.selectedXianbieTag,
                    this.selectedJixingTag,
                    this.selectedJieduanTag,
                    this.selectedShichangTag
                ).then(resp => {
                    this.loading = false;
                    this.refreshing = false;
                    resp.data.forEach(album => {
                        album.srcList.forEach(src => {
                            src.url = genSrcPreviewSrc(src.srcNeid, src.srcHash, src.srcRev, previewType, this.userInfo.session)
                        })
                        if (album.shareLocalCoverId) {
                            let localCoverItem = this.albumCoverOption.find(item => album.shareLocalCoverId === item.value + "");
                            if (localCoverItem) {
                                album.cover = localCoverItem.url;
                            }else {
                                let images = album.srcList.filter(item => {
                                    return item.srcType.startsWith("image") && item.srcNeid === album.shareCoverNeid;
                                })
                                album.cover = images.length > 0 ? images[0].url : 'cover.png';
                            }
                        } else {
                            let images = album.srcList.filter(item => {
                                return item.srcType.startsWith("image") && item.srcNeid === album.shareCoverNeid;
                            })
                            album.cover = images.length > 0 ? images[0].url : 'cover.png';
                        }
                        this.albumList.push(album);
                    });
                    if (resp.data.length < this.pageSize) {
                        this.finished = true;
                    }
                    this.curPage++;
                })
            },
            handleClickShareAlbum() {
                this.$router.push("/editShare");
            },
        },
        computed: {
            ...mapGetters([
                'userInfo'
            ])
        },
        created() {
            this.handleListAlbum()
        }
    }
</script>

<style scoped>
    .my-swipe .van-swipe-item {
        color: #fff;
        font-size: 20px;
        line-height: 150px;
        text-align: center;
        background-color: #39a9ed;
    }

    .float-button {
        position: fixed;
        bottom: 90px;
        right: 50px;
    }

    .van-card__content .van-tag::after {
        border-color: #ffffff;
    }
</style>