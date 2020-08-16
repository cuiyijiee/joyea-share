import axios from 'axios'


// 创建axios实例
const service = axios.create({
    //baseURL: process.env.BASE_API, // api 的 base_url
    //baseURL: process.env.NODE_ENV === 'production' ? '' : '',
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

export function recordAlbumDownload(albumId) {
    return service.post('api/v1/album/download/record', {
        albumId: albumId
    }).then(res => {
        return res.data;
    })
}

export function getTopSearchKey() {
    return service.post('api/v1/search/key/top', {}).then(res => {
        return res.data;
    })
}

export function findUploadRecord(curPage, pageSize) {
    return service.post('api/v1/upload/get', {
        curPage, pageSize
    }).then(res => {
        return res.data;
    })
}

export function uploadRecordManage(recordId, allow, refuseReason) {
    return service.post('api/v1/upload/manage', {
        recordId,
        allow: allow,
        refuseReason: refuseReason
    }).then(res => {
        return res.data;
    })
}