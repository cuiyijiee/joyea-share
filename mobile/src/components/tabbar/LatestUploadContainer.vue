<template>
    <div id="latest-upload">
        <ul>
            <li v-for="item in latestUploadRecordList">
                <div v-if="item.lastReadFlag">
                    <van-divider content-position="center" :class="{lastReadFlag:item.lastReadFlag}"
                                 :style="{ color: '#bbbbbb', borderColor: '#bbbbbb', padding: '0 16px' }">
                        上次看到这里
                    </van-divider>
                </div>
                <van-card>
                    <template #thumb>
                        <div @click="handlePreview(item)">
                            <van-image width="80" height="80" v-if="item.srcType.startsWith('image')"
                                       :src="getPreviewUrl(item)"/>
                            <van-image v-else width="80" height="80"
                                       :src="getDocumentImage(item.srcType)"></van-image>
                        </div>
                    </template>
                    <template #tags>
                        <div>上传人:{{ item.uploaderName }}</div>
                        <div>文件名:{{ item.srcName }}</div>
                        <div>文件说明:{{ item.srcDesc }}</div>
                        <div>审核时间:{{ item.checkedAt | dateFormat }}</div>
                    </template>
                    <template #footer>
                        <van-button icon="plus" size="small" type="danger" plain
                                    style="border: #ffffff;background: #fafafa"
                                    @click="handleAddToOrderList(item)"/>
                    </template>
                </van-card>
            </li>
        </ul>
    </div>
</template>

<script>
import {latestUpload} from "@/api";
import {genSrcPreviewSrc, getDocumentImage} from "@/util/JoyeaUtil";
import {mapActions, mapGetters} from "vuex";
import {convertItem} from "@/util/ImageViewUtil";
import {getLastReadUploadRecordId, handleGoToPreview, setLastReadUploadRecordId} from "../../util/JoyeaUtil";
import moment from 'moment';

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
            'clearFunc', 'addFunc', 'setOrderEditInfoFunc'
        ]),
        handleGetLatestUploadList() {
            latestUpload(1000).then(resp => {
                let itemList = [];
                let localReadId = getLastReadUploadRecordId(this.userInfo.email);
                //console.log("local id is:" + localReadId);
                for (let index = 0; index < resp.data.length; index++) {
                    let item = resp.data[index];
                    if (item.uploadPath) {  //过滤未审核文件
                        if (item.id === localReadId) {
                            console.log("the record is last read:" + item);
                            // if (item.id === 200) {
                            item.lastReadFlag = true;
                        }
                        itemList.push(item);
                    }
                }
                this.latestUploadRecordList = itemList;
            }).finally(() => {
                this.$nextTick(() => {
                    let lastReadEleList = document.getElementsByClassName('lastReadFlag');
                    if (lastReadEleList.length > 0) {
                        this.$nextTick(() => {
                            let lastReadEle = lastReadEleList[0];
                            lastReadEle.scrollIntoView(true);
                            let scrolledY = window.scrollY;
                            if (scrolledY) {
                                window.scroll(0, scrolledY - 200);
                            }
                        })
                    } else {
                    }
                    if (this.latestUploadRecordList.length > 0) {
                        let latestUploadItem = this.latestUploadRecordList[0];
                        //console.log("latest upload item is: " + JSON.stringify(latestUploadItem));
                        setLastReadUploadRecordId(this.userInfo.email,latestUploadItem.id);
                    }
                })
            })
        },
        handlePreview(item) {
            item = convertItem(item);
            handleGoToPreview(this, item, this.userInfo.session, this.latestUploadRecordList);
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
    activated() {
        this.handleGetLatestUploadList();
    }
}
</script>

<style scoped>
#latest-upload {
    margin: 0px 0 15px 0;
}
</style>