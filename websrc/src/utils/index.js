import {previewFile} from "../api";

export default function genSrcPreviewUrl(neid) {
    return "/apiv2/imagePreview?neid=" + neid + "&thumbtail=false"
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