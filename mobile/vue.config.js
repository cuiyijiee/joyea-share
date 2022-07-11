const path = require('path');

function resolve(dir) {
    return path.join(__dirname, dir);
}

module.exports = {
    lintOnSave: true,
    productionSourceMap: false,
    devServer: {
        allowedHosts: [
            '.in.ngrok.io'   // .是二级域名的通配符
        ],
        proxy: {
            'api/preview': {
                target: 'http://localhost:8080',
                changeOrigin: true
            },
            'api/addRedirectPath': {
                target: 'http://localhost:8080',
                changeOrigin: true
            },
            'api/redirectPath': {
                target: 'http://localhost:8080',
                changeOrigin: true
            },
            'apiv2/': {
                //target: 'http://192.168.1.35:8081',
                target: 'http://sck.joyea.cn:8000',
                changeOrigin: true
            },
            '/': {
                target: 'http://sck.joyea.cn:8000',
                //target: 'http://127.0.0.1:8000',
                changeOrigin: true
            }
        }
    },
    publicPath: '/mobile/',
    chainWebpack: config => {
        config.resolve.alias
            .set('@', resolve('src'))
            .set('@assets', resolve('src/assets'))
    }
};
