package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Order {
    String id;
    List<Product> products;
    Long orderTime;
    String author;
    String status;

    public Order(String id, List<Product> products, Long orderTime, String author) {
        this.id = id;
        this.orderTime = orderTime;
        this.author = author;
        this.status = OrderStatus.WAITING_PAYMENT.name();

        if (products.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.products = products;
    }

    public Order(String id, List<Product> products, Long orderTime, String author, String status) {
        this(id, products, orderTime, author);

        if (!OrderStatus.contains(status)) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }

    public void setStatus(String status) {
        if (!OrderStatus.contains(status)) {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }
}