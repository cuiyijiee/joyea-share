const path = require('path');

function resolve(dir) {
    return path.join(__dirname, dir);
}

module.exports = {
    lintOnSave: true,
    productionSourceMap: false,
    devServer: {
        proxy: {
            'api/preview': {
                target: 'http://sck.joyea.cn:8000/',
                changeOrigin: true
            },
            '/': {
                target: 'http://localhost:8000',
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
