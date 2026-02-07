package Repositories;

import Database.DatabaseConnection;
import Interfaces.IRepository;
import Models.MenuItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Menu Repository - работает с БД через JDBC
 * Реализует Generic IRepository<MenuItem, Integer>
 */
public class MenuRepository implements IRepository<MenuItem, Integer> {

    @Override
    public Optional<MenuItem> findById(Integer id) throws Exception {
        String sql = "SELECT id, name, price, available FROM menu_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MenuItem item = new MenuItem(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getBoolean("available")
                    );
                    return Optional.of(item);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<MenuItem> findAll() throws Exception {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT id, name, price, available FROM menu_items";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                MenuItem item = new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getBoolean("available")
                );
                items.add(item);
            }
        }
        return items;
    }

    
    //Lambda
    
    public List<MenuItem> filterItems(java.util.function.Predicate<MenuItem> filter) throws Exception {
        return findAll().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    public List<MenuItem> findAvailableItems() throws Exception {
        return filterItems(MenuItem::isAvailable);
    }

    
    //(Lambda выражение)
    
    public List<MenuItem> findByPriceRange(double minPrice, double maxPrice) throws Exception {
        return filterItems(item -> item.getPrice() >= minPrice && item.getPrice() <= maxPrice);
    }

    @Override
    public void save(MenuItem item) throws Exception {
        String sql = "INSERT INTO menu_items (name, price, available) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setBoolean(3, item.isAvailable());
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(MenuItem item) throws Exception {
        String sql = "UPDATE menu_items SET name = ?, price = ?, available = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setBoolean(3, item.isAvailable());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM menu_items WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
