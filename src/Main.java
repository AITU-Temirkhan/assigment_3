import Service.OrderService;

public class Main {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();

        try {
            orderService.placeOrder(1, 1, 2);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}