package org.infiniteam.autoservice.service.impl;

import org.infiniteam.autoservice.model.Vehicle;
import org.infiniteam.autoservice.model.VehicleOwner;
import org.infiniteam.autoservice.repository.VehicleRepository;
import org.infiniteam.autoservice.service.EntityNotFoundException;
import org.infiniteam.autoservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceJpa implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Vehicle fetch(long vehicleId) {
        return findById(vehicleId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Optional<Vehicle> findById(long vehicleId) {
        return vehicleRepository.findById(vehicleId);
    }

    @Override
    public List<Vehicle> findAllByOwner(VehicleOwner owner) {
        return vehicleRepository.findAllByOwner(owner);
    }

    @Override
    public boolean existsByLicencePlateAndOwner(String licencePlate, VehicleOwner owner) {
        return vehicleRepository.existsByLicencePlateAndOwner(licencePlate, owner);
    }

    @Override
    public Vehicle create(VehicleData vehicleData, VehicleOwner owner) {
        Vehicle vehicle = new Vehicle(vehicleData, owner);
        return vehicleRepository.save(vehicle);
    }

}
