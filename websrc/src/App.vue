<template>
    <div id="app">
        <transition name="fade"
                    mode="out-in">
            <router-view></router-view>
        </transition>
    </div>
</template>
<script>
    import {mapGetters, mapActions} from "vuex"
    import {check} from "./api";

    export default {
        name: 'App',
        methods: {
            ...mapActions('userInfo', [
                'refreshSessionFunc'
            ]),
            _checkLogin() {
                let _this = this;
                check().then(resp => {
                    if (resp.code === 4001) {
                        _this.$message.error("登录信息已过期，请重新登陆！");
                        _this.$router.push("/login");
                    } else {
                        _this.refreshSessionFunc(resp.data);
                    }
                }).catch(e => {
                    _this.$message.error("与服务器断开链接，请联系管理员：" + e);
                    _this.$router.push("/login");
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
            if (localStorage.getItem("pc_store")) {
                this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(localStorage.getItem("pc_store"))))
            }
            setInterval(() => {
                localStorage.setItem("pc_store", JSON.stringify(this.$store.state))
            }, 2000)
            //在页面刷新时将vuex里的信息保存到sessionStorage里
            window.addEventListener("beforeunload", () => {
                localStorage.setItem("pc_store", JSON.stringify(this.$store.state))
            });
            document.oncontextmenu = function (event) {
                event.preventDefault();
            };
            if (this.$route.path !== '/login') {        //不在登陆界面
                if (this.userInfo.session){
                }else{
                    if (this.userInfo.session.length === 0) {
                        this.$router.push("/login");
                    }
                }
            } else {                                    //登陆界面
                // if (this.userInfo && this.userInfo.session && this.userInfo.session.length !== 0) {
                //     this.$router.replace({
                //         name: "/",
                //         params: {checked: true}
                //     });
                // }
            }
            this.checkLogin();
        },
        computed: {
            ...mapGetters({
                'userInfo': 'userInfo/userInfo'
            }),
        }
    }
</script>

<style lang="scss">
    body {
        margin: 0;
        padding: 0;
        /*background: url(assets/bg1.jpg) center !important;
            background-size: cover;*/
        // background: #1F2D3D;
        font-family: 'Noto Sans SC', Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
        font-size: 14px;
        -webkit-font-smoothing: antialiased;
    }

    #app {
        position: absolute;
        top: 0;
        bottom: 0;
        width: 100%;
    }

    .el-submenu [class^=fa] {
        vertical-align: baseline;
        margin-right: 10px;
    }

    .el-menu-item [class^=fa] {
        vertical-align: baseline;
        margin-right: 10px;
    }

    .toolbar {
        background: #f2f2f2;
        padding: 10px;
        //border:1px solid #dfe6ec;
        margin: 10px 0;

        .el-form-item {
            margin-bottom: 10px;
        }
    }

    .fade-enter-active,
    .fade-leave-active {
        transition: all .2s ease;
    }

    .fade-enter,
    .fade-leave-active {
        opacity: 0;
    }
</style>
