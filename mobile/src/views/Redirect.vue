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
            let redirectType = queryParams.type;
            window.localStorage.setItem("redirectPath", JSON.stringify({
                path: redirectPath,
                type: redirectType
            }))
            this.$router.push("/")
        }
    }
}
</script>

<style scoped>

</style>
