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
        name: "STICK线", path: "STICK线", icon: "menu-icon/11.png"
    },
    {
        name: "听装线", path: "听装线", icon: "menu-icon/12.png"
    },
    {
        name: "泡罩线", path: "泡罩线", icon: "menu-icon/15.png"
    },
    // {
    //     name: "软双铝线", path: "软双铝线", icon: "menu-icon/13.png"
    // },
    {
        name: "制粒线", path: "制粒线", icon: "menu-icon/14.png"
    },
    {
        name: "智能物流系统", path: "智能物流系统", icon: "menu-icon/19.png"
    },
    {
        name: "其他生产线", path: "其他生产线", icon: "menu-icon/18.png"
    },
    {
        name: "公司介绍", path: "公司介绍", icon: "menu-icon/16.png"
    },
    {
        name: "友商信息", path: "友商信息", icon: "menu-icon/17.png"
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
    console.log(dir)
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