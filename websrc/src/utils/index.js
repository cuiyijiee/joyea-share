import {previewFile} from "@/api";

export default function genSrcPreviewUrl(neid, hash, rev, previewType, sessionId) {
    return 'https://console.box.lenovo.com/v2/preview_router?type=' + previewType + '&root=databox&path=&path_type=ent&from=&neid='
        + neid + '&hash=' + hash + '&rev=' + rev + "&X-LENOVO-SESS-ID=" + sessionId + "&xxoo=" + new Date().getTime();
}

export function getVideoPreviewUrl(neid, times) {
    return new Promise(function (resolve, reject) {
        function attempt() {
            previewFile(neid).then(resolve).catch(function (err) {
                console.log("第" + times +"次尝试获取视频预览地址")
                if (0 === times) {
                    reject(err)
                } else {
                    times--;
                    setTimeout(attempt, 1000)
                }
            })
        }

        attempt()
    })
}