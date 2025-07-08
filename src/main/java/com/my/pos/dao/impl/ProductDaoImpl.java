package com.my.pos.dao.impl;

import com.my.pos.dao_Interface.ProductDao;
import com.my.pos.model.Product;
import com.my.pos.util.JdbcUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    @Override
    public void insert(Product p) throws SQLException {
        String sql = "INSERT INTO PRODUCT (NAME, MANUFACTURER, EXPIRY_DATE, IS_ADULT_ONLY, PRICE) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"PRODUCT_ID"})) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getManufacturer());
            ps.setDate(3, Date.valueOf(p.getExpiryDate()));
            ps.setString(4, p.isAdultOnly() ? "Y" : "N");
            ps.setBigDecimal(5, p.getPrice());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setProductId(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void update(Product p) throws SQLException {
        String sql = "UPDATE PRODUCT SET NAME=?, MANUFACTURER=?, EXPIRY_DATE=?, IS_ADULT_ONLY=?, PRICE=? " +
                "WHERE PRODUCT_ID=?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getName());
            ps.setString(2, p.getManufacturer());
            ps.setDate(3, Date.valueOf(p.getExpiryDate()));
            ps.setString(4, p.isAdultOnly() ? "Y" : "N");
            ps.setBigDecimal(5, p.getPrice());
            ps.setInt(6, p.getProductId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(int productId) throws SQLException {
        String sql = "DELETE FROM PRODUCT WHERE PRODUCT_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Product findById(int id) throws SQLException {
        String sql = "SELECT * FROM PRODUCT WHERE PRODUCT_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("PRODUCT_ID"));
                    p.setName(rs.getString("NAME"));
                    p.setManufacturer(rs.getString("MANUFACTURER"));
                    p.setExpiryDate(rs.getDate("EXPIRY_DATE").toLocalDate());
                    p.setAdultOnly("Y".equals(rs.getString("IS_ADULT_ONLY")));
                    p.setPrice(rs.getBigDecimal("PRICE"));
                    return p;
                }
                return null;
            }
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT * FROM PRODUCT";
        List<Product> list = new ArrayList<>();
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("PRODUCT_ID"));
                p.setName(rs.getString("NAME"));
                p.setManufacturer(rs.getString("MANUFACTURER"));
                p.setExpiryDate(rs.getDate("EXPIRY_DATE").toLocalDate());
                p.setAdultOnly("Y".equals(rs.getString("IS_ADULT_ONLY")));
                p.setPrice(rs.getBigDecimal("PRICE"));
                list.add(p);
            }
            return list;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Product> findExpired() throws SQLException {
        String sql = "SELECT * FROM PRODUCT WHERE EXPIRY_DATE < TRUNC(SYSDATE)";
        List<Product> list = new ArrayList<>();
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("PRODUCT_ID"));
                p.setName(rs.getString("NAME"));
                p.setManufacturer(rs.getString("MANUFACTURER"));
                p.setExpiryDate(rs.getDate("EXPIRY_DATE").toLocalDate());
                p.setAdultOnly("Y".equals(rs.getString("IS_ADULT_ONLY")));
                p.setPrice(rs.getBigDecimal("PRICE"));
                list.add(p);
            }
            return list;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}
