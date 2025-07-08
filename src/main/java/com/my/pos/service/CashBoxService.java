// src/main/java/com/my/pos/service/CashBoxService.java
package com.my.pos.service;

import com.my.pos.dao_Interface.CashBoxDao;
import com.my.pos.dao.impl.CashBoxDaoImpl;
import com.my.pos.model.CashBox;
import java.math.BigDecimal;
import java.sql.SQLException;

public class CashBoxService {
    private final CashBoxDao dao = new CashBoxDaoImpl();

    /** 현재 잔고 조회 */
    public CashBox getCashBox() throws SQLException {
        return dao.find();
    }

    /** 잔고 변화(충전·차감) */
    public BigDecimal changeBalance(BigDecimal delta) throws SQLException {
        CashBox cb = dao.find();
        BigDecimal updated = cb.getBalance().add(delta);
        dao.updateBalance(updated);
        return updated;
    }
}
