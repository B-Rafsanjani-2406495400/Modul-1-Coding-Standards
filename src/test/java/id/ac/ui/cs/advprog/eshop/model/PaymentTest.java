package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        order = new Order("order-1", java.util.List.of(new Product()), 1708560000L, "Rafsan");
        paymentData = new HashMap<>();
    }

    @Test
    void testCreatePaymentDefaultPending() {
        Payment payment = new Payment("payment-1", order, "VOUCHER_CODE", paymentData);

        assertEquals("payment-1", payment.getId());
        assertEquals(order, payment.getOrder());
        assertEquals("VOUCHER_CODE", payment.getMethod());
        assertEquals(PaymentStatus.PENDING.name(), payment.getStatus());
    }

    @Test
    void testVoucherValidShouldBeSuccess() {
        paymentData.put("voucherCode", "ESHOP12345678AB");
        Payment payment = new Payment("payment-1", order, "VOUCHER_CODE", paymentData);

        payment.process();

        assertEquals(PaymentStatus.SUCCESS.name(), payment.getStatus());
    }

    @Test
    void testVoucherInvalidShouldBeRejected() {
        paymentData.put("voucherCode", "INVALID");
        Payment payment = new Payment("payment-1", order, "VOUCHER_CODE", paymentData);

        payment.process();

        assertEquals(PaymentStatus.REJECTED.name(), payment.getStatus());
    }

    @Test
    void testCodMissingAddressShouldBeRejected() {
        Payment payment = new Payment("payment-1", order, "CASH_ON_DELIVERY", paymentData);

        payment.process();

        assertEquals(PaymentStatus.REJECTED.name(), payment.getStatus());
    }

    @Test
    void testBankTransferMissingBankShouldBeRejected() {
        Payment payment = new Payment("payment-1", order, "BANK_TRANSFER", paymentData);

        payment.process();

        assertEquals(PaymentStatus.REJECTED.name(), payment.getStatus());
    }
}