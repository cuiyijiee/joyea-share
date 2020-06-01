package com;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import scala.util.parsing.json.JSON;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        File file = new File("/Users/cuiyijie/Desktop/员工名单.xlsx");
        List<Map<Integer,String>> list = EasyExcel.read(file).sheet(0).doReadSync();
        list.forEach(entry -> {
            System.out.println(entry);
        });
    }



    static class MyListener extends AnalysisEventListener<TestUser> {
        List<TestUser> list = new ArrayList<>();

        @Override
        public void invoke(TestUser data, AnalysisContext context) {
            System.out.println("解析到一条数据:" + data.toString());
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            System.out.println(list);
        }
    }
}

class TestUser {
    private Long id;
    private String name;
    private String position;
    private String department;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}


