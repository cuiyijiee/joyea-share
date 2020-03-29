import Vue from 'vue';
import App from './App.vue';
import NutUI from '@nutui/nutui';
import '@nutui/nutui/dist/nutui.css';

Vue.config.productionTip = false;
NutUI.install(Vue);

new Vue({
  render: h => h(App),
}).$mount('#app')
