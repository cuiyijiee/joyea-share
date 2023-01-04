<template>
    <el-button @click="handleRedirect">跳转</el-button>

</template>

<script>
export default {
    name: "Test",
    data(){
        return{

        }
    },
    methods:{
        handleRedirect() {
            let requestUrl = 'http://223.240.79.0:8002//speech-api/v1/pb/third/login'
            let channel = 'zhyl'
            let domainUrl = 'http://10.186.100.90:8001/'
            let userId = '18326616992'
            let secretKey = 'xPufPiNUNinUy69B'

            let timestamp = new Date().getTime();
            let query = `channel=${channel}&domainUrl=${domainUrl}&timestamp=${timestamp}&userid=${userId}`
            let queryValue = `${channel}${domainUrl}${timestamp}${userId}${secretKey}`
            let url = `${requestUrl}?${query}&sign=${this.$md5(queryValue)}`
            console.log(url)
            window.location.href = url;
        }
    }
}
</script>

<style scoped>

</style>
