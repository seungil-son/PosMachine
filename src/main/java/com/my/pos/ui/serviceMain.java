//[1/3]
// src/main/java/com/my/pos/ui/serviceMain.java
package com.my.pos.ui;
import com.my.pos.ui.StockScheduler;

import com.my.pos.model.*;
import com.my.pos.service.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class serviceMain {
    private static final Scanner sc = new Scanner(System.in);
    private static final EmployeeService empSvc = new EmployeeService();
    private static final ProductService prodSvc = new ProductService();
    private static final StockService stockSvc = new StockService();

    private static final MsalesService msalesSvc = new MsalesService();
    private static Employee currentEmp = null;
    private static final CashBoxService cashSvc = new CashBoxService();
    private static final SalesService saleSvc = new SalesService();

    private static Product findByName(String name) throws Exception {
        for (Product p : prodSvc.findAll()) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    private static final Customer customer =
            new Customer("C001",
                    BigDecimal.valueOf(100_000),
                    BigDecimal.valueOf(100_000));

    public static void main(String[] args) {
        System.out.println("=== POS 시스템 시작 ===");

        // 1) 시작하자마자 로그인
        while (currentEmp == null) {
            loginPrompt();
        }
        StockScheduler stockScheduler = new StockScheduler(prodSvc, stockSvc);
        stockScheduler.start();

        boolean running = true;
        while (running) {
            showMenu();
            switch (sc.nextLine().trim()) {
                case "1":
                    empMenu();
                    break;
                case "2":
                    prodMenu();
                    break;
                case "3":
                    stockMenu();
                    break;
                case "4":
                    dailySalesMenu();
                    break;
                case "5":
                    try {
                        CashBox cb = cashSvc.getCashBox();
                        System.out.printf("현재 POS 잔고: %,d원%n",
                                cb.getBalance().longValue());
                    } catch (Exception e) {
                        System.out.println("잔고 조회 오류: " + e.getMessage());
                    }
                    break;
                case "6":
                    simulateCustomerPurchase();
                    break;
                case "0":
                    logout();
                    break;
                default:
                    System.out.println("잘못된 선택입니다.");
            }
        }
        stockScheduler.stop();
        sc.close();
    }

    private static final DateTimeFormatter LOGIN_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 로그인 프롬프트
     */
    private static void loginPrompt() {
        System.out.println("\n-- 로그인 --");
        System.out.print("ID> ");
        String id = sc.nextLine().trim();
        System.out.print("PW> ");
        String pw = sc.nextLine().trim();
        try {
            currentEmp = empSvc.login(id, pw);
            String when = currentEmp.getLoginTime().format(LOGIN_FMT);
            System.out.printf("%s님 환영합니다! (로그인 시각: %s)%n",
                    currentEmp.getName(), when);
        } catch (Exception ex) {
            System.out.println("로그인 실패: " + ex.getMessage());
        }
    }


    private static final BigDecimal WAGE_PER_MINUTE = BigDecimal.valueOf(11_000);

    private static void logout() {
        try {
            // 근무 분 반환
            int minutesWorked = empSvc.logout(currentEmp.getEmpId());

            // 분당 시급 곱셈으로 일당 계산
            BigDecimal salary = WAGE_PER_MINUTE
                    .multiply(BigDecimal.valueOf(minutesWorked));

            // 결과 출력
            System.out.printf(
                    "근무시간: %d분, 오늘 일당: %,d원%n",
                    minutesWorked,
                    salary.longValue()
            );

            // 종료 인사 후 프로그램 종료
            System.out.println(currentEmp.getName() + "님 수고하셨습니다. 프로그램을 종료합니다.");
            System.exit(0);

        } catch (Exception ex) {
            System.out.println("로그아웃 오류: " + ex.getMessage());
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("1. 사원 정보");
        System.out.println("2. 상품 관리");
        System.out.println("3. 재고 관리");
        System.out.println("4. 매출 조회");
        System.out.println("5. 현재 잔고 조회");
        System.out.println("6. 고객 구매 시뮬");
        System.out.println("0. 로그아웃");
        System.out.print("선택> ");
    }

    private static void empMenu() {
        // 1) 로그인 시각 표시
        LocalDateTime loginTime = currentEmp.getLoginTime();
        String when = loginTime.format(LOGIN_FMT);
        System.out.printf("%s님 (로그인 시각: %s)%n", currentEmp.getName(), when);

        // 2) 로그인부터 지금까지 경과한 분 계산
        LocalDateTime now = LocalDateTime.now();
        long minutesWorked = Duration.between(loginTime, now).toMinutes();

        // 3) 현재 급여 계산
        BigDecimal currentSalary = WAGE_PER_MINUTE.multiply(
                BigDecimal.valueOf(minutesWorked)
        );

        // 4) 포맷팅해서 출력
        System.out.printf("현재 근무 시간: %d분%n", minutesWorked);
        System.out.printf("현재 급여: %,d원%n", currentSalary.longValue());
    }

//[2/3]
    // 2. 상품 관리
    private static void prodMenu() {
        System.out.println("\n-- 상품 관리 --");
        System.out.println("1) 등록  2) 수정  3) 삭제  4) 찾기  5) 전체 조회  6) 유통기한 지난 상품  0) 뒤로");
        System.out.print("선택> ");
        try {
            switch (sc.nextLine().trim()) {
                case "1":
                    // 1-1) 상품 기본 정보 입력
                    System.out.print("이름> ");
                    String name = sc.nextLine().trim();
                    System.out.print("제조사> ");
                    String manu = sc.nextLine().trim();
                    System.out.print("만료일(YYYY-MM-DD)> ");
                    LocalDate exp = LocalDate.parse(sc.nextLine().trim());
                    System.out.print("성인전용(Y/N)> ");
                    boolean adult = sc.nextLine().equalsIgnoreCase("Y");
                    System.out.print("가격> ");
                    BigDecimal price = new BigDecimal(sc.nextLine().trim());
                    // 1-2) 재고 수량 입력
                    System.out.print("초기 재고 수량> ");
                    int initQty = Integer.parseInt(sc.nextLine().trim());
                    // 1-3) 상품 등록
                    var p = new Product(name, manu, exp, adult, price);
                    prodSvc.addProduct(p);
                    int newPid = p.getProductId();


                    System.out.printf(
                            "등록ID=%d, 초기재고=%d%n",
                            newPid, initQty
                    );
                    break;
                case "2":
                    // 2) 수정 (제품명으로 찾기)
                    System.out.print("수정할 제품명> ");
                    String toModify = sc.nextLine().trim();

                    Product exist = findByName(toModify);
                    if (exist == null) {
                        System.out.println("해당 이름의 제품을 찾을 수 없습니다.");
                        break;
                    }
                    System.out.println("현재 정보: " + exist);

                    // ◼︎ 제품 정보 수정
                    System.out.print("새 이름(" + exist.getName() + ")> ");
                    String mName = sc.nextLine().trim();
                    if (!mName.isEmpty()) exist.setName(mName);

                    System.out.print("새 제조사(" + exist.getManufacturer() + ")> ");
                    String mManu = sc.nextLine().trim();
                    if (!mManu.isEmpty()) exist.setManufacturer(mManu);

                    System.out.print("새 만료일(" + exist.getExpiryDate() + ")> ");
                    String sExp = sc.nextLine().trim();
                    if (!sExp.isEmpty()) exist.setExpiryDate(LocalDate.parse(sExp));

                    System.out.print("성인전용 여부(" + (exist.isAdultOnly() ? "Y" : "N") + ")> ");
                    String sAdult = sc.nextLine().trim();
                    if (!sAdult.isEmpty())
                        exist.setAdultOnly(sAdult.equalsIgnoreCase("Y"));

                    System.out.print("새 가격(" + exist.getPrice() + ")> ");
                    String sPrice = sc.nextLine().trim();
                    if (!sPrice.isEmpty())
                        exist.setPrice(new BigDecimal(sPrice));

                    // ◼︎ 재고 정보 수정
                    Stock currentStock = stockSvc.getStock(exist.getProductId());
                    int currQty = (currentStock != null ? currentStock.getQuantity() : 0);
                    System.out.print("새 재고 수량(" + currQty + ")> ");
                    String sQty = sc.nextLine().trim();
                    if (!sQty.isEmpty()) {
                        int newQty = Integer.parseInt(sQty);
                        if (currentStock == null) {
                            stockSvc.initStock(new Stock(exist.getProductId(), newQty));
                        } else {
                            stockSvc.updateStock(exist.getProductId(), newQty);
                        }
                        System.out.println("재고 정보가 업데이트 되었습니다.");
                    }

                    // DB 반영
                    prodSvc.updateProduct(exist);
                    System.out.println("상품 정보가 수정되었습니다.");
                    break;

                case "3":
                    System.out.print("삭제할 제품명> ");
                    String toDelete = sc.nextLine().trim();

                    Product delP = findByName(toDelete);
                    if (delP == null) {
                        System.out.println("해당 이름의 제품을 찾을 수 없습니다.");
                        break;
                    }

                    int pid = delP.getProductId();
                    try {
                        // 1) 재고가 존재하면 먼저 삭제
                        Stock stk = stockSvc.getStock(pid);
                        if (stk != null) {
                            stockSvc.deleteStock(pid);
                            System.out.println("→ 연관된 재고 정보가 삭제되었습니다.");
                        }

                        // 2) 상품 삭제
                        prodSvc.removeProduct(pid);
                        System.out.printf(" 삭제 완료: ID=%d, 이름=%s%n", pid, delP.getName());

                    } catch (SQLIntegrityConstraintViolationException fkEx) {
                        // 혹시 다른 자식 테이블 제약이 있을 때
                        System.out.println(" 삭제 실패: 자식 레코드가 존재합니다. 연관 데이터를 먼저 삭제하세요.");
                    } catch (Exception ex) {
                        System.out.println("오류 발생: " + ex.getMessage());
                    }
                    break;
                case "4":
                    // 4) 찾기
                    productSearchMenu();
                    break;

                case "5":
                    System.out.println("\n-- 전체 상품 조회 --");
                    // 헤더: 제품명, 가격, 제조사, 19금, 재고, 유통기한
                    System.out.printf("%-20s %-10s %-15s %-6s %-6s %s%n",
                            "제품명", "가격", "제조사", "19금", "재고", "유통기한");
                    System.out.println("--------------------------------------------------------------------------");

                    try {
                        List<Product> products = prodSvc.findAll();
                        for (Product p2 : products) {
                            // 재고 조회
                            Stock stk = stockSvc.getStock(p2.getProductId());
                            int qty = (stk != null ? stk.getQuantity() : 0);

                            // 각 상품 출력 (재고 컬럼 추가)
                            System.out.printf("%-20s %-10s %-15s %-6s %-6d %s%n",
                                    p2.getName(),
                                    p2.getPrice(),
                                    p2.getManufacturer(),
                                    p2.isAdultOnly() ? "Y" : "N",
                                    qty,
                                    p2.getExpiryDate()
                            );
                        }
                    } catch (SQLException e) {
                        System.out.println("조회 오류: " + e.getMessage());
                    }
                    break;


                case "6":
                    // 6) 유통기한 지난 상품
                    prodSvc.findExpired()
                            .forEach(System.out::println);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("잘못된 선택");
            }
        } catch (Exception ex) {
            System.out.println("오류: " + ex.getMessage());
        }
    }

//[3/3]
    // 3. 재고 관리
    private static void stockMenu() {
        System.out.println("\n-- 재고 관리 --");
        System.out.println("1) 조회  2) 수정  3) 전체조회  0) 뒤로");
        System.out.print("선택> ");
        try {
            switch (sc.nextLine().trim()) {
                case "1":
                    // 조회할 때 '*'로 표시
                    System.out.print("상품ID> ");
                    int pid2 = Integer.parseInt(sc.nextLine().trim());
                    Stock stock = stockSvc.getStock(pid2);
                    if (stock == null) {
                        System.out.println("재고 정보가 없습니다.");
                    } else {
                        int qty2 = stock.getQuantity();
                        // qty만큼 '*' 반복. 너무 길면 max 50자로 자를 수도 있습니다.
                        String stars = "*".repeat(Math.min(qty2, 50));
                        if (qty2 > 50) {
                            stars += "...";  // 50개 초과일 땐 생략부호
                        }
                        System.out.printf("재고 수량 [%d]: %s%n", qty2, stars);
                    }
                    break;
                case "2":
                    System.out.print("상품ID> ");
                    int pid = Integer.parseInt(sc.nextLine());
                    System.out.print("새 수량> ");
                    int qty = Integer.parseInt(sc.nextLine());
                    stockSvc.updateStock(pid, qty);
                    System.out.println("수정 완료");
                    break;
                case "3":
                    stockSvc.findAll().forEach(System.out::println);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("잘못된 선택");
            }
        } catch (Exception ex) {
            System.out.println("오류: " + ex.getMessage());
        }
    }


    private static boolean isAdult(String rrn) {
        try {
            // 1) 생년월일 파싱
            int yy = Integer.parseInt(rrn.substring(0, 2));
            int mm = Integer.parseInt(rrn.substring(2, 4));
            int dd = Integer.parseInt(rrn.substring(4, 6));

            // 2) 세기 결정
            char code = rrn.charAt(7);
            int century;
            if (code == '1' || code == '2') {
                century = 1900;
            } else if (code == '3' || code == '4') {
                century = 2000;
            } else {
                // 그 외 (외국인 등)는 성인 확인 불가 처리
                return false;
            }

            LocalDate birthDate = LocalDate.of(century + yy, mm, dd);
            LocalDate today = LocalDate.now();

            // 3) 나이 계산
            int age = Period.between(birthDate, today).getYears();
            return age >= 19;

        } catch (Exception e) {
            // 포맷 파싱 실패 시 성인 확인 불가
            return false;
        }
    }


    // 5. 일별 매출 조회
    private static void dailySalesMenu() {
        while (true) {
            System.out.println("\n-- 매출 조회 --");
            System.out.println("1) 특정 날짜 조회");
            System.out.println("2) 전체 날짜 조회");
            System.out.println("0) 뒤로");
            System.out.print("선택> ");

            String sel = sc.nextLine().trim();
            try {
                switch (sel) {
                    case "1":
                        System.out.print("조회할 날짜(YYYY-MM-DD)> ");
                        LocalDate date = LocalDate.parse(sc.nextLine().trim());
                        DailySales ds = msalesSvc.findByDate(date);
                        if (ds == null) {
                            System.out.println("해당 일자에 매출 기록이 없습니다.");
                        } else {
                            System.out.printf("판매일: %s, 매출: %s%n",
                                    ds.getSaleDate(), ds.getTotalSales());
                        }
                        break;

                    case "2":
                        List<DailySales> list = msalesSvc.findAll();
                        if (list.isEmpty()) {
                            System.out.println("매출 기록이 없습니다.");
                        } else {
                            System.out.printf("%-12s %s%n", "판매일", "매출");
                            System.out.println("------------------------");
                            for (DailySales d : list) {
                                System.out.printf("%-12s %s%n",
                                        d.getSaleDate(),
                                        d.getTotalSales());
                            }
                        }
                        break;

                    case "0":
                        return;

                    default:
                        System.out.println("잘못된 선택입니다.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("날짜 형식이 올바르지 않습니다.");
            } catch (SQLException e) {
                System.out.println("조회 오류: " + e.getMessage());
            }
        }
    }

    private static void productSearchMenu() {
        System.out.println("\n-- 제품 찾기 --");
        System.out.print("검색어> ");
        String keyword = sc.nextLine().trim();

        try {
            // 1) 전체 상품 조회
            List<Product> all = prodSvc.findAll();
            List<Product> matched = new ArrayList<>();

            // 2) 이름 기준 필터링
            for (Product p : all) {
                if (p.getName().contains(keyword)) {
                    matched.add(p);
                }
            }

            if (matched.isEmpty()) {
                System.out.println("검색 결과가 없습니다.");
                return;
            }

            // 3) 헤더 출력 (제품명, 가격, 제조사, 수량, 유통기한)
            System.out.printf("%-20s %-10s %-15s %-6s %s%n",
                    "제품명", "가격", "제조사", "수량", "유통기한");
            System.out.println("----------------------------------------------------------------------");

            // 4) 결과 출력
            for (Product p : matched) {
                Stock stk = stockSvc.getStock(p.getProductId());
                int qty = (stk == null ? 0 : stk.getQuantity());

                System.out.printf("%-20s %-10s %-15s     %-6d  %s%n",
                        p.getName(),
                        p.getPrice(),
                        p.getManufacturer(),
                        qty,
                        p.getExpiryDate()
                );
            }

        } catch (Exception ex) {
            System.out.println("검색 중 오류 발생: " + ex.getMessage());
        }
    }

    //7. 고객 구매
    private static void simulateCustomerPurchase() {
        System.out.println("\n-- 고객 구매 시뮬레이션 --");
        try {
            // 1) 재고가 있는 상품 리스트
            List<Product> all = prodSvc.findAll();
            List<Product> available = new ArrayList<>();
            for (Product p : all) {
                Stock s = stockSvc.getStock(p.getProductId());
                if (s != null && s.getQuantity() > 0) {
                    available.add(p);
                }
            }
            if (available.isEmpty()) {
                System.out.println(" 재고가 있는 상품이 없습니다.");
                return;
            }

            // 2) 랜덤 상품 선택
            Random rand = new Random();
            Product p = available.get(rand.nextInt(available.size()));
            Stock s = stockSvc.getStock(p.getProductId());
            BigDecimal price = p.getPrice();
            System.out.printf("고객이 [%s] (ID=%d, 가격=%s원)을 선택했습니다.%n",
                    p.getName(), p.getProductId(), price);
            LocalDate today = LocalDate.now();

            if (p.getExpiryDate().isBefore(today)) {
                System.out.printf(" 이 상품은 %s에 만료되었습니다. 구매할 수 없습니다.%n",
                        p.getExpiryDate());
                return;
            }
            // 3) 19금 상품일 경우 성인 인증
            if (p.isAdultOnly()) {
                System.out.print("주민등록번호(######-#######)> ");
                String rrn = sc.nextLine().trim();
                if (!isAdult(rrn)) {
                    System.out.println(" 성인이 아닙니다. 구매할 수 없습니다.");
                    return;
                }
            }

            // 4) 결제 수단 랜덤
            boolean payByCard = rand.nextBoolean();
            if (payByCard) {
                System.out.println("결제수단: 카드");
                BigDecimal cardBal = customer.getCardBalance();
                if (cardBal.compareTo(price) < 0) {
                    System.out.println(" 카드 잔액 부족. 구매 실패.");
                    return;
                }
                customer.setCardBalance(cardBal.subtract(price));
                System.out.printf(" 남은 카드잔액: %,d원%n",
                        customer.getCardBalance().longValue());

            } else {
                System.out.println("결제수단: 현금");
                BigDecimal cashBal = customer.getCashBalance();
                if (cashBal.compareTo(price) < 0) {
                    System.out.println(" 현금 잔액 부족. 구매 실패.");
                    return;
                }
                customer.setCashBalance(cashBal.subtract(price));
                System.out.printf(" 남은 현금잔액: %,d원%n", customer.getCashBalance().longValue());
            }

            // 5) 재고 차감
            int newQty = s.getQuantity() - 1;
            stockSvc.updateStock(p.getProductId(), newQty);
            System.out.printf(" 남은 재고: %d개%n", newQty);

        } catch (Exception ex) {
            System.out.println("시뮬레이션 오류: " + ex.getMessage());
        }
    }
}
