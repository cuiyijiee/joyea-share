import {convertItem, GenImageListView} from "./ImageViewUtil";
import store from "../store";
import {Dialog} from 'vant';
import {previewFile} from "@/api";

export function genSrcPreviewSrc(neid, hash, rev, previewType, sessionId) {
    // let url = 'https://console.box.lenovo.com/v2/preview_router?type=' + previewType + '&root=databox&path=&path_type=ent&from=&neid='
    //     + neid + "&rev=&X-LENOVO-SESS-ID=" + sessionId + "&xxoo=" + new Date().getTime();
    // console.log("preview url: " + url);
    // return url;
    return "/apiv2/imagePreview?neid=" + neid + "&thumbtail=false"
}

export function genSrcOriginSrc(path, neid, sessionId) {
    // return 'https://console.box.lenovo.com/v2/dl_router/databox/' + path +
    //     '?neid=' + neid + '&rev=&_=' + new Date().getTime() + "&X-LENOVO-SESS-ID=" + sessionId;
    return "/apiv2/imagePreview?neid=" + neid + "&thumbtail=false"
}

let videoLoading = false;

export function handleGoToPreview(context, row, session, itemList) {
    row = convertItem(row)
    let fileName = row.path.substr(row.path.lastIndexOf('/') + 1)
    let previewType = 'pic';    // if video is av
    if (row.mime_type.startsWith("doc")) {
        previewType = 'doc'
    } else if (row.mime_type.startsWith("video")) {
        previewType = 'av'
    }
    let isRealImage = store.state.showRealImage;
    let url = isRealImage ?
        genSrcOriginSrc(row.path, row.neid, session) :
        genSrcPreviewSrc(row.neid, row.hash, row.rev, previewType, session);
    if (row.mime_type.startsWith("video")) {
        videoLoading = true;
        const {promise,abort} = getVideoPreviewUrl(row.neid, 30)
        promise.then(resp => {
            if (videoLoading) {
                callNextPlusPreview(fileName, resp);
            }
            Dialog.close();
        });
        Dialog.alert({
            title: "转码中，成功后将自动播放，请耐心等待。。。",
            message: "<img src='loading.gif' style='width: 50px;height: 50px'></img>",
            allowHtml: true,
            showConfirmButton: true,
            confirmButtonText: '取消'
        }).then(()=>{
            abort();
        })
        //callNextPlusPreview(fileName, url)
    } else if (row.mime_type.startsWith("doc") && !row.mime_type.endsWith("pdf")) {
        if (row.mime_type.endsWith("pdf")) {
            //callNextPlusPreview(fileName, url)
        } else {
            //callNextPlusPreview(fileName, url)
            window.open(url)
        }
    } else if (row.mime_type.startsWith("image")) {
        GenImageListView(context, itemList, session, row);
    } else if (row.mime_type.startsWith("word")) {
        window.location.href = 'esen://word?wordId=' + row.neid;
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
    } else if (mimeType.startsWith("word")) {
        result = "word.png"
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

const latest_record_id_prefix = "last_read_upload_record_id_";

export function getLastReadUploadRecordId(myUid) {
    let lastReadId = localStorage.getItem(latest_record_id_prefix + myUid);
    //console.log("uid:" + myUid + ",upload id:" + lastReadId);
    return lastReadId ? parseInt(lastReadId) : 0;
}

export function setLastReadUploadRecordId(myUid, readId) {
    localStorage.setItem(latest_record_id_prefix + myUid, readId + "");
}

export function getVideoPreviewUrl(neid, times) {
    console.log("start check preview:" + neid)
    let _res, _rej;
    const promise = new Promise(function (resolve, reject) {
        _res = resolve;
        _rej = reject;

        function attempt() {
            previewFile(neid).then(function (previewUrl) {
                console.log(previewUrl)
                if(previewUrl.code) {
                    if (0 === times) {
                        reject(err)
                    } else {
                        times--;
                        setTimeout(attempt, 1000)
                    }
                } else{
                    resolve(previewUrl)
                }
            }).catch(function (err) {
                console.log("第" + times + "次尝试获取视频预览地址")
                if (0 === times) {
                    reject(err)
                } else {
                    times--;
                    setTimeout(attempt, 1000)
                }
            })
        }

        attempt()
    });

    return {
        promise,
        abort: (opt = {}) => {
            times = 0;
            _rej({
                name: "abort",
                message: "the promise is aborted",
                aborted: true,
            })
        }
    }
}
