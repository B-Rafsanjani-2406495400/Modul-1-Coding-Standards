package id.ac.ui.cs.advprog.eshop.model;

public enum PaymentMethod {
    VOUCHER_CODE,
    CASH_ON_DELIVERY,
    BANK_TRANSFER;

    public static boolean contains(String value) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}