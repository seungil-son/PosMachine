// src/main/java/com/my/pos/dao_Interface/CashBoxDao.java
package com.my.pos.dao_Interface;

import com.my.pos.model.CashBox;
import java.math.BigDecimal;
import java.sql.SQLException;

public interface CashBoxDao {
    CashBox find() throws SQLException;                     // 항상 ID=1 조회
    void updateBalance(BigDecimal newBalance) throws SQLException;
}
