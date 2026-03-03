package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarService;
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

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Test
    void getCreateCar_shouldShowCreateCarPageAndPutEmptyCarInModel() throws Exception {
        MvcResult result = mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("CreateCar"))
                .andExpect(model().attributeExists("car"))
                .andReturn();

        Object carObj = result.getModelAndView().getModel().get("car");
        assertNotNull(carObj);
        assertTrue(carObj instanceof Car);
    }

    @Test
    void postCreateCar_shouldCallServiceCreate_andRedirectToListCar() throws Exception {
        when(carService.create(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/car/createCar")
                        .param("carName", "Civic")
                        .param("carColor", "Black")
                        .param("carQuantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
        verify(carService, times(1)).create(captor.capture());

        Car sent = captor.getValue();
        assertEquals("Civic", sent.getCarName());
        assertEquals("Black", sent.getCarColor());
        assertEquals(2, sent.getCarQuantity());
    }

    @Test
    void getListCar_shouldShowCarListPageWithCarsFromService() throws Exception {
        Car c1 = new Car();
        c1.setCarId("c-1");
        c1.setCarName("A");
        c1.setCarColor("Red");
        c1.setCarQuantity(1);

        Car c2 = new Car();
        c2.setCarId("c-2");
        c2.setCarName("B");
        c2.setCarColor("Blue");
        c2.setCarQuantity(2);

        when(carService.findAll()).thenReturn(List.of(c1, c2));

        MvcResult result = mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("CarList"))
                .andExpect(model().attributeExists("cars"))
                .andReturn();

        Object carsObj = result.getModelAndView().getModel().get("cars");
        assertNotNull(carsObj);
        assertTrue(carsObj instanceof List<?>);
        assertEquals(2, ((List<?>) carsObj).size());

        verify(carService, times(1)).findAll();
    }

    @Test
    void getEditCar_shouldShowEditCarPageWithCarFromService() throws Exception {
        String id = "c-1";
        Car car = new Car();
        car.setCarId(id);
        car.setCarName("Jazz");
        car.setCarColor("Silver");
        car.setCarQuantity(3);

        when(carService.findById(id)).thenReturn(car);

        MvcResult result = mockMvc.perform(get("/car/editCar/{carId}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("EditCar"))
                .andExpect(model().attributeExists("car"))
                .andReturn();

        Object carObj = result.getModelAndView().getModel().get("car");
        assertSame(car, carObj);

        verify(carService, times(1)).findById(id);
    }

    @Test
    void postEditCar_shouldCallServiceUpdate_andRedirectToListCar() throws Exception {
        doNothing().when(carService).update(anyString(), any(Car.class));

        mockMvc.perform(post("/car/editCar")
                        .param("carId", "c-9")
                        .param("carName", "Accord")
                        .param("carColor", "White")
                        .param("carQuantity", "9"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
        verify(carService, times(1)).update(eq("c-9"), captor.capture());

        Car sent = captor.getValue();
        assertEquals("c-9", sent.getCarId());
        assertEquals("Accord", sent.getCarName());
        assertEquals("White", sent.getCarColor());
        assertEquals(9, sent.getCarQuantity());
    }

    @Test
    void postDeleteCar_shouldCallServiceDelete_andRedirectToListCar() throws Exception {
        doNothing().when(carService).deleteCarById("c-1");

        mockMvc.perform(post("/car/deleteCar")
                        .param("carId", "c-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carService, times(1)).deleteCarById("c-1");
    }
}