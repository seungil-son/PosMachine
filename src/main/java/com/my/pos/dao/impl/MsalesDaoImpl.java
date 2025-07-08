package com.my.pos.dao.impl;

import com.my.pos.dao_Interface.MsalesDao;
import com.my.pos.model.DailySales;
import com.my.pos.util.JdbcUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/*
매 호출 시 JdbcUtil.getConnection()으로 데이터베이스 연결을 획득

try-with-resources 구문을 사용해 Connection, PreparedStatement, ResultSet 자원을 자동 해제

예외 발생 시 원인 예외를 포장하여 SQLException으로 호출부에 전달
 */
public class MsalesDaoImpl implements MsalesDao {

    @Override
    public void insert(DailySales record) throws SQLException {
        String sql = "INSERT INTO MSALES (SALE_DATE, TOTAL_SALES) VALUES (?, ?)";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(record.getSaleDate()));
            ps.setBigDecimal(2, record.getTotalSales());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateTotalSales(LocalDate date, double amount) throws SQLException {
        String sql = "UPDATE MSALES SET TOTAL_SALES = TOTAL_SALES + ? WHERE SALE_DATE = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, BigDecimal.valueOf(amount));
            ps.setDate(2, Date.valueOf(date));
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public DailySales findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM MSALES WHERE SALE_DATE = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    DailySales ds = new DailySales();
                    ds.setSaleDate(rs.getDate("SALE_DATE").toLocalDate());
                    ds.setTotalSales(rs.getBigDecimal("TOTAL_SALES"));
                    return ds;
                }
                return null;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<DailySales> findAll() throws SQLException {
        String sql = "SELECT * FROM MSALES";
        List<DailySales> list = new ArrayList<>();
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                DailySales ds = new DailySales();
                ds.setSaleDate(rs.getDate("SALE_DATE").toLocalDate());
                ds.setTotalSales(rs.getBigDecimal("TOTAL_SALES"));
                list.add(ds);
            }
            return list;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}
