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
            <van-tabbar-item icon="home-o" to="/home">工作台</van-tabbar-item>
            <van-tabbar-item icon="shopping-cart-o" :badge="orderListSize" to="/order">素材车</van-tabbar-item>
            <van-tabbar-item icon="manager-o" to="/mine">我的</van-tabbar-item>
        </van-tabbar>
    </div>
</template>

<script>

    import {mapState} from 'vuex'

    export default {
        name: "Main.vue",
        data() {
            return {
                active: 0,
            };
        },
        methods: {},
        computed: {
            ...mapState({
                orderListSize: state => state.orderList.length
            }),
        }
    }
</script>

<style scoped>

</style>