import {convertItem} from "./ImageViewUtil";

export function genSrcPreviewSrc(neid, hash, rev, previewType, sessionId) {
    return 'https://console.box.lenovo.com/v2/preview_router?type=' + previewType + '&root=databox&path=&path_type=ent&from=&neid='
        + neid + '&hash=' + hash + '&rev=' + rev + "&X-LENOVO-SESS-ID=" + sessionId + "&xxoo=" + new Date().getTime();
}

export function newGenSrcPreviewSrc(path, neid, sessionId) {
    return 'https://console.box.lenovo.com/v2/dl_router/databox/' + path +
        '?neid=' + neid + '&rev=&_=' + new Date().getTime() + "&X-LENOVO-SESS-ID=" + sessionId;
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

export function handleGoToPreview(context, row, session) {
    row = convertItem(row)
    let fileName = row.path.substr(row.path.lastIndexOf('/') + 1)
    let previewType = 'pic';    // if video is av
    if (row.mime_type.startsWith("doc")) {
        previewType = 'doc'
    } else if (row.mime_type.startsWith("video")) {
        previewType = 'av'
    }
    let url = genSrcPreviewSrc(row.neid, row.hash, row.rev, previewType, session);
    //let url = newGenSrcPreviewSrc(row.path, row.neid, session);
    if (row.mime_type.startsWith("video")) {
        callNextPlusPreview(fileName, url)
    } else if (row.mime_type.startsWith("doc") && !row.mime_type.endsWith("pdf")) {
        if (row.mime_type.endsWith("pdf")) {
            //callNextPlusPreview(fileName, url)
        } else {
            //callNextPlusPreview(fileName, url)
            window.open(url)
        }
    } else if (row.mime_type.startsWith("image")) {

    }
}

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

export function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

export function extname(filename) {
    if (!filename || typeof filename != 'string') {
        return false
    }
    let a = filename.split('').reverse().join('');
    return a.substring(0, a.search(/\./)).split('').reverse().join('')
}

export function replaceFileName(file) {
    let fileExtend = extname(file);
    return uuid() + "." + fileExtend;
}

export function genFileName(fullPath) {
    return fullPath.substr(fullPath.lastIndexOf('/') + 1);
}

export function filterDirList(fileList) {
    return fileList.filter(item => item['is_dir']);
}