import Vue from 'vue'
import App from './App.vue'
import router from './router/index'
import ElementUI from 'element-ui'
import VueClipboard from 'vue-clipboard2'
import preview from 'vue-photo-preview';
import 'vue-photo-preview/dist/skin.css';
import './style/vars.scss'
import store from './store'

import Video from 'video.js'
import video_zhCN from 'video.js/dist/lang/zh-CN.json'
import video_en from  'video.js/dist/lang/en.json'
import 'video.js/dist/video-js.css'
import "./assets/iconfont/iconfont.css"
import "./assets/iconfont-color/iconfont.css"


Video.addLanguage('zh-CN', video_zhCN);
Video.addLanguage('en', video_en);

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(preview);
Vue.use(VueClipboard)

import moment from "moment";
Vue.filter('dateFormat', function (dataStr, pattern = 'YYYY-MM-DD HH:mm:ss') {
  return moment(new Date(dataStr)).format(pattern);
})

Vue.prototype.$EventBus = new Vue();

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app');
