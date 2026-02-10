package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


@Repository
public class ProductRepository {
    private List<Product> productData = new ArrayList<>();

    public Product create(Product product) {
        productData.add(product);
        return product;
    }

    public Iterator<Product> findAll() {
        return productData.iterator();
    }

    public Product  findById(String id) {
        Iterator<Product> iterator = productData.iterator();
        while (iterator.hasNext()) {
            Product product = iterator.next();
            if (id.equals(product.getProductId())) {
                return product;
            }
        }
        return null;
    }

    public void edit(String id, Product product) {
        Product product_old = findById(id);
        Iterator<Product> iterator = productData.iterator();
        if (id.equals(product.getProductId())) {
            product_old.setProductName(product.getProductName());
            product_old.setProductQuantity(product.getProductQuantity());
        }
    }

    public void delete(String id) {
        productData.removeIf(product -> product.getProductId().equals(id));
    }
}