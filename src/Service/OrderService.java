package Service;

import Exceptions.InvalidQuantityException;
import Exceptions.MenuItemNotAvailableException;
import Models.MenuItem;
import Models.OrderItem;
import Repositories.IMenuRepository;
import Repositories.IOrderRepository;
import Repositories.MenuRepository;
import Repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final IMenuRepository menuRepository;
    private final IOrderRepository orderRepository;

    public OrderService() {
        this.menuRepository = new MenuRepository();
        this.orderRepository = new OrderRepository();
    }

    public void placeOrder(int customerId, int menuItemId, int quantity) throws Exception {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity must be greater than zero.");
        }

        MenuItem item = menuRepository.getMenuItemById(menuItemId);
        if (item == null) {
            throw new OrderNotFoundException("Menu item with ID " + menuItemId + " not found.");
        }
        if (!item.isAvailable()) {
            throw new MenuItemNotAvailableException("Item '" + item.getName() + "' is out of stock.");
        }

        List<OrderItem> items = new ArrayList<>();
        items.add(new OrderItem(menuItemId, quantity));

        orderRepository.saveOrder(customerId, items);

        System.out.println("Success! Order for " + item.getName() + " saved to Supabase.");
    }
}