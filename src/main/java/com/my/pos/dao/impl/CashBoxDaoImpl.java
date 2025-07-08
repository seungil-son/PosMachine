// src/main/java/com/my/pos/dao/impl/CashBoxDaoImpl.java
package com.my.pos.dao.impl;

import com.my.pos.dao_Interface.CashBoxDao;
import com.my.pos.model.CashBox;
import com.my.pos.util.JdbcUtil;

import java.math.BigDecimal;
import java.sql.*;

public class CashBoxDaoImpl implements CashBoxDao {
    @Override
    public CashBox find() throws SQLException {
        String sql = "SELECT * FROM CASHBOX WHERE ID = 1";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new CashBox(
                        rs.getInt("ID"),
                        rs.getBigDecimal("BALANCE")
                );
            }
            return null;
        }
    }

    @Override
    public void updateBalance(BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE CASHBOX SET BALANCE = ? WHERE ID = 1";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newBalance);
            ps.executeUpdate();
        }
    }
}
