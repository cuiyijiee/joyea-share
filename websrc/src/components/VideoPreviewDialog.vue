<template>
  <el-dialog :title="toPlayVideo.title" :visible="visible.videoDialogVisible" @close="handleCloseVideo"
             @opened="initVideoPlayer()">
    <video ref="previewVideo" class="video-js vjs-fluid vjs-16-9 video-box"
           oncontextmenu="return false">
      <p class="vjs-no-js">请升级你的浏览器观看视频</p>
    </video>
  </el-dialog>
</template>

<script>
import videojs from 'video.js'
import {getVideoPreviewUrl} from "@/utils";

let videoLoading = require("@/assets/video-loading.jpg")

export default {
  name: "VideoPreviewDialog",
  data() {
    return {
      visible: {
        videoDialogVisible: false,
        videoPrepared: false,
      },
      srcItem: Object,
      player: null,
      toPlayVideo: {
        title: "",
        url: ""
      },
      videoPreviewPromise: Object,
      videoPreviewAbort: Object
    }
  },
  methods: {
    handleCloseVideo() {
      this.visible.videoDialogVisible = false;
      if (this.videoPreviewAbort) {
        this.videoPreviewAbort();
      }
      if (this.player != null) {
        this.player.src({});
        this.player.reset();
        this.player.pause();
      }
      this.$emit('close');
    },
    initVideoPlayer() {
      let _this = this;
      if (this.player == null) {
        videojs(this.$refs.previewVideo, {
          controls: true, // 是否显示控制条
          preload: 'auto',
          autoplay: false,
          fluid: true, // 自适应宽高
          language: 'zh-CN', // 设置语言
          muted: false, // 是否静音
          inactivityTimeout: false,
          poster: videoLoading,
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
          sources: []
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
    startPlayVideo(videoUrl) {
      const data = {
        src: videoUrl,
        type: 'video/mp4'
      };
      this.player.pause();
      this.player.src(data);
      this.player.load(data);
      this.player.play();
    },
    playVideo(srcItem) {
      this.visible.videoDialogVisible = true;
      this.srcItem = srcItem;

      let title = srcItem.path.substr(srcItem.path.lastIndexOf("/") + 1);
      const {promise, abort} = getVideoPreviewUrl(srcItem.neid, 30)
      this.videoPreviewPromise = promise;
      this.videoPreviewAbort = abort;
      let _this = this;
      this.videoPreviewPromise.then(videoUrl => {
        _this.visible.videoPrepared = true;
        _this.toPlayVideo.url = videoUrl;
        _this.toPlayVideo.title = title;
        if (_this.visible.videoDialogVisible) {
          _this.startPlayVideo(videoUrl);
        }
      })
    }
  },
  created() {
    console.log(this.srcItem)
  },
  beforeDestroy() {
    this.handleCloseVideo();
  }
}
</script>

<style lang="scss" scoped>

.video-box {
  box-sizing: border-box;
  border: 0;
  display: block;
  width: 100%;
  height: 720px;
  outline: none !important;
}

:deep(.el-dialog) {
  .el-dialog__body {
    padding: 0 0 0 0 !important;
    text-align: center;
  }
}

</style>
