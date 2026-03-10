package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Shampoo");
        product.setProductQuantity(1);

        order = new Order("order-1", List.of(product), 1708560000L, "Rafsan");
    }

    @Test
    void testCreatePaymentDefaultStatusIsPending() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "VOUCHER_CODE",
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        assertEquals("payment-1", payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
    }

    @Test
    void testSetStatusToSuccessShouldSetOrderStatusToSuccess() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "VOUCHER_CODE",
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusToRejectedShouldSetOrderStatusToFailed() {
        Payment payment = new Payment(
                "payment-1",
                order,
                "VOUCHER_CODE",
                Map.of("voucherCode", "INVALID")
        );

        payment.setStatus("REJECTED");

        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testCreatePaymentWithInvalidMethodShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Payment(
                "payment-1",
                order,
                "CRYPTO",
                Map.of()
        ));
    }
}