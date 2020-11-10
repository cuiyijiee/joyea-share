import Vuex from 'vuex';
import Vue from 'vue';

Vue.use(Vuex);

const store = new Vuex.Store({
    state: {
        orderList: [],
        orderEditInfo: {
            name: "",
            id: ""
        },
        userInfo: {
            session: "",
            name: "",
            email: ""
        },
        showRealImage: false, //是否显示原图,
        imagePreviewShow: false,
        imagePreviewImages: [],
        imagePreviewStartIndex: 0,
        latestReadUploadSrcId: 76
    },
    getters: {
        getStateOrderList: function (state) {
            return state.orderList;
        },
        exist: (state) => (neid) => {
            return state.orderList.filter(item => item['neid'] === neid).length > 0;
        },
        userInfo: function (state) {
            return state.userInfo;
        },
        showRealImage: function (state) {
            return state.showRealImage;
        },
        getOrderEditInfo: function (state) {
            return state.orderEditInfo;
        },
        imagePreviewShow: function (state) {
            return state.imagePreviewShow;
        },
        imagePreviewImages: function (state) {
            return state.imagePreviewImages;
        },
        imagePreviewStartIndex: function (state) {
            return state.imagePreviewStartIndex;
        },
        latestReadUploadSrcId: function (state) {
            return state.latestReadUploadSrcId;
        }
    },
    mutations: {
        switchRealImage(state, isOn) {
            state.showRealImage = isOn;
        },
        add(state, item) {
            state.orderList.push(item);
        },
        refreshSession(state, newToken) {
            state.userInfo.session = newToken
        },
        cleanSession(state) {
            state.userInfo.session = ""
        },
        updateUserInfo(state, userInfo) {
            state.userInfo = {
                session: userInfo.session,
                name: userInfo.name,
                email: userInfo.email
            }
        },
        remove(state, neid) {
            state.orderList = state.orderList.filter(item => {
                return item["neid"] !== neid
            })
        },
        clear(state) {
            state.orderList = []
        },
        setEditInfo(state, albumInfo) {
            state.orderEditInfo = albumInfo;
        },
        setLatestReadUploadSrcId(state, id) {
            state.latestReadUploadSrcId = id;
        },
        swapOrderItem(state, newIndex, oldIndex) {
            let targetRow = state.orderList.splice(oldIndex, 1)[0];
            state.orderList.splice(newIndex, 0, targetRow);
        }
    },
    actions: {
        switchRealImage(state, isOn) {
            state.commit("switchRealImage", isOn);
        },
        addFunc(state, item) {
            state.commit("add", item)
        },
        removeFunc(state, neid) {
            state.commit("remove", neid)
        },
        clearFunc(state) {
            state.commit("clear")
        },
        clearUserSessionFunc(state) {
            state.commit("cleanSession")
        },
        updateUserInfoFunc(state, userInfo) {
            state.commit("updateUserInfo", userInfo)
        },
        refreshSessionFunc(state, token) {
            state.commit("refreshSession", token)
        },
        setOrderEditInfoFunc(state, albumInfo) {
            state.commit("setEditInfo", albumInfo)
        },
        setLatestReadUploadSrcIdFunc(state, id) {
            state.commit("setLatestReadUploadSrcId", id);
        },
        swapOrderItemFunc(state, newIndex, oldIndex) {
            state.commit("swapOrderItem", newIndex, oldIndex);
        }
    }
});

export default store;