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