package Patterns;


//Singleton Pattern: PricingRules Configuration
 
public class PricingRules {
    private static PricingRules instance;

    private double deliveryCharge = 5.00;
    private double pickupDiscount = 0.0;
    private double minOrderForFreeDelivery = 50.0;
    private double taxRate = 0.12;

    private PricingRules() {
    }

    public static synchronized PricingRules getInstance() {
        if (instance == null) {
            instance = new PricingRules();
        }
        return instance;
    }

    public double getDeliveryCharge() { return deliveryCharge; }
    public void setDeliveryCharge(double charge) { this.deliveryCharge = charge; }

    public double getPickupDiscount() { return pickupDiscount; }
    public void setPickupDiscount(double discount) { this.pickupDiscount = discount; }

    public double getMinOrderForFreeDelivery() { return minOrderForFreeDelivery; }
    public void setMinOrderForFreeDelivery(double min) { this.minOrderForFreeDelivery = min; }

    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double rate) { this.taxRate = rate; }

    public double calculateFinalPrice(String deliveryOption, double subtotal) {
        double price = subtotal * (1 + taxRate);

        if ("DELIVERY".equalsIgnoreCase(deliveryOption)) {
            if (subtotal >= minOrderForFreeDelivery) {
                return price;
            } else {
                return price + deliveryCharge;
            }
        } else if ("PICKUP".equalsIgnoreCase(deliveryOption)) {
            return price * (1 - pickupDiscount);
        }

        return price;
    }

    @Override
    public String toString() {
        return String.format("PricingRules{delivery=%.2f, tax=%.2f%%}", deliveryCharge, taxRate * 100);
    }
}
