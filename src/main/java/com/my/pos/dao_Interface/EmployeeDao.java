// src/main/java/com/my/pos/dao_Interface/EmployeeDao.java
package com.my.pos.dao_Interface;

import com.my.pos.model.Employee;
import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao {
    void insert(Employee employee) throws SQLException; //새로운 사원 정보를 EMPLOYEE 테이블에 추가

    Employee findById(String empId) throws SQLException; //주어진 사원 아이디(empId)에 해당하는 사원 정보를 조회해 Employee 객체로 반환

    void updateLoginTime(String empId) throws SQLException; //로그인 시각을 현재 시간으로 갱신 EMPLOYEE.LOGIN_TIME 컬럼을 업데이트

    void updateLogoutTime(String empId, int additionalMinutes) throws SQLException; //로그아웃 시각을 현재 시간으로 갱신하고,
                                                                                    // additionalMinutes를 기존 WORK_MINUTES에 더해 누적 근무 시간을 업데이트

    List<Employee> findAll() throws SQLException;  //EMPLOYEE 테이블의 모든 사원 정보를 조회해 리스트로 반환

    Employee findByName(String name) throws SQLException;
}
