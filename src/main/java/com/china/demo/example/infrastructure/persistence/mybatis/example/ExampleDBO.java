package com.china.demo.example.infrastructure.persistence.mybatis.example;

import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Table(name = "example_resource")
public class ExampleDBO {

    @Id
    @KeySql(sql = "select uuid()")
    private String id;

    private String name;

    private String description;

    private String createdBy;

    private String updatedBy;

    private Date updatedAt;

    private Date createdAt;
}