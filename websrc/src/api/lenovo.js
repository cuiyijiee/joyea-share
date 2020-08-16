import lenovoService from "../utils/LenovoRequest"

export function listDir(path, type) {
    return lenovoService({
        url: '/metadata/root/' + (path === undefined ? '' : path),
        method: 'get',
        params: {
            path_type: 'ent',
            'X-LENOVO-SESS-ID': 'bd98a9cffe1e4bf5b0be03000d44d56b_346341_696047_meta'
        }
    })
}

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
