<template>
    <div style="padding: 10px">
        <van-steps :active="active">
            <van-step>选清单</van-step>
            <van-step>配封面</van-step>
            <van-step>打标签</van-step>
        </van-steps>
        <div v-if="active === 0">
            <van-empty v-if="myAlbumList.length === 0" class="custom-image"
                       image="https://img.yzcdn.cn/vant/custom-empty-image.png" description="您暂时没有清单"/>
            <div v-else>
                <van-cell v-for="(item,index) in myAlbumList" :title="item.albumName">
                    <template #right-icon>
                        <!--                        <van-tag v-if="item.shared" style="margin-right: 20px" type="danger">已分享</van-tag>-->
                        <van-button :type="item.shared ? 'danger':'primary'" @click="handleSelectShareAlbum(index)">
                            {{item.shared ? "取消分享" : "编辑分享"}}
                        </van-button>
                    </template>
                </van-cell>
            </div>
        </div>
        <div v-else-if="active===1">
            <h5>选封面</h5>
            <van-grid :column-num="3">
                <van-grid-item v-for="value in 6" :key="value" icon="photo-o" text="文字"/>
            </van-grid>
            <h5>填写清单简介</h5>
            <van-field v-model="editAlbum.shareDesc" rows="2" autosize placeholder="简介"
                       type="textarea" maxlength="50" show-word-limit/>
            <van-button type="danger" block @click="active = 0">上一步</van-button>
            <div style="margin: 5px"></div>
            <van-button type="primary" block @click="active = 2">下一步</van-button>
        </div>
        <div v-else-if="active===2">
            <van-cell-group>
                <van-cell title="行业">
                    <template #right-icon>
                        <van-dropdown-menu>
                            <van-dropdown-item v-model="hangyeId" :options="hangyeOption"/>
                        </van-dropdown-menu>
                    </template>
                </van-cell>
                <van-cell title="线别">
                    <template #right-icon>
                        <van-dropdown-menu>
                            <van-dropdown-item v-model="jixingId" :options="jixingOption"/>
                        </van-dropdown-menu>
                    </template>
                </van-cell>
                <van-cell title="剂型">
                    <template #right-icon>
                        <van-dropdown-menu>
                            <van-dropdown-item v-model="xianbieId" :options="xianbieOption"/>
                        </van-dropdown-menu>
                    </template>
                </van-cell>
            </van-cell-group>
            <van-button type="danger" block @click="active = 1">上一步</van-button>
            <div style="margin: 5px"></div>
            <van-button type="primary" block @click="handleShareAlbum">立即分享</van-button>
        </div>
    </div>
</template>

<script>
    import {listMineAlbum, switchShare} from "../api";
    import {hangyeOption, jixingOption, xianbieOption} from "../util/JoyeaTagUtil";

    export default {
        name: "EditShareAlbum",
        data() {
            return {
                hangyeId: -1,
                xianbieId: -1,
                jixingId: -1,
                hangyeOption: hangyeOption,
                xianbieOption: xianbieOption,
                jixingOption: jixingOption,
                active: 0,
                myAlbumList: [],
                editAlbum: {},
            }
        },
        methods: {
            handleListMineAlbum() {
                listMineAlbum().then(resp => {
                    this.myAlbumList = resp.data;
                })
            },
            handleSelectShareAlbum(index) {
                let toHandleAlbum = this.myAlbumList[index];
                if (toHandleAlbum.shared) {
                    switchShare(toHandleAlbum.albumId, false).then(resp => {
                        if (resp.data) {
                            this.$notify({
                                type: "success",
                                message: "取消分享成功！"
                            })
                            toHandleAlbum.shared = false;
                        }
                    })
                } else {
                    if (!toHandleAlbum.shareDesc) {
                        toHandleAlbum.shareDesc = "";
                    }
                    this.editAlbum = toHandleAlbum;
                    this.active = 1;
                }
            },
            handleShareAlbum() {
                switchShare(this.editAlbum.albumId, true, "", this.editAlbum.shareDesc)
                    .then(resp => {
                        if (resp.data) {
                            this.$notify({
                                type: "success",
                                message: "取消分享成功！"
                            });
                            this.$router.back();
                        }
                    })
            },
            handleResetForm() {
                this.active = 0;
                this.editAlbum = {};
            },
        },
        created() {
            this.handleResetForm();
            this.handleListMineAlbum();
        }
    }
</script>

<style scoped>

</style>