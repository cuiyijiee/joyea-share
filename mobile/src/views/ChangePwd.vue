<template>
  <div>
    <br/>
    <van-cell-group>
      <van-field v-model="curPwd" label="原密码" type="password" placeholder="请输入原密码"/>

    </van-cell-group>
    <br/>
    <van-cell-group>
      <van-field v-model="newPwd" label="新密码" type="password" placeholder="请输入新密码"/>
      <van-field v-model="newPwdConfirm" label="确认新密码" type="password" placeholder="请确认新密码"/>
    </van-cell-group>
    <br/>
    <van-button type="primary" block style="padding: 10px" @click="handleChangePwd">修改密码</van-button>
  </div>
</template>

<script>
import {changePwd, logout} from "@/api";
import {mapActions} from "vuex";

export default {
  name: "ChangePwd",
  data() {
    return {
      curPwd: "",
      newPwd: "",
      newPwdConfirm: ""
    }
  },
  methods: {
    ...mapActions([
      'clearUserSessionFunc'
    ]),
    handleChangePwd() {
      if (this.newPwd !== this.newPwdConfirm) {
        this.$notify('请确认输入的新密码一致！');
        return
      }
      if (this.newPwd.length < 6) {
        this.$notify('请确保密码长度大于6位！');
        return
      }
      if (this.newPwd === this.curPwd) {
        this.$notify('请确认新密码和旧密码不一致！');
        return
      }
      changePwd(this.curPwd, this.newPwd).then(resp => {
        if (resp.data) {
          this.$dialog.alert({
            confirmButtonText: "重新登陆",
            showCancelButton: false,
            message: '密码修改成功',
            theme: 'round-button',
          }).then(() => {
            logout().then(resp => {
              this.clearUserSessionFunc().then(() => {
                this.$router.push("/login");
              })
            })
          })
        } else {
          this.$notify('密码修改错误，请确保原密码正确！');
        }
      })
    }
  }
}
</script>

<style scoped>

</style>