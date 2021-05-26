<template>
  <el-form :model="loginForm" :rules="loginRule" ref="loginForm" label-position="left" label-width="0px"
           class="demo-ruleForm login-container">
    <h3 class="title">仅一素材库系统</h3>
    <el-form-item prop="user">
      <el-input type="text" v-model="loginForm.user" auto-complete="off" placeholder="工号"></el-input>
    </el-form-item>
    <el-form-item prop="pwd">
      <el-input type="password" @keyup.enter.native="handleLogin" v-model="loginForm.pwd" auto-complete="off"
                placeholder="密码"></el-input>
    </el-form-item>
    <el-checkbox v-model="loginForm.rememberPwd" checked class="remember">记住密码</el-checkbox>
    <el-form-item style="width:100%;">
      <el-button type="primary" style="width:100%;" @click.native.prevent="handleLogin" :loading="loginLoading">登录
      </el-button>
      <!--<el-button @click.native.prevent="handleReset2">重置</el-button>-->
    </el-form-item>
  </el-form>
</template>
<script>
import {login} from "../../api";
import {mapActions} from "vuex"

const localStorage = window.localStorage;

export default {
  data() {
    return {
      systemTitle: "仅一素材库系统",
      loginLoading: false,
      loginForm: {
        user: '520',
        pwd: '123456',
        rememberPwd: true
      },
      loginRule: {
        user: [
          {required: true, message: '请输入工号', trigger: 'blur'},
        ],
        pwd: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ]
      },
    };
  },
  methods: {
    ...mapActions('userInfo', [
      'updateUserInfoFunc'
    ]),
    handleLogin(ev) {
      let _this = this;
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          _this.loginLoading = true;
          login(_this.loginForm.user, btoa(_this.loginForm.pwd)).then(resp => {
            _this.loginLoading = false;
            if (resp.code === 2000) {
              _this.$message.success("登陆成功,【" + resp.data.userName + "】欢迎您回来！");
              if (_this.loginForm.rememberPwd) {
                localStorage.setItem("u", btoa(JSON.stringify(_this.loginForm)))
              } else {
                localStorage.removeItem("u")
              }
              _this.updateUserInfoFunc({
                session: resp.data['session'],
                name: resp.data['userName'],
                email: this.loginForm.user
              }).then(() => {
                if (_this.$route.query.ref) {
                  let refInfo = JSON.parse(atob(_this.$route.query.ref));
                  if (refInfo.fromShare) {
                    _this.$router.push({
                      name: "share",
                      query: {albumId: refInfo.albumId}
                    })
                  } else {
                    _this.$router.push({
                      name: "build",
                      params: {checked: true}
                    })
                  }
                } else {
                  _this.$router.push({
                    name: "build",
                    params: {checked: true}
                  })
                }
              })
            } else {
              _this.$message.error("登陆失败，请检查用户名密码！");
            }
          }).finally(() => {
            _this.loginLoading = false;
          })
        } else {
          _this.$message.error("请检查用户密码是否规范！");
          return false;
        }
      });
    }
  },
  mounted() {
    let userInfoOpt = localStorage.getItem("u");
    if (userInfoOpt) {
      this.loginForm = JSON.parse(atob(userInfoOpt))
    }
  }
}
</script>
<style lang="scss" scoped>

.login-container {
  /*box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);*/
  -webkit-border-radius: 5px;
  border-radius: 5px;
  -moz-border-radius: 5px;
  background-clip: padding-box;
  margin: 180px auto;
  width: 350px;
  padding: 35px 35px 15px 35px;
  background: #fff;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #cac6c6;

  .title {
    margin: 0 auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .remember {
    margin: 0 0 35px 0;
  }
}
</style>
