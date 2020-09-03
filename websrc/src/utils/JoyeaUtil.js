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
        name: "装配作业指导类文件", path: "装配作业指导类文件", icon: "menu-icon/2-1.png"
    },
    {
        name: "调试作业指导文件", path: "调试作业指导文件", icon: "menu-icon/2-2.png"
    },
    {
        name: "我司设备培训素材", path: "我司设备培训素材", icon: "menu-icon/2-3.png"
    },
    {
        name: "外购大件类培训文件", path: "外购大件类培训文件", icon: "menu-icon/2-4.png"
    },
    {
        name: "通用元器件使用说明", path: "通用元器件使用说明", icon: "menu-icon/2-5.png"
    },
    {
        name: "新工具使用说明", path: "新工具使用说明", icon: "menu-icon/2-6.png"
    },
    {
        name: "实验室", path: "实验室", icon: "menu-icon/2-7.png"
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