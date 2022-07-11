<template>
    <div id="main">
        <!-- 中间的 路由 router-view 区域 -->
        <transition style="padding-bottom: 80px">
            <keep-alive>
                <router-view></router-view>
            </keep-alive>
        </transition>
        <van-tabbar
            v-model="active"
            active-color="#07c160"
            inactive-color="#000"
            safe-area-inset-bottom
            z-index="4">
            <van-tabbar-item icon="home-o" to="/home" @click="handleClickRoot">工作台</van-tabbar-item>
            <van-tabbar-item :badge="orderListSize" icon="shopping-cart-o" to="/order">素材车</van-tabbar-item>
<!--            <van-tabbar-item icon="gem-o" to="/square">广场</van-tabbar-item>-->
            <van-tabbar-item icon="manager-o" to="/mine">我的</van-tabbar-item>
        </van-tabbar>

    </div>
</template>

<script>

import {mapGetters, mapState} from 'vuex';
import eventBus from "@/service/eventbus";

export default {
    name: "Main.vue",
    data() {
        return {
            active: 0,
        };
    },
    methods: {
        onChange(index) {
            this.index = index;
            this.currentJoyeaDesc = this.imagePreviewImages[parseInt(index)].joyeaDesc;
        },
        resumeTab() {
            let path = this.$route.path
            switch (path) {
                case '/home':
                    this.active = 0;
                    break;
                case '/order':
                    this.active = 1;
                    break
                case '/square':
                    this.active = 2;
                    break
                case '/mine':
                    this.active = 3;
                    break
            }
        },
        handleClickRoot() {
            if (this.$route.path === '/home') {
                eventBus.$emit("goRoot");
            }
        }
    },
    computed: {
        ...mapState({
            orderListSize: state => state.orderList.length
        }),
        ...mapGetters([
            'userInfo'
        ]),
    },
    created() {
        this.resumeTab();
    }
}
</script>

<style scoped>

</style>