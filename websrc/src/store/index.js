import Vue from 'vue'
import Vuex from 'vuex'

import downloadStatus from './modules/DownloadStatus'
import userInfo from "./modules/UserInfo";

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        downloadStatus,
        userInfo
    },
});

export default store;