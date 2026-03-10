package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {
    private OrderRepository orderRepository;
    private List<Product> products;
    private Order order;

    @BeforeEach
    void setUp() {
        orderRepository = new OrderRepository();

        products = new ArrayList<>();

        Product product1 = new Product();
        product1.setProductId("1");
        product1.setProductName("Sampo");
        product1.setProductQuantity(5);

        products.add(product1);

        order = new Order("order-1", products, 1708560000L, "Rafsan");
    }

    @Test
    void testSaveCreateNewOrder() {
        Order result = orderRepository.save(order);

        assertEquals(order, result);
        assertEquals(order, orderRepository.findById("order-1"));
    }

    @Test
    void testSaveUpdateExistingOrder() {
        orderRepository.save(order);

        Order updatedOrder = new Order("order-1", products, 1708560001L, "Rafsan", "SUCCESS");
        Order result = orderRepository.save(updatedOrder);

        assertEquals(updatedOrder, result);
        assertEquals("SUCCESS", orderRepository.findById("order-1").getStatus());
    }

    @Test
    void testFindByIdIfIdExists() {
        orderRepository.save(order);

        Order found = orderRepository.findById("order-1");

        assertNotNull(found);
        assertEquals("order-1", found.getId());
    }

    @Test
    void testFindByIdIfIdNotExists() {
        Order found = orderRepository.findById("unknown-id");

        assertNull(found);
    }

    @Test
    void testFindAllByAuthorIfAuthorExists() {
        orderRepository.save(order);
        orderRepository.save(new Order("order-2", products, 1708560001L, "Rafsan"));

        List<Order> results = orderRepository.findAllByAuthor("Rafsan");

        assertEquals(2, results.size());
    }

    @Test
    void testFindAllByAuthorCaseSensitive() {
        orderRepository.save(order);

        List<Order> results = orderRepository.findAllByAuthor("rafsan");

        assertEquals(0, results.size());
    }
}