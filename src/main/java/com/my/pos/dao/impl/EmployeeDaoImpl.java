package com.my.pos.dao.impl;

import com.my.pos.dao_Interface.EmployeeDao;
import com.my.pos.model.Employee;
import com.my.pos.util.JdbcUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
/*
JdbcUtil.getConnection()을 통해 매 호출 시마다 새로운 데이터베이스 연결(Connection)을 획득

try-with-resources 구문을 사용해 Connection·PreparedStatement·ResultSet 자동 해제

예외 발생 시 원인 예외를 감싸 SQLException으로 전달
 */
    @Override                                       //전달된 Employee 객체의 empId, password, name을 바인딩 executeUpdate()로 신규 사원 레코드 삽입
    public void insert(Employee employee) throws SQLException {
        String sql = "INSERT INTO EMPLOYEE (EMP_ID, PASSWORD, NAME) VALUES (?, ?, ?)";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employee.getEmpId());
            ps.setString(2, employee.getPassword());
            ps.setString(3, employee.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

                                                // 사원 아이디를 파라미터로 설정 조회 결과가 존재하면 Employee 객체로 매핑
                                                // LOGIN_TIME, LOGOUT_TIME은 Timestamp → LocalDateTime 변환  WORK_MINUTES는 int 형태로 세팅
    @Override                                   // 레코드가 없으면 null 반환
    public Employee findById(String empId) throws SQLException {
        String sql = "SELECT * FROM EMPLOYEE WHERE EMP_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee e = new Employee();
                    e.setEmpId(rs.getString("EMP_ID"));
                    e.setPassword(rs.getString("PASSWORD"));
                    e.setName(rs.getString("NAME"));
                    Timestamp lt = rs.getTimestamp("LOGIN_TIME");
                    if (lt != null) e.setLoginTime(lt.toLocalDateTime());
                    Timestamp lo = rs.getTimestamp("LOGOUT_TIME");
                    if (lo != null) e.setLogoutTime(lo.toLocalDateTime());
                    e.setWorkMinutes(rs.getInt("WORK_MINUTES"));
                    return e;
                }
                return null;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
    @Override
    public Employee findByName(String name) throws SQLException {
        String sql = "SELECT * FROM EMPLOYEE WHERE NAME = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Employee e = new Employee();
                    e.setEmpId(rs.getString("EMP_ID"));
                    e.setPassword(rs.getString("PASSWORD"));
                    e.setName(rs.getString("NAME"));
                    Timestamp lt = rs.getTimestamp("LOGIN_TIME");
                    if (lt != null) e.setLoginTime(lt.toLocalDateTime());
                    Timestamp lo = rs.getTimestamp("LOGOUT_TIME");
                    if (lo != null) e.setLogoutTime(lo.toLocalDateTime());
                    e.setWorkMinutes(rs.getInt("WORK_MINUTES"));
                    return e;
                }
                return null;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override               // 지정된 사원의 LOGIN_TIME 컬럼을 현재 시각(SYSTIMESTAMP)으로 갱신
    public void updateLoginTime(String empId) throws SQLException {
        String sql = "UPDATE EMPLOYEE SET LOGIN_TIME = SYSTIMESTAMP WHERE EMP_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

                                             // LOGOUT_TIME을 현재 시각으로 업데이트
    @Override                                //  WORK_MINUTES에 추가 근무 시간(additionalMinutes)을 더해 누적
    public void updateLogoutTime(String empId, int additionalMinutes) throws SQLException {
        String sql = "UPDATE EMPLOYEE " +
                "SET LOGOUT_TIME = SYSTIMESTAMP, WORK_MINUTES = WORK_MINUTES + ? " +
                "WHERE EMP_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, additionalMinutes);
            ps.setString(2, empId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

                                     // 사원 테이블 전체 조회
    @Override                        // 각 레코드를 Employee 객체로 변환 후 리스트에 담아 반환
    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT * FROM EMPLOYEE";
        List<Employee> list = new ArrayList<>();
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Employee e = new Employee();
                e.setEmpId(rs.getString("EMP_ID"));
                e.setPassword(rs.getString("PASSWORD"));
                e.setName(rs.getString("NAME"));
                Timestamp lt = rs.getTimestamp("LOGIN_TIME");
                if (lt != null) e.setLoginTime(lt.toLocalDateTime());
                Timestamp lo = rs.getTimestamp("LOGOUT_TIME");
                if (lo != null) e.setLogoutTime(lo.toLocalDateTime());
                e.setWorkMinutes(rs.getInt("WORK_MINUTES"));
                list.add(e);
            }
            return list;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}
