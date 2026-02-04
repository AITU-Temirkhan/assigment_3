package Interfaces;

import java.util.List;
import java.util.Optional;


public interface IRepository<T, ID> {
    Optional<T> findById(ID id) throws Exception;
    List<T> findAll() throws Exception;
    void save(T entity) throws Exception;
    void update(T entity) throws Exception;
    void delete(ID id) throws Exception;
}