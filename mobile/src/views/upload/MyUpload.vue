<template>
    <div>
        <van-card
            v-for="item in recordList"
            @click="handleGotoPan(item)"
        >
            <template #thumb>
                <van-image width="80" height="80" v-if="item.srcType.startsWith('image')" :src="getPreviewUrl(item)"/>
                <van-image v-else width="80" height="80" :src="getDocumentImage(item.srcType)"></van-image>
            </template>
            <template #tags>
                <van-tag type="primary" v-if="!item.checkedAt">待审核</van-tag>
                <van-tag type="success" v-if="item.checkedAt && item.checked">通过</van-tag>
                <van-tag type="danger" v-if="item.checkedAt && !item.checked">未通过</van-tag>
                <div v-if="item.checkedAt && item.checked">
                    管理员移至:{{ item.uploadPath.replace("/行政自助服务", "首页") + "/" + item.srcName }}
                </div>
                <div v-if="item.checkedAt && !item.checked">审核意见:{{ item.refuseReason }}</div>
                <div>上传时间:{{ item.createdAt | dateFormat }}</div>
            </template>
        </van-card>
    </div>
</template>

<script>

import {getMyUploadRecord} from "@/api";
import {genSrcPreviewSrc, getDocumentImage} from "@/util/JoyeaUtil";
import {mapGetters} from "vuex";

export default {

    name: "MyUpload",
    data() {
        return {
            recordList: []
        }
    },
    methods: {
        getDocumentImage,
        handleGotoPan(item) {
            if (item.checkedAt && item.checked) {

            }
        },
        getMyUploadRecord() {
            getMyUploadRecord().then(resp => {
                this.recordList = resp.data;
            })
        },
        getPreviewUrl(item) {
            let previewType = 'pic';    // if video is av
            if (item.srcType.startsWith("doc")) {
                previewType = 'doc'
            } else if (item.srcType.startsWith("video")) {
                previewType = 'av'
            }
            return genSrcPreviewSrc(item.srcNeid, item.srcHash, item.srcRev, previewType, this.userInfo.session);
        },
    },
    computed: {
        ...mapGetters([
            'userInfo'
        ]),
    },
    created() {
        this.getMyUploadRecord()
    }
}
</script>

<style scoped>

</style>