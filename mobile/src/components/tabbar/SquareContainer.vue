<template>
    <div style="margin-bottom: 50px">
        <van-pull-refresh v-model="refreshing" @refresh="handleRefreshAlbum">
            <van-list
                    v-model="loading"
                    :finished="finished"
                    finished-text="没有更多了"
                    @load="handleListAlbum">
                <van-cell v-for="item in albumList" :key="item.albumId" :title="item.albumName">
                    {{item.userName}}
                </van-cell>
            </van-list>
        </van-pull-refresh>
    </div>
</template>

<script>
    import {pageListAlbum} from "../../api";

    export default {
        name: "SquareContainer",
        data() {
            return {
                curPage: 1,
                pageSize: 10,
                loading: false,
                refreshing: false,
                finished: false,
                albumList: []
            }
        },
        methods: {
            handleRefreshAlbum() {
                this.finished = false;
                this.refreshing = true;
                this.curPage = 1;
                this.albumList = [];
                this.handleListAlbum();
            },
            handleListAlbum() {
                this.loading = true;
                pageListAlbum(this.curPage, this.pageSize).then(resp => {
                    this.loading = false;
                    this.refreshing = false;
                    resp.data.forEach(album => {
                        this.albumList.push(album);
                    });
                    if (resp.data.length < this.pageSize) {
                        this.finished = true;
                    }
                    this.curPage++;
                })
            }
        },
        created() {
            this.handleListAlbum()
        }
    }
</script>

<style scoped>

</style>