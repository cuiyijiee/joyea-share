export function genSrcPreviewSrc(neid, hash, rev, previewType, sessionId) {
    return 'https://console.box.lenovo.com/v2/preview_router?type=' + previewType + '&root=databox&path=&path_type=ent&from=&neid='
        + neid + '&hash=' + hash + '&rev=' + rev + "&X-LENOVO-SESS-ID=" + sessionId + "&xxoo=" + new Date().getTime();
}

export function handleGoToPreview(row, session) {
    let previewType = 'pic';    // if video is av
    if (row.mime_type.startsWith("doc")) {
        previewType = 'doc'
    } else if (row.mime_type.startsWith("video")) {
        previewType = 'av'
    }
    let url = genSrcPreviewSrc(row.neid, row.hash, row.rev, previewType, session);
    if (row.mime_type.startsWith("doc") || row.mime_type.startsWith("video")) {
        window.open(url);
    } else if (row.mime_type.startsWith("image")) {

    }
}