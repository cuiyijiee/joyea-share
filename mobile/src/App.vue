<template>
    <div id="app">
        <van-nav-bar :title="this.$route.name" fixed style="background: #1f1731;color: #ffffff"
                     :left-arrow="this.$route.meta.allowBack" @click-left="handleClickLeftNav">
            <template #right>
                <van-icon v-if="allowHome" @click="handleClickRightNav" name="home-o" size="18"/>
            </template>
        </van-nav-bar>
        <transition>
            <router-view/>
        </transition>
    </div>
</template>

<script>

    import {mapActions, mapGetters} from 'vuex'
    import {check} from "./api";

    export default {
        name: "App",
        data() {
            return {}
        },
        methods: {
            ...mapActions([
                'refreshSessionFunc'
            ]),
            handleClickLeftNav() {
                this.$router.back();
            },
            handleClickRightNav() {
                this.$router.push("/home")
            },
            _checkLogin() {
                let _this = this;
                check().then(resp => {
                    if (resp.code === 4001) {
                        _this.$notify({type: 'danger', message: '登录信息已过期，请重新登陆！'});
                        _this.$router.push("/login");
                    } else {
                        _this.refreshSessionFunc(resp.data);
                    }
                })
            },
            checkLogin() {
                let _this = this;
                this._checkLogin();
                setInterval(() => {
                    if (_this.$route.path !== '/login') {
                        _this._checkLogin()
                    }
                }, 2000)
            }
        },
        created() {
            //在页面加载时读取sessionStorage里的状态信息
            if (localStorage.getItem("store")) {
                this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(localStorage.getItem("store"))))
            }
            setInterval(() => {
                localStorage.setItem("store", JSON.stringify(this.$store.state))
            }, 2000)
            //在页面刷新时将vuex里的信息保存到sessionStorage里
            window.addEventListener("beforeunload", () => {
                localStorage.setItem("store", JSON.stringify(this.$store.state))
            });

            if (this.$route.path !== '/login') {        //不在登陆界面
                if (this.userInfo.session.length === 0) {
                    this.$router.push("/login");
                }
            } else {                                    //登陆界面
                if (this.userInfo.session.length !== 0) {
                    this.$router.replace({
                        name: "/",
                        params: {checked: true}
                    });
                }
            }
            this.checkLogin();
        },
        computed: {
            ...mapGetters([
                'userInfo'
            ]),
            allowHome: function () {
                return this.$route.meta.allowHome;
            }
        },
    }
</script>

<style>
    #app {
        overflow: hidden;
        margin-top: 40px;
    }
</style>
