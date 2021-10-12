const state = {
    barVisible: false,
};

const getters = {
    barVisible: state => state.downloadStatus.barVisible
}

const mutations = {
    SET_VISIBLE: (state, visible) => {
        state.barVisible = visible
    },
};

const actions = {
    setVisible({commit}, visible) {
        console.log(visible);
        commit('SET_VISIBLE', visible)
    },
    openVisible({commit}) {
        commit('SET_VISIBLE', true)
    },
    closeVisible({commit}) {
        commit('SET_VISIBLE', false)
    },
};

export default {
    namespaced: true,
    state,
    mutations,
    actions
}