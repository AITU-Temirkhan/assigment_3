import Models.*;
import Interfaces.MenuItemFilter;
import Patterns.*;
import Service.MenuService;
import Service.OrderService;
import Repositories.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════════════════════════════╗");
            System.out.println("║     Food Delivery System - OOP Project Milestone 2             ║");
            System.out.println("║     JDBC Version + Design Patterns                             ║");
            System.out.println("╚════════════════════════════════════════════════════════════════╝");

            MenuService menuService = new MenuService();
            OrderService orderService = new OrderService();

            // 1. SINGLETON PATTERN
            System.out.println("\n[1] SINGLETON PATTERN - PricingRules");
            System.out.println("═══════════════════════════════════════════════════");
            PricingRules pricing = PricingRules.getInstance();
            System.out.println("Current Pricing: " + pricing);
            System.out.println("Delivery charge: $" + pricing.getDeliveryCharge());

            PricingRules pricing2 = PricingRules.getInstance();
            System.out.println("Same instance? " + (pricing == pricing2) + " (Singleton verified)");

            // 2. GENERICS + JDBC
            System.out.println("\n[2] GENERICS - Generic Repository<T, ID> + JDBC");
            System.out.println("═══════════════════════════════════════════════════");
            menuService.printMenu();

            // 3. LAMBDAS
            System.out.println("\n[3] LAMBDAS & FUNCTIONAL INTERFACES");
            System.out.println("═══════════════════════════════════════════════════");

            System.out.println("\nAvailable items (Lambda: MenuItem::isAvailable):");
            List<MenuItem> availableItems = menuService.getAvailableItems();
            availableItems.forEach(item -> System.out.println("  - " + item.getName() + ": $" + item.getPrice()));

            System.out.println("\nItems under $10 (Lambda price filter):");
            List<MenuItem> cheapItems = menuService.searchByPrice(0, 10);
            cheapItems.forEach(item -> System.out.println("  - " + item.getName() + ": $" + item.getPrice()));

            // 4. BUILDER PATTERN
            System.out.println("\n[4] BUILDER PATTERN - OrderBuilder");
            System.out.println("═══════════════════════════════════════════════════");

            List<OrderItem> orderItems = new ArrayList<>();
            orderItems.add(new OrderItem(1, 1, 2, 12.50));  // Pizza x2

            OrderBuilder builder = new OrderBuilder(1, 1)
                    .addItems(orderItems)
                    .withDeliveryOption("DELIVERY")
                    .withStatus(OrderStatus.PENDING)
                    .applyPricing();

            Order order1 = builder.build();
            System.out.println("Order created with Builder: " + order1);
            System.out.println("  Items: " + order1.getItems().size());
            System.out.println("  Total: $" + String.format("%.2f", order1.getTotalPrice()));

            // 5. FACTORY PATTERN
            System.out.println("\n[5] FACTORY PATTERN - DeliveryOrderFactory");
            System.out.println("═══════════════════════════════════════════════════");

            DeliveryOrder deliveryOrder = DeliveryOrderFactory.createOrder(
                    "DELIVERY", "ORD-001", "Test User", "test@example.com", order1.getTotalPrice());

            DeliveryOrder pickupOrder = DeliveryOrderFactory.createOrder(
                    "PICKUP", "ORD-002", "Test User", "Restaurant", 15.00);

            System.out.println("Created orders using Factory:");
            System.out.println("  1. " + deliveryOrder + " - Time: " + deliveryOrder.calculateDeliveryTime() + " min");
            System.out.println("  2. " + pickupOrder + " - Time: " + pickupOrder.calculateDeliveryTime() + " min");

            // 6. CALLBACKS WITH LAMBDAS
            System.out.println("\n[6] LAMBDAS IN CALLBACKS");
            System.out.println("═══════════════════════════════════════════════════");

            Order placedOrder = orderService.placeOrder(
                    1,
                    orderItems,
                    "DELIVERY",
                    message -> System.out.println("✓ " + message)  // Lambda callback
            );

            // 7. SUMMARY
            System.out.println("\n[7] FINAL SUMMARY");
            System.out.println("═══════════════════════════════════════════════════");
            System.out.println("✓ Generics: IRepository<T, ID> + JDBC");
            System.out.println("✓ Lambdas: Used for filtering, sorting, callbacks");
            System.out.println("✓ Functional Interfaces: MenuItemFilter, OrderCallback");
            System.out.println("✓ Singleton: PricingRules - single instance");
            System.out.println("✓ Builder: OrderBuilder - fluent interface");
            System.out.println("✓ Factory: DeliveryOrderFactory - 3 order types");
            System.out.println("✓ JDBC: All repositories connected to Supabase DB");
            System.out.println("\nAll patterns + JDBC implemented successfully!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}