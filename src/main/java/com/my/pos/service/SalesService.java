package com.my.pos.service;

import com.my.pos.dao_Interface.*;
import com.my.pos.dao.impl.*;
import com.my.pos.model.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SalesService {
    private final SalesDao    salesDao;
    private final StockDao    stockDao;
    private final CashBoxDao  cashBoxDao;
    private final MsalesDao   msalesDao;
    private final ProductService productService;

    public SalesService() {
        this.salesDao       = new SalesDaoImpl();
        this.stockDao       = new StockDaoImpl();
        this.cashBoxDao     = new CashBoxDaoImpl();
        this.msalesDao      = new MsalesDaoImpl();
        this.productService = new ProductService();
    }

    /**
     * 판매 처리
     *
     * @param empId         사원 ID
     * @param productId     상품 ID
     * @param quantity      판매 수량
     * @param paymentMethod "Cash" or "Card"
     * @param paidAmount    고객이 실제 건넨 금액 (Cash일 때 필수, Card면 null 허용)
     * @return SaleReceipt  (Sale 객체 + 거스름돈 + POS 잔고)
     */
    public SaleReceipt recordSale(String empId,
                                  int productId,
                                  int quantity,
                                  String paymentMethod,
                                  BigDecimal paidAmount)
            throws SQLException {

        // 1) 총 금액 계산
        BigDecimal unitPrice  = productService.findById(productId).getPrice();
        BigDecimal totalAmount= unitPrice.multiply(BigDecimal.valueOf(quantity));

        // 2) Sale 객체 세팅 & DB 삽입
        Sale sale = new Sale();
        sale.setEmpId(empId);
        sale.setProductId(productId);
        sale.setQuantity(quantity);
        sale.setTotalAmount(totalAmount);
        sale.setPaymentMethod(paymentMethod);
        sale.setSaleTime(LocalDateTime.now());
        salesDao.insert(sale);

        // 3) 재고 차감
        Stock stock = stockDao.findByProductId(productId);
        stockDao.updateQuantity(
                productId,
                stock.getQuantity() - quantity
        );

        // 4) POS 금고 업데이트 (현금·카드 구분 없이 매출액 모두 반영)
        CashBox cb    = cashBoxDao.find();
        BigDecimal newPos = cb.getBalance().add(totalAmount);
        cashBoxDao.updateBalance(newPos);

        // 5) 일별 매출 집계
        LocalDate today = LocalDate.now();
        DailySales ds   = msalesDao.findByDate(today);
        if (ds == null) {
            msalesDao.insert(new DailySales(today, totalAmount));
        } else {
            msalesDao.updateTotalSales(
                    today, ds.getTotalSales().add(totalAmount).doubleValue()
            );
        }

        // 6) 거스름돈 계산
        BigDecimal change = BigDecimal.ZERO;
        if ("Cash".equalsIgnoreCase(paymentMethod)) {
            if (paidAmount == null) {
                throw new IllegalArgumentException("현금 결제 시 받은 금액을 입력하세요.");
            }
            change = paidAmount.subtract(totalAmount);
        }

        // 7) 영수증 반환
        return new SaleReceipt(sale, change, newPos);
    }

    // 조회 API
    public Sale getSaleById(int saleId) throws SQLException {
        return salesDao.findById(saleId);
    }
    public List<Sale> getSalesByDate(LocalDate date) throws SQLException {
        return salesDao.findByDate(date);
    }
    public List<Sale> getSalesByEmployee(String empId) throws SQLException {
        return salesDao.findByEmployee(empId);
    }
}
