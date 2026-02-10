package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductService  {
    public Product create(Product product);
    public List<Product> findAll();
    public Product findById(String id);
    public void edit(Product product,   String id);
    public void delete(String id);
}