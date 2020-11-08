<template>
    <div>
        <div style="margin: 10px;color: #1f1731;">请选择要上传的文件(最多可选9个文件)</div>
        <van-uploader v-model="filePreviewList" multiple accept="*" max-count="9" :after-read="afterRead"
                      :preview-full-image="false">
        </van-uploader>
        <van-cell-group>
            <div v-for="(file,index) in filePreviewList">
                <van-divider :style="{borderColor:'#000000'}">
                    {{ '文件 ' + (index + 1) }}
                </van-divider>
                <van-field v-model="file.desc" label="审核说明:"
                           :key=file.name type="textarea" autosize rows="2"
                           :placeholder='fileDescPlaceholder'/>
            </div>
        </van-cell-group>
        <van-button type="info" round block style="margin-top: 100px" @click="handleClickUpload">开始上传</van-button>
        <van-button type="primary" round block style="margin-top: 10px" @click="handleClickUploadRecord">上传记录
        </van-button>
    </div>
</template>

<script>
import {authUpload, realUpload} from "@/api/lenovo";
import {addUploadRecord} from "@/api";
import {mapGetters} from "vuex";
import LenovoDirSelector from "@/components/LenovoDirSelector";
import {genSrcPreviewSrc, replaceFileName} from "@/util/JoyeaUtil";
import {Toast} from "vant";

export default {
    name: "Index",
    components: {LenovoDirSelector},
    data() {
        return {
            fileDescPlaceholder: "请输入素材内容、素材使用场景、素材使用说明等。",
            pathSelectorVisible: false,
            uploadPath: "",
            uploadPathNeid: "",
            uploadFile: {},
            uploadFileName: "",
            preUploadLoading: false,
            filePreviewList: [],
        }
    },
    created() {
    },
    methods: {
        handleSelectPath(path) {
            this.uploadPath = "/" + path.path.join("/");
            this.uploadPathNeid = path.neid;
            this.pathSelectorVisible = false;
        },
        getPreviewUrl(item) {
            let previewType = 'pic';    // if video is av
            if (item.mime_type.startsWith("doc")) {
                previewType = 'doc'
            } else if (item.mime_type.startsWith("video")) {
                previewType = 'av'
            }
            return genSrcPreviewSrc(item.neid, item.hash, item.rev, previewType, this.userInfo.session);
        },
        afterRead(file) {
            this.filePreviewList.forEach(file => {
                file.tempFileName = replaceFileName(file.file.name);
            })
        },
        handleClickUploadRecord() {
            this.$router.push("/upload/mime")
        },
        handleClickUpload() {
            for (let index = 0; index < this.filePreviewList.length; index++) {
                if (!this.filePreviewList[index].desc || this.filePreviewList[index].desc.trim().length === 0) {
                    this.$notify({type: 'warning', message: '请填写文件[ ' + (index + 1) + ' ]文件说明！'});
                    return;
                }
            }
            const toast = Toast.loading({
                duration: 0, // 持续展示 toast
                forbidClick: true,
                message: '拼命上传中...',
            });
            let toUploadNum = this.filePreviewList.length;
            let uploadProgress = 0;
            this.filePreviewList.forEach(file => {
                authUpload(file.file.size, this.userInfo.session).then(resp => {
                    let realUploadDomain = resp.region;
                    file.status = 'uploading';
                    file.message = '上传中...';
                    realUpload(realUploadDomain, file.file, file.tempFileName, this.userInfo.session).then(resp => {
                        addUploadRecord(this.userInfo.email, file.tempFileName, resp.neid, resp.mime_type, resp.hash,
                            resp.rev, file.desc, []).then(resp => {
                            file.status = 'done';
                            file.message = '上传完成';
                            uploadProgress++;
                            if (uploadProgress === toUploadNum) {
                                toast.clear();
                                this.$dialog.alert({
                                    confirmButtonText: "继续上传",
                                    cancelButtonText: "返回",
                                    showCancelButton: true,
                                    message: toUploadNum + '个资源已经上传成功，管理员审核后方可被使用！',
                                    theme: 'round-button',
                                }).then(() => {
                                    this.filePreviewList = [];
                                }).catch(() => {
                                    this.$router.back();
                                })
                            }
                        })
                    })
                })
            })
        }
    },
    computed: {
        ...mapGetters([
            'userInfo'
        ]),
    },
}
</script>

<style scoped>

</style>