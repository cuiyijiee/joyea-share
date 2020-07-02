<template>
    <div id="login">
        <div class="header">
            <div class="logo">
                <img src="../assets/logo.png" alt="default_logo">
            </div>
        </div>
        <div class="content">
            <van-field placeholder="用户名" v-model="loginForm.user" left-icon="phone-o"/>
            <!-- eye -->
            <van-field placeholder="密码" v-model="loginForm.pwd" left-icon="lock" type="password" :type="passwordType">
                <van-icon slot="right-icon" :name="passwordIcon" @click="switchPasswordType"/>
            </van-field>
            <div class="button-wrap">
                <van-button :loading="loginLoading" size="large" @click="handleLogin" type="info" loading-text="登录中...">
                    登录
                </van-button>
            </div>
        </div>
    </div>
</template>

<script>

    import {check, login} from "../api";

    const localStorage = window.localStorage;
    import {mapActions, mapGetters} from 'vuex';

    export default {
        name: "Login",
        data() {
            return {
                loginLoading: false,
                passwordType: "password",
                loginForm: {
                    user: "",
                    pwd: ""
                }
            }
        },
        methods: {
            ...mapActions([
                'updateUserInfoFunc'
            ]),
            switchPasswordType() {
                this.passwordType = this.passwordType === 'password' ? 'text' : 'password'
            },
            handleLogin() {
                this.loginLoading = true;
                let _this = this;
                login(this.loginForm.user, btoa(this.loginForm.pwd)).then(resp => {
                    if (resp.code === 2000) {
                        this.$notify({type: 'success', message: '欢迎回来，' + resp.data.userName + '！'});
                        _this.updateUserInfoFunc({
                            session: resp.data['session'], name: resp.data['userName'], email: this.loginForm.user
                        }).then(() => {
                        })
                        this.$router.replace({
                            name: "/",
                            params: {checked: true}
                        });
                    } else {
                        this.$notify({type: 'danger', message: '登陆失败，请检查用户名密码！'});
                    }
                    this.loginLoading = false;
                })
            }
        },
        computed: {
            passwordIcon: function () {
                return this.passwordType === 'password' ? 'closed-eye' : 'eye'
            },
            ...mapGetters([
                'userInfo'
            ]),
        },
        created() {
            this.loginForm.user = this.userInfo.email;
        }
    }
</script>

<style lang="scss" scoped>
    .header {
        width: 100%;
        height: 165px;
        display: flex;
        justify-content: center;
        align-items: center;

        .logo {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            overflow: hidden;

            img {
                width: 100%;
            }
        }
    }

    .content {
        width: 100%;
        height: auto;
        padding: 0 15px;
        box-sizing: border-box;

        .button-wrap {
            width: 100%;
            height: auto;
            margin-top: 15px;
        }
    }

    .more-wrap {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-top: 15px;
        font-size: 14px;

        a.link {
            color: #1989fa;
        }

        .switch-way {
            color: #333;
        }
    }
</style>