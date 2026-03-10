package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private List<Product> products;
    private Order order;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();

        Product product1 = new Product();
        product1.setProductId("1");
        product1.setProductName("Sampo");
        product1.setProductQuantity(5);

        products.add(product1);

        order = new Order("order-1", products, 1708560000L, "Rafsan");
    }

    @Test
    void testCreateOrderIfNew() {
        when(orderRepository.findById(order.getId())).thenReturn(null);

        Order result = orderService.createOrder(order);

        assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testCreateOrderIfAlreadyExists() {
        when(orderRepository.findById(order.getId())).thenReturn(order);

        Order result = orderService.createOrder(order);

        assertNull(result);
        verify(orderRepository, never()).save(order);
    }

    @Test
    void testUpdateStatusSuccess() {
        when(orderRepository.findById("order-1")).thenReturn(order);

        Order updated = orderService.updateStatus("order-1", "SUCCESS");

        assertEquals("SUCCESS", updated.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateStatusInvalidStatus() {
        when(orderRepository.findById("order-1")).thenReturn(order);

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateStatus("order-1", "HIO");
        });
    }

    @Test
    void testUpdateStatusOrderNotFound() {
        when(orderRepository.findById("order-404")).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            orderService.updateStatus("order-404", "SUCCESS");
        });
    }

    @Test
    void testFindByIdIfExists() {
        when(orderRepository.findById("order-1")).thenReturn(order);

        Order result = orderService.findById("order-1");

        assertEquals(order, result);
    }

    @Test
    void testFindByIdIfNotExists() {
        when(orderRepository.findById("order-404")).thenReturn(null);

        Order result = orderService.findById("order-404");

        assertNull(result);
    }

    @Test
    void testFindAllByAuthorIfExists() {
        List<Order> orders = List.of(order);
        when(orderRepository.findAllByAuthor("Rafsan")).thenReturn(orders);

        List<Order> result = orderService.findAllByAuthor("Rafsan");

        assertEquals(1, result.size());
    }

    @Test
    void testFindAllByAuthorCaseSensitive() {
        when(orderRepository.findAllByAuthor("rafsan")).thenReturn(new ArrayList<>());

        List<Order> result = orderService.findAllByAuthor("rafsan");

        assertEquals(0, result.size());
    }
}