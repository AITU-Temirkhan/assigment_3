package Repositories;

import Models.OrderItem;
import java.util.List;

public interface IOrderRepository {
    void saveOrder(int customerId, List<OrderItem> items) throws Exception;
}