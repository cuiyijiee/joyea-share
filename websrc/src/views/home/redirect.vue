<template>
    <div>
        redirect...
    </div>
</template>

<script>
export default {
    name: "redirect",
    methods: {},
    mounted() {
        let queryParams = this.$route.query;
        if (queryParams && queryParams.path) {
            let redirectPath = queryParams.path;
            window.localStorage.setItem("redirectPath", redirectPath)
            this.$router.push("/build")
        }
    }
}
</script>

<style scoped>

</style>