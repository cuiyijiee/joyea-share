<template>
    <div id="latest-upload">
        <div v-for="item in latestUploadRecordList">
            <van-card v-if="item.uploadPath">
                <template #thumb>
                    <div @click="handlePreview(item)">
                        <van-image width="80" height="80" v-if="item.srcType.startsWith('image')"
                                   :src="getPreviewUrl(item)"/>
                        <van-image v-else width="80" height="80" :src="getDocumentImage(item.srcType)"></van-image>
                    </div>
                </template>
                <template #tags>
                    <div>上传人:{{ item.uploaderName }}</div>
                    <!--                    <div v-if="item.checkedAt && item.uploadPath">-->
                    <!--                        上传至:{{ item.uploadPath.replace("/营销素材展示", "首页") + "/" + item.srcName }}-->
                    <!--                    </div>-->
                    <div>文件名:{{ item.srcName }}</div>
                    <div>文件说明:{{ item.srcDesc }}</div>
                    <div>审核时间:{{ item.checkedAt }}</div>
                </template>
                <template #footer>
                    <van-button icon="plus" size="small" type="danger" plain style="border: #ffffff;background: #fafafa"
                                @click="handleAddToOrderList(item)"/>
                </template>
            </van-card>
        </div>
    </div>
</template>

<script>
import {latestUpload} from "@/api";
import {genSrcPreviewSrc, getDocumentImage} from "@/util/JoyeaUtil";
import {mapActions, mapGetters} from "vuex";
import {convertItem} from "@/util/ImageViewUtil";
import {handleGoToPreview} from "../../util/JoyeaUtil";

export default {
    name: "LatestUploadContainer",
    data() {
        return {
            latestUploadRecordList: []
        }
    },
    computed: {
        ...mapGetters([
            'userInfo'
        ])
    },
    methods: {
        getDocumentImage,
        ...mapActions([
            'clearFunc', 'addFunc', 'setOrderEditInfoFunc', 'setLatestReadUploadSrcIdFunc'
        ]),
        handleGetLatestUploadList() {
            latestUpload(1000).then(resp => {
                this.latestUploadRecordList = resp.data;
                if (this.latestUploadRecordList.length > 0) {
                    this.setLatestReadUploadSrcIdFunc(this.latestUploadRecordList[0].id);
                }
            })
        },
        handlePreview(item) {
            item = convertItem(item);
            handleGoToPreview(this, item, this.userInfo.session);
        },
        handleAddToOrderList(item) {
            item.srcPath = item.uploadPath + "/" + item.srcName;
            item.path = item.uploadPath + "/" + item.srcName;
            item.filename = item.srcName;
            item = convertItem(item);
            this.addFunc(item);
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
    created() {
        this.handleGetLatestUploadList();
    }
}
</script>

<style scoped>
#latest-upload {
    margin: 5px 0 15px 0;
}
</style>