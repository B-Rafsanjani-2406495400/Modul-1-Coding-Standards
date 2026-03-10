package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import id.ac.ui.cs.advprog.eshop.service.payment.PaymentDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final List<PaymentDataValidator> paymentDataValidators;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              List<PaymentDataValidator> paymentDataValidators) {
        this.paymentRepository = paymentRepository;
        this.paymentDataValidators = paymentDataValidators;
    }

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        Payment payment = new Payment(
                UUID.randomUUID().toString(),
                order,
                method,
                paymentData
        );

        PaymentDataValidator validator = findValidator(method);

        if (validator.isValid(paymentData)) {
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

    private PaymentDataValidator findValidator(String method) {
        for (PaymentDataValidator validator : paymentDataValidators) {
            if (validator.supports(method)) {
                return validator;
            }
        }
        throw new IllegalArgumentException("Unsupported payment method: " + method);
    }
}