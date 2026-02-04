package Patterns;

import Patterns.DeliveryOrder;
import Patterns.PickupOrder;
import Patterns.DeliveryOrderImpl;
import Patterns.DineInOrder;

/**
 * Factory Pattern: Creates different order delivery types
 */
public class DeliveryOrderFactory {

    public static DeliveryOrder createOrder(String orderType, String orderId,
                                            String customerName, String destination, double price) {
        if (orderType == null) {
            throw new IllegalArgumentException("Order type cannot be null");
        }

        switch (orderType.toUpperCase()) {
            case "DELIVERY":
                double deliveryCost = PricingRules.getInstance().getDeliveryCharge();
                return new DeliveryOrderImpl(orderId, customerName, destination, price, deliveryCost);

            case "PICKUP":
                return new PickupOrder(orderId, customerName, destination, price);

            case "DINE_IN":
                return new DineInOrder(orderId, customerName, destination, price, "TBD");

            default:
                throw new IllegalArgumentException("Unknown order type: " + orderType);
        }
    }
}