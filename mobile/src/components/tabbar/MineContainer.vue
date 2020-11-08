<template>
    <div id="mine" style="background-color: #f7f8fa;">
        <van-cell @click="myProfileVisible=true">
            <van-row>
                <van-col span="8">
                    <avatars backgroundColor="#eb7808" color="#ffffff" :size="80" :username="userInfo.name"></avatars>
                </van-col>
                <van-col span="16">
                    <h3 style="margin: 2px 0 0 0;">{{ userInfo.name }}</h3>
                    <van-divider style="margin: 2px 0 0 0;"/>
                    <h4 style="margin: 2px 0 0 0;">{{ userInfo.email }}</h4>
                </van-col>
            </van-row>
        </van-cell>
        <van-popup v-model="myProfileVisible" position="bottom" :style="{ height: '30%' }">
            <div style="text-align: center;padding: 15px 0px 5px 0px">我的功能</div>
            <van-divider/>
            <van-cell title="修改密码" is-link @click="handleClickChangePwd"/>
            <van-cell title="注销登录" is-link @click="handleLogout"/>
        </van-popup>
        <van-divider/>
        <van-cell-group title="功能">
            <van-cell title="排行榜" is-link @click="handleClickLeaderboard"/>
            <van-cell title="素材上传" is-link @click="handleClickUpload"/>
            <van-cell title="我的清单" is-link @click="handleClickMyList"/>

            <van-cell is-link @click="handleClickLatestUpload">
                <template #title>
                    <span class="custom-title">最新素材</span>
                    <van-tag v-if="hasNewUpload" type="danger" style="margin-left: 5px">NEW</van-tag>
                </template>
            </van-cell>
        </van-cell-group>
        <van-cell-group title="设置">
            <van-cell center title="图片加载原图">
                <template #right-icon>
                    <van-switch v-model="showRealImage" size="24"/>
                </template>
            </van-cell>
        </van-cell-group>
    </div>
</template>

<script>
import {mapGetters, mapActions} from "vuex";
import avatars from 'vue-avatars'
import {logout} from "@/api";
import {latestUpload} from "../../api";

export default {
    components: {
        avatars
    },
    name: "MineContainer",
    data() {
        return {
            myProfileVisible: false,
            nowDate: new Date().getTime(),// 获取时间戳
            hasNewUpload: false
        }
    },
    computed: {
        showRealImage: {
            get: function () {
                return this.$store.state.showRealImage;
            },
            set: function (newValue) {
                this.$store.state.showRealImage = newValue;
            }
        },
        ...mapGetters([
            'userInfo', 'latestReadUploadSrcId'
        ])
    },
    methods: {
        ...mapActions([
            'clearUserSessionFunc'
        ]),
        handleClickMyLike() {
            this.$notify('收藏功能开发中。。。');
        },
        handleClickMyList() {
            this.$router.push("/album")
        },
        handleClickChangePwd() {
            this.$router.push("/user/changePwd")
        },
        handleClickLeaderboard() {
            this.$router.push("/leaderboard")
        },
        handleClickUpload() {
            this.$router.push("/upload/index")
        },
        handleClickLatestUpload() {
            this.$router.push("/latestUpload")
        },
        handleLogout() {
            this.$dialog.confirm({
                message: "确定退出登录？"
            }).then(() => {
                logout().then(resp => {
                    this.clearUserSessionFunc().then(() => {
                        this.$router.push("/login");
                    })
                })
            }).catch(e => {
                console.log(e)
            })
        },
        handleTestMoment() {
        },
        handleGetHasNewUpload() {
            latestUpload(1).then(resp => {
                if (resp.code === 2000 && resp.data.length > 0) {
                    if (resp.data[0].id > this.latestReadUploadSrcId) {
                        this.hasNewUpload = true;
                    }
                }
            })
        }
    },
    activated() {
        this.handleGetHasNewUpload()
    }
}
let moment = require("moment") // 引入
</script>

<style scoped>

</style>