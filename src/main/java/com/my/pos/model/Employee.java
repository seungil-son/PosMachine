package com.my.pos.model;

import java.time.LocalDateTime;

public class Employee {
    private String empId;
    private String password;
    private String name;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
    private int workMinutes;

    public Employee() {}

    public Employee(String empId, String password, String name) {
        this.empId = empId;
        this.password = password;
        this.name = name;
    }

    public String getEmpId() {
        return empId;
    }
    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
    public LocalDateTime getLogoutTime() {
        return logoutTime;
    }
    public void setLogoutTime(LocalDateTime logoutTime) {
        this.logoutTime = logoutTime;
    }
    public int getWorkMinutes() {
        return workMinutes;
    }
    public void setWorkMinutes(int workMinutes) {
        this.workMinutes = workMinutes;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId='" + empId + '\'' +
                ", name='" + name + '\'' +
                ", loginTime=" + loginTime +
                ", logoutTime=" + logoutTime +
                ", workMinutes=" + workMinutes +
                '}';
    }
}
