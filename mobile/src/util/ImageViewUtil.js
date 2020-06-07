import {ImagePreview} from 'vant';
import {genSrcPreviewSrc} from './JoyeaUtil'

export function GenImageListView(context, itemList, sessionId, clickItem) {
    let index = 0;
    let tmp = 0;
    let list = itemList.filter(item => {
        return item.mime_type && item.mime_type.startsWith('image')
    }).map(item => {
        if (item.neid === clickItem.neid) {
            index = tmp;
        }
        tmp ++;
        return genSrcPreviewSrc(item.neid, item.hash, item.rev, "pic", sessionId);
    });
    if (list.length === 0) {
        context.$notify({
            message: "当前清单没有可以预览的图片！"
        });
    } else {
        ImagePreview({
            images: list,
            startPosition: index ? index : 0,
            closeable: true
        })
    }
}