import Vue from 'vue';
import App from './App.vue';
import VueRouter from 'vue-router';

import Vant from 'vant';
import 'vant/lib/index.css';

import './style/joyea.css'

import router from './router'
import store from "./store";

Vue.config.productionTip = false;


import VConsole from 'vconsole'
if (process.env.NODE_ENV !== 'production') {
    new VConsole()
}

Vue.use(Vant);
Vue.use(VueRouter);

/**
 * 定义空的vue实例，作为 eventbus实现非父子组件之间的通信(vue2.x中去掉了broadcast)
 */
let eventBus = new Vue({});
export default eventBus;

new Vue({
    store,
    router,
    render: h => h(App),
}).$mount('#app');
