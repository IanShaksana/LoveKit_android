package com.example.testprod.model;

import org.json.JSONObject;

import java.util.Date;

import lombok.*;

@Getter
@Setter
public class model_login {
    private String id;
    private String password;
    private String email;
    private String detail;
    private String testDetail;
    private Integer status;
    private String onesignalid;
    private String propic;
    private Integer hearttank;

    private String uCreatedBy;
    private Date vCreatedAt;
    private String wLastModifiedBy;
    private Date xLastModifiedAt;
    private Integer yVersion;
    private String zDeletedAt;

}
