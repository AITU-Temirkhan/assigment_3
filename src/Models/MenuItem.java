package Models;

public class MenuItem {
    private int id;
    private String name;
    private double price;
    private boolean available;
    private String category;

    public MenuItem(int id, String name, double price, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
    }

    public MenuItem(int id, String name, double price, boolean available, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.available = available;
        this.category = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isAvailable() { return available; }
    public String getCategory() { return category; }

    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return String.format("MenuItem{id=%d, name='%s', price=%.2f, available=%s}",
                id, name, price, available);
    }
}
