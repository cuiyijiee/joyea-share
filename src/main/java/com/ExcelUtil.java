package com;

import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.util.List;
import java.util.Map;

public class ExcelUtil {
    public static List<Map<Integer, String>> getExcelData(File excelFile, int sheetNum) {
        return EasyExcel.read(excelFile).sheet(sheetNum).doReadSync();
    }
}
