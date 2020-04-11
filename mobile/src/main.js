import Vue from 'vue';
import App from './App.vue';
import VueRouter from 'vue-router';

import NutUI from '@nutui/nutui';
import '@nutui/nutui/dist/nutui.css';

import Vant from 'vant';
import 'vant/lib/index.css';

import router from './router'

Vue.config.productionTip = false;

NutUI.install(Vue);
Vue.use(Vant);
Vue.use(VueRouter);


new Vue({
    router,
    render: h => h(App),
}).$mount('#app');
