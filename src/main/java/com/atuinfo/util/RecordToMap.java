package com.atuinfo.util;

import com.jfinal.plugin.activerecord.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordToMap {
    public static List<Map<String, Object>> recordToMap(List<Record> list){
        List<Map<String, Object>> nursingRecords = new ArrayList<>();
        for (Record record : list) {
            Map<String, Object> map = record.getColumns();
            nursingRecords.add(map);
        }
        return nursingRecords;
    }

}
