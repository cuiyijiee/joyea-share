import axios from 'axios'

// 创建axios实例
const service = axios.create({
    //baseURL: process.env.BASE_API, // api 的 base_url
    //baseURL: process.env.NODE_ENV === 'production' ? 'http://sck.joyea.cn:8000/' : '/',
    baseURL: "/",
    timeout: 60 * 60 * 1000 // 请求超时时间
});

export function authUpload(size, session) {
    return service.get('https://console.box.lenovo.com/v2/fileops/auth_upload/databox/' +
        '行政自助服务/素材库上传临时文件夹?path_type=ent&X-LENOVO-SESS-ID=' + session + "&bytes=" + size,).then(res => {
        return res.data;
    })
}

export function realUpload(realUploadDomain, file,realFileName, session,) {
    let uploadUrl = realUploadDomain + "/v2/files/databox/行政自助服务/素材库上传临时文件夹/" + realFileName +
        "?X-LENOVO-SESS-ID=" + session + "&path_type=ent&bytes=" + file.size + "&filename=" + realFileName + "$overwrite=true";

    let formData = new FormData();
    formData.append('file', file)

    //设置请求头
    let config = {
        headers: {'Content-Type': 'multipart/form-data'},
        onUploadProgress: progressEvent => {
            // let completed = (progressEvent.loaded / progressEvent.total * 100 | 0) + "%";
        }
    };

    return service.post(uploadUrl, formData, config).then(res => {
        return res.data;
    })
}