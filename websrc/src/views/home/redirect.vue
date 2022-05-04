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
            let redirectType = queryParams.type;
            window.localStorage.setItem("redirectPath", JSON.stringify({
                path: redirectPath,
                type: redirectType
            }))
            this.$router.push("/build")
        }
    }
}
</script>

<style scoped>

</style>