package Models;

public class OrderItem {
    private int id;
    private int orderId;
    private int menuItemId;
    private int quantity;
    private double itemPrice;

    public OrderItem(int orderId, int menuItemId, int quantity, double itemPrice) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public OrderItem(int id, int orderId, int menuItemId, int quantity, double itemPrice) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
    }

    public int getId() { return id; }
    public int getOrderId() { return orderId; }
    public int getMenuItemId() { return menuItemId; }
    public int getQuantity() { return quantity; }
    public double getItemPrice() { return itemPrice; }
    public double getTotalPrice() { return itemPrice * quantity; }

    @Override
    public String toString() {
        return String.format("OrderItem{menuItemId=%d, qty=%d, price=%.2f, total=%.2f}",
                menuItemId, quantity, itemPrice, getTotalPrice());
    }
}