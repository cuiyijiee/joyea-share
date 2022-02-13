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
