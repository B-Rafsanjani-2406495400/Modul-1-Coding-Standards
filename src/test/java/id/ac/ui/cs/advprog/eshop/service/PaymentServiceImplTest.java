package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.OrderStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.payment.BankTransferValidator;
import id.ac.ui.cs.advprog.eshop.service.payment.PaymentDataValidator;
import id.ac.ui.cs.advprog.eshop.service.payment.VoucherCodeValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentServiceImpl paymentService;
    private Order order;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setProductId("product-1");
        product.setProductName("Shampoo");
        product.setProductQuantity(1);

        order = new Order("order-1", List.of(product), 1708560000L, "Rafsan");

        List<PaymentDataValidator> validators = List.of(
                new VoucherCodeValidator(),
                new BankTransferValidator()
        );

        paymentService = new PaymentServiceImpl(paymentRepository, validators);
    }

    @Test
    void testAddPaymentWithValidVoucherShouldSetSuccess() {
        Map<String, String> paymentData = Map.of("voucherCode", "ESHOP1234ABC5678");

        Payment payment = paymentService.addPayment(
                order,
                PaymentMethod.VOUCHER_CODE.name(),
                paymentData
        );

        assertNotNull(payment);
        assertEquals(PaymentMethod.VOUCHER_CODE.name(), payment.getMethod());
        assertEquals(PaymentStatus.SUCCESS.name(), payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.name(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithInvalidVoucherShouldSetRejected() {
        Map<String, String> paymentData = Map.of("voucherCode", "INVALID");

        Payment payment = paymentService.addPayment(
                order,
                PaymentMethod.VOUCHER_CODE.name(),
                paymentData
        );

        assertNotNull(payment);
        assertEquals(PaymentStatus.REJECTED.name(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.name(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithEmptyBankTransferDataShouldSetRejected() {
        Map<String, String> paymentData = Map.of(
                "bankName", "",
                "referenceCode", "REF001"
        );

        Payment payment = paymentService.addPayment(
                order,
                PaymentMethod.BANK_TRANSFER.name(),
                paymentData
        );

        assertNotNull(payment);
        assertEquals(PaymentStatus.REJECTED.name(), payment.getStatus());
        assertEquals(OrderStatus.FAILED.name(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentWithValidBankTransferDataShouldSetSuccess() {
        Map<String, String> paymentData = Map.of(
                "bankName", "BCA",
                "referenceCode", "REF001"
        );

        Payment payment = paymentService.addPayment(
                order,
                PaymentMethod.BANK_TRANSFER.name(),
                paymentData
        );

        assertNotNull(payment);
        assertEquals(PaymentStatus.SUCCESS.name(), payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.name(), order.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetPayment() {
        Payment payment = new Payment(
                "payment-1",
                order,
                PaymentMethod.VOUCHER_CODE.name(),
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        when(paymentRepository.findById("payment-1")).thenReturn(payment);

        Payment result = paymentService.getPayment("payment-1");

        assertEquals(payment, result);
    }

    @Test
    void testGetAllPayments() {
        when(paymentRepository.findAll()).thenReturn(List.of());

        List<Payment> result = paymentService.getAllPayments();

        assertNotNull(result);
    }

    @Test
    void testSetStatusShouldReturnUpdatedPayment() {
        Payment payment = new Payment(
                "payment-1",
                order,
                PaymentMethod.VOUCHER_CODE.name(),
                Map.of("voucherCode", "ESHOP1234ABC5678")
        );

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.name());

        assertEquals(payment, result);
        assertEquals(PaymentStatus.SUCCESS.name(), payment.getStatus());
        assertEquals(OrderStatus.SUCCESS.name(), order.getStatus());
    }
}