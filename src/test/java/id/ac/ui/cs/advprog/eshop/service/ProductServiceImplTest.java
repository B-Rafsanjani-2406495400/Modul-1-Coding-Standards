package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setProductId("existing-id");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(100);
    }

    @Test
    void create_shouldGenerateValidUuid_setToProduct_andCallRepositoryCreate() {
        Product newProduct = new Product();
        newProduct.setProductName("Kecap Cap Bango");
        newProduct.setProductQuantity(50);

        when(productRepository.create(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product saved = productService.create(newProduct);

        assertNotNull(saved.getProductId());
        assertDoesNotThrow(() -> UUID.fromString(saved.getProductId()));
        assertEquals("Kecap Cap Bango", saved.getProductName());
        assertEquals(50, saved.getProductQuantity());

        verify(productRepository, times(1)).create(newProduct);
    }

    @Test
    void findAll_shouldReturnAllProductsFromRepositoryIterator() {
        Product p1 = new Product();
        p1.setProductId("p-1");
        p1.setProductName("A");
        p1.setProductQuantity(1);

        Product p2 = new Product();
        p2.setProductId("p-2");
        p2.setProductName("B");
        p2.setProductQuantity(2);

        Iterator<Product> iterator = List.of(p1, p2).iterator();
        when(productRepository.findAll()).thenReturn(iterator);

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("p-1", result.get(0).getProductId());
        assertEquals("p-2", result.get(1).getProductId());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findAll_whenEmpty_shouldReturnEmptyList() {
        when(productRepository.findAll()).thenReturn(List.<Product>of().iterator());

        List<Product> result = productService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldDelegateToRepository() {
        when(productRepository.findById("p-1")).thenReturn(product);

        Product found = productService.findById("p-1");

        assertSame(product, found);
        verify(productRepository, times(1)).findById("p-1");
    }

    @Test
    void edit_shouldDelegateToRepository() {
        doReturn(product).when(productRepository).edit(product);

        productService.edit(product);

        verify(productRepository, times(1)).edit(product);
    }

    @Test
    void delete_shouldDelegateToRepository() {
        doNothing().when(productRepository).delete("p-1");

        productService.delete("p-1");

        verify(productRepository, times(1)).delete("p-1");
    }
}