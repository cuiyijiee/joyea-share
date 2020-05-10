<template>
    <div id="app">
        <van-nav-bar :title="this.$route.name" fixed
                     :left-arrow="this.$route.meta.allowBack" @click-left="handleClickLeftNav"
                     >
            <template #right>
                <van-icon v-if="allowHome" @click="handleClickRightNav" name="home-o" size="18"/>
            </template>
        </van-nav-bar>
        <transition >
            <router-view/>
        </transition>
    </div>
</template>

<script>

    import {mapGetters} from 'vuex'

    export default {
        name: "App",
        data() {
            return {}
        },
        methods: {
            handleClickLeftNav() {
                this.$router.back();
            },
            handleClickRightNav() {
                this.$router.push("/home")
                // this.$dialog.confirm({
                //     title: '跳转提示',
                //     message: '是否一键返回工作台',
                // }).then(() => {
                //
                // })
            }
        },
        created() {
            //在页面加载时读取sessionStorage里的状态信息
            if (sessionStorage.getItem("store")) {
                this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(sessionStorage.getItem("store"))))
            }
            //在页面刷新时将vuex里的信息保存到sessionStorage里
            window.addEventListener("beforeunload", () => {
                sessionStorage.setItem("store", JSON.stringify(this.$store.state))
            });
            if (this.userInfo.session.length === 0 && this.$route.path !== '/login') {
                this.$router.push("/login")
            }
        },
        computed: {
            ...mapGetters([
                'userInfo'
            ]),
            allowHome:function () {
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
