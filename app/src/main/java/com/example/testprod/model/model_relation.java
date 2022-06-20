package com.example.testprod.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class model_relation {
    private String id;
    private String pasangan1;
    private String nama1;
    private String pasangan2;
    private String nama2;
    // 0 pengajuan
    // 1 ditolak
    // 2 diterima
    // 3 selesai
    private Integer status;

    private String uCreatedBy;
    private Date vCreatedAt;
    private String wLastModifiedBy;
    private Date xLastModifiedAt;
    private Integer yVersion;
    private String zDeletedAt;

}
