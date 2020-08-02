export function getDocumentImage(mimeType) {
    let result = "document.png"
    if (mimeType.endsWith("pdf")) {
        result = "pdf.png"
    } else if (mimeType.endsWith("ppt") || mimeType.endsWith("pptx")) {
        result = "ppt.png"
    } else if (mimeType.endsWith("doc")) {
        result = "docm.png"
    } else if (mimeType.endsWith("xlsx")) {
        result = "xlsx.png"
    }
    return result;
}

export let joyeaMenuPath = [
    {
        name: "STICK线", path: "STICK线", icon: "menu-icon/11.png"
    },
    {
        name: "听装线", path: "听装线", icon: "menu-icon/12.png"
    },
    {
        name: "软双铝线", path: "软双铝线", icon: "menu-icon/13.png"
    },
    {
        name: "制粒线", path: "制粒线", icon: "menu-icon/14.png"
    },
    {
        name: "泡罩线", path: "泡罩线", icon: "menu-icon/15.png"
    },
    {
        name: "其他生产线", path: "其他生产线", icon: "menu-icon/18.png"
    },
    {
        name: "公司介绍", path: "公司介绍", icon: "menu-icon/16.png"
    },
    {
        name: "友商信息", path: "友商信息", icon: "menu-icon/17.png"
    }
]