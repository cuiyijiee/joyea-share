<template>
    <div id="latest-upload">
        <ul>
            <li v-for="item in latestUploadRecordList">
                <div v-if="item.emptyLine">
                    <!--日期分割线-->
                    <van-divider content-position="left"
                                 :style="{ color: '#000000', borderColor: '#000000', padding: '0 16px' }"
                    >
                        {{ item.nextDate }}
                    </van-divider>
                </div>
                <div v-else-if="item.lastReadFlag">
                    <div style="height: 50px" class="lastReadFlag">
                        <van-divider content-position="left"
                                     :style="{ color: '#000000', borderColor: '#000000', padding: '0 16px' }">
                            上次更新到此 👇👇👇
                        </van-divider>
                    </div>
                </div>
                <van-card v-else>
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
import {handleGoToPreview} from "../../util/JoyeaUtil";
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
            'userInfo', 'latestReadUploadSrcId'
        ])
    },
    methods: {
        getDocumentImage,
        ...mapActions([
            'clearFunc', 'addFunc', 'setOrderEditInfoFunc', 'setLatestReadUploadSrcIdFunc'
        ]),
        handleGetLatestUploadList() {
            latestUpload(1000).then(resp => {
                let itemList = [];
                for (let index = 0; index < resp.data.length; index++) {
                    let item = resp.data[index];
                    if (item.uploadPath) {  //过滤未审核文件

                        if (index + 1 < resp.data.length && this.latestReadUploadSrcId !== 0) {
                            if (resp.data[index + 1].id === this.latestReadUploadSrcId) {
                                itemList.push({
                                    lastReadFlag: true
                                })
                            }
                        }

                        itemList.push(item);

                        //计算日期分割线
                        let currentIndexCheckedAtDate = moment(new Date(item.checkedAt)).format('YYYY-MM-DD');
                        if (index + 1 < resp.data.length) {
                            let nextItem = resp.data[index + 1];
                            let nextIndexCheckedAtDate = moment(new Date(nextItem.checkedAt)).format('YYYY-MM-DD');
                            if (nextIndexCheckedAtDate !== currentIndexCheckedAtDate) {
                                itemList.push({
                                    emptyLine: true,
                                    nextDate: nextIndexCheckedAtDate
                                })
                            }
                        }
                    }
                }
                this.latestUploadRecordList = itemList;
            }).finally(() => {
                this.$nextTick(() => {
                    let lastReadEleList = document.getElementsByClassName('lastReadFlag');
                    if (lastReadEleList.length > 0) {
                        lastReadEleList[0].scrollIntoView({
                            behavior: "smooth"
                        });
                    }
                    if (this.latestUploadRecordList.length > 0) {
                        this.setLatestReadUploadSrcIdFunc(this.latestUploadRecordList[0].id);
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
    created() {
        this.handleGetLatestUploadList();
    }
}
</script>

<style scoped>
#latest-upload {
    margin: 0px 0 15px 0;
}
</style>