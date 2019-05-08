<template>
    <section
            v-loading="loading"
            element-loading-text="拼命加载中">
        <el-card style="margin-bottom: 10px">
            <div slot="header" class="clearfix">
                <span style="color: #fff;font-weight:bold;">{{album.name}}</span>
                <!--                <span style="float: right">-->
                <!--                        <el-button :type="album.shared ? 'warning':''" icon="el-icon-share" circle size="mini"-->
                <!--                                   @click="handleShare(index)"/>-->
                <!--                        <el-button icon="el-icon-delete" circle size="mini" @click="handleDelete(index)"/>-->
                <!--                    </span>-->
            </div>
            <el-row>
                <el-col :md="8" :xs="24" v-for="(img,fileIndex) in album.list">
                    <el-card style="padding: 2px;margin: 5px">
                        <img :src="img.url"
                             style="width: 100%; height: 150px"
                             :preview="album.name" :preview-text="'解说词：' + img.desc">
                        <div style="height: 20px;margin-top: 10px">
                            <el-tooltip class="item" effect="dark" :content="img.desc" placement="bottom">
                                <span class="svg_name"><b>解说词：</b>{{img.desc}}</span>
                            </el-tooltip>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </el-card>
    </section>
</template>

<script>
    import api from "../../api";
    import genSrcPreviewUrl from "../../utils/index"

    export default {
        name: "share",
        data() {
            return {
                userInfo: {},
                loading: false,
                album: {
                    name: "",
                    src: {}
                }
            }
        },
        methods: {},
        mounted() {
            this.loading = true;
            let _this = this;
            let albumId = this.$route.query.albumId;
            if (albumId) {
                let user = localStorage.getItem('userInfo');
                if (user) {
                    this.userInfo = JSON.parse(user)
                }
                api({
                    action: "user",
                    method: "check"
                }).then(response => {
                    if (response.result) {
                        api({
                            action: "album",
                            method: "shareList",
                            album_id: parseInt(albumId)
                        }).then(response => {
                            if (response.result) {
                                let imgUrl = [];
                                let previewType = 'pic';    // if video is av
                                response.album.src.forEach(src => {
                                    imgUrl.push({
                                        url: genSrcPreviewUrl(src.neid, src.hash, src.rev, previewType, this.userInfo.session),
                                        desc: src.desc
                                    })
                                });
                                _this.album.list = imgUrl;
                                _this.album.name = response.album.album_name;
                                _this.album.id = response.album.album_id;
                            } else {
                                this.$notify.error({
                                    duration: 0,
                                    title: "提示",
                                    message: "获取分享信息失败：" + response.msg
                                })
                            }
                            this.loading = false;
                        })
                    } else {
                        this.$router.push({
                            name: "login",
                            query: {
                                ref: btoa(JSON.stringify({
                                    fromShare: true,
                                    albumId: albumId
                                }))
                            }
                        });
                        this.loading = false;
                    }
                })
            } else {
                this.$message.error("分享链接无效，已跳转至登录界面");
                this.$router.push("/login")
            }
        }
    }
</script>

<style scoped>
    .px10_divider {
        margin-top: 10px;
    }

    #list {
        overflow-x: auto;
        list-style: none;
        white-space: nowrap;
        width: auto;
    }

    ::-webkit-scrollbar {
        width: 0 !important
    }

    .svg_name {
        white-space: normal;
        color: #606266;
        font-weight: 500;
        font-size: 14px;
    }

    .item {
        display: inline-block;
        padding-right: 20px;
    }

    ul {
        margin-block-start: 0;
        margin-block-end: 0;
        padding-inline-start: 20px;
    }

    .no_display {
        display: none;
    }
</style>