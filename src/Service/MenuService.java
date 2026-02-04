package Service;

import Interfaces.IRepository;
import Models.MenuItem;
import Repositories.MenuRepository;
import java.util.List;

/**
 * Menu Service
 */
public class MenuService {
    private final IRepository<MenuItem, Integer> menuRepository;

    public MenuService() {
        this.menuRepository = new MenuRepository();
    }

    public List<MenuItem> getAllMenuItems() throws Exception {
        return menuRepository.findAll();
    }

    public List<MenuItem> getAvailableItems() throws Exception {
        return ((MenuRepository) menuRepository).findAvailableItems();
    }

    public List<MenuItem> searchByPrice(double min, double max) throws Exception {
        return ((MenuRepository) menuRepository).findByPriceRange(min, max);
    }

    public void printMenu() throws Exception {
        System.out.println("\n=== Menu ===");
        getAllMenuItems().forEach(item -> {
            String status = item.isAvailable() ? "✓" : "✗";
            System.out.printf("%d. %s - $%.2f %s\n",
                    item.getId(), item.getName(), item.getPrice(), status);
        });
    }
}