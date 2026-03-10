package id.ac.ui.cs.advprog.eshop.service.payment;

import java.util.Map;

public interface PaymentDataValidator {
    boolean supports(String method);
    boolean isValid(Map<String, String> paymentData);
}