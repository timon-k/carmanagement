package com.example.carmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@SpringBootTest
public class CarRepositoryTest {

    @Autowired CarRepository repository;
    @Autowired AuditingEntityListener listener;
    @Autowired EntityManager entityManager;

    String licensePlate = "L-CS8877E";

    Car createNewCar() {
        var car = new Car();
        car.setManufacturer("Carcorp");
        car.setBrand("Flexa");
        car.setLicensePlate(licensePlate);
        car.setOperationCity("Newtown");
        car.setStatus(Car.Status.IN_MAINTENANCE);
        return car;
    }

    @Test
    void carCreationShouldWork() {
        assertThat(ReflectionTestUtils.getField(listener, "handler")).isNotNull();

        var car = repository.save(createNewCar());

        assertEquals(licensePlate, car.getLicensePlate());
        assertNotNull(car.getCreatedAt());
        assertNotNull(car.getLastUpdatedAt());
        assertNotNull(car.getId());
    }

    @Test
    void updatingCarShouldWork() throws Exception {
        var existingCar = repository.save(createNewCar());

        // Force the ORM to enter audit timestamps now
        entityManager.flush();
        // Let a minimum amount of time pass in order to enforce modified audit timestamps below
        Thread.sleep(10);

        var newStatus = Car.Status.AVAILABLE;
        var previousLastUpdatedAt = existingCar.getLastUpdatedAt();
        var previousCreatedAt = existingCar.getCreatedAt();
        existingCar.setStatus(newStatus);
        var updatedCar = repository.save(existingCar);

        entityManager.flush(); // Force the ORM to enter audit timestamps now

        assertEquals(newStatus, updatedCar.getStatus());
        assertEquals(previousCreatedAt, updatedCar.getCreatedAt());
        assertThat(previousLastUpdatedAt).isBefore(updatedCar.getLastUpdatedAt());
    }
}
