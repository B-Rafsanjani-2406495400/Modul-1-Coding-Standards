package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.UUID;

@Service
public class ProductServiceImpl implements   ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        UUID uuid = UUID.randomUUID();
        product.setProductId(uuid.toString());
        productRepository.create(product);
        return product;
    }

    @Override
    public List<Product> findAll() {
        Iterator<Product>productIterator = productRepository.findAll();
        List<Product> allProduct = new ArrayList<>();
        productIterator.forEachRemaining(allProduct::add);
        return allProduct;

    }

    @Override
    public void edit(Product product, String id) {
        productRepository.edit(id, product);
    }

    @Override
    public void delete(String id) {
        productRepository.delete(id);
    }

    @Override
    public Product findById(String id) {
        return productRepository.findById(id);
    }
}
