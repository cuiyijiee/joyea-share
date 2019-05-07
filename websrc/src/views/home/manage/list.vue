<template>
    <section>
        <!--工具条-->
        <el-input placeholder="输入清单名称查找" v-model="search.key" class="input-with-select px10_divider"
                  style="display: none">
            <el-button slot="append" icon="el-icon-search" v-on:click="handleSearch"></el-button>
        </el-input>

        <div class="px10_divider">
            <el-card v-for="(album,index) in albumList" style="margin-bottom: 10px">
                <div slot="header" class="clearfix">
                    <span style="color: #fff;font-weight:bold;">{{album.name}}</span>
                    <span style="float: right">
                        <el-button :type="album.shared ? 'warning':''" icon="el-icon-share" circle size="mini"
                                   @click="handleShare(index)"/>
                        <el-button icon="el-icon-delete" circle size="mini" @click="handleDelete(index)"/>
                    </span>
                </div>
                <el-row>
                    <el-col :md="8" :xs="24" v-for="(img,fileIndex) in album.list">
                        <el-card style="padding: 2px;margin: 5px">
                            <img :src="img.url"
                                 style="width: 100%; height: 150px"
                                 :preview="album.name" :preview-text="img.desc">
                            <div style="height: 20px;margin-top: 10px">
                                <el-tooltip class="item" effect="dark" :content="img.desc" placement="bottom">
                                    <span class="svg_name"><b>解说词：</b>{{img.desc}}</span>
                                </el-tooltip>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
            </el-card>
        </div>
    </section>
</template>

<script>

    import api from "../../../api"
    import genSrcPreviewUrl from "../../../utils/index"

    export default {
        name: "list",
        data() {
            return {
                userInfo: {},
                search: {
                    key: ''
                },
                albumList: []
            }
        },
        methods: {
            handleSearch() {

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
                                    url: genSrcPreviewUrl(src.neid, src.hash, src.rev, previewType, this.userInfo.session),
                                    desc: src.desc
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
            },
            handleDelete(index) {
                this.$alert('即将删除清单【 ' + this.albumList[index].name + ' 】！', '删除提示', {
                    confirmButtonText: '删除',
                    callback: action => {
                        if (action === "confirm") {
                            api({
                                action: "album",
                                method: "delete",
                                album_id: this.albumList[index].id
                            }).then(response => {
                                if (response.result) {
                                    this.$message.success("清单【 " + this.albumList[index].name + " 】删除成功！");
                                    this.albumList.splice(index, 1)
                                } else {
                                    this.$notify.error({
                                        title: "提示",
                                        message: "清单【 " + this.albumList[index].name + " 】删除失败！"
                                    })
                                }
                            })
                        }
                    }
                });
            },
            handleShare(index) {
                api({
                    action: "album",
                    method: this.albumList[index].shared ? "unShare" : "share",
                    album_id: this.albumList[index].id
                }).then(response => {
                    if (response.result) {
                        this.albumList[index].shared = !this.albumList[index].shared;

                    } else {
                        this.$message.error("操作失败！")
                    }
                })
            }
        },
        mounted() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
            this.listAlbum();
        }
    }
</script>

<style scoped lang="scss">

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