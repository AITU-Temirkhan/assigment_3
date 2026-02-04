package Service;

import Database.DatabaseConnection;
import Interfaces.IRepository;
import Interfaces.MenuItemFilter;
import Interfaces.OrderCallback;
import Models.*;
import Patterns.*;
import Repositories.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderService {
    private final IRepository<MenuItem, Integer> menuRepository;
    private final IRepository<Order, Integer> orderRepository;
    private final IRepository<Customer, Integer> customerRepository;
    private final PricingRules pricingRules;
    private int nextOrderId = 1;

    public OrderService() {
        this.menuRepository = new MenuRepository();
        this.orderRepository = new OrderRepository();
        this.customerRepository = new CustomerRepository();
        this.pricingRules = PricingRules.getInstance();
    }

    public List<MenuItem> getAvailableMenuItems() throws Exception {
        return ((MenuRepository) menuRepository).findAvailableItems();
    }

    public List<MenuItem> searchMenuItemsByCategory(String category) throws Exception {
        List<MenuItem> items = menuRepository.findAll();
        return items.stream()
                .filter(item -> item.getCategory() != null && item.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<MenuItem> searchMenuItems(MenuItemFilter filter) throws Exception {
        List<MenuItem> items = menuRepository.findAll();
        return items.stream()
                .filter(filter::test)
                .collect(Collectors.toList());
    }

    public Order createOrderWithBuilder(int customerId, List<OrderItem> items, String deliveryOption) throws Exception {
        OrderBuilder builder = new OrderBuilder(nextOrderId++, customerId);

        builder.addItems(items)
                .withDeliveryOption(deliveryOption)
                .withStatus(OrderStatus.PENDING)
                .applyPricing();

        return builder.build();
    }

    public Order placeOrder(int customerId, List<OrderItem> items, String deliveryOption,
                            OrderCallback callback) throws Exception {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }

        for (OrderItem item : items) {
            Optional<MenuItem> menuItem = menuRepository.findById(item.getMenuItemId());
            if (!menuItem.isPresent() || !menuItem.get().isAvailable()) {
                throw new IllegalArgumentException("Menu item not available");
            }
        }

        Order order = createOrderWithBuilder(customerId, items, deliveryOption);
        orderRepository.save(order);

        // Lambda Callback
        if (callback != null) {
            callback.onSuccess("Order #" + order.getId() + " placed successfully!");
        }

        return order;
    }

    public List<Order> getCustomerOrders(int customerId) throws Exception {
        return ((OrderRepository) orderRepository).findByCustomerId(customerId).stream()
                .sorted((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    public List<Order> getActiveOrders() throws Exception {
        return ((OrderRepository) orderRepository).findActiveOrders();
    }

    public void completeOrder(int orderId) throws Exception {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order o = order.get();
            o.setStatus(OrderStatus.COMPLETED);
            orderRepository.update(o);
        }
    }

    public DeliveryOrder createDeliveryOrder(int orderId, String deliveryType,
                                             int customerId, double totalPrice) throws Exception {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new IllegalArgumentException("Customer not found");
        }

        Customer c = customer.get();
        return DeliveryOrderFactory.createOrder(
                deliveryType,
                "ORD-" + orderId,
                c.getName(),
                c.getEmail(),
                totalPrice
        );
    }

    public PricingRules getPricingRules() {
        return pricingRules;
    }

    public void printOrderSummary() throws Exception {
        List<Order> allOrders = orderRepository.findAll();
        System.out.println("\n=== Order Summary ===");
        System.out.println("Total Orders: " + allOrders.size());

        double totalRevenue = allOrders.stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));

        long activeCount = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.ACTIVE)
                .count();
        System.out.println("Active Orders: " + activeCount);
    }
}