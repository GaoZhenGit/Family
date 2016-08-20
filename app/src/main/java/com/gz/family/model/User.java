package com.gz.family.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by host on 2016/1/29.
 */
@DatabaseTable(tableName = "user")
public class User implements Serializable {

    public static final int MALE = 1;
    public static final int FEMALE =0;

    @DatabaseField(id = true)
    private String id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "sex")
    private int sex;
    @DatabaseField(columnName = "avatar")
    private String avatar;
    @DatabaseField(columnName = "address")
    private String address;
    @DatabaseField(columnName = "detail")
    private String detail;
    @DatabaseField(columnName = "age")
    private int age;
    @DatabaseField(columnName = "phone")
    private String phone;
    private Map<String, User> relatives = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Map<String, User> getRelatives() {
        return relatives;
    }

    public void setRelatives(Map<String, User> relatives) {
        this.relatives = relatives;
    }

    @Override
    public boolean equals(Object o) {
        User u = (User) o;
        if (u.getId().equals(this.getId())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
