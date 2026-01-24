package Repositories;

import Database.DatabaseConnection;
import Models.OrderItem;
import java.sql.*;
import java.util.List;

public class OrderRepository implements IOrderRepository {
    @Override
    public void saveOrder(int customerId, List<OrderItem> items) throws Exception {
        String orderSql = "INSERT INTO orders (customer_id, status) VALUES (?, 'ACTIVE') RETURNING id";
        String itemSql = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Включаем транзакцию

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql)) {
                orderStmt.setInt(1, customerId);
                ResultSet rs = orderStmt.executeQuery();

                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                        for (OrderItem item : items) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setInt(2, item.getMenuItemId());
                            itemStmt.setInt(3, item.getQuantity());
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }
}