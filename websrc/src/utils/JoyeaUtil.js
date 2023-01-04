export function getDocumentImage(mimeType) {
    let result = "document.png"
    if (mimeType.startsWith("video")) {
        result = "video.png"
    } else if (mimeType.endsWith("pdf")) {
        result = "pdf.png"
    } else if (mimeType.endsWith("ppt") || mimeType.endsWith("pptx")) {
        result = "ppt.png"
    } else if (mimeType.endsWith("doc")) {
        result = "docm.png"
    } else if (mimeType.endsWith("xlsx")) {
        result = "xlsx.png"
    }
    return result;
}

export let joyeaMenuPath = [
    {
        name: "STICK线", path: "/营销素材展示/STICK线", icon: require("../assets/menu-icon/11.png")
    },
    {
        name: "听装线", path: "/营销素材展示/听装线", icon: require("../assets/menu-icon/12.png")
    },
    {
        name: "泡罩线", path: "/营销素材展示/泡罩线", icon: require("../assets/menu-icon/15.png")
    },
    {
        name: "制粒线", path: "/营销素材展示/制粒线", icon: require("../assets/menu-icon/14.png")
    },
    {
        name: "智能物流系统", path: "/营销素材展示/智能物流系统", icon: require("../assets/menu-icon/19.png")
    },
    {
        name: "其他生产线", path: "/营销素材展示/其他生产线", icon: require("../assets/menu-icon/18.png")
    },
    {
        name: "公司介绍", path: "/营销素材展示/公司介绍", icon: require("../assets/menu-icon/16.png")
    },
    {
        name: "友商信息", path: "/营销素材展示/友商信息", icon: require("../assets/menu-icon/17.png")
    },
    {
        name: "一家园素材库", path: "/营销素材展示/一家园素材库", icon: require("../assets/menu-icon/20.png")
    },
    {
        name: "元一素材库", path: "/营销素材展示/元一素材库", icon: require("../assets/menu-icon/21.png")
    }
]

export function extname(filename) {
    if (!filename || typeof filename != 'string') {
        return false
    }
    let a = filename.split('').reverse().join('');
    return a.substring(0, a.search(/\./)).split('').reverse().join('')
}

export function genFileName(fileName) {
    return fileName.substring(fileName.lastIndexOf("/") + 1);
}

export function getFileNameWithoutExtension(fileName) {
    let dotIndex = fileName.lastIndexOf(".");
    return fileName.substring(0, dotIndex);
}

export function filterDirList(dir) {
    return dir.filter(item => {
        return item.is_dir;
    })
}

export function convertItem(item) {
    if (item.srcType) item.mime_type = item.srcType;
    if (item.srcNeid) item.neid = item.srcNeid;
    if (item.srcHash) item.hash = item.srcHash;
    if (item.srcRev) item.rev = item.srcRev;
    if (item.srcPath) item.path = item.srcPath;
    return item;
}

export function getQueryParam(key) {
    if (!key) {
        return false;
    }

    var value = '';
    var paramStr = window.location.search ? window.location.search.substr(1) : '';

    if (paramStr) {
        paramStr.split('&').forEach(function (param) {
            var arr = param.split('=');
            if (arr[0] == key) {
                value = arr[1];
            }
        });
    }
    return value;
}
