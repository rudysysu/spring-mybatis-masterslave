package com.dy.spring.mybatis.masterslave.domain;

import java.io.Serializable;

public class User implements Serializable {
    private Long id;

    private String name;

    public User(String name) {
        this.name = name;
    }

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
}