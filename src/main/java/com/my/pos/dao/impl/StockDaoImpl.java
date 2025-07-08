package com.my.pos.dao.impl;

import com.my.pos.dao_Interface.StockDao;
import com.my.pos.model.Stock;
import com.my.pos.util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StockDaoImpl implements StockDao {

    @Override
    public Stock findByProductId(int productId) throws SQLException {
        String sql = "SELECT PRODUCT_ID, QUANTITY FROM STOCK WHERE PRODUCT_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Stock(
                            rs.getInt("PRODUCT_ID"),
                            rs.getInt("QUANTITY")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Stock> findAll() throws SQLException {
        String sql = "SELECT PRODUCT_ID, QUANTITY FROM STOCK";
        List<Stock> list = new ArrayList<>();

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(new Stock(
                        rs.getInt("PRODUCT_ID"),
                        rs.getInt("QUANTITY")
                ));
            }
        }
        return list;
    }

    @Override
       public int insert(Stock stock) throws SQLException {
        String sql = "INSERT INTO STOCK (PRODUCT_ID, QUANTITY) VALUES (?, ?)";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, stock.getProductId());
            pstmt.setInt(2, stock.getQuantity());
            pstmt.executeUpdate();
        }
        return 0;
    }

    // ← 여기 메서드 시그니처가 인터페이스와 정확히 일치해야 합니다.
    @Override
    public int updateQuantity(int productId, int newQty) throws SQLException {
        String sql = "UPDATE STOCK SET QUANTITY = ? WHERE PRODUCT_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQty);
            pstmt.setInt(2, productId);
            return pstmt.executeUpdate();
        }
    }

    @Override
    public int delete(int productId) throws SQLException {
        String sql = "DELETE FROM STOCK WHERE PRODUCT_ID = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);
            return pstmt.executeUpdate();
        }
    }
}
