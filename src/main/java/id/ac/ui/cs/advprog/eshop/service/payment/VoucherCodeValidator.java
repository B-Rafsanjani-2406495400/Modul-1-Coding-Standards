package id.ac.ui.cs.advprog.eshop.service.payment;

import id.ac.ui.cs.advprog.eshop.model.PaymentMethod;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VoucherCodeValidator implements PaymentDataValidator {

    @Override
    public boolean supports(String method) {
        return PaymentMethod.VOUCHER_CODE.name().equals(method);
    }

    @Override
    public boolean isValid(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");

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
}