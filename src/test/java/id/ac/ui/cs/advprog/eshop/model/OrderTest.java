package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private List<Product> products;

    @BeforeEach
    void setUp() {
        this.products = new ArrayList<>();

        Product product1 = new Product();
        product1.setProductId("1c5a3555-8b12-407f-bf56-fa6b1391d97f");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(5);

        Product product2 = new Product();
        product2.setProductId("2c623d70-a747-446a-85a7-f23db8b20155");
        product2.setProductName("Sabun Cap Wangi");
        product2.setProductQuantity(30);

        this.products.add(product1);
        this.products.add(product2);
    }

    @Test
    void testCreateOrderEmptyProduct() {
        this.products.clear();

        assertThrows(IllegalArgumentException.class, () -> {
            new Order("1c5a3555-8b12-407f-bf56-fa6b1391d97f",
                    this.products, 1708560000L, "Maman Surahman");
        });
    }

    @Test
    void testCreateOrderDefaultStatus() {
        Order order = new Order("1c5a3555-8b12-407f-bf56-fa6b1391d97f",
                this.products, 1708560000L, "Maman Surahman");

        assertSame(this.products, order.getProducts());
        assertEquals(2, order.getProducts().size());
        assertEquals("Sampo Cap Bambang", order.getProducts().get(0).getProductName());
        assertEquals("Sabun Cap Wangi", order.getProducts().get(1).getProductName());

        assertEquals("1c5a3555-8b12-407f-bf56-fa6b1391d97f", order.getId());
        assertEquals(1708560000L, order.getOrderTime());
        assertEquals("Maman Surahman", order.getAuthor());
        assertEquals("WAITING_PAYMENT", order.getStatus());
    }

    @Test
    void testCreateOrderSuccessStatus() {
        Order order = new Order("1c5a3555-8b12-407f-bf56-fa6b1391d97f",
                this.products, 1708560000L, "Maman Surahman", "SUCCESS");

        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testCreateOrderInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Order("1c5a3555-8b12-407f-bf56-fa6b1391d97f",
                    this.products, 1708560000L, "Maman Surahman", "HIO");
        });
    }

    @Test
    void testSetStatusToCancelled() {
        Order order = new Order("1c5a3555-8b12-407f-bf56-fa6b1391d97f",
                this.products, 1708560000L, "Maman Surahman");
        order.setStatus("CANCELLED");
        assertEquals("CANCELLED", order.getStatus());
    }

    @Test
    void testSetStatusToInvalid() {
        Order order = new Order("1c5a3555-8b12-407f-bf56-fa6b1391d97f",
                this.products, 1708560000L, "Maman Surahman");
        assertThrows(IllegalArgumentException.class, () -> {
            order.setStatus("HIO");
        });
    }
}