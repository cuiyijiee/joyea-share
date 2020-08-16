import {convertItem} from "./ImageViewUtil";

export function genSrcPreviewSrc(neid, hash, rev, previewType, sessionId) {
    return 'https://console.box.lenovo.com/v2/preview_router?type=' + previewType + '&root=databox&path=&path_type=ent&from=&neid='
        + neid + '&hash=' + hash + '&rev=' + rev + "&X-LENOVO-SESS-ID=" + sessionId + "&xxoo=" + new Date().getTime();
}

export function newGenSrcPreviewSrc(path, neid, sessionId) {
    return 'https://console.box.lenovo.com/v2/dl_router/databox/' + path +
        '?neid=' + neid + '&rev=&_=' + new Date().getTime() + "&X-LENOVO-SESS-ID=" + sessionId;
}

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
    if (mimeType.endsWith("pdf")) {
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

export function genFileName(fullPath){
    return fullPath.substr(fullPath.lastIndexOf('/')+1);
}

export function filterDirList(fileList){
    return fileList.filter(item => item['is_dir'] );
}