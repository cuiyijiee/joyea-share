import {ImagePreview} from 'vant';
import {genSrcPreviewSrc, newGenSrcPreviewSrc} from './JoyeaUtil'

export function convertItem(item) {
    if (item.srcType) item.mime_type = item.srcType;
    if (item.srcNeid) item.neid = item.srcNeid;
    if (item.srcHash) item.hash = item.srcHash;
    if (item.srcRev) item.rev = item.srcRev;
    if (item.srcPath) item.path = item.srcPath;
    return item;
}

export function GenImageListView(context, itemList, sessionId, clickItem) {
    itemList.forEach(item => {
        convertItem(item)
    });
    convertItem(clickItem)
    let index = 0;
    let tmp = 0;
    let list = itemList.filter(item => {
        return item.mime_type && item.mime_type.startsWith('image')
    }).map(item => {
        if (item.neid === clickItem.neid) {
            index = tmp;
        }
        tmp++;
        //return newGenSrcPreviewSrc(item.path, item.neid, sessionId);
        return genSrcPreviewSrc( item.neid, item.hash, item.rev, "pic", sessionId); //neid, hash, rev, previewType, sessionId
    });
    if (list.length === 0) {
        context.$notify({
            message: "当前清单没有可以预览的图片！"
        });
    } else {
        ImagePreview({
            images: list,
            startPosition: index ? index : 0,
            maxZoom: 10,
            minZoom: 1 / 10,
            closeable: true
        })
    }
}