<template>
  <el-dialog :visible="visible" title="预览控件" @close="handleCloseVideo"
             @open="playVideo()">
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
</template>

<script>

import videojs from 'video.js'

export default {
  name: "VideoPreviewDialog",
  props: {
    visible: {
      type: Boolean,
      default: true
    },
    srcItem: {
      type: Object
    }
  },
  methods: {
    handleCloseVideo() {
      if (this.player != null) {
        this.player.pause();
      }
      this.$emit('close');
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
              src: "https://vjs.zencdn.net/v/oceans.mp4",
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
    }
  },
  created() {
    console.log(this.srcItem)
  }
}
</script>

<style scoped>

</style>