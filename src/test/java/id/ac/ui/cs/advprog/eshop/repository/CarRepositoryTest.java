package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.util.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarRepositoryTest {

    private CarRepository carRepository;
    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = mock(IdGenerator.class);
        carRepository = new CarRepository(idGenerator);
    }

    private static List<Car> iteratorToList(Iterator<Car> it) {
        List<Car> result = new ArrayList<>();
        it.forEachRemaining(result::add);
        return result;
    }

    private static Car makeCar(String id, String name, String color, int qty) {
        Car c = new Car();
        c.setCarId(id);
        c.setCarName(name);
        c.setCarColor(color);
        c.setCarQuantity(qty);
        return c;
    }

    @Test
    void create_whenCarIdNull_shouldUseGenerator_andStoreCar() {
        when(idGenerator.generate()).thenReturn("generated-id");

        Car car = makeCar(null, "Civic", "Black", 2);
        Car saved = carRepository.create(car);

        assertSame(car, saved);
        assertEquals("generated-id", saved.getCarId());
        verify(idGenerator, times(1)).generate();

        List<Car> all = iteratorToList(carRepository.findAll());
        assertEquals(1, all.size());
        assertSame(saved, all.get(0));
    }

    @Test
    void create_whenCarIdAlreadySet_shouldKeepId_andNotCallGenerator() {
        Car car = makeCar("existing-id", "Jazz", "Silver", 3);

        carRepository.create(car);

        assertEquals("existing-id", car.getCarId());
        assertSame(car, carRepository.findById("existing-id"));
        verify(idGenerator, never()).generate();                 // ✅ generator tidak dipakai
    }

    @Test
    void findById_whenNotFound_shouldReturnNull() {
        assertNull(carRepository.findById("not-exist"));
    }

    @Test
    void findAll_shouldReturnAllCars() {
        Car c1 = makeCar("id-1", "A", "Red", 1);
        Car c2 = makeCar("id-2", "B", "Blue", 2);

        carRepository.create(c1);
        carRepository.create(c2);

        List<Car> all = iteratorToList(carRepository.findAll());
        assertEquals(2, all.size());
        assertTrue(all.contains(c1));
        assertTrue(all.contains(c2));
    }

    @Test
    void update_whenFound_shouldUpdateFields_andReturnSameObject() {
        Car original = makeCar("id-1", "Old", "Red", 1);
        carRepository.create(original);

        Car updated = makeCar("ignored", "New", "Blue", 99);
        Car result = carRepository.update("id-1", updated);

        assertNotNull(result);
        assertSame(original, result);
        assertEquals("id-1", result.getCarId());
        assertEquals("New", result.getCarName());
        assertEquals("Blue", result.getCarColor());
        assertEquals(99, result.getCarQuantity());
    }

    @Test
    void update_whenNotFound_shouldReturnNull() {
        Car updated = makeCar("x", "New", "Blue", 99);
        assertNull(carRepository.update("not-exist", updated));
    }

    @Test
    void delete_shouldRemoveOnlyMatchingCar() {
        Car c1 = makeCar("id-1", "A", "Red", 1);
        Car c2 = makeCar("id-2", "B", "Blue", 2);

        carRepository.create(c1);
        carRepository.create(c2);

        carRepository.delete("id-1");

        assertNull(carRepository.findById("id-1"));
        assertNotNull(carRepository.findById("id-2"));

        List<Car> all = iteratorToList(carRepository.findAll());
        assertEquals(1, all.size());
        assertSame(c2, all.get(0));
    }

    @Test
    void delete_whenIdNotFound_shouldNotChangeData() {
        Car c1 = makeCar("id-1", "A", "Red", 1);
        carRepository.create(c1);

        carRepository.delete("not-exist");

        assertNotNull(carRepository.findById("id-1"));
        assertEquals(1, iteratorToList(carRepository.findAll()).size());
    }
}