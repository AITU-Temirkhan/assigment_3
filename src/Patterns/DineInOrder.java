package Patterns;

public class DineInOrder extends DeliveryOrder {
    private static final double DINE_IN_TIME_MINUTES = 30;
    private String tableNumber;

    public DineInOrder(String orderId, String customerName, String destination, double price, String tableNumber) {
        super(orderId, customerName, destination, price);
        this.tableNumber = tableNumber;
    }

    @Override
    public String getDeliveryType() { return "DINE_IN"; }

    @Override
    public double calculateDeliveryTime() { return DINE_IN_TIME_MINUTES; }

    @Override
    public double calculateCost() { return 0.0; }

    public String getTableNumber() { return tableNumber; }
}