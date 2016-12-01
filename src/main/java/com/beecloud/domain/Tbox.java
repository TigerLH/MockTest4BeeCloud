package com.beecloud.domain;

/**
 * @author hong.lin
 * @description
 * @date 2016/11/24.
 */
public class Tbox {
    private int id;
    private String name;
    private String vin;
    private String data;
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

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Tbox{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", vin='" + vin + '\'' +
                ", data='" + data + '\'' +
                ", status=" + status +
                '}';
    }
}
