package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.model.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                order,
                method,
                paymentData
        );

        if (isPaymentDataValid(method, paymentData)) {
            payment.setStatus(PaymentStatus.SUCCESS.name());
        } else {
            payment.setStatus(PaymentStatus.REJECTED.name());
        }

        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    private boolean isPaymentDataValid(String method, Map<String, String> paymentData) {
        if (PaymentMethod.VOUCHER_CODE.name().equals(method)) {
            return isValidVoucherCode(paymentData.get("voucherCode"));
        }

        if (PaymentMethod.CASH_ON_DELIVERY.name().equals(method)) {
            return isNotBlank(paymentData.get("address"))
                    && isNotBlank(paymentData.get("deliveryFee"));
        }

        if (PaymentMethod.BANK_TRANSFER.name().equals(method)) {
            return isNotBlank(paymentData.get("bankName"))
                    && isNotBlank(paymentData.get("referenceCode"));
        }

        throw new IllegalArgumentException("Unsupported payment method");
    }

    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null || voucherCode.length() != 16) {
            return false;
        }

        if (!voucherCode.startsWith("ESHOP")) {
            return false;
        }

        int digitCount = 0;
        for (char currentChar : voucherCode.toCharArray()) {
            if (Character.isDigit(currentChar)) {
                digitCount++;
            }
        }

        return digitCount == 8;
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}