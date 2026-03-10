package id.ac.ui.cs.advprog.eshop.service.payment;

import id.ac.ui.cs.advprog.eshop.model.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CashOnDeliveryValidator implements PaymentDataValidator {

    @Override
    public boolean supports(String method) {
        return PaymentMethod.CASH_ON_DELIVERY.name().equals(method);
    }

    @Override
    public boolean isValid(Map<String, String> paymentData) {
        return isNotBlank(paymentData.get("address"))
                && isNotBlank(paymentData.get("deliveryFee"));
    }

    private boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}