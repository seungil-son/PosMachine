// src/main/java/com/my/pos/ui/StockScheduler.java
package com.my.pos.ui;

import com.my.pos.model.Product;
import com.my.pos.model.Stock;
import com.my.pos.service.ProductService;
import com.my.pos.service.StockService;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 1~5분마다 랜덤 물품 입고를 자동으로 수행하는 스케줄러
 */
public class StockScheduler {
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private final ProductService prodSvc;
    private final StockService stockSvc;
    private final Random random = new Random();

    public StockScheduler(ProductService prodSvc, StockService stockSvc) {
        this.prodSvc = prodSvc;
        this.stockSvc = stockSvc;
    }

    /** 애플리케이션 시작 시 호출하여 스케줄러를 기동 */
    public void start() {
        scheduleNext();
    }

    /** 다음 입고 작업을 1~5분 뒤에 예약 */
    private void scheduleNext() {
        int delayMin = 1 + random.nextInt(5);  // 1~5
        scheduler.schedule(this::doReceive, delayMin, TimeUnit.MINUTES);
    }

    /** 실제 입고 로직 실행 후, 다시 scheduleNext() */
    private void doReceive() {
        try {
            List<Product> list = prodSvc.findAll();
            if (list.isEmpty()) {
                System.out.println("[자동입고] 등록된 상품 없음, 스케줄러 중지");
                scheduler.shutdown();
                return;
            }

            // 랜덤 상품, 랜덤 수량(5~20)
            Product p = list.get(random.nextInt(list.size()));
            int qty = 5 + random.nextInt(16);

            // 재고 반영
            Stock stk = stockSvc.getStock(p.getProductId());
            if (stk == null) {
                stockSvc.initStock(new Stock(p.getProductId(), qty));
            } else {
                stockSvc.updateStock(p.getProductId(), stk.getQuantity() + qty);
            }

            // 콘솔에 입고 내용 출력
            System.out.printf(
                    "[자동입고] %s(ID=%d) %d개 입고됨. 현재재고=%d%n",
                    p.getName(), p.getProductId(), qty,
                    (stk == null ? qty : stk.getQuantity() + qty)
            );
        } catch (Exception ex) {
            System.out.println("[자동입고] 오류 발생: " + ex.getMessage());
            // 오류가 계속된다면 멈춰도 좋음
            // scheduler.shutdown();
        } finally {
            // 다음 스케줄 예약
            scheduleNext();
        }
    }

    /** 애플리케이션 종료 시 호출하여 스케줄러 정리 */
    public void stop() {
        scheduler.shutdownNow();
    }
}
