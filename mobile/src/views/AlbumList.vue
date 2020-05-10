<template>
    <div id="album">
        <van-swipe-cell v-for="item in albumList" :key="item.path">
            <van-card
                    :num="item.list.length"
                    @click="handleShowAlbum(item)"
                    :title="item.name"
                    thumb="play.png"/>
            <template #right>
                <van-button square text="编辑" type="primary" class="delete-button" @click="handleEditAlbum(item)"/>
                <van-button square text="删除" type="danger" class="delete-button" @click="handleDeleteAlbum(item)"/>
            </template>
        </van-swipe-cell>
        <van-action-sheet v-model="albumVisible" :title="albumItem.name">
            <van-grid border :column-num="3">
                <van-grid-item
                        v-for="item in albumItem.list">
                    <van-image class="my_icon my_icon_size_large" @click="handlePreview(albumItem)"
                               :src="item.mime_type.startsWith('image') ? genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type) :'unknown.png'"/>
                    <div>
                        <van-field :value="item.path.substr(item.path.lastIndexOf('/')+1)" placeholder="123" readonly/>
                        <van-tag round type="success" v-for="tag in item.tags">{{tag.replace(markReg,"")}}</van-tag>
                    </div>
                </van-grid-item>
            </van-grid>
        </van-action-sheet>
    </div>
</template>

<script>
    import api from "../api";
    import {genSrcPreviewSrc} from "../util/JoyeaUtil"
    import {mapGetters, mapActions} from "vuex";
    import {ImagePreview} from 'vant';

    export default {
        components: {
            [ImagePreview.Component.name]: ImagePreview.Component,
        },
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
            ...mapActions([
                'clearFunc', 'addFunc', 'setOrderEditInfoFunc'
            ]),
            handleEditAlbum(item) {
                this.$dialog.confirm({
                    title: "重新编辑提醒",
                    message: "即将重新编辑清单【" + item.name + "】?"
                }).then(() => {
                    this.clearFunc().then(() => {
                        item.list.forEach(src => {
                            this.addFunc(src)
                        });
                        this.setOrderEditInfoFunc({
                            name:item.name,
                            id:item.id
                        }).then(() => {
                            this.$router.push("/order");
                        })
                    })
                })
            },
            handleDeleteAlbum(item) {
                this.$dialog.confirm({
                    title: "删除提醒",
                    message: "即将删除清单【" + item.name + "】?"
                }).then(() => {
                    api({
                        action: "album",
                        method: "delete",
                        album_id: item.id
                    }).then(response => {
                        if (response.result) {
                            this.albumList = this.albumList.filter(album => {
                                return album.id !== item.id;
                            });
                            this.$notify({
                                type: "success",
                                message: "你的清单【" + item.name + "】已成功删除！"
                            });
                        }
                    })
                })
            },
            handlePreview(album) {
                let list = album.list.filter(item => {
                    return item.mime_type.startsWith('image')
                }).map(item => item.url);
                if (list.length === 0) {
                    this.$notify({
                        message: "当前清单没有可以预览的图片！"
                    });
                } else {
                    ImagePreview({
                        images: list,
                        closeable: true
                    })
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
            handleShowAlbum(item) {
                this.albumItem = item;
                this.albumVisible = true;
            },
            listAlbum() {
                api({
                    action: 'album',
                    method: 'list'
                }).then(response => {
                    if (response.result) {
                        this.albumList = [];
                        let previewType = 'pic';    // if video is av
                        response.data.forEach(list => {
                            let imgUrl = [];
                            list.src.forEach(src => {
                                imgUrl.push({
                                    url: genSrcPreviewSrc(src.neid, src.hash, src.rev, previewType, this.userInfo.session),
                                    desc: src.desc,
                                    joyeaDesc: src.desc,
                                    path: src.path,
                                    neid: src.neid,
                                    hash: src.hash,
                                    rev: src.rev,
                                    filename: src.filename,
                                    bytes: src.bytes,
                                    size: src.size,
                                    mime_type: src.mime_type,
                                })
                            });
                            this.albumList.push({
                                id: list.album_id,
                                name: list.album_name,
                                shared: list.shared,
                                list: imgUrl
                            })
                        })
                    } else {
                        console.log(response.msg);
                        this.$message.error("加载失败！")
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
            this.listAlbum();
        }
    }
</script>

<style scoped>
    .my_icon_size_large {
        width: 100%;
        height: 100px;
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

    .delete-button {
        height: 100%;
    }
</style>