<template>
    <div id="bind" style="padding-top: 20px">
        <van-cell-group inset>
            <van-field v-model="originName" type="tel" label="工号"/>
            <van-field v-model="originPwd" type="password" label="密码"/>
        </van-cell-group>
        <van-button :loading="loginLoading" size="large" @click="handleBindCheck" type="info" loading-text="校验中...">
            绑定
        </van-button>

        <div style="text-align: center;padding-top: 50px;text-decoration: underline ;color: #8c939d"
             @click="checkNewConfirmDialogVisible = true">我没有系统账号，直接登录
        </div>

        <van-divider></van-divider>
        <div style="padding: 50px; font-size: small;text-indent:25px; vertical-align: bottom">
            <p style="color: #8c939d;">
                由于版本更新，现在首次登录需要您进行一次pc端账号绑定（通常管理员已使用您的工号注册了账号）或重新注册一个账号。绑定后就可以使用该账号登录PC端素材库了。
            </p>
        </div>
        <van-popup
            v-model:show="checkConfirmDialogVisible"
            round
            @close="handleClearCheckInfo"
            position="bottom"
            :style="{ height: '50%' }"
        >
            <div style="text-align: center">
                <h3>绑定确认</h3>
                <div>您原登录名:<b>{{ joyeaUser.joyeaName }}</b></div>
                <div>Next+用户名为:<b>{{ nextPlusUser.name }}</b></div>
                <br>
                <div style="margin-left: 30px;margin-right: 30px">
                    是否绑定这两个账号数据？绑定后两个账号合并为一个账号，PC端认然可用原账号密码登录，手机端默认使用Next+账号自动登录
                </div>
            </div>

            <van-button :loading="loginLoading" style="margin-top: 30px" size="large" @click="handleBind(false)"
                        type="info" loading-text="校验中...">
                我已知晓
            </van-button>

        </van-popup>

        <van-popup
            v-model:show="checkNewConfirmDialogVisible"
            round
            position="bottom"
            :style="{ height: '50%' }"
        >
            <div style="text-align: center">
                <h3>绑定确认</h3>
                <div>您电脑端登录账号为:<b>{{ ytmMobile }}</b></div>
                <div>默认密码为:<b>123456</b></div>
                <br>
            </div>

            <van-button :loading="loginLoading" style="margin-top: 30px" size="large" @click="handleBind(true)"
                        type="info" loading-text="校验中...">
                我已知晓
            </van-button>

        </van-popup>

    </div>
</template>

<script>
import {mapActions, mapGetters} from "vuex";
import {userBindCheck, userBind} from "@/api";

export default {
    name: "UserBind",
    data() {
        return {
            originName: "",
            originPwd: "",
            loginLoading: false,
            checkConfirmDialogVisible: false,
            checkNewConfirmDialogVisible: false,

            nextPlusUser: {},
            joyeaUser: {},
            ytmId: "",
            ytmMobile: ""
        }
    },
    methods: {
        ...mapActions([
            'refreshSessionFunc', 'updateUserInfoFunc', 'clearFunc'
        ]),
        handleClearCheckInfo() {
            this.nextPlusUser = {};
            this.joyeaUser = {};
        },
        handleBind(isNew) {
            let _this = this;
            this.loginLoading = true;
            userBind(this.joyeaUser.joyeaId, this.ytmId, isNew).then(resp => {
                if (resp.code === 2000) {
                    _this.updateUserInfoFunc({
                        session: resp.data['session'], name: resp.data['userName'], email: _this.ytmMobile, isBind: true
                    })
                    _this.$notify({type: 'success', message: '绑定成功！'});
                    _this.$router.push("/");
                } else {
                    _this.$notify({type: 'danger', message: '绑定失败，请检查用户名密码或该用户已经被绑定！'});
                }
            }).finally(() => {
                this.loginLoading = false;
            })
        },
        handleBindCheck() {
            userBindCheck(this.originName, this.originPwd, this.ytmId).then(resp => {
                if (resp.code === 2000) {
                    this.nextPlusUser = resp.data.nextPlusUser;
                    this.joyeaUser = resp.data.joyeaUser;
                    this.checkConfirmDialogVisible = true;
                } else {
                    this.$notify({type: 'danger', message: '绑定失败，请检查用户名密码或该用户已经被绑定！'});
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
        this.ytmId = this.$route.query.ytmId;
        this.ytmMobile = this.$route.query.ytmMobile;
    }
}
</script>
<style scoped>
</style>