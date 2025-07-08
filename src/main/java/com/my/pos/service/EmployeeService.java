package com.my.pos.service;

import com.my.pos.dao_Interface.EmployeeDao;
import com.my.pos.dao.impl.EmployeeDaoImpl;
import com.my.pos.model.Employee;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;

public class EmployeeService {
    private final EmployeeDao dao = new EmployeeDaoImpl();



    /** 로그인 검사 후 로그인 시각 기록 */
    public Employee login(String loginKey, String password) throws SQLException {
        // 1) ID로 시도
        Employee e = dao.findById(loginKey);

        // 2) ID 실패 시 이름으로 재시도
        if (e == null) {
            e = dao.findByName(loginKey);
        }

        // 3) 둘 다 실패면 예외
        if (e == null) {
            throw new IllegalArgumentException("존재하지 않는 사원 ID/이름: " + loginKey);
        }

        // 4) 비밀번호 검증
        if (!e.getPassword().equals(password)) {
            throw new IllegalArgumentException("패스워드가 일치하지 않습니다.");
        }

        // 5) 로그인 시간 기록 (DB PK인 empId 사용)
        dao.updateLoginTime(e.getEmpId());
        e.setLoginTime(LocalDateTime.now());

        return e;
    }


    /** 로그아웃 시 근무 시간 계산·저장 */
    public int logout(String empId) throws SQLException {
        Employee e = dao.findById(empId);
        LocalDateTime loginTime = e.getLoginTime();
        if (loginTime == null) {
            throw new IllegalStateException("로그인 기록이 없습니다.");
        }
        LocalDateTime now = LocalDateTime.now();
        int worked = (int) Duration.between(loginTime, now).toMinutes();
        dao.updateLogoutTime(empId, worked);
        return worked;
    }

    public Employee findById(String empId) throws SQLException {
        return dao.findById(empId);
    }

    public java.util.List<Employee> findAll() throws SQLException {
        return dao.findAll();
    }
}
