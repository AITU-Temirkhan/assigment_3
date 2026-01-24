package Repositories;

import Models.MenuItem;
import java.util.List;

public interface IMenuRepository {
    List<MenuItem> getAllMenuItems() throws Exception;
    MenuItem getMenuItemById(int id) throws Exception;
}
