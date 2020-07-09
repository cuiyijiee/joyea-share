import Vue from 'vue'
import Vuex from 'vuex'

import downloadStatus from './modules/DownloadStatus'
import UserInfo from "./modules/UserInfo";

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        downloadStatus,
        UserInfo
    },
    getters: {
        barVisible: state => state.downloadStatus.barVisible
    }
});

export default store;