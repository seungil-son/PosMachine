package com.my.pos.service;

import com.my.pos.dao_Interface.MsalesDao;
import com.my.pos.dao.impl.MsalesDaoImpl;
import com.my.pos.model.DailySales;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MsalesService {
    private final MsalesDao dao = new MsalesDaoImpl();

    /** 신규 혹은 누적 일별 매출 기록 */
    public void recordDailySales(LocalDate date, BigDecimal amount) throws SQLException {
        DailySales existing = dao.findByDate(date);
        if (existing == null) {
            dao.insert(new DailySales(date, amount));
        } else {
            dao.updateTotalSales(date, amount.doubleValue());
        }
    }

    public DailySales findByDate(LocalDate date) throws SQLException {
        return dao.findByDate(date);
    }

    public List<DailySales> findAll() throws SQLException {
        return dao.findAll();
    }
}
