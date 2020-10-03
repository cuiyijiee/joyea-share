<template>
  <div id="mine" style="background-color: #f7f8fa;">
    <van-cell>
      <van-row>
        <van-col span="8">
          <!--                    <van-image round width="5rem" height="5rem" fit="fill" src="avatar.jpg"/>-->
          <avatars backgroundColor="#eb7808" color="#ffffff" :size="80" :username="userInfo.name"></avatars>
        </van-col>
        <van-col span="16">
          <h3 style="margin: 2px 0 0 0;">{{ userInfo.name }}</h3>
          <van-divider style="margin: 2px 0 0 0;"/>
          <h4 style="margin: 2px 0 0 0;">{{ userInfo.email }}</h4>
        </van-col>
      </van-row>
    </van-cell>
    <van-divider/>
    <van-cell-group title="功能">
      <van-cell title="素材上传" is-link @click="handleClickUpload"/>
      <van-cell title="我的上传" is-link @click="handleClickMyUpload"/>
      <van-cell title="我的清单" is-link @click="handleClickMyList"/>
      <van-cell title="修改密码" is-link @click="handleClickChangePwd"/>
    </van-cell-group>
    <van-cell-group title="设置">
      <van-cell center title="图片加载原图">
        <template #right-icon>
          <van-switch v-model="showRealImage" size="24"/>
        </template>
      </van-cell>
    </van-cell-group>
    <van-button type="info" round block style="margin-top: 100px" @click="handleLogout">注销登录</van-button>
  </div>
</template>

<script>
import {mapGetters, mapActions} from "vuex";
import avatars from 'vue-avatars'
import {logout} from "../../api";

export default {
  components: {
    avatars
  },
  name: "MineContainer",
  data() {
    return {}
  },
  computed: {
    showRealImage: {
      get: function () {
        return this.$store.state.showRealImage;
      },
      set: function (newValue) {
        this.$store.state.showRealImage = newValue;
      }
    },
    ...mapGetters([
      'userInfo'
    ])
  },
  methods: {
    ...mapActions([
      'clearUserSessionFunc'
    ]),
    handleClickMyLike() {
      this.$notify('收藏功能开发中。。。');
    },
    handleClickMyList() {
      this.$router.push("/album")
    },
    handleClickChangePwd() {
      this.$router.push("/user/changePwd")
    },
    handleClickUpload() {
      this.$router.push("/upload/index")
    },
    handleClickMyUpload() {
      this.$router.push("/upload/mime")
    },
    handleLogout() {
      this.$dialog.confirm({
        message: "确定退出登录？"
      }).then(() => {
        logout().then(resp => {
          this.clearUserSessionFunc().then(() => {
            this.$router.push("/login");
          })
        })
      }).catch(e => {
        console.log(e)
      })
    }
  },
  created() {
  }
}
</script>

<style scoped>

</style>