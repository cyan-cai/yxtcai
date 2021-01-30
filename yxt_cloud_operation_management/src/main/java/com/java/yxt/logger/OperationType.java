package com.java.yxt.logger;

public enum OperationType {
    notDef("未定义操作",0),
    get("查询操作",1),
    post("插入操作",2),
    put("修改操作",3),
    delete("删除操作",4)
    ;
    private String name;
    private int value;
    private OperationType(String name,int value){
        this.name  = name ;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public int getValue() {
        return value;
    }
}
