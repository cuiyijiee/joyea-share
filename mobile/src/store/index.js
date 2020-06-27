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
        }
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
        getOrderEditInfo: function (state) {
            return state.orderEditInfo;
        }
    },
    mutations: {
        add(state, item) {
            state.orderList.push(item);
        },
        refreshSession(state, newToken) {
            state.userInfo.session = newToken
        },
        cleanSession(state) {
            state.userInfo.session = ""
        },
        remove(state, neid) {
            state.orderList = state.orderList.filter(item => {
                return item["neid"] !== neid
            })
        },
        clear(state) {
            state.orderList = []
        },
        updateUserInfo(state, userInfo) {
            state.userInfo = {
                session: userInfo.session,
                name: userInfo.name,
                email: userInfo.email
            }
        },
        setEditInfo(state, albumInfo) {
            state.orderEditInfo = albumInfo;
        }
    },
    actions: {
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
        setOrderEditInfoFunc(state, albumInfo) {
            state.commit("setEditInfo", albumInfo)
        },
        refreshSessionFunc(state, token) {
            state.commit("refreshSession", token)
        }
    }
});

export default store;