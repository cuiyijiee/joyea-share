import axios from 'axios'

// 创建axios实例
const service = axios.create({
    //baseURL: process.env.BASE_API, // api 的 base_url
    baseURL: process.env.NODE_ENV === 'production' ? 'http://192.168.0.10:8000/' : 'http://192.168.1.38:8080/',
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