<template>
    <div id="main">
        <div style="height: 50px"></div>
        <van-tabbar
                v-model="active"
                active-color="#07c160"
                inactive-color="#000"
                safe-area-inset-bottom
                z-index="4">
            <van-tabbar-item icon="home-o" replace to="/home">工作台</van-tabbar-item>
            <van-tabbar-item icon="shopping-cart-o" replace to="/order">素材车</van-tabbar-item>
            <van-tabbar-item icon="manager-o" replace to="/mine">我的</van-tabbar-item>
        </van-tabbar>
    </div>
</template>

<script>
    export default {
        name: "Main.vue",
        data() {
            return {
                active: 0,
            };
        },
        methods: {}
    }
</script>

<style scoped>

</style>