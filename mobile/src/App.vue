<template>
  <div id="app">
    <van-nav-bar :title="this.$route.name" fixed style="background: #1f1731;color: #ffffff;z-index: 1000"
                 :left-arrow="this.$route.meta.allowBack" @click-left="handleClickLeftNav">
      <template #right>
        <van-icon v-if="allowHome" @click="handleClickRightNav" name="home-o" size="18"/>
      </template>
    </van-nav-bar>
    <transition>
      <router-view/>
    </transition>
    <van-image-preview v-model="showImagePreview" :startPosition="imagePreviewStartIndex"
                       :images="imagePreviewImages.map(item => item.url)" @change="onChange">
      <template v-slot:cover>
        <div>
          <!--                    <p>解说词:{{ currentJoyeaDesc.length === 0 ? '暂未设置' : currentJoyeaDesc }}</p>-->
          <!--          <van-button>显示原图</van-button>-->
        </div>
      </template>
    </van-image-preview>
  </div>
</template>

<script>

import {mapActions, mapGetters} from 'vuex'
import {check, getLoginTicket, getUserProfile} from "./api";
import store from "@/store";

export default {
  name: "App",
  data() {
    return {
      currentJoyeaDesc: "",
    }
  },
  methods: {
    ...mapActions([
      'refreshSessionFunc','updateUserInfoFunc','clearFunc'
    ]),
    onChange(index) {
      this.index = index;
      this.currentJoyeaDesc = this.imagePreviewImages[parseInt(index)].joyeaDesc;
    },
    handleClickLeftNav() {
      this.$router.back();
    },
    handleClickRightNav() {
      this.$router.push("/home")
    },
    handleTestLogin() {
      let _this = this;
      getLoginTicket().then(resp => {
        callNextPlusLogin(resp.data, authCode => {
          console.log("auth code: " + authCode)
          getUserProfile(authCode).then(resp => {
            alert("get user profile: " + JSON.stringify(resp));
            this.$notify({type: 'success', message: '欢迎回来，' + resp.data.profile.name + '！'});
            _this.updateUserInfoFunc({
              session: resp.data['session'], name: resp.data.profile['name'], email: resp.data.profile['ytmId']
            }).then(() => {
            })
            _this.clearFunc();
            this.$router.replace({
              name: "/",
              params: {checked: true}
            });
          })
        })
      }).catch(e => {
        console.error("exist error: " + e)
      }).finally(ctx => {
        this.checkLogin();
      })
    },
    _checkLogin() {
        let _this = this;
        check().then(resp => {
            if (resp.code === 4001) {
                // _this.$notify({type: 'danger', message: '登录信息已过期，请重新登陆！'});
                // _this.$router.push("/login");
              //_this.handleTestLogin();
            } else {
                _this.refreshSessionFunc(resp.data);
            }
        })
    },
    checkLogin() {
        let _this = this;
        this._checkLogin();
        setInterval(() => {
            if (_this.$route.path !== '/login') {
                _this._checkLogin()
            }
        }, 2000)
    }
  },
  created() {
    //在页面加载时读取sessionStorage里的状态信息
    if (localStorage.getItem("store")) {
      this.$store.replaceState(Object.assign({}, this.$store.state, JSON.parse(localStorage.getItem("store"))))
    }
    setInterval(() => {
      console.log("save store info")
      localStorage.setItem("store", JSON.stringify(this.$store.state))
    }, 2000)
    //在页面刷新时将vuex里的信息保存到sessionStorage里
    // window.addEventListener("beforeunload", () => {
    //     localStorage.setItem("store", JSON.stringify(this.$store.state))
    // });
    this.$store.state.imagePreviewShow = false;
    if (this.userInfo.session.length === 0) {
      //this.$router.push("/login");
      this.handleTestLogin();
    }else{
      this.checkLogin();
    }
    // if (this.$route.path !== '/login') {        //不在登陆界面
    //   if (this.userInfo.session.length === 0) {
    //     this.$router.push("/login");
    //   }
    // } else {                                    //登陆界面
    //   if (this.userInfo.session.length !== 0) {
    //     this.$router.replace({
    //       name: "/",
    //       params: {checked: true}
    //     });
    //   }
    // }
    document.oncontextmenu = function (event) {
      event.stopPropagation();
    };
  },
  computed: {
    ...mapGetters([
      'userInfo', 'imagePreviewImages', 'imagePreviewStartIndex'
    ]),
    showImagePreview: {
      get: function () {
        return this.$store.state.imagePreviewShow;
      },
      set: function (newValue) {
        this.$store.state.imagePreviewShow = newValue;
      }
    },
    allowHome: function () {
      return this.$route.meta.allowHome;
    }
  },
}
</script>

<style>
#app {
  overflow: hidden;
  margin-top: 40px;
  font-family: 'Noto Sans SC', Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, SimSun, sans-serif;
}
</style>
