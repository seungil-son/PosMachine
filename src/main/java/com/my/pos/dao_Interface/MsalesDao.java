// src/main/java/com/my/pos/dao_Interface/MsalesDao.java
package com.my.pos.dao_Interface;

import com.my.pos.model.DailySales;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface MsalesDao {
    void insert(DailySales record) throws SQLException;     // 새로운 일별 매출 기록을 MSALES 테이블에 추가

    void updateTotalSales(LocalDate date, double amount) throws SQLException;  // 지정한 날짜의 총매출(TOTAL_SALES) 값에 amount를 더해 업데이트

    DailySales findByDate(LocalDate date) throws SQLException;  //주어진 날짜(date)에 해당하는 일별 매출 정보를 조회해 DailySales 객체로 반환

    List<DailySales> findAll() throws SQLException;  //MSALES 테이블의 모든 일별 매출 기록을 리스트로 반환
}
