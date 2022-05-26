<template>
    <div class="selector-div">
        <el-image :src="selfSpaceBg" style="width: 49%" @click="handleSelectDirectoryType('SELF')"></el-image>
        <el-image :src="lenovoSpaceBg" style="width: 49%;float: right" @click="handleSelectDirectoryType('LENOVO')"></el-image>
    </div>
</template>

<script>
export default {
    name: "SpaceSelector",
    data() {
        return {
            selfSpaceBg:require("@assets/self-space-bg.png"),
            lenovoSpaceBg:require("@assets/lenovo-space-bg.png"),
        }
    },
    methods: {
        handleSelectDirectoryType(type) {
            this.$emit("onDirectoryTypeSelected", type);
        }
    }
}
</script>

<style scoped>

.selector-div {
    -moz-user-select: none; /*火狐*/
    -webkit-user-select: none; /*webkit浏览器*/
    -ms-user-select: none; /*IE10*/
    -khtml-user-select: none; /*早期浏览器*/
    padding: 40px 80px 10px;
}

</style>