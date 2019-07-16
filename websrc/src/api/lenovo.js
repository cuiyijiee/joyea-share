import lenovoService from "../utils/LenovoRequest"

export function listDir(path, type) {

    return lenovoService({
        url: '/metadata/root/' + (path === undefined ? '' : path),
        method: 'get',
        params: {
            path_type: 'ent',
            'X-LENOVO-SESS-ID':'bd98a9cffe1e4bf5b0be03000d44d56b_346341_696047_meta'
        }
    })
}