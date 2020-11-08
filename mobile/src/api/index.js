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

export function getTopSearchKey() {
    return service.post('api/v1/search/key/top', {}).then(res => {
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

export function addUploadRecord(uid, tempSrcName, srcNeid, srcType, srcHash, srcRev, srcDesc, tags) {
    return service.post("/api/v1/upload/add", {
        tempSrcName: tempSrcName,
        uploader: uid,
        srcNeid: srcNeid,
        srcType: srcType,
        srcHash: srcHash,
        srcRev: srcRev,
        srcDesc: srcDesc,
        tags: tags
    }).then(res => {
        return res.data
    })
}

export function getMyUploadRecord() {
    return service.post("api/v1/upload/mime", {}).then(res => {
        return res.data;
    })
}

export function latestUpload(num) {
    return service.post("api/v1/upload/latest", {
        num: num
    }).then(res => {
        return res.data;
    })
}

export function getDefaultJoyeaDesc(neid) {
    return service.post("api/src/getDefaultJoyeaDesc", {
        neid: neid
    }).then(res => {
        return res.data;
    })
}

export function changePwd(curPwd, newPwd) {
    return service.post("api/v1/user/changePwd", {
        curPwd: curPwd,
        newPwd: newPwd,
    }).then(res => {
        return res.data;
    })
}

export function createAlbum(name, fileList) {
    return service.post('/api/v1/album/collect/save', {
        name: name,
        src: fileList
    }).then(resp => {
        return resp.data;
    })
}

export function uploadIntegralLeaderboard(year, month) {
    return service.post('api/v1/leaderboard/uploadIntegral', {
        year: year,
        month: month
    }).then(resp => {
        return resp.data;
    })
}

export function srcQuoteLeaderboard(year, month) {
    return service.post('api/v1/leaderboard/srcQuote', {
        year: year,
        month: month
    }).then(resp => {
        return resp.data;
    })
}

export function albumShareLeaderboard(year, month) {
    return service.post('api/v1/leaderboard/albumShare', {
        year: year,
        month: month
    }).then(resp => {
        return resp.data;
    })
}

export function albumQuoteLeaderboard(year, month) {
    return service.post('api/v1/leaderboard/albumQuote', {
        year: year,
        month: month
    }).then(resp => {
        return resp.data;
    })
}