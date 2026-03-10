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
    }

    public Order(String id, List<Product> products, Long orderTime, String author, String status) {
    }

    public void setStatus(String status) {
    }
}