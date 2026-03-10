package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Shampoo");
        product.setProductQuantity(1);

        Order order = new Order("order-1", List.of(product), 1708560000L, "Rafsan");

        payment = new Payment(
                "payment-1",
                order,
                PaymentMethod.VOUCHER_CODE.name(),
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );
    }

    @Test
    void testSavePayment() {
        Payment savedPayment = paymentRepository.save(payment);

        assertEquals(payment, savedPayment);
    }

    @Test
    void testFindByIdIfExists() {
        paymentRepository.save(payment);

        Payment foundPayment = paymentRepository.findById("payment-1");

        assertNotNull(foundPayment);
        assertEquals("payment-1", foundPayment.getId());
    }

    @Test
    void testFindByIdIfNotExists() {
        Payment foundPayment = paymentRepository.findById("unknown-payment");

        assertNull(foundPayment);
    }

    @Test
    void testFindAllPayments() {
        paymentRepository.save(payment);

        assertEquals(1, paymentRepository.findAll().size());
    }
}