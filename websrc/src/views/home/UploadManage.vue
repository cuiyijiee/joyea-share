<template>
    <div style="padding: 10px 150px 0 150px;">
        <h1 v-if="recordList.length === 0">暂时没有需要审核的上传文件！</h1>
        <el-row>
            <el-col :span="6" v-for="(record, index) in recordList" :key="index">
                <el-card style="margin: 20px">
                    <div @click="handleGoToPreview(record)">
                        <img v-if="record.srcType.startsWith('image')"
                             preview="dir_image_list" :preview-text="record.srcNeid"
                             :src="record.srcPreviewUrl"
                             class="image" style="height: 400px;" fit="contain"/>
                        <img v-else :src="getDocumentImage(record.srcType)" class="image" style="height: 400px"
                             fit="contain"/>
                    </div>
                    <div style="padding: 14px;">
                        <span></span>
                        <div>
                            <div>上传者:{{ record.uploaderName }}</div>
                            <div style="height:60px;line-height:20px">上传文件说明:{{ record.srcDesc }}</div>
                            <time class="time">{{ record.createdAt }}</time>
                        </div>
                        <div class="bottom clearfix">
                            <el-button type="danger" @click="handleRefuseUpload(record)">不通过</el-button>
                            <el-button type="primary" @click="handleStartAllow(record,false)">通过</el-button>
                            <el-button type="primary" @click="handleStartAllow(record,true)">通过并统计</el-button>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-dialog :visible.sync="manageDialogVisible" title="审核详情">
            <div v-if="manageDialogVisible">
                <el-image v-if="toManageRecord.srcType.startsWith('image')"
                          :src="toManageRecord.srcPreviewUrl"
                          class="image" style="height: 400px" fit="contain"></el-image>
                <el-image v-else :src="getDocumentImage(toManageRecord.srcType)" class="image" style="height: 400px"
                          fit="contain"></el-image>
            </div>
            <el-form ref="form" label-width="80px">
                <el-form-item label="文件名">
                    <el-input v-model="toManageRecord.srcName">
                        <template slot="append">.{{ toManageRecord.suffix }}</template>
                    </el-input>
                </el-form-item>
                <el-form-item label="文件路径">
                    <el-button @click="selectPathVisible = true">选择路径</el-button>
                    <el-input disabled v-model="toManageRecord.uploadPath"></el-input>
                </el-form-item>
                <el-form-item label="文件积分">
                    <el-input-number v-model="integral" :min="1" :max="99"
                                     label="文件积分"></el-input-number>
                </el-form-item>
                <el-form-item label="常用积分">
                    <el-button @click="integral = 1">+ 1</el-button>
                    <el-button @click="integral = 5">+ 5</el-button>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
        <el-button @click="manageDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleAllowRecord">确 定</el-button>
      </span>
        </el-dialog>
        <lenovo-dir-selector :visible.sync="selectPathVisible" @close="selectPathVisible=false"
                             @selectPath="handleSelectUploadPath"></lenovo-dir-selector>
        <el-dialog :title="toPlayVideo.title" :visible.sync="visible.videoDialogVisible" @close="handleCloseVideo"
                   @opened="playVideo()">
            <video id="myVideo" class="video-js vjs-big-play-centered vjs-fluid" oncontextmenu="return false">
                <p class="vjs-no-js">
                    To view this video please enable JavaScript, and consider upgrading to a
                    web browser that
                    <a href="https://videojs.com/html5-video-support/" target="_blank">
                        supports HTML5 video
                    </a>
                </p>
            </video>
        </el-dialog>
    </div>
</template>
<script>

import {findUploadRecord, uploadRecordManage} from "../../api"
import genSrcPreviewSrc from "../../utils";
import {mapGetters} from "vuex";
import {getDocumentImage, convertItem, extname} from "../../utils/JoyeaUtil";
import LenovoDirSelector from "../../components/LenovoDirSelector";
import videojs from "video.js";

