package id.ac.ui.cs.advprog.eshop.model;

public enum OrderStatus {
    WAITING_PAYMENT,
    FAILED,
    CANCELLED,
    SUCCESS;

    public static boolean contains(String param) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equals(param)) {
                return true;
            }
        }
        return false;
    }
}