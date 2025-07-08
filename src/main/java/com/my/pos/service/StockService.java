package com.my.pos.service;

import com.my.pos.dao_Interface.StockDao;
import com.my.pos.dao.impl.StockDaoImpl;
import com.my.pos.model.Stock;

import java.sql.SQLException;
import java.util.List;

public class StockService {
    private final StockDao dao = new StockDaoImpl();

    public void initStock(Stock s) throws SQLException {
        dao.insert(s);
    }

    public Stock getStock(int productId) throws SQLException {
        return dao.findByProductId(productId);
    }

    public void updateStock(int productId, int newQuantity) throws SQLException {
        dao.updateQuantity(productId, newQuantity);
    }

    public List<Stock> findAll() throws SQLException {
        return dao.findAll();
    }
    public void deleteStock(int productId) throws SQLException {
        dao.delete(productId);
    }
}
