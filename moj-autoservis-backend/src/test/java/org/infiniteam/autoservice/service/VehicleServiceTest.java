package org.infiniteam.autoservice.service;

import org.infiniteam.autoservice.model.Vehicle;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.infiniteam.autoservice.repository.VehicleRepository;
import org.infiniteam.autoservice.service.impl.VehicleData;
import org.infiniteam.autoservice.service.impl.VehicleServiceJpa;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceTest {

    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    HuoService huoService;

    @InjectMocks
    VehicleService vehicleService = new VehicleServiceJpa();


    @BeforeEach
    public void setup() throws HuoServiceException {
        when(huoService.fetchVehicleData(any())).then(invocation -> {
            return new VehicleData(invocation.getArgument(0), "MOCK-VIN", "Audi A1");
        });
    }

    @Test
    public void createVehicleReturnsTheVehicleWithCorrectDataFromHuoServiceAndOwner() throws HuoServiceException {
        when(huoService.fetchVehicleData(any())).thenReturn(
                new VehicleData("ZG1234AA", "MOCK-VIN", "Audi A1")
        );
        when(vehicleRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        VehicleOwner owner = new VehicleOwner();

        Vehicle vehicle = vehicleService.create("ZG1234AA", owner);

        assertEquals("ZG1234AA", vehicle.getLicencePlate());
        assertEquals("MOCK-VIN", vehicle.getVinNumber());
        assertEquals("Audi A1", vehicle.getVehicleModel());
        assertEquals(owner, vehicle.getOwner());
    }

    @Test
    public void createVehicleThrowsWhenHuoServiceThrows() throws HuoServiceException {
        when(huoService.fetchVehicleData(any())).thenThrow(new HuoServiceException());
        VehicleOwner owner = new VehicleOwner();

        Assertions.assertThrows(RuntimeException.class, () -> vehicleService.create("ZG1234AA", owner));
    }

    @Test
    public void createVehicleFailsWhenOwnerHasThatVehicle() throws HuoServiceException {
        VehicleData data = new VehicleData("ZG1234AA", "MOCK-VIN", "Audi A1");
        //when(huoService.fetchVehicleData(any())).thenReturn(data);
        VehicleOwner owner = new VehicleOwner();
        Vehicle vehicle = new Vehicle(data, owner);
        owner.setVehicles(List.of(vehicle));
        when(vehicleRepository.existsByLicencePlateAndOwner(any(), any())).thenReturn(true);
        //when(vehicleRepository.findAllByOwner(any())).thenReturn(List.of(vehicle));

        Assertions.assertThrows(AlreadyExistsException.class, () -> vehicleService.create("ZG1234AA", owner));
    }





}
