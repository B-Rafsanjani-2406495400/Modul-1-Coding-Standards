package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void getCreate_shouldShowCreateProductPageAndPutEmptyProductInModel() throws Exception {
        MvcResult result = mockMvc.perform(get("/product/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateProduct"))
                .andExpect(model().attributeExists("product"))
                .andReturn();

        Object productObj = result.getModelAndView().getModel().get("product");
        assertNotNull(productObj);
        assertTrue(productObj instanceof Product);
    }

    @Test
    void postCreate_shouldCallServiceCreate_andRedirectToList() throws Exception {
        when(service.create(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/product/create")
                        .param("productName", "Keyboard")
                        .param("productQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("list"));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(service, times(1)).create(captor.capture());

        Product sent = captor.getValue();
        assertEquals("Keyboard", sent.getProductName());
        assertEquals(10, sent.getProductQuantity());
    }

    @Test
    void getEdit_shouldShowEditProductPageWithProductFromService() throws Exception {
        String id = "p-1";
        Product product = new Product();
        product.setProductId(id);
        product.setProductName("Mouse");
        product.setProductQuantity(5);

        when(service.findById(id)).thenReturn(product);

        MvcResult result = mockMvc.perform(get("/product/edit/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("EditProduct"))
                .andExpect(model().attributeExists("product"))
                .andReturn();

        Object productObj = result.getModelAndView().getModel().get("product");
        assertSame(product, productObj);

        verify(service, times(1)).findById(id);
    }

    @Test
    void postEdit_shouldCallServiceEdit_andRedirectToProductList() throws Exception {
        doNothing().when(service).edit(any(Product.class));

        mockMvc.perform(post("/product/edit/{id}", "p-1")
                        .param("productId", "p-1")
                        .param("productName", "Mouse Updated")
                        .param("productQuantity", "7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(service, times(1)).edit(captor.capture());

        Product sent = captor.getValue();
        assertEquals("p-1", sent.getProductId());
        assertEquals("Mouse Updated", sent.getProductName());
        assertEquals(7, sent.getProductQuantity());
    }

    @Test
    void getDelete_shouldCallServiceDelete_andRedirectToProductList() throws Exception {
        doNothing().when(service).delete(anyString());

        mockMvc.perform(get("/product/delete/{id}", "p-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/product/list"));

        verify(service, times(1)).delete("p-1");
    }

    @Test
    void getList_shouldShowProductListPageWithProductsFromService() throws Exception {
        Product p1 = new Product();
        p1.setProductId("p-1");
        p1.setProductName("A");
        p1.setProductQuantity(1);

        Product p2 = new Product();
        p2.setProductId("p-2");
        p2.setProductName("B");
        p2.setProductQuantity(2);

        when(service.findAll()).thenReturn(List.of(p1, p2));

        MvcResult result = mockMvc.perform(get("/product/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ProductList"))
                .andExpect(model().attributeExists("products"))
                .andReturn();

        Object productsObj = result.getModelAndView().getModel().get("products");
        assertNotNull(productsObj);
        assertTrue(productsObj instanceof List<?>);
        assertEquals(2, ((List<?>) productsObj).size());

        verify(service, times(1)).findAll();
    }
}