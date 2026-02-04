package Repositories;

import Database.DatabaseConnection;
import Interfaces.IRepository;
import Models.Order;
import Models.OrderItem;
import Models.OrderStatus;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Order Repository - работает с БД через JDBC
 * Реализует Generic IRepository<Order, Integer>
 */
public class OrderRepository implements IRepository<Order, Integer> {

    @Override
    public Optional<Order> findById(Integer id) throws Exception {
        String sql = "SELECT id, customer_id, status, created_at FROM orders WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order(
                            rs.getInt("id"),
                            rs.getInt("customer_id"),
                            OrderStatus.valueOf(rs.getString("status")),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    // Загружаем items отдельно
                    order.setItems(getOrderItems(id, conn));
                    return Optional.of(order);
                }
            }
        }
        return Optional.empty();
    }

    private List<OrderItem> getOrderItems(int orderId, Connection conn) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.menu_item_id, oi.quantity, mi.price " +
                "FROM order_items oi " +
                "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
                "WHERE oi.order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem(
                            rs.getInt("id"),
                            orderId,
                            rs.getInt("menu_item_id"),
                            rs.getInt("quantity"),
                            rs.getDouble("price")
                    );
                    items.add(item);
                }
            }
        }
        return items;
    }

    @Override
    public List<Order> findAll() throws Exception {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, customer_id, status, created_at FROM orders";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        OrderStatus.valueOf(rs.getString("status")),
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                order.setItems(getOrderItems(order.getId(), conn));
                orders.add(order);
            }
        }
        return orders;
    }

    /**
     * Фильтрация заказов с использованием Lambda
     */
    public List<Order> filterOrders(java.util.function.Predicate<Order> filter) throws Exception {
        return findAll().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    /**
     * Найти активные заказы
     */
    public List<Order> findActiveOrders() throws Exception {
        return filterOrders(order -> order.getStatus() == OrderStatus.ACTIVE);
    }

    /**
     * Найти заказы по статусу
     */
    public List<Order> findByStatus(OrderStatus status) throws Exception {
        return filterOrders(order -> order.getStatus() == status);
    }

    /**
     * Найти заказы клиента
     */
    public List<Order> findByCustomerId(Integer customerId) throws Exception {
        return filterOrders(order -> order.getCustomerId() == customerId);
    }

    @Override
    public void save(Order order) throws Exception {
        String orderSql = "INSERT INTO orders (customer_id, status, created_at) VALUES (?, ?, ?) RETURNING id";
        String itemSql = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {
                orderStmt.setInt(1, order.getCustomerId());
                orderStmt.setString(2, order.getStatus().name());
                orderStmt.setTimestamp(3, Timestamp.valueOf(order.getCreatedAt()));

                try (ResultSet rs = orderStmt.executeQuery()) {
                    if (rs.next()) {
                        int orderId = rs.getInt(1);

                        try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                            for (OrderItem item : order.getItems()) {
                                itemStmt.setInt(1, orderId);
                                itemStmt.setInt(2, item.getMenuItemId());
                                itemStmt.setInt(3, item.getQuantity());
                                itemStmt.addBatch();
                            }
                            itemStmt.executeBatch();
                        }
                    }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public void update(Order order) throws Exception {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getStatus().name());
            stmt.setInt(2, order.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM orders WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}