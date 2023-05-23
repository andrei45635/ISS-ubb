package com.example.demo.model;

import java.io.Serializable;
import java.util.Objects;

public class QA implements Entity<Integer>, Serializable {
    private int id;
    private String username;
    private String password;

    public QA() {}

    public QA(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public QA(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setId(Integer integer) {
        this.id = integer;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QA qa = (QA) o;
        return id == qa.id && Objects.equals(username, qa.username) && Objects.equals(password, qa.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

    @Override
    public String toString() {
        return "QA{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
