package Patterns;

import Models.Order;
import Models.OrderItem;
import Models.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder Pattern: OrderBuilder
 * Создает сложные Order объекты пошагово
 */
public class OrderBuilder {
    private int id;
    private int customerId;
    private List<OrderItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.PENDING;
    private double totalPrice = 0.0;
    private LocalDateTime createdAt = LocalDateTime.now();
    private String deliveryOption = "DELIVERY";

    public OrderBuilder(int id, int customerId) {
        this.id = id;
        this.customerId = customerId;
    }

    public OrderBuilder addItem(OrderItem item) {
        this.items.add(item);
        this.totalPrice += item.getTotalPrice();
        return this;
    }

    public OrderBuilder addItems(List<OrderItem> items) {
        this.items.addAll(items);
        items.forEach(item -> this.totalPrice += item.getTotalPrice());
        return this;
    }

    public OrderBuilder withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderBuilder withDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
        return this;
    }

    public OrderBuilder applyPricing() {
        PricingRules pricing = PricingRules.getInstance();
        double finalPrice = pricing.calculateFinalPrice(deliveryOption, totalPrice);
        this.totalPrice = finalPrice;
        return this;
    }

    public Order build() {
        if (items.isEmpty()) {
            throw new IllegalStateException("Order must have at least one item");
        }
        if (customerId <= 0) {
            throw new IllegalStateException("Customer ID must be valid");
        }

        return new Order(id, customerId, items, status, totalPrice, createdAt, deliveryOption);
    }

    @Override
    public String toString() {
        return String.format("OrderBuilder{id=%d, customer=%d, items=%d, delivery=%s}",
                id, customerId, items.size(), deliveryOption);
    }
}