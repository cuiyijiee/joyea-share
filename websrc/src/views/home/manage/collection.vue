<template>
    <h1 style="color: #303133;" v-if="collectList.length === 0">您暂时没有收藏的资源！</h1>
    <section v-else>
        <div class="px10_divider">
            <el-row>
                <el-col :md="8" :xs="24" v-for="(img,fileIndex) in collectList" :key="img.path">
                    <el-card style="padding: 2px;margin: 5px">
                        <img :src="img.url"
                             style="width: 100%; height: 150px"
                             preview="我的收藏" :preview-text="img.path">
                        <div style="height: 20px;margin-top: 10px" class="clearfix">
                            <el-tooltip class="item" effect="dark" :content="img.path" placement="bottom">
                                <span class="svg_name">{{img.path.length > 30 ? img.path.substr(0,30) + "..." : img.path}}</span>
                            </el-tooltip>
                            <el-button type="danger" size="mini" icon="el-icon-delete"
                                       @click="handleDel(img,fileIndex)"></el-button>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
        <div style="width: 100%;margin: 10px 0" :class="{no_display:collectList.length === 0}">
            <el-pagination
                    background
                    layout="prev, pager, next"
                    @current-change="pageFind"
                    :page-size="pageSize"
                    :current-page="curPage"
                    :total="total">
            </el-pagination>
        </div>
    </section>
</template>

<script>

    import api from "../../../api";
    import genSrcPreviewUrl from "../../../utils/index"

    export default {
        name: "collection",
        data() {
            return {
                userInfo: {},
                curPage: 1,
                pageSize: 15,
                total: 0,
                collectList: []
            }
        },
        methods: {
            pageFind(curPage) {
                api({
                    action: 'srcCollect',
                    method: 'pageFind',
                    curPage: curPage,
                    pageSize: this.pageSize
                }).then(response => {
                    if (response.result) {
                        this.curPage = response.curPage;
                        this.pageSize = response.pageSize;
                        this.total = response.total;
                        let previewType = 'pic';    // if video is av
                        this.collectList = [];
                        response.list.forEach(src => {
                            this.collectList.push({
                                id: src.id,
                                url: genSrcPreviewUrl(src.neid, src.hash, src.rev, previewType, this.userInfo.session),
                                path: src.path
                            })
                        })
                    } else {
                        console.log(response.msg)
                    }
                })
            },
            handleDel(img, index) {
                this.$alert('你即将删除：【' + img.path + '】', '提示', {
                    confirmButtonText: '确定',
                    callback: action => {
                        if(action === 'confirm'){
                            api({
                                action: "srcCollect",
                                method: "delete",
                                id: img.id
                            }).then(resp => {
                                if (resp.result) {
                                    this.$message.success("成功删除收藏:" + img.path);
                                    this.collectList.splice(index,1);
                                } else {
                                    this.$message.error("删除失败,请重新再试！");
                                    console.log("删除失败：" + resp.msg);
                                }
                            })
                        }
                    }
                });
            }
        },
        mounted() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
            this.pageFind(this.curPage);
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
        width: 80%;
    }

    ul {
        margin-block-start: 0;
        margin-block-end: 0;
        padding-inline-start: 20px;
    }

    .no_display {
        display: none;
    }

    .clearfix:before,
    .clearfix:after {
        display: table;
        content: "";
    }

    .clearfix:after {
        clear: both
    }
</style>