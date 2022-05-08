<template>
    <div class="selector-div">
        <span class="self-bg" @click="handleSelectDirectoryType('SELF')">
             <div class="title-div">
                <el-image :src="selfIcon"></el-image>
                <span class="title"><b>细分市场</b></span>
            </div>
            <div class="sub-title-div">细分市场专为素材库的新手用户定制，提供更加便捷的素材查找搜索服务。</div>
            <el-image style="position:relative;right: -80%;bottom: 20%" :src="inIcon"></el-image>
        </span>
        <span class="lenovo-bg">
             <div class="title-div" @click="handleSelectDirectoryType('LENOVO')">
                <el-image :src="lenovoIcon"></el-image>
                <span class="title"><b>素材库</b></span>
            </div>
            <span class="sub-title-div">原素材库入口，按照原始的项目 - 名称 - 素材的信息层级进行查找。</span>
            <el-image style="position:relative;right: -80%;bottom: 20%" :src="inIcon"></el-image>
        </span>
    </div>
</template>

<script>
export default {
    name: "SpaceSelector",
    data() {
        return {
            selfIcon: require("@assets/icon_xifenshichang_50px.png"),
            lenovoIcon: require("@assets/icon_sucaiku_50px.png"),
            inIcon: require("@assets/icon_in_66_66.png"),
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
    padding: 40px 150px 10px;
}

.lenovo-bg {
    display: inline-block;
    width: 49%;
    height: 300px;
    background: url("../assets/background-lenovo.png") no-repeat;
    background-size: 100% 100%;
    float: right;
}

.self-bg {
    display: inline-block;
    width: 49%;
    height: 300px;
    background: url("../assets/background-self.png") no-repeat;
    background-size: 100% 100%;
}

.self-bg .title {
    font-size: 35px;
    vertical-align: center;
}

.lenovo-bg .title {
    font-size: 35px;
    vertical-align: center;
}

.title-div {
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 150px 0px 0px 50px;
}

.sub-title-div {
    display: flex;
    flex-direction: row;
    align-items: center;
    padding: 10px 0px 0px 50px;
}

</style>