const state = {
    session: "",
    name: "",
    email: ""
};

const getters = {
    userInfo: function (state) {
        return {
            session:state.session,
            name:state.name,
            email:state.email,
        }
    },
}

const mutations = {
    refreshSession(state, newToken) {
        state.session = newToken
    },
    cleanSession(state) {
        state.session = ""
    },
    updateUserInfo(state, userInfo) {
        state.session =  userInfo.session;
        state.name =  userInfo.name;
        state.email =  userInfo.email;
    },
};


const actions = {
    clearUserSessionFunc(state) {
        state.commit("cleanSession")
    },
    updateUserInfoFunc(state, userInfo) {
        state.commit("updateUserInfo", userInfo)
    },
    refreshSessionFunc(state, token) {
        state.commit("refreshSession", token)
    },
};

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}