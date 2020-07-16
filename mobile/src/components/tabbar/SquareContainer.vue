<template>
    <div style="margin-bottom: 50px">
        <van-dropdown-menu>
            <van-dropdown-item v-model="selectTag" :options="hangyeOption"/>
            <van-dropdown-item v-model="selectTag" :options="xianbieOption"/>
            <van-dropdown-item v-model="selectTag" :options="jixingOption"/>
        </van-dropdown-menu>
        <van-tabs @click="">
            <van-tab title="收藏数排序">
                <van-pull-refresh v-model="refreshing" @refresh="handleRefreshAlbum">
                    <van-list
                            v-model="loading"
                            :finished="finished"
                            finished-text="没有更多了"
                            @load="handleListAlbum">
                        <van-swipe-cell v-for="item in albumList" :key="item.path">
                            <van-card
                                    style="margin: 2px 0 2px 0"
                                    :num="item.srcList.length"
                                    @click="handleShowAlbum(item)"
                                    :title="item.albumName"
                                    :desc="item.shareDesc ? item.shareDesc : '暂未设置简介'"
                                    :thumb="item.cover">
                                <template #tags>
                                    <div style="color: #8c939d;line-height: 20px">上传者:{{item.userName}}</div>
                                </template>
                                <template #footer>
                                    <van-button size="mini" @click="handleCollect(item)">收藏</van-button>
                                </template>
                            </van-card>
                            <template #right>
                            </template>
                        </van-swipe-cell>
                    </van-list>
                </van-pull-refresh>
            </van-tab>
            <van-tab title="时间排序">
                <van-pull-refresh v-model="refreshing" @refresh="handleRefreshAlbum">
                    <van-list
                            v-model="loading"
                            :finished="finished"
                            finished-text="没有更多了"
                            @load="handleListAlbum">
                        <van-swipe-cell v-for="item in albumList" :key="item.path">
                            <van-card
                                    style="margin: 2px 0 2px 0"
                                    :num="item.srcList.length"
                                    @click="handleShowAlbum(item)"
                                    :title="item.albumName"
                                    :desc="item.shareDesc ? item.shareDesc : '暂未设置简介'"
                                    :thumb="item.cover">
                                <template #tags>
                                    <van-tag plain type="danger">{{item.userName}}</van-tag>
                                </template>
                                <template #footer>
                                    <van-button size="mini" @click="handleCollect(item)">收藏</van-button>
                                </template>
                            </van-card>
                            <template #right>
                            </template>
                        </van-swipe-cell>
                    </van-list>
                </van-pull-refresh>
            </van-tab>
        </van-tabs>
        <van-action-sheet v-model="albumVisible" :title="albumItem.albumName">
            <van-grid border :column-num="3" :gutter="5">
                <van-grid-item
                        v-for="item in albumItem.srcList">
                    <van-image v-if="item.srcType.startsWith('image')" class="my_icon my_preview_size"
                               @click="handlePreview(item)"
                               :src="genPreviewUrl(item.srcNeid,item.srcHash,item.srcRev,item.srcType)"/>
                    <van-image v-else-if="item.srcType.startsWith('video')" class="my_icon my_preview_size"
                               @click="handlePreview(item)"
                               src="video.png"/>
                    <van-image v-else-if="item.srcType.startsWith('doc')" class="my_icon my_preview_size"
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
    import {pageListAlbum} from "../../api";
    import {genSrcPreviewSrc, getDocumentImage} from "../../util/JoyeaUtil";
    import {mapGetters} from "vuex";
    import {hangyeOption, jixingOption, xianbieOption} from "../../util/JoyeaTagUtil";
    import {GenImageListView} from "../../util/ImageViewUtil";

    export default {
        name: "SquareContainer",
        data() {
            return {
                hangyeOption: hangyeOption,
                xianbieOption: xianbieOption,
                jixingOption: jixingOption,
                selectTag: -1,
                curPage: 1,
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
            handleCollect(item) {
                event.stopPropagation()
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
                pageListAlbum(this.curPage, this.pageSize, true).then(resp => {
                    this.loading = false;
                    this.refreshing = false;
                    resp.data.forEach(album => {
                        album.srcList.forEach(src => {
                            src.url = genSrcPreviewSrc(src.srcNeid, src.srcHash, src.srcRev, previewType, this.userInfo.session)
                        })
                        let images = album.srcList.filter(item => {
                            return item.srcType.startsWith("image")
                        })
                        album.cover = images.length > 0 ? images[0].url : 'cover.png';
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