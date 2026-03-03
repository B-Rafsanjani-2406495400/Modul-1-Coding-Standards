package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("c-1");
        car.setCarName("Brio");
        car.setCarColor("Red");
        car.setCarQuantity(4);
    }

    @Test
    void create_shouldDelegateToRepository() {
        when(carRepository.create(car)).thenReturn(car);

        Car saved = carService.create(car);

        assertSame(car, saved);
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void findAll_shouldReturnListFromRepositoryIterator() {
        Car c1 = new Car(); c1.setCarId("c-1");
        Car c2 = new Car(); c2.setCarId("c-2");

        Iterator<Car> it = List.of(c1, c2).iterator();
        when(carRepository.findAll()).thenReturn(it);

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        assertEquals("c-1", result.get(0).getCarId());
        assertEquals("c-2", result.get(1).getCarId());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void findAll_whenEmpty_shouldReturnEmptyList() {
        when(carRepository.findAll()).thenReturn(List.<Car>of().iterator());

        List<Car> result = carService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldDelegateToRepository() {
        when(carRepository.findById("c-1")).thenReturn(car);

        Car found = carService.findById("c-1");

        assertSame(car, found);
        verify(carRepository, times(1)).findById("c-1");
    }

    @Test
    void update_shouldDelegateToRepository() {
        doReturn(car).when(carRepository).update(eq("c-1"), any(Car.class));

        carService.update("c-1", car);

        verify(carRepository, times(1)).update("c-1", car);
    }

    @Test
    void delete_shouldDelegateToRepository() {
        doNothing().when(carRepository).delete("c-1");

        carService.deleteCarById("c-1");

        verify(carRepository, times(1)).delete("c-1");
    }
}