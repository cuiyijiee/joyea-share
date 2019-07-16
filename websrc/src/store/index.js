import Vue from 'vue'
import Vuex from 'vuex'

import downloadStatus from './modules/DownloadStatus'

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        downloadStatus
    },
    getters :{
        barVisible:state => state.downloadStatus.barVisible
    }
});

export default store;