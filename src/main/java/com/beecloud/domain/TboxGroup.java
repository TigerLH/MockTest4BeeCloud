package com.beecloud.domain;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/22.
 */
public class TboxGroup {
    private int id;
    private String name;
    private String description;
    private String tboxs;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTboxs() {
        return tboxs;
    }

    public void setTboxs(String tboxs) {
        this.tboxs = tboxs;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TboxGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", tboxs='" + tboxs + '\'' +
                ", status=" + status +
                '}';
    }
}
