<template>
    <div id="mine" style="background-color: #f7f8fa;">
        <van-cell style="margin-top: 50px">
            <van-row>
                <van-col span="8">
                    <van-image round width="5rem" height="5rem" fit="fill" src="https://img.yzcdn.cn/vant/cat.jpeg"/>
                </van-col>
                <van-col span="16">
                    <h3 style="margin: 2px 0 0 0;">{{userInfo.name}}</h3>
                    <van-divider style="margin: 2px 0 0 0;"/>
                    <h4 style="margin: 2px 0 0 0;">{{userInfo.email}}</h4>
                </van-col>
            </van-row>
        </van-cell>
        <van-divider/>
        <van-cell title="我的清单" is-link @click="handleClickMyList"/>
        <van-cell title="我的收藏" is-link @click="handleClickMyLike"/>
    </div>
</template>

<script>
    export default {
        name: "MineContainer",
        data() {
            return {
                userInfo: {}
            }
        },
        methods: {
            handleClickMyLike() {
                this.$notify('收藏功能开发中。。。');
            },
            handleClickMyList() {
                this.$router.push("/album")
            }
        },
        created() {
            let user = localStorage.getItem('userInfo');
            if (user) {
                this.userInfo = JSON.parse(user)
            }
        }
    }
</script>

<style scoped>

</style>