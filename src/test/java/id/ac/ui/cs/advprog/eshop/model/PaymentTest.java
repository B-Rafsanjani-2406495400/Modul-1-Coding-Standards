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
                PaymentMethod.VOUCHER_CODE.name(),
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        assertEquals("payment-1", payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals(PaymentMethod.VOUCHER_CODE.name(), payment.getMethod());
        assertEquals(PaymentStatus.PENDING.name(), payment.getStatus());
    }

    @Test
    void testSetStatusToSuccessShouldSetOrderStatusToSuccess() {
        Payment payment = new Payment(
                "payment-1",
                order,
                PaymentMethod.VOUCHER_CODE.name(),
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        payment.setStatus(PaymentStatus.SUCCESS.name());

        assertEquals(PaymentStatus.SUCCESS.name(), payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.name(), order.getStatus());
    }

    @Test
    void testSetStatusToRejectedShouldSetOrderStatusToFailed() {
        Payment payment = new Payment(
                "payment-1",
                order,
                PaymentMethod.VOUCHER_CODE.name(),
                Map.of("voucherCode", "INVALID")
        );

        payment.setStatus(PaymentStatus.REJECTED.name());

        assertEquals(PaymentStatus.REJECTED.name(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.name(), order.getStatus());
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

    @Test
    void testSetStatusWithInvalidStatusShouldThrowException() {
        Payment payment = new Payment(
                "payment-1",
                order,
                PaymentMethod.VOUCHER_CODE.name(),
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("INVALID"));
    }
}