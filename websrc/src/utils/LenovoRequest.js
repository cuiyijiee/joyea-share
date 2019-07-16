import axios from 'axios'

const LENOVO_BASE_URL = "https://box.lenovo.com/v2";
axios.defaults.withCredentials=true;
// create an axios instance
const lenovoService = axios.create({
    baseURL: LENOVO_BASE_URL, // url = base url + request url
    // withCredentials: true, // send cookies when cross-domain requests
    timeout: 30000 // request timeout
});

// request interceptor
lenovoService.interceptors.request.use(
    config => {
        // do something before request is sent
        config.headers['X-LENOVO-SESS-ID'] = 'bd98a9cffe1e4bf5b0be03000d44d56b_346341_696047_meta';
        return config
    },
    error => {
        // do something with request error
        console.log(error) // for debug
        return Promise.reject(error)
    }
);


export default lenovoService;