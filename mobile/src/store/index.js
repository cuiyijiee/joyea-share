import Vuex from 'vuex';
import Vue from 'vue';

Vue.use(Vuex);

const store = new Vuex.Store({
    state: {
        orderList: []
    },
    getters: {
        getStateOrderList: function (state) {
            return state.orderList;
        }
    },
    mutations: {
        add(state, item) {
            state.orderList.push(item);
        },
        remove(state, neid) {
            state.orderList = state.orderList.filter(item => {
                return item["neid"] !== neid
            })
        },
        clear(state) {
            state.orderList = []
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
        }
    }
});

export default store;