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

export function uploadRecordManage(recordId, allow, refuseReason, uploadPath, uploadPathNeid, srcName, needCount, integral) {
    let data = {
        recordId,
        allow: allow,
        refuseReason: refuseReason,
        uploadPath: uploadPath,
        uploadPathNeid: Number(uploadPathNeid),
        srcName: srcName,
        needCount: needCount,
        integral: integral
    };
    console.log(data);
    return service.post('api/v1/upload/manage', data).then(res => {
        return res.data;
    })
}

export function uploadFile(file, fileDesc) {

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

export function prepareDownloadFile(fileList) {
    return service.post("api/v1/download/prepare", {
        src: fileList
    }).then(resp => {
        return resp.data;
    })
}

export function queryDownload(downloadTaskId) {
    return service.post("api/v1/download/query", {
        id: downloadTaskId
    }).then(resp => {
        return resp.data;
    })
}

export function getTodayDownload() {
    return service.post("api/v1/download/today", {}).then(resp => {
        return resp.data;
    })
}

export function getMyWordList(search,pageSize,pageNum) {
    return service.post("api/v1/esunyun/myword", {
        search: search,
        pageSize:pageSize,
        pageNum:pageNum
    }).then(resp => {
        return resp.data;
    })
}

export function addWordToDir(neid, wordList) {
    return service.post("api/v1/word/add", {
        neid: neid,
        word: wordList
    }).then(resp => {
        return resp.data;
    })
}

export function previewFile(neid){
    return service.get("apiv2/preview?neid=" + neid)
        .then(resp => {
            return resp.data;
        })
}

export function addRedirectPath(path){
    return service.get("apiv2/addRedirectPath?path=" + path)
        .then(resp => {
            return resp.data;
        })
}

export function addTranscodeVideo(neid) {
    return service.post("apiv2/transcode/insert", {
        neid:neid
    }).then(resp => {
        return resp.data;
    })
}

export function deleteTranscodeVideo(id) {
    return service.post("apiv2/transcode/delete", {
        id:id
    }).then(resp => {
        return resp.data;
    })
}

export function listTranscodeVideo(fileName,pageNum,pageSize) {
    return service.post("apiv2/transcode/list", {
        fileName:fileName,
        pageNum:pageNum,
        pageSize:pageSize,
    }).then(resp => {
        return resp.data;
    })
}

