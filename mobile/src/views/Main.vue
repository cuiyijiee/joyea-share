<template>
    <div id="main">
        <!-- 中间的 路由 router-view 区域 -->
        <transition style="background-color: #f7f8fa;">
            <router-view ></router-view>
        </transition>
        <van-tabbar
                v-model="active"
                active-color="#07c160"
                inactive-color="#000"
                safe-area-inset-bottom
                z-index="4">
            <van-tabbar-item icon="home-o" replace to="/home">工作台</van-tabbar-item>
            <van-tabbar-item icon="shopping-cart-o" :badge="orderListSize" replace to="/order">素材车</van-tabbar-item>
            <van-tabbar-item icon="manager-o" replace to="/mine">我的</van-tabbar-item>
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
            })
        }
    }
</script>

<style scoped>

</style>