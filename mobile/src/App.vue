<template>
    <div id="app">
        <van-nav-bar title="仅一素材库" fixed/>
        <transition>
            <router-view></router-view>
        </transition>

    </div>
</template>

<script>
    export default {
        name: "App",
    };
</script>

<style>
    #app {
        overflow: hidden;
        margin-top: 40px;
    }
</style>
