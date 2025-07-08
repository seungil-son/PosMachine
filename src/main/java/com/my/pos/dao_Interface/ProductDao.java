// src/main/java/com/my/pos/dao_Interface/ProductDao.java
package com.my.pos.dao_Interface;

import com.my.pos.model.Product;
import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    void insert(Product product) throws SQLException;    // 새 상품 정보를 PRODUCT 테이블에 추가

    void update(Product product) throws SQLException;    // 기존 상품 정보(이름·제조사·유통기한·가격·19금 여부 등)를 갱신

    void delete(int productId) throws SQLException;      // 주어진 상품 고유 번호(productId)에 해당하는 레코드를 삭제

    Product findById(int productId) throws SQLException; // 특정 상품 고유 번호에 해당하는 상품 정보를 조회해 Product 객체로 반환

    List<Product> findAll() throws SQLException;         // PRODUCT 테이블에 저장된 모든 상품 정보를 리스트로 반환

    List<Product> findExpired() throws SQLException;     // 현재 날짜를 기준으로 유통기한이 지난 상품들을 조회해 리스트로 반환

}
