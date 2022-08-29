import {previewFile} from "@/api";

export default function genSrcPreviewUrl(neid) {
    return "/apiv2/imagePreview?neid=" + neid + "&thumbtail=false"
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
                if (previewUrl.code) {
                    if (0 === times) {
                        reject(err)
                    } else {
                        times--;
                        setTimeout(attempt, 1000)
                    }
                } else {
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

export function formatFileShowType(searchResultRow) {
    let defaultType = "--";
    if (searchResultRow.is_dir) {
        defaultType = "文件夹"
    } else if (!searchResultRow.is_dir && searchResultRow.mime_type) {
        let filePath = searchResultRow.path
        let fileExtension = filePath.slice((filePath.lastIndexOf(".") - 1 >>> 0) + 2)
        defaultType = fileExtension.toLowerCase() + "文件"
    }
    return defaultType;
}
