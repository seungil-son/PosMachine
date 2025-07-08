package com.my.pos.dao.impl;

import com.my.pos.dao_Interface.SalesDao;
import com.my.pos.model.Sale;
import com.my.pos.util.JdbcUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SalesDaoImpl implements SalesDao {

    @Override
    public void insert(Sale sale) throws SQLException {
        String sql = "INSERT INTO SALES " +
                "(EMP_ID, PRODUCT_ID, QUANTITY, TOTAL_AMOUNT, PAYMENT_METHOD, CUSTOMER_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"SALE_ID"})) {
            ps.setString(1, sale.getEmpId());
            ps.setInt(2, sale.getProductId());
            ps.setInt(3, sale.getQuantity());
            ps.setBigDecimal(4, sale.getTotalAmount());
            ps.setString(5, sale.getPaymentMethod());
            ps.setString(6, sale.getCustomerId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) sale.setSaleId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Sale findById(int saleId) throws SQLException {
        String sql = "SELECT * FROM SALES WHERE SALE_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, saleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Sale s = extractSale(rs);
                    return s;
                }
                return null;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Sale> findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM SALES WHERE TRUNC(SALE_TIME) = ?";
        List<Sale> list = new ArrayList<>();
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractSale(rs));
                }
            }
            return list;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Sale> findByEmployee(String empId) throws SQLException {
        String sql = "SELECT * FROM SALES WHERE EMP_ID = ?";
        List<Sale> list = new ArrayList<>();
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(extractSale(rs));
                }
            }
            return list;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    private Sale extractSale(ResultSet rs) throws SQLException {
        Sale s = new Sale();
        s.setSaleId(rs.getInt("SALE_ID"));
        Timestamp t = rs.getTimestamp("SALE_TIME");
        if (t != null) s.setSaleTime(t.toLocalDateTime());
        s.setEmpId(rs.getString("EMP_ID"));
        s.setProductId(rs.getInt("PRODUCT_ID"));
        s.setQuantity(rs.getInt("QUANTITY"));
        s.setTotalAmount(rs.getBigDecimal("TOTAL_AMOUNT"));
        s.setPaymentMethod(rs.getString("PAYMENT_METHOD"));
        s.setCustomerId(rs.getString("CUSTOMER_ID"));
        return s;
    }
}
