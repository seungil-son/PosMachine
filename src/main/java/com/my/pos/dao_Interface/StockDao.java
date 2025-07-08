// src/main/java/com/my/pos/dao_Interface/StockDao.java
package com.my.pos.dao_Interface;

import com.my.pos.model.Stock;
import java.sql.SQLException;
import java.util.List;

public interface StockDao {
    int insert(Stock stock) throws SQLException;                               // 새로운 재고 레코드를 STOCK 테이블에 추가

    Stock findByProductId(int productId) throws SQLException;                   // 주어진 상품 고유 번호(productId)에 해당하는 재고 정보를 조회해 Stock 객체로 반환

    int updateQuantity(int productId, int newQty) throws SQLException;    // 특정 상품의 재고 수량을 newQuantity 값으로 갱신

    List<Stock> findAll() throws SQLException;
    int delete(int productId) throws SQLException;

}
