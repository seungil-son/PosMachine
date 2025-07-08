// src/main/java/com/my/pos/dao_Interface/SalesDao.java
package com.my.pos.dao_Interface;

import com.my.pos.model.Sale;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface SalesDao {
    void insert(Sale sale) throws SQLException;                     // 새로운 판매 내역을 SALES 테이블에 추가

    Sale findById(int saleId) throws SQLException;                  // 주어진 판매 고유 번호(saleId)에 해당하는 판매 기록을 조회하여 Sale 객체로 반환

    List<Sale> findByDate(LocalDate date) throws SQLException;      // 특정 날짜(date)에 이루어진 모든 판매 내역을 리스트로 조회

    List<Sale> findByEmployee(String empId) throws SQLException;    // 특정 사원 아이디(empId)가 처리한 모든 판매 내역을 리스트로 조회
}
