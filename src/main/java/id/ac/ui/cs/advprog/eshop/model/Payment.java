package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.Map;

@Getter
public class Payment {
    private String id;
    private Order order;
    private String method;
    private String status;
    private Map<String, String> paymentData;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        if (!PaymentMethod.contains(method)) {
            throw new IllegalArgumentException();
        }

        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;
        this.status = PaymentStatus.PENDING.name();
    }

    public void setStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException();
        }

        this.status = status;

        if (PaymentStatus.SUCCESS.name().equals(status)) {
            order.setStatus(OrderStatus.SUCCESS.name());
        } else if (PaymentStatus.REJECTED.name().equals(status)) {
            order.setStatus(OrderStatus.FAILED.name());
        }
    }
}