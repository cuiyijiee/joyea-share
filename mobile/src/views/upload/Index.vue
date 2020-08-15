<template>
  <div>
    <van-uploader accept="image/*,.pdf" :after-read="afterRead">
      <van-button icon="plus" type="primary">上传文件</van-button>
    </van-uploader>
  </div>
</template>

<script>
import {authUpload, realUpload} from "@/api/lenovo";
import {mapGetters, mapState} from "vuex";

export default {
  name: "Index",
  methods: {
    afterRead(file) {

      let content = file.file;

      authUpload(content.size, this.userInfo.session).then(resp => {
        let realUploadDomain = resp.region;
        realUpload(realUploadDomain,content,this.userInfo.session).then(resp =>{
          console.log(resp)
        })
      })



      console.log(content);
    },
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