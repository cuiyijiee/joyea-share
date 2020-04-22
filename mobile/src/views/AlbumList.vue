<template>
    <div id="album">
        <van-card
                v-for="item in albumList"
                :num="item.list.length"
                desc="描述信息"
                @click="handleShowAlbum(item)"
                :title="item.name"
                thumb="play.png"/>
        <van-action-sheet v-model="albumVisible" :title="albumItem.name" >
            <van-row v-for="item in albumItem.list">
                <van-col span="24">
                    <van-card
                            :desc="item.path"
                            :title="item.path.substr(item.path.lastIndexOf('/')+1)"
                            :thumb="item.url"/>
                </van-col>
            </van-row>

        </van-action-sheet>
    </div>
</template>

<script>
    import api from "../api";
    import {genSrcPreviewSrc} from "../util/JoyeaUtil"

    export default {
        name: "AlbumList",
        data() {
            return {
                albumVisible: false,
                albumItem: {},
                userInfo: {},
                albumList: []
            }
        },
        methods: {
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
                                    path: src.path,
                                    neid: src.neid,
                                    hash: src.hash,
                                    rev: src.rev,
                                    filename: src.filename,
                                    bytes: src.bytes,
                                    size: src.size,
                                    mime_type: src.mime_type
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
        created() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
            this.listAlbum();
        }
    }
</script>

<style scoped>

</style>