package com.my.pos.service;

import com.my.pos.dao_Interface.ProductDao;
import com.my.pos.dao.impl.ProductDaoImpl;
import com.my.pos.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDao dao = new ProductDaoImpl();

    public Product addProduct(Product p) throws SQLException {
        dao.insert(p);
        return p;
    }

    public void updateProduct(Product p) throws SQLException {
        dao.update(p);
    }

    public void removeProduct(int productId) throws SQLException {
        dao.delete(productId);
    }

    public Product findById(int productId) throws SQLException {
        return dao.findById(productId);
    }

    public List<Product> findAll() throws SQLException {
        return dao.findAll();
    }

    public List<Product> findExpired() throws SQLException {
        return dao.findExpired();
    }

}
