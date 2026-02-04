package Patterns;

public class DeliveryOrderImpl extends DeliveryOrder {
    private static final double DELIVERY_TIME_MINUTES = 45;
    private double deliveryCost;

    public DeliveryOrderImpl(String orderId, String customerName, String destination, double price, double deliveryCost) {
        super(orderId, customerName, destination, price);
        this.deliveryCost = deliveryCost;
    }

    @Override
    public String getDeliveryType() { return "DELIVERY"; }

    @Override
    public double calculateDeliveryTime() { return DELIVERY_TIME_MINUTES; }

    @Override
    public double calculateCost() { return deliveryCost; }
}