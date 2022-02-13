<template>
    <div>
        redirect...
    </div>
</template>

<script>
export default {
    name: "Redirect",
    methods: {},
    mounted() {
        let queryParams = this.$route.query;
        if (queryParams && queryParams.path) {
            let redirectPath = queryParams.path;
            window.localStorage.setItem("redirectPath", redirectPath)
            this.$router.push("/")
        }
    }
}
</script>

<style scoped>

</style>