package maryam.data.order;

import maryam.models.order.Order;
import maryam.models.product.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long> {
    Iterable<Order> findByCheckedIsNull();
}
