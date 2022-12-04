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
                <el-col :md="8" :xs="24" v-for="(img,fileIndex) in album.list" :key="fileIndex">
                    <el-card style="padding: 2px;margin: 5px;text-align:center;">
                        <img :src="img.url" style="width:100%;object-fit: contain;" :preview="album.name"
                             :onerror="defaultImg"
                             :preview-text="img.desc">
                        <div style="height: 20px;margin-top: 10px">
                            <el-tooltip class="item" effect="dark" :content="img.desc" placement="bottom">
                                <span class="svg_name"><b>解说词：</b>{{img.desc.length === 0 ? '暂未设置解说词':img.desc}}</span>
                            </el-tooltip>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </el-card>
    </section>
</template>

<script>
    import api, {check} from "../../api";
    import genSrcPreviewUrl from "../../utils/index"
    import VueLoadImage from 'vue-load-image'
    import {mapGetters} from "vuex";

    export default {
        name: "share",
        components: {
            'vue-load-image': VueLoadImage
        },
        data() {
            return {
                loading: false,
                album: {
                    name: "",
                    src: {}
                },
                defaultImg: 'this.src="' + require('@assets/error.png') + '"' //默认图地址
            }
        },
        computed:{
            ...mapGetters({
                'userInfo': 'userInfo/userInfo'
            })
        },
        methods: {},
        mounted() {
            this.loading = true;
            let _this = this;
            let albumId = this.$route.query.albumId;
            if (albumId) {
                check().then(resp => {
                    if (resp.code === 4001) {
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
                    } else {
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