export default {
    name: "UploadManage",
    components: {LenovoDirSelector},
    data() {
        return {
            manageDialogVisible: false,
            recordList: [],
            toManageRecord: {
            },
            integral: 1,
            selectPathVisible: false,
            toPlayVideo: {
                title: '',
                url: ''
            },
            visible: {
                videoDialogVisible: false,
            }
        }
    },
    computed: {
        ...mapGetters({
            'userInfo': 'userInfo/userInfo'
        })
    },
    methods: {
        handleAllowRecord() {
            if (!this.toManageRecord.srcName || !this.toManageRecord.uploadPath) {
                this.$message.error("请先完善上传文件的文件名和上传路径！")
            } else {
                uploadRecordManage(this.toManageRecord.id, true, null,
                    this.toManageRecord.uploadPath, this.toManageRecord.uploadPathNeid,
                    this.toManageRecord.srcName + "." + this.toManageRecord.suffix,
                    this.toManageRecord.needCount
                ).then(resp => {
                    if (resp.data) {
                        this.$message.success("审核成功！");
                        this.getUnCheckRecord();
                        this.manageDialogVisible = false;
                    }
                })
            }
        },
        handleStartAllow(record, needCount) {
            this.integral = 1;
            this.toManageRecord = record;
            this.toManageRecord.suffix = extname(this.toManageRecord.tempSrcName);
            this.toManageRecord.needCount = needCount;
            this.manageDialogVisible = true;
        },
        handleGoToPreview(row) {
            row = convertItem(row);
            let previewType = 'pic';    // if video is av
            if (row.mime_type.startsWith("doc")) {
                previewType = 'doc'
            } else if (row.mime_type.startsWith("video")) {
                previewType = 'av'
            }
            let url = genSrcPreviewSrc(row.neid, row.hash, row.rev, previewType, this.userInfo.session);
            if (row.mime_type.startsWith("video")) {
                this.handlePlayVideo(url, '视频预览',);
            } else if (row.mime_type.startsWith("image")) {
            } else if (row.mime_type.startsWith("doc")) {
                window.open(url);
            } else {
                this.$message.error("暂不支持的预览类型！")
            }
        },
        handlePlayVideo(url, title) {
            this.visible.videoDialogVisible = true;
            this.toPlayVideo.url = url;
            this.toPlayVideo.title = title;
        },
        handleCloseVideo() {
            if (this.player != null) {
                this.player.pause();
            }
        },
        playVideo() {
            let _this = this;
            if (this.player == null) {
                videojs(document.getElementById('myVideo'), {
                    controls: true, // 是否显示控制条
                    preload: 'auto',
                    autoplay: false,
                    fluid: true, // 自适应宽高
                    language: 'zh-CN', // 设置语言
                    muted: false, // 是否静音
                    inactivityTimeout: false,
                    controlBar: { // 设置控制条组件
                        children: [
                            {name: 'playToggle'}, // 播放按钮
                            {name: 'currentTimeDisplay'}, // 当前已播放时间
                            {name: 'progressControl'}, // 播放进度条
                            {name: 'durationDisplay'}, // 总时间
                            {
                                name: 'volumePanel', // 音量控制
                                inline: false, // 不使用水平方式
                            },
                            {name: 'FullscreenToggle'} // 全屏
                        ]
                    },
                    sources: [ // 视频源
                        {
                            src: _this.toPlayVideo.url,
                            type: 'video/mp4',
                        }
                    ]
                }, function () {
                    _this.player = this;
                });
            } else {
                const data = {
                    src: _this.toPlayVideo.url,
                    type: 'video/mp4'
                };
                this.player.pause();
                this.player.src(data);
                this.player.load(data);
                this.player.play();
            }
        },
        genSrcPreviewSrc,
        getDocumentImage,
        handleSelectUploadPath(path) {
            this.selectPathVisible = false;
            this.toManageRecord.uploadPath = "/" + path.path.join("/")
            this.toManageRecord.uploadPathNeid = path.neid;
        },
        handleRefuseUpload(record) {
            this.$prompt('给点建议吧！', '拒绝提示', {
                confirmButtonText: '拒绝',
                cancelButtonText: '我再想想',
            }).then(({value}) => {
                uploadRecordManage(record.id, false, value).then(resp => {
                    if (resp.data) {
                        this.$message.success("审核成功！");
                        this.getUnCheckRecord();
                    }
                })
            });
        },
        getUnCheckRecord() {
            findUploadRecord(1, 9999).then(resp => {
                resp.data.record.map(record => {
                    record.uploaderName = resp.data.user[record.uploader];
                    record.srcPreviewUrl = this.genSrcPreviewSrc(record.srcNeid, record.srcHash, record.srcRev, 'pic', this.userInfo.session);
                })
                this.recordList = resp.data.record;
            })
        }
    },
    created() {
        this.getUnCheckRecord();
    }
}
</script>

<style scoped>
.time {
    font-size: 13px;
    color: #999;
}

.bottom {
    margin-top: 13px;
    line-height: 12px;
}

.button {
    padding: 0;
    float: right;
}

.image {
    width: 100%;
    display: block;
}

.clearfix:before,
.clearfix:after {
    display: table;
    content: "";
}

.clearfix:after {
    clear: both
}
</style>