import {previewFile} from "@/api";

export default function genSrcPreviewUrl(neid, hash, rev, previewType, sessionId) {
    return 'https://console.box.lenovo.com/v2/preview_router?type=' + previewType + '&root=databox&path=&path_type=ent&from=&neid='
        + neid + '&hash=' + hash + '&rev=' + rev + "&X-LENOVO-SESS-ID=" + sessionId + "&xxoo=" + new Date().getTime();
}

export function getVideoPreviewUrl(neid, times) {

    let _res, _rej;
    const promise = new Promise(function (resolve, reject) {
        _res = resolve;
        _rej = reject;

        function attempt() {
            previewFile(neid).then(resolve).catch(function (err) {
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