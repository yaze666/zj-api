package com.atuinfo.common.model;

import com.jfinal.plugin.activerecord.generator.MetaBuilder;

import javax.sql.DataSource;

public class MyMetaBulider extends MetaBuilder {
    //public String tablePrefix = "contact_temp";
    public String tablePrefix;

    public MyMetaBulider(DataSource dataSource){
        super(dataSource);
    }

    public void setTablePrefix(String tablePrefix){
        this.tablePrefix = tablePrefix;
    }

    @Override
    protected  boolean isSkipTable(String tableName){
        //return !tableName.equals(tablePrefix);
        return !tableName.contains(tablePrefix);
    }
}
