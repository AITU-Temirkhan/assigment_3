package Patterns;

public class PickupOrder extends DeliveryOrder {
    private static final double PICKUP_TIME_MINUTES = 20;
    private static final double PICKUP_COST = 0.0;

    public PickupOrder(String orderId, String customerName, String destination, double price) {
        super(orderId, customerName, destination, price);
    }

    @Override
    public String getDeliveryType() { return "PICKUP"; }

    @Override
    public double calculateDeliveryTime() { return PICKUP_TIME_MINUTES; }

    @Override
    public double calculateCost() { return PICKUP_COST; }
}