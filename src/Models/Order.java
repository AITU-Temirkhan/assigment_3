package Models;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private int id;
    private int customerId;
    private List<OrderItem> items;
    private OrderStatus status;
    private double totalPrice;
    private LocalDateTime createdAt;
    private String deliveryOption;

    public Order(int id, int customerId, OrderStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order(int id, int customerId, List<OrderItem> items, OrderStatus status,
                 double totalPrice, LocalDateTime createdAt, String deliveryOption) {
        this.id = id;
        this.customerId = customerId;
        this.items = items;
        this.status = status;
        this.totalPrice = totalPrice;
        this.createdAt = createdAt;
        this.deliveryOption = deliveryOption;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getDeliveryOption() { return deliveryOption; }

    public void setStatus(OrderStatus status) { this.status = status; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return String.format("Order{id=%d, customerId=%d, status=%s, total=%.2f}",
                id, customerId, status, totalPrice);
    }
}