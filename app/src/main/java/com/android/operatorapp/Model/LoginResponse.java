package com.android.operatorapp.Model;

public class LoginResponse {
    private String id;
    private String fullname;
    private String username;
    private String password;
    private String phone_number;
    private String status;
    private String user_role;
    private String sell_point_id;
    private String token;
    private String created_at;
    private String updated_at;
    private String work_start_date;
    private String date_of_birthday;
    private String unique_id;
    private String role_name;
    private String user_number;

    public LoginResponse(String id, String fullname, String username, String password, String phone_number, String status, String user_role, String sell_point_id, String token, String created_at, String updated_at, String work_start_date, String date_of_birthday, String unique_id, String role_name, String user_number) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.status = status;
        this.user_role = user_role;
        this.sell_point_id = sell_point_id;
        this.token = token;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.work_start_date = work_start_date;
        this.date_of_birthday = date_of_birthday;
        this.unique_id = unique_id;
        this.role_name = role_name;
        this.user_number = user_number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getSell_point_id() {
        return sell_point_id;
    }

    public void setSell_point_id(String sell_point_id) {
        this.sell_point_id = sell_point_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getWork_start_date() {
        return work_start_date;
    }

    public void setWork_start_date(String work_start_date) {
        this.work_start_date = work_start_date;
    }

    public String getDate_of_birthday() {
        return date_of_birthday;
    }

    public void setDate_of_birthday(String date_of_birthday) {
        this.date_of_birthday = date_of_birthday;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }
}
