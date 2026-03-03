package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.util.IdGenerator;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Repository
public class CarRepository {

    private final List<Car> carData = new ArrayList<>();
    private final IdGenerator idGenerator;

    public CarRepository(IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public Car create(Car car) {
        if (car.getCarId() == null || car.getCarId().isBlank()) {
            car.setCarId(idGenerator.generate());
        }
        carData.add(car);
        return car;
    }

    public Iterator<Car> findAll() {
        return carData.iterator();
    }

    public Car findById(String id) {
        for (Car car : carData) {
            if (Objects.equals(car.getCarId(), id)) {
                return car;
            }
        }
        return null;
    }

    public Car update(String id, Car updatedCar) {
        for (int i = 0; i < carData.size(); i++) {
            Car car = carData.get(i);
            if (Objects.equals(car.getCarId(), id)) {
                car.setCarName(updatedCar.getCarName());
                car.setCarColor(updatedCar.getCarColor());
                car.setCarQuantity(updatedCar.getCarQuantity());
                return car;
            }
        }
        return null;
    }

    public void delete(String id) {
        carData.removeIf(car -> Objects.equals(car.getCarId(), id));
    }
}