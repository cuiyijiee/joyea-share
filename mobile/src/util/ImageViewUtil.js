import {genSrcPreviewSrc, newGenSrcPreviewSrc} from './JoyeaUtil'

import store from "../store"

export function convertItem(item) {
    if (item.srcType) item.mime_type = item.srcType;
    if (item.srcNeid) item.neid = item.srcNeid;
    if (item.srcHash) item.hash = item.srcHash;
    if (item.srcRev) item.rev = item.srcRev;
    if (item.srcPath) item.path = item.srcPath;
    //if (item.uploadPath) item.path = item.uploadPath;
    item.joyeaDesc = item.srcDesc ? item.srcDesc : "";
    item.filename = item.srcName ? item.srcName : "";

    if(!item.size) item.size = "0";
    if(!item.bytes) item.bytes = 0;

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
        let isRealImage = store.state.showRealImage;
        if (isRealImage) {
            return {
                url: newGenSrcPreviewSrc(item.path, item.neid, sessionId),
                joyeaDesc: item.joyeaDesc
            };
        } else {
            return {
                url: genSrcPreviewSrc(item.neid, item.hash, item.rev, "pic", sessionId), //neid, hash, rev, previewType, sessionId
                joyeaDesc: item.joyeaDesc
            }
        }
    });
    if (list.length === 0) {
        context.$notify({
            message: "当前清单没有可以预览的图片！"
        });
    } else {
        store.state.imagePreviewShow = true;
        store.state.imagePreviewStartIndex = index ? index : 0;
        store.state.imagePreviewImages = list;
    }
}