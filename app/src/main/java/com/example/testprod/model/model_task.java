package com.example.testprod.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class model_task {
    private String id;
    private String idRelationship;
    private String title;
    private String description;
    private Integer status;
    private Integer nilai;
    private Integer repeatable;
    private Integer rep;
    private Integer completion;
    private Integer reviewed;
    // 1 word, 2 touch, 3 quality, 4 gift, 5 service, 0 notype
    private Integer type;

}
