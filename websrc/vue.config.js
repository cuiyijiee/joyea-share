const path = require('path');

function resolve(dir) {
    return path.join(__dirname, dir);
}

module.exports = {
    lintOnSave: true,
    productionSourceMap: false,
    devServer: {
        proxy: {
            'apiv2/': {
                target: 'http://192.168.1.38:8081',
                changeOrigin: true
            },
            '/': {
                target: 'http://sck.joyea.cn:8000',
                changeOrigin: true
            }
        }
    },
    chainWebpack: config => {
        config.resolve.alias
            .set('@', resolve('src'))
            .set('@assets', resolve('src/assets'))
    }
};
