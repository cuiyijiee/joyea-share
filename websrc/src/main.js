import Vue from 'vue'
import App from './App.vue'
import router from './router/index'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import preview from 'vue-photo-preview';
import 'vue-photo-preview/dist/skin.css';
import './style/vars.scss'

Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(preview);

new Vue({
  router,
  render: h => h(App)
}).$mount('#app');
