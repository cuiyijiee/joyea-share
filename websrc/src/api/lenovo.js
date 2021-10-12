import lenovoService from "../utils/LenovoRequest"

export function moveFile(fromPathNeid, toPath, toPathNeid, session) {
    return lenovoService.post(`https://console.box.lenovo.com/v2/fileops/manage?X-LENOVO-SESS-ID=${session}`, {
        op: 'move',
        auto_rename: false,
        info: {
            op_info: [{neid: fromPathNeid}],
            to_info: {neid: toPathNeid}
        },
    }).then(res => {
        return res.data;
    })
}
