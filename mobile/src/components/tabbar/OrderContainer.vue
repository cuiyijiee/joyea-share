<template>
    <div id="order">
        <van-empty v-if="orderList.length === 0" description="清单车空空如也"/>
        <van-cell v-for="item in orderList"
                  :key="item.path">
            <van-card
                    v-if="item.mime_type.startsWith('image')"
                    desc="描述信息"
                    :title="item.path.substr(item.path.lastIndexOf('/')+1)"
                    :thumb="genPreviewUrl(item.neid,item.hash,item.rev,item.mime_type)"/>
            <van-card
                    v-else-if="!item.mime_type.startsWith('image')"
                    desc="描述信息"
                    :title="item.path.substr(item.path.lastIndexOf('/')+1)"
                    thumb="play.png"/>
        </van-cell>

        <!-- 底部占位 -->
        <van-submit-bar style="margin-bottom: 50px" button-text="提交清单" :safe-area-inset-bottom="false"
                        @submit="handleSaveList">
            <div>{{"合计：" + this.$store.getters.getStateOrderList.length + "个"}}</div>
        </van-submit-bar>
    </div>
</template>

<script>

    import {mapState, mapActions} from "vuex"
    import {genSrcPreviewSrc} from "../../util/JoyeaUtil";
    import api from "../../api";

    export default {
        name: "OrderContainer",
        data() {
            return {
                userInfo: {}
            }
        },
        methods: {
            ...mapActions([
                'clearFunc',
            ]),
            handleSaveList() {
                this.$dialog.confirm({
                    title: '确认提交？',
                    message: '新清单',
                }).then(() => {
                    api({
                        action: 'album',
                        //method: this.toCreateAlbum.idEditMode ? 'reSave' : 'save',
                        method: 'save',
                        name: this.guid(),
                        src: this.orderList
                    }).then(response => {
                        if (response.result) {
                            this.$notify({type: 'success', message: '保存成功'});
                            this.$store.dispatch("clearFunc");
                        } else {
                            this.$notify({type: 'warning', message: '保存失败'});
                        }
                    });
                }).catch(() => {
                    console.log("取消保存！")
                })
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
            onClickItem(item) {

            },
            guid() {
                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    const r = Math.random() * 16 | 0,
                        v = c === 'x' ? r : (r & 0x3 | 0x8);
                    return v.toString(16);
                });
            }
        },
        computed: {
            ...mapState({
                orderList: state => state.orderList
            })
        },
        created() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
        },
    }
</script>

<style scoped>

</style>