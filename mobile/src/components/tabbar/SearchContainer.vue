<template>
    <div id="search" style="padding-top: 5px;margin-bottom: 80px">
        <vm-back-top :bottom="100" :duration="1000" :timing="'ease'"></vm-back-top>
        <van-tabs v-model="currentTypeActive" sticky>
            <van-tab title="全部">
                <van-empty v-if="searchResultList.length === 0" description="没有找到搜索结果！"/>
                <van-list
                        v-model="searchLoading"
                        :finished="searchFinished"
                        finished-text="没有更多了"
                        @load="handleSearch">
                    <van-cell v-for="item in searchResultList" :key="item.path">
                        <van-row>
                            <van-col span="4">
                                <van-icon v-if="item['is_dir']" class="my_icon" name="credit-pay"/>
                                <van-icon v-else-if="item.mime_type.startsWith('video')" class="my_icon"
                                          name="video-o"/>
                                <img v-else-if="item.mime_type.startsWith('image')" class="my_icon my_icon_size"
                                     preview="previewList" :preview-text="item.path"
                                     :src="genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type)"/>
                                <van-icon v-else-if="item.mime_type.startsWith('doc')" class="my_icon" name="orders-o"/>
                                <van-icon v-else class="my_icon" name="info-o"/>
                            </van-col>
                            <van-col span="16" @click="handleClickItem(item)">
                                {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                                <van-tag mark style="margin-right: 2px" v-for="tag in item.tags">
                                    {{tag.replace(markReg,"")}}
                                </van-tag>
                            </van-col>
                            <van-col span="4">
                                <van-button v-if="!item['is_dir']" icon="plus" size="small" type="danger" plain
                                            style="border: #ffffff" @click="handleClickAddItem(item)"/>
                            </van-col>
                        </van-row>
                    </van-cell>
                </van-list>
            </van-tab>
            <van-tab title="文件夹" :badge="searchResultList.filter(item => item.is_dir).length">
                <van-list
                        v-model="searchLoading"
                        :finished="searchFinished"
                        finished-text="没有更多了"
                        @load="handleSearch">
                    <van-cell v-for="item in searchResultList" v-if="item.is_dir"
                              :key="item.path" @click="handleClickItem(item)">
                        <van-icon class="my_icon" name="credit-pay"/>
                        {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                        <van-tag mark style="margin-right: 2px" v-for="tag in item.tags">{{tag.replace(markReg,"")}}
                        </van-tag>
                    </van-cell>
                </van-list>
            </van-tab>
            <van-tab title="图片"
                     :badge="searchResultList.filter(item => !item.is_dir && item.mime_type.startsWith('image')).length">
                <van-list
                        v-model="searchLoading"
                        :finished="searchFinished"
                        finished-text="没有更多了"
                        @load="handleSearch">
                    <van-grid :border="false" :column-num="3" style="background: #ffffff">
                        <van-grid-item
                                v-for="item in searchResultList.filter(item => !item.is_dir && item.mime_type.startsWith('image'))">
                            <div>
                                <img class="my_icon my_icon_size_large" preview="buildList" :preview-text="item.path"
                                     :src="genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type)"/>
                                <van-button @click="handleClickAddItem(item)" round plain hairline type="danger"
                                            style="position: absolute; top: 0; left: 0; border:#ffffff" icon="plus"
                                            size="small"/>
                            </div>
                            <div>
                                <div style="font-size:10px;-webkit-text-size-adjust: none;">
                                    {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                                </div>
                                <van-tag mark style="margin-right: 2px" v-for="tag in item.tags">
                                    {{tag.replace(markReg,"")}}
                                </van-tag>
                            </div>
                        </van-grid-item>
                    </van-grid>
                </van-list>
            </van-tab>
            <van-tab title="视频"
                     :badge="searchResultList.filter(item => !item.is_dir && item.mime_type.startsWith('video')).length">
                <van-list
                        v-model="searchLoading"
                        :finished="searchFinished"
                        finished-text="没有更多了"
                        @load="handleSearch">
                    <van-cell v-for="item in searchResultList" v-if="!item.is_dir && item.mime_type.startsWith('video')"
                              :key="item.path">
                        <van-row>
                            <van-col span="4">
                                <van-icon class="my_icon" name="video-o"/>
                            </van-col>
                            <van-col span="16" @click="handlePreview(item)">
                                {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                                <van-tag mark style="margin-right: 2px" v-for="tag in item.tags">
                                    {{tag.replace(markReg,"")}}
                                </van-tag>
                            </van-col>
                            <van-col span="4">
                                <van-button v-if="!item['is_dir']" icon="plus" size="small" type="danger" plain
                                            style="border: #ffffff" @click="handleClickAddItem(item)"/>
                            </van-col>
                        </van-row>
                    </van-cell>
                </van-list>
            </van-tab>
            <van-tab title="文档"
                     :badge="searchResultList.filter(item => !item.is_dir && item.mime_type.startsWith('doc')).length">
                <van-list
                        v-model="searchLoading"
                        :finished="searchFinished"
                        finished-text="没有更多了"
                        @load="handleSearch">
                    <van-cell v-for="item in searchResultList" v-if="!item.is_dir && item.mime_type.startsWith('doc')"
                              :key="item.path">
                        <van-row>
                            <van-col span="4">
                                <van-icon class="my_icon" name="video-o"/>
                            </van-col>
                            <van-col span="16" @click="handlePreview(item)">
                                {{item.path.substr(item.path.lastIndexOf('/')+1)}}
                                <van-tag mark style="margin-right: 2px" v-for="tag in item.tags">
                                    {{tag.replace(markReg,"")}}
                                </van-tag>
                            </van-col>
                            <van-col span="4">
                                <van-button v-if="!item['is_dir']" icon="plus" size="small" type="danger" plain
                                            style="border: #ffffff" @click="handleClickAddItem(item)"/>
                            </van-col>
                        </van-row>
                    </van-cell>
                </van-list>
            </van-tab>
        </van-tabs>
    </div>
</template>

<script>
    import api from "../../api";
    import {mapGetters} from "vuex";
    import {genSrcPreviewSrc, handleGoToPreview} from "../../util/JoyeaUtil";
    import eventBus from "../../service/eventbus"
    import VmBackTop from 'vue-multiple-back-top'

    export default {
        name: "SearchResult",
        components: {
            VmBackTop
        },
        computed: {
            ...mapGetters([
                'userInfo'
            ])
        },
        data() {
            return {
                markReg: /<mark>|<\/mark>/g,
                currentTypeActive: 0,
                searchResultList: [],
                searchKey: "",
                searchOffset: 0,
                searchLoading: false,
                searchFinished: false,
                dir: {
                    currentPath: [],
                    tableData: [],
                    loadingDir: false
                }
            }
        },
        methods: {
            handleClickItem(item) {
                if (item['is_dir']) {
                    eventBus.$emit('showDir', item);
                    this.$router.back();
                } else {
                    this.handlePreview(item)
                }
            },
            handlePreview(item) {
                handleGoToPreview(item, this.userInfo.session)
            },
            handleClickAddItem(item) {
                if (!this.$store.getters.exist(item['neid'])) {
                    this.$store.dispatch("addFunc", item).then(() => {
                        this.$notify({type: 'success', message: '加入素材车成功！'});

                    }).catch(exception => {
                        this.$dialog.alert({
                            message: '加入素材车失败：' + exception
                        })
                    })
                } else {
                    this.$dialog.alert({
                        message: '该素材已经在素材车中'
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
            handleGoToPreview(row) {
                let previewType = 'pic';    // if video is av
                if (row.mime_type.startsWith("doc")) {
                    previewType = 'doc'
                } else if (row.mime_type.startsWith("video")) {
                    previewType = 'av'
                }
                let url = genSrcPreviewSrc(row.neid, row.hash, row.rev, previewType, this.userInfo.session);
                if (row.mime_type.startsWith("doc")) {
                    window.open(url);
                } else if (row.mime_type.startsWith("video")) {
                    window.open(url);
                }
            },
            handleSearch() {
                this.searchLoading = true;
                api({
                    action: 'search',
                    searchKey: this.searchKey,
                    offset: this.searchOffset,
                }).then(response => {
                    if (response.result) {
                        this.searchFinished = !response["has_more"];
                        if (!this.searchFinished) {
                            this.searchOffset = response["next_offset"];
                        }
                        if (response.content.length === 0) {
                            this.$dialog({
                                message: "没有搜索到与【" + this.searchKey + "】有关的文件或文件夹！"
                            });
                            //this.$router.back();
                        } else {
                            response.content.forEach(item => {
                                item.joyeaDesc = "";
                                item.isModify = false;
                                this.searchResultList.push(item);
                            })
                        }
                    } else {
                        this.$notify.error({
                            title: '搜索出错',
                            message: '搜索过程出现错误：' + response.msg
                        });
                        console.log(response.msg)
                    }
                }).finally(() => {
                    // 加载状态结束
                    this.searchLoading = false;
                });
            },
        },
        activated() {
            this.currentTypeActive = 0;
            if (this.$route.query.searchKey) {
                this.searchKey = this.$route.query.searchKey;
                this.searchResultList = [];
                this.searchOffset = 0;
                this.searchFinished = false;
                this.handleSearch();
            }
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

    .top {
        padding: 10px;
        background: #ee0a24;
        color: #fff;
        text-align: center;
        border-radius: 2px;
    }

</style>