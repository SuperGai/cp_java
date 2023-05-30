package com.hh.appraisal.springboot.model;


import lombok.Data;

import java.io.Serializable;
@Data
public class FileResult implements Serializable {
    //判断结果
    private boolean success;
    //返回信息
    private String message;
    //文件地址
    private String fileAddress;

    private String start;

    public FileResult(boolean success, String message,String fileAddress) {
        this.success = success;
        this.message = message;
        this.fileAddress = fileAddress;
    }

    public boolean isSuccess() {
        return success;
    }
}

