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

export function uploadRecordManage(recordId, allow, refuseReason, uploadPath, uploadPathNeid, srcName,needCount) {
    return service.post('api/v1/upload/manage', {
        recordId,
        allow: allow,
        refuseReason: refuseReason,
        uploadPath: uploadPath,
        uploadPathNeid: Number(uploadPathNeid),
        srcName: srcName,
        needCount:needCount
    }).then(res => {
        return res.data;
    })
}

export function uploadFile(file,fileDesc) {

    let formData = new FormData();
    formData.append('file', file)
    formData.append('srcDesc', fileDesc)

    //设置请求头
    let config = {
        headers: {'Content-Type': 'multipart/form-data'},
        onUploadProgress: progressEvent => {
            let completed = (progressEvent.loaded / progressEvent.total * 100 | 0) + "%";
            console.log(completed)
        }
    };

    return service.post('upload', formData, config).then(res => {
        return res.data;
    })
}