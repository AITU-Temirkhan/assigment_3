package Patterns;

/**
 * Factory Pattern: Base class for different delivery order types
 */
public abstract class DeliveryOrder {
    protected String orderId;
    protected String customerName;
    protected String destination;
    protected double price;

    public DeliveryOrder(String orderId, String customerName, String destination, double price) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.destination = destination;
        this.price = price;
    }

    public abstract String getDeliveryType();
    public abstract double calculateDeliveryTime();
    public abstract double calculateCost();

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getDestination() { return destination; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("%s{id='%s', customer='%s', type=%s}",
                this.getClass().getSimpleName(), orderId, customerName, getDeliveryType());
    }
}