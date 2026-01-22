package Models;

public class OrderItem {
    private int menuItemId;
    private int quantity;

    public OrderItem(int menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public int getMenuItemId() { return menuItemId; }
    public int getQuantity() { return quantity; }
}