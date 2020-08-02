import axios from 'axios'

// 创建axios实例
const service = axios.create({
    //baseURL: process.env.BASE_API, // api 的 base_url
    //baseURL: process.env.NODE_ENV === 'production' ? 'http://sck.joyea.cn:8000/' : '/',
    baseURL: "/",
    timeout: 60 * 60 * 1000 // 请求超时时间
});

export default function api(params) {
    return service.post('api', params).then(res => {
        return res.data;
    })
}

export function login(user, pwd) {
    return service.post('api/v1/user/login', {
        user: user,
        pwd: pwd
    }).then(res => {
        return res.data;
    })
}

export function logout() {
    return service.post('api/v1/user/logout', {}).then(res => {
        return res.data;
    })
}

export function check() {
    return service.post('api/v1/user/check', {}).then(res => {
        return res.data;
    })
}

export function pageListAlbum(curPage, pageSize, shared, sortType,
                              hangyeTagId, xianbieTagId, jixingTagId, jieduanTagId, shichangTagId) {
    let requestForm = {
        curPage: curPage,
        pageSize: pageSize,
        shared: shared,
        sortType: sortType,
        hangyeTagId: hangyeTagId,
        xianbieTagId: xianbieTagId,
        jixingTagId: jixingTagId,
        jieduanTagId: jieduanTagId,
        shichangTagId: shichangTagId,
    };
    return service.post('api/v1/album/pageList', requestForm).then(res => {
        return res.data;
    })
}

export function listMineAlbum() {
    return service.post("api/v1/album/list/mine", {}).then(res => {
        return res.data;
    })
}

export function switchShare(albumId, share, albumName, shareCoverId, shareLocalCoverId, shareDesc, hangyeTagId, xianbieTagId, jixingTagId, jieduanTagId, shichangTagId) {
    return service.post("api/v1/album/switch/share", {
        albumId: albumId,
        share: share,
        albumName: albumName,
        coverId: shareCoverId,
        localCoverId: shareLocalCoverId,
        shareDesc: shareDesc,
        hangyeTagId: hangyeTagId,
        xianbieTagId: xianbieTagId,
        jixingTagId: jixingTagId,
        jieduanTagId: jieduanTagId,
        shichangTagId: shichangTagId
    }).then(res => {
        return res.data;
    })
}

export function copyAlbum(albumId) {
    return service.post("api/v1/album/copy", {
        albumId: albumId
    }).then(res => {
        return res.data;
    })
}