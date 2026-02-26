package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import id.ac.ui.cs.advprog.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "CreateProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute  Product product, Model model) {
        service.create(product);
        return "redirect:list";
    }

    @GetMapping("edit/{id}")
    public String editProductPage(Model model,  @PathVariable String id) {
        Product product = service.findById(id);
        model.addAttribute("product", product);
        return "EditProduct";

    }

    @PostMapping("edit/{id}")
    public String editProductPost(@ModelAttribute  Product product, @PathVariable String id) {
        service.edit(product);
        return "redirect:/product/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        service.delete(id);
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts  = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }
}

@Controller
@RequestMapping("/car")
class CarController extends ProductController {

    @Autowired
    private id.ac.ui.cs.advprog.eshop.service.CarServiceImpl carservice;

    @GetMapping("/createCar")
    public String createCarPage(org.springframework.ui.Model model) {
        id.ac.ui.cs.advprog.eshop.model.Car car = new id.ac.ui.cs.advprog.eshop.model.Car();
        model.addAttribute("car", car);
        return "CreateCar";
    }

    @PostMapping("/createCar")
    public String createCarPost(@ModelAttribute id.ac.ui.cs.advprog.eshop.model.Car car,
                                org.springframework.ui.Model model) {
        carservice.create(car);
        return "redirect:listCar";
    }

    @GetMapping("/listCar")
    public String carListPage(org.springframework.ui.Model model) {
        java.util.List<id.ac.ui.cs.advprog.eshop.model.Car> allCars = carservice.findAll();
        model.addAttribute("cars", allCars);
        return "CarList";
    }

    @GetMapping("/editCar/{carId}")
    public String editCarPage(@PathVariable String carId, org.springframework.ui.Model model) {
        id.ac.ui.cs.advprog.eshop.model.Car car = carservice.findById(carId);
        model.addAttribute("car", car);
        return "EditCar";
    }

    @PostMapping("/editCar")
    public String editCarPost(@ModelAttribute id.ac.ui.cs.advprog.eshop.model.Car car,
                              org.springframework.ui.Model model) {
        carservice.update(car.getCarId(), car);
        return "redirect:listCar";
    }

    @PostMapping("/deleteCar")
    public String deleteCar(@RequestParam("carId") String carId) {
        carservice.deleteCarById(carId);
        return "redirect:listCar";
    }
}
