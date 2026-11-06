package ArrayList;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class demo5 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("===== 多线程抢票模拟系统 =====");
            int totalTickets = readPositiveInt(sc, "请输入总票数");
            int requestCount = readPositiveInt(sc, "请输入购票请求数量：");
            List<PurchaseRequest> requests = readRequests(sc, requestCount);
            TicketOffice ticketOffice = new TicketOffice(totalTickets);
            List<PurchaseResult> purchaseResults = executeRequests(ticketOffice, requests);
        }
    }
    private static List<PurchaseResult> executeRequests(TicketOffice ticketOffice,List<PurchaseRequest> requests){
        int threadCount = Math.min(4, requests.size());
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startGate = new CountDownLatch(1);
        ArrayList<Future<PurchaseResult>> futures = new ArrayList<>();
        for (PurchaseRequest request : requests) {
            Future<PurchaseResult> future = executor.submit(() -> {
                startGate.await();
                return ticketOffice.purchase(request);
            });
        }
    }
    private  static List<PurchaseRequest> readRequests(Scanner scanner,int requestCount){
        ArrayList<PurchaseRequest> request = new ArrayList<>();
        for (int i = 1; i <= requestCount; i++) {
            System.out.println("\n--- 第 " + i + " 个请求 ---");
            String customerName = readText(scanner, "请输入客户的姓名");
            int quantity = readPositiveInt(scanner,"请输入购买数量:");
            request.add(new PurchaseRequest(i,customerName,quantity));
        }
        return request;
    }
    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) return value;
            }catch (NumberFormatException e){
            }
            System.out.println("请输入大于 0 的整数。");
        }
    }
    private static String readText(Scanner scanner, String prompt) {
        while (true){
            System.out.println(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("姓名不能为空");
        }
    }
    static class PurchaseRequest{
        /// 请求id
        private final int requestId;
        /// 客户名
        private final String customerName;
        /// 购买数量
        private final int quantity;

        public PurchaseRequest(int requestId, String customerName, int quantity) {
            this.requestId = requestId;
            this.customerName = customerName;
            this.quantity = quantity;
        }

        public int getRequestId() {
            return requestId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public int getQuantity() {
            return quantity;
        }
    }
    static class TicketOffice{
        ///初始票
        private final int initialTickets;
        ///剩余票
        private int remainingTickets;
        /// 已售票
        private int soldTickets;

        public int getInitialTickets() {
            return initialTickets;
        }

        public int getRemainingTickets() {
            return remainingTickets;
        }

        public int getSoldTickets() {
            return soldTickets;
        }

        public TicketOffice(int initialTickets) {
            this.initialTickets = initialTickets;
            this.remainingTickets = initialTickets;
        }
        public synchronized PurchaseResult purchase(PurchaseRequest request){
            String threadName = Thread.currentThread().getName();
            //防止单人购买票数超过总票数
            if (request.getQuantity() > remainingTickets){
                return new PurchaseResult(request,false,"余票不足，当前余票：" + remainingTickets,threadName);
            }
            remainingTickets -= request.getQuantity();
            soldTickets += request.getQuantity();
            return new PurchaseResult(request,true,"购买后余票" + remainingTickets,threadName);
        }
    }
    /// 返回结果
    static class PurchaseResult{
        /// 请求
        private final PurchaseRequest request;
        /// 返回结果
        private final boolean success;
        private final String message;
        private final String threadName;

        public PurchaseResult(PurchaseRequest request, boolean success, String message, String threadName) {
            this.request = request;
            this.success = success;
            this.message = message;
            this.threadName = threadName;
        }

        public boolean isSuccess() {
            return success;
        }

        @Override
        public String toString() {
            return String.format("请求%d | 客户：%s | 购买：%d张 | %s | %s | 线程：%s",
                    request.getRequestId(), request.getCustomerName(), request.getQuantity(),
                    success ? "成功" : "失败", message, threadName);
        }

    }

}
