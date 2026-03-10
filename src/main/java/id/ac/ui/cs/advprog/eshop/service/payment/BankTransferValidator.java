package id.ac.ui.cs.advprog.eshop.service.payment;

import id.ac.ui.cs.advprog.eshop.model.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BankTransferValidator implements PaymentDataValidator {

    @Override
    public boolean supports(String method) {
        return PaymentMethod.BANK_TRANSFER.name().equals(method);
    }

    @Override
    public boolean isValid(Map<String, String> paymentData) {
        return isNotBlank(paymentData.get("bankName"))
                && isNotBlank(paymentData.get("referenceCode"));
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}