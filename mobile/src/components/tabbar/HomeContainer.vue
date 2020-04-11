<template>
    <div id="home">
        <form action="/">
            <van-search
                    v-model="searchKey"
                    placeholder="请输入搜索要搜索的资源"
                    @search="onSearch"
                    @cancel="onCancel"
            />
        </form>
        <van-tabs v-model="currentTypeActive">
            <van-tab title="全部">内容 1</van-tab>
            <van-tab title="图片">内容 2</van-tab>
            <van-tab title="视频">内容 3</van-tab>
            <van-tab title="文档">内容 4</van-tab>
            <van-tab title="其他">内容 5</van-tab>
        </van-tabs>
    </div>
</template>

<script>
    export default {
        name: "HomeContainer",
        data() {
            return {
                searchKey: "",
                currentTypeActive: 0
            }
        },
        methods: {
            onSearch() {

            },
            onCancel() {

            }
        }
    }
</script>

<style scoped>

</style>