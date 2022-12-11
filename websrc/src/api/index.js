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
        user: user, pwd: pwd
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
        headers: {'Content-Type': 'multipart/form-data'}, onUploadProgress: progressEvent => {
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

export function getMyWordList(search, pageSize, pageNum) {
    return service.post("api/v1/esunyun/myword", {
        search: search, pageSize: pageSize, pageNum: pageNum
    }).then(resp => {
        return resp.data;
    })
}

export function addWordToDir(neid, wordList) {
    return service.post("api/v1/word/add", {
        neid: neid, word: wordList
    }).then(resp => {
        return resp.data;
    })
}

export function previewFile(neid) {
    return service.get("apiv2/preview?neid=" + neid)
        .then(resp => {
            return resp.data;
        })
}

export function addRedirectPath(path, directoryType) {
    return service.get("apiv2/addRedirectPath?path=" + path + "&type=" + directoryType)
        .then(resp => {
            return resp.data;
        })
}

export function addTranscodeVideo(neid) {
    return service.post("apiv2/transcode/insert", {
        neid: neid
    }).then(resp => {
        return resp.data;
    })
}

export function deleteTranscodeVideo(id) {
    return service.post("apiv2/transcode/delete", {
        id: id
    }).then(resp => {
        return resp.data;
    })
}

export function listTranscodeVideo(fileName, pageNum, pageSize) {
    return service.post("apiv2/transcode/list", {
        fileName: fileName, pageNum: pageNum, pageSize: pageSize,
    }).then(resp => {
        return resp.data;
    })
}

export function getFileMetadata(directoryType, path, parentDirId) {
    return service.post("apiv2/lenovo/fileMetadata", {
        directoryType: directoryType, path: path, parentDirId: parentDirId,
    }).then(resp => {
        return resp.data;
    })
}

export function ftsSearch(searchKey, offset, limit, searchType, searchOrderType) {
    return service.post("apiv2/lenovo/ftsSearch", {
        searchKey: searchKey, offset: offset, limit: limit,
        searchType: searchType, searchOrderType: searchOrderType
    }).then(resp => {
        return resp.data;
    })
}

export function newPrivateDir(parentDirId, dirName) {
    return service.post("apiv2/privateDir/new", {
        parentDirId: parentDirId, dirName: dirName
    }).then(resp => {
        return resp.data;
    })
}

export function removePrivateDir(dirId) {
    return service.post("apiv2/privateDir/remove", {
        dirId: dirId
    }).then(resp => {
        return resp.data;
    })
}

export function newPrivateDirSrc(parentDirId, srcPath) {
    return service.post("apiv2/privateDir/newSrc", {
        parentDirId: parentDirId, srcPath: srcPath
    }).then(resp => {
        return resp.data;
    })
}

export function batchNewPrivateDirSrc(parentDirId, srcPaths) {
    return service.post("apiv2/privateDir/batchNewSrc", {
        parentDirId: parentDirId, srcPaths: srcPaths
    }).then(resp => {
        return resp.data;
    })
}

export function removePrivateDirSrc(parentDirId, neid) {
    return service.post("apiv2/privateDir/removeSrc", {
        parentDirId: parentDirId, neid: neid
    }).then(resp => {
        return resp.data;
    })
}

export function updateSrcAlias(parentDirId, neid, alias) {
    return service.post("apiv2/privateDir/updateAlias", {
        parentDirId: parentDirId, neid: neid, alias: alias
    }).then(resp => {
        return resp.data;
    })
}

export function listPrivateDir(parentDirId) {
    return service.post("apiv2/privateDir/list", {
        parentDirId: parentDirId,
    }).then(resp => {
        return resp.data;
    })
}

export function renamePrivateDir(dirId, newDirName) {
    return service.post("apiv2/privateDir/rename", {
        dirId: dirId, newDirName: newDirName,
    }).then(resp => {
        return resp.data;
    })
}

export function listJoyeaUser(joyeaName) {
    return service.post("apiv2/joyeaUser/list", {
        joyeaName: joyeaName,
    }).then(resp => {
        return resp.data;
    })
}

export function updatePrivateDirectoryAdmin(dirId, admins) {
    return service.post("apiv2/privateDir/updateAdmin", {
        dirId: dirId, admins: admins,
    }).then(resp => {
        return resp.data;
    })
}

export function listPrivateDirectoryAdmin(dirId) {
    return service.post("apiv2/privateDir/listAdmin", {
        dirId: dirId
    }).then(resp => {
        return resp.data;
    })
}